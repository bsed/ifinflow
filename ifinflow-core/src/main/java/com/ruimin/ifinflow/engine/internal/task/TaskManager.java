package com.ruimin.ifinflow.engine.internal.task;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfRole;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.engine.flowmodel.Variable;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.util.BeanUtil;
import com.ruimin.ifinflow.engine.flowmodel.vo.*;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.engine.pvm.cmd.*;
import com.ruimin.ifinflow.model.flowmodel.cache.TemplateCacheManager;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
import com.ruimin.ifinflow.model.flowmodel.external.IModelService;
import com.ruimin.ifinflow.util.DefinitionUtil;
import com.ruimin.ifinflow.util.TemplateCacheUtil;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.jbpm.api.*;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryComment;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.cmd.AddTaskCommentCmd;
import org.jbpm.pvm.internal.cmd.CompositeCmd;
import org.jbpm.pvm.internal.cmd.GetPriorNodeCmd;
import org.jbpm.pvm.internal.cmd.SignalCmd;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.task.ParticipationImpl;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.jbpm.pvm.internal.task.TopTaskVO;
import org.jbpm.pvm.internal.util.CollectionUtil;

import java.util.*;


public class TaskManager {
    private ProcessEngine processEngine;
    private TaskService taskService;
    private HistoryService historyService;
    private RepositoryService repositoryService;
    private ExecutionService executionService;
    private IModelService<?> modelService;

    public TaskManager(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.taskService = processEngine.getTaskService();
        this.historyService = processEngine.getHistoryService();
        this.repositoryService = processEngine.getRepositoryService();
        this.executionService = processEngine.getExecutionService();
        this.modelService = processEngine.getModelService();
    }


    public void takeTask(String taskId, WfStaffVO user)
            throws IFinFlowException {
        this.processEngine.execute(new TaskAssigneeCmd(taskId, user.getWfUserInfo(), true));
    }


    public void completeTask(TaskImpl task, String outcome, int status, WfStaffVO user, VariableSet vs, String memo)
            throws IFinFlowException {
        String taskId = task.getId();
        if ((user == null) || (StringUtils.isEmpty(user.getStaffId()))) {
            throw new IFinFlowException(101001);
        }


        CompositeCmd compositeCmd = new CompositeCmd();

        if ((vs != null) && (vs.size() > 0)) {
            TaskVariableSetCmd taskVariablesSetCmd = new TaskVariableSetCmd(taskId, vs);

            compositeCmd.addCommand(taskVariablesSetCmd);
        }

        if (!StringUtils.isEmpty(memo)) {
            compositeCmd.addCommand(new AddTaskCommentCmd(taskId, memo));
        }

        compositeCmd.addCommand(new TaskCompleteCmd(taskId, outcome, Integer.valueOf(status), user != null ? user.getWfUserInfo() : null));


        compositeCmd.addCommand(new WorkloadUpdateCmd(user.getStaffId(), user.getStaffName(), -1));

        this.processEngine.execute(compositeCmd);
    }


    public void rejectTask(String taskId, String targetName, WfStaffVO user, VariableSet vs, String memo)
            throws IFinFlowException {
        if (StringUtils.isEmpty(taskId)) {
            throw new IFinFlowException(103018);
        }

        final TaskImpl task = (TaskImpl) this.taskService.getTask(taskId);
        if (task == null) {
            throw new IFinFlowException(103002, new Object[]{taskId});
        }


        String nodeId = task.getActivityName();

        ProcessDefinitionImpl processDefinition = getDefinitionImpl(task);
        NodeVo nodeVo = TemplateCacheUtil.getNodeVo(processDefinition, nodeId);


        ActivityImpl fromActivity = processDefinition.findActivity(task.getActivityName());


        if (StringUtils.isBlank(targetName)) {
            ActivityImpl priorActivity = null;

            if (nodeVo.getRejectDefault().equals("1")) {


                targetName = getFirstNodeNameBeforeCurrNode(taskId);
                if (StringUtils.isBlank(targetName)) {
                    throw new IFinFlowException(109001);
                }

            } else if (nodeVo.getRejectDefault().equals("2")) {
                targetName = (String) this.processEngine.execute(new GetPriorNodeCmd(task, processDefinition));

            } else if (nodeVo.getRejectDefault().equals("3")) {
                targetName = nodeVo.getRejectDefaultNodeId();
            }
        }


        ExecutionImpl executionImpl = (ExecutionImpl) this.executionService.findExecutionById(task.getExecutionId());


        fromActivity.setReject(true);

        boolean isSame = isSameExecutionId(task.getExecutionId(), targetName, task.getProcessInstance().getDbid());

        List<ExecutionImpl> executions = null;
        List<String> otherBranches = null;
        ExecutionImpl parentExecution = executionImpl.getParent();

        if (!isSame) {
            List<HistoryActivityInstance> list = this.historyService.createHistoryActivityInstanceQuery().activityName(targetName).historyProcessInstance(task.getProcessInstance().getDbid()).list();


            if ((list != null) && (list.size() > 0)) {
                String branchId = ((HistoryActivityInstance) list.get(0)).getExecutionId();

                if ((!branchId.contains(executionImpl.getExecutionImplId())) || (branchId.equals(executionImpl.getExecutionImplId()))) {
                    if (parentExecution != null) {
                        executions = (List) parentExecution.getExecutions();
                        otherBranches = findOtherBranches(task.getExecutionId(), executions);
                        if (otherBranches != null) {
                            dealOtherBranches(otherBranches, executions);
                        }
                    }
                }
            } else {
                processDefinition = DefinitionUtil.getSuperDefinitionImpl(task.getProcessInstance());

                fromActivity = (ActivityImpl) this.processEngine.execute(new Command<ActivityImpl>() {
                    private static final long serialVersionUID = 1L;

                    public ActivityImpl execute(Environment environment) throws Exception {
                        return task.getProcessInstance().getSuperProcessExecution().getActivity();
                    }
                });
                fromActivity.setReject(true);
            }
        }


        String outcome = DefinitionUtil.addOutGoingTransition(processDefinition, fromActivity, targetName);

        completeTask(task, outcome, 4, user, vs, memo);


        if ((parentExecution != null) && (!isSame) && (otherBranches != null)) {
            deleteExecutionImpls(otherBranches);
        }
    }

    private boolean isExsitInActivityList(String targetName, List<HistoryActivityInstance> activityList) {
        for (HistoryActivityInstance hai : activityList) {
            if (targetName.equals(hai.getActivityName())) {
                return true;
            }
        }
        return false;
    }

    private String getPriorNode(ActivityImpl activityImpl) {
        List<? extends Transition> transitionList = activityImpl.getIncomingTransitions();


        if ((!transitionList.isEmpty()) && (!StringUtils.equals("1", activityImpl.getType()))) {

            if (!StringUtils.equals("1", ((ActivityImpl) ((Transition) transitionList.get(0)).getSource()).getType())) {

                return ((Transition) transitionList.get(0)).getSource().getName();
            }
        }
        return null;
    }


    private boolean isSameExecutionId(String executionId, String targetId, String processInstanceId) {
        boolean result = false;
        List<HistoryActivityInstance> haiList = this.historyService.createHistoryActivityInstanceQuery().activityName(targetId).executionId(executionId).list();


        if ((haiList != null) && (!haiList.isEmpty())) {
            result = true;
        }
        return result;
    }


    public void retractTask(String taskId, WfStaffVO user, VariableSet vs)
            throws IFinFlowException {
        Object[] obj = (Object[]) this.processEngine.execute(new TaskRetractionPathGetCmd(taskId));


        if ((obj != null) && (obj[0] != null) && (obj[1] != null)) {
            HistoryTaskImpl task = (HistoryTaskImpl) obj[0];
            CompositeCmd compositeCmd = new CompositeCmd();

            if ((vs != null) && (vs.size() > 0)) {
                TaskVariableSetCmd taskVariablesSetCmd = new TaskVariableSetCmd(task.getId(), vs);

                compositeCmd.addCommand(taskVariablesSetCmd);
            }

            compositeCmd.addCommand(new TaskCompleteCmd(task.getId(), (String) obj[1], Integer.valueOf(5), user != null ? user.getWfUserInfo() : null));


            this.processEngine.execute(compositeCmd);
        } else {
            throw new IFinFlowException(109004);
        }
    }


    public void returnTask(String taskId, WfStaffVO user)
            throws IFinFlowException {
        TaskImpl task = (TaskImpl) this.taskService.getTask(taskId);
        this.processEngine.execute(new TaskReturnCmd(task, user));
    }


    public void skipTask(String taskId, String targetName, WfStaffVO user, VariableSet vs, String memo)
            throws IFinFlowException {
        final TaskImpl task = (TaskImpl) this.taskService.getTask(taskId);
        if (StringUtils.isEmpty(targetName)) {
            throw new IFinFlowException(103044);
        }
        if (task == null) {
            throw new IFinFlowException(103002, new Object[]{taskId});
        }


        ProcessDefinitionImpl pd = (ProcessDefinitionImpl) this.processEngine.execute(new ProcessDefinitionFindByTaskId(task));

        ActivityImpl fromActivity = pd.findActivity(task.getActivityName());

        ExecutionImpl executionImpl = (ExecutionImpl) this.executionService.findExecutionById(task.getExecutionId());


        ActivityImpl targetActivity = pd.findActivity(targetName);

        if (targetActivity != null) {
            int skipValue = skipJudge(pd, pd.findActivity(task.getActivityName()), targetActivity, -1);

            if (skipValue != 1) {


                if (skipValue == 2) {
                    ExecutionImpl parentExecution = executionImpl.getParent();

                    if (parentExecution != null) {
                        List<ExecutionImpl> executions = (List) parentExecution.getExecutions();
                        List<String> otherBranches = findOtherBranches(task.getExecutionId(), executions);
                        if (otherBranches != null) {
                            dealOtherBranches(otherBranches, executions);
                        }
                    }
                }
            }
        } else {
            pd = DefinitionUtil.getSuperDefinitionImpl(task.getExecution());

            fromActivity = (ActivityImpl) this.processEngine.execute(new Command<ActivityImpl>() {
                private static final long serialVersionUID = 1L;

                public ActivityImpl execute(Environment environment) throws Exception {
                    return task.getExecution().getSuperProcessExecution().getActivity();
                }
            });
        }


        fromActivity.setSkip(true);

        String path = DefinitionUtil.addOutGoingTransition(pd, fromActivity, targetName, DefinitionUtil.getSuperDefinitionImpl(task.getExecution()));

        completeTask(task, path, 6, user, vs, memo);
    }

    private int skipJudge(ProcessDefinitionImpl pd, ActivityImpl sourceAct, ActivityImpl targetAct, int flag) {
        if (StringUtils.equals(sourceAct.getName(), targetAct.getName())) {
            return flag;
        }
        TransitionImpl defaultOutgoingTransition = sourceAct.getDefaultOutgoingTransition();
        ActivityImpl nextAct = null;
        if (defaultOutgoingTransition != null) {
            nextAct = defaultOutgoingTransition.getDestination();
        } else {
            return flag;
        }

        if (StringUtils.equals("5", nextAct.getType())) {
            flag = 1;
            return flag;
        }

        if (StringUtils.equals("6", nextAct.getType())) {
            if (StringUtils.equals(nextAct.getName(), targetAct.getName())) {
                return flag;
            }
            flag = 2;

            return flag;
        }
        sourceAct = nextAct;
        skipJudge(pd, sourceAct, targetAct, flag);


        return flag;
    }


    public TaskVO getTask(String taskId)
            throws IFinFlowException {
        if (StringUtils.isEmpty(taskId)) {
            throw new IFinFlowException(103018);
        }

        HistoryTaskImpl task = (HistoryTaskImpl) this.historyService.createHistoryTaskQuery().taskId(taskId).uniqueResult();


        if (task == null) {

            throw new IFinFlowException(103003, new Object[]{taskId});
        }
        return BeanUtil.createTaskVO(task);
    }


    public void entrustTask(String taskId, WfStaffVO oldUser, WfStaffVO toUser, String memo)
            throws IFinFlowException {
        try {
            this.processEngine.execute(new TaskEntrustCmd(taskId, oldUser.getStaffId(), toUser.getStaffId(), memo));
        } catch (Exception e) {
            throw new IFinFlowException(103005, e, new Object[]{taskId});
        }
    }


    public List<TaskVO> getTodoTask(WfStaffVO user, int startNum, int pageSize)
            throws IFinFlowException {
        if ((user == null) || (StringUtils.isEmpty(user.getStaffId()))) {
            throw new IFinFlowException(101001);
        }
        List<TopTaskVO> list = (List) this.processEngine.execute(new TodoTaskQueryCmd(user.getStaffId(), startNum, pageSize));


        return BeanUtil.createTaskVOs(list);
    }


    public long getTodoTaskCount(WfStaffVO user)
            throws IFinFlowException {
        if ((user == null) || (StringUtils.isEmpty(user.getStaffId()))) {
            throw new IFinFlowException(101001);
        }
        return ((Long) this.processEngine.execute(new TodoTaskQueryCountCmd(user.getStaffId()))).longValue();
    }


    public List<TaskVO> getDoneTask(WfStaffVO user, int startNum, int pageSize)
            throws IFinFlowException {
        if ((user == null) || (StringUtils.isEmpty(user.getStaffId()))) {
            throw new IFinFlowException(101001);
        }
        Map<String, Object> params = new HashMap();
        params.put("staffId", user.getStaffId());

        String hql = "select task from org.jbpm.pvm.internal.history.model.HistoryTaskImpl task\twhere task.assignee=:staffId and task.status in(16,4,6)\torder by task.priority desc,task.createTime desc";

        List<HistoryTask> list = (List) this.processEngine.execute(new FindPageByNameQueryCmd(hql, params, startNum, pageSize));


        return BeanUtil.createTaskVOs(list);
    }


    public long getDoneTaskCount(WfStaffVO user)
            throws IFinFlowException {
        if ((user == null) || (StringUtils.isEmpty(user.getStaffId()))) {
            throw new IFinFlowException(101001);
        }
        Map<String, Object> params = new HashMap();
        params.put("staffId", user.getStaffId());

        String hql = "select count(task) from org.jbpm.pvm.internal.history.model.HistoryTaskImpl task where task.assignee=:staffId and task.status in(16,4,6)";

        List<Long> list = (List) this.processEngine.execute(new FindPageByNameQueryCmd(hql, params));

        return ((Long) list.get(0)).longValue();
    }


    public List<TaskVO> searchTask(WfStaffVO user, int startNum, int pageSize)
            throws IFinFlowException {
        if ((user == null) || (StringUtils.isEmpty(user.getStaffId()))) {
            throw new IFinFlowException(101001);
        }
        String userId = user.getStaffId();
        List<String> groupIds = getGroupIds(userId);

        String hql = searchTaskHql(userId, groupIds, null, null, null, null, 3, null, null, null);


        Map<String, Object> params = setTaskParams(userId, groupIds, null, null, null, null, 3, null, null, null);


        List<HistoryTask> list = CollectionUtil.checkList((List) this.processEngine.execute(new FindPageByNameQueryCmd(hql, params, startNum, pageSize)), HistoryTask.class);


        return BeanUtil.createTaskVOs(list);
    }


    public long searchTaskCount(WfStaffVO user)
            throws IFinFlowException {
        if ((user == null) || (StringUtils.isEmpty(user.getStaffId()))) {
            throw new IFinFlowException(101001);
        }
        String userId = user.getStaffId();

        List<String> groupIds = getGroupIds(userId);
        String hql = "select count(task) " + searchTaskHql(userId, groupIds, null, null, null, null, 3, null, null, null);


        Map<String, Object> params = setTaskParams(userId, groupIds, null, null, null, null, 3, null, null, null);


        List<Long> list = CollectionUtil.checkList((List) this.processEngine.execute(new FindPageByNameQueryCmd(hql, params)), Long.class);


        return ((Long) list.get(0)).longValue();
    }


    public List<TaskVO> searchTask(String staffId, String nodeId, String nodeName, Date createdDatetimeStart, Date createdDatetimeEnd, int taskStatus, int startNumber, int pageSize, String processId, String packageId, String templateId)
            throws IFinFlowException {
        List<String> groupIds = getGroupIds(staffId);

        String hql = searchTaskHql(staffId, groupIds, nodeId, nodeName, createdDatetimeStart, createdDatetimeEnd, taskStatus, processId, packageId, templateId);


        Map<String, Object> params = setTaskParams(staffId, groupIds, nodeId, nodeName, createdDatetimeStart, createdDatetimeEnd, taskStatus, processId, packageId, templateId);


        List<HistoryTask> list = CollectionUtil.checkList((List) this.processEngine.execute(new FindPageByNameQueryCmd(hql.toString(), params, startNumber, pageSize)), HistoryTask.class);


        return BeanUtil.createTaskVOs(list);
    }


    public long searchTaskCount(String staffId, String nodeId, String nodeName, Date createdDatetimeStart, Date createdDatetimeEnd, int taskStatus, String processId, String packageId, String templateId)
            throws IFinFlowException {
        List<String> groupIds = getGroupIds(staffId);

        String hql = "select count(task) " + searchTaskHql(staffId, groupIds, nodeId, nodeName, createdDatetimeStart, createdDatetimeEnd, taskStatus, processId, packageId, templateId);


        Map<String, Object> params = setTaskParams(staffId, groupIds, nodeId, nodeName, createdDatetimeStart, createdDatetimeEnd, taskStatus, processId, packageId, templateId);


        List<Long> list = CollectionUtil.checkList((List) this.processEngine.execute(new FindPageByNameQueryCmd(hql.toString(), params)), Long.class);


        return ((Long) list.get(0)).longValue();
    }


    private String searchTaskHql(String userId, List<String> groupIds, String nodeId, String nodeName, Date createdDatetimeStart, Date createdDatetimeEnd, int taskStatus, String processId, String packageId, String templateId) {
        StringBuffer hql = new StringBuffer();
        hql.append("from " + HistoryTaskImpl.class.getName() + " task ");
        hql.append(" where 1=1 ");

        if (!StringUtils.isEmpty(userId)) {
            hql.append(" and (task.assignee=:userId or exists(select part from org.jbpm.pvm.internal.task.ParticipationImpl part where task.dbid=part.task.dbid and task.assignMode=2 and part.type='candidate' and (part.userId=:userId");


            if (groupIds.isEmpty()) {
                hql.append(")))");
            } else {
                hql.append(" or part.groupId in (:groups))))");
            }
        }


        if (createdDatetimeStart != null) {
            hql.append(" and task.createTime>=:createdDatetimeStart");
        }
        if (createdDatetimeEnd != null) {
            hql.append(" and task.createTime<=:createdDatetimeEnd");
        }


        if (taskStatus == 0) {
            hql.append(" and task.status in (1,2,256) ");


        } else if (taskStatus == 1) {
            hql.append(" and task.status in (4,5,6,16) ");
        }


        if (!StringUtils.isEmpty(nodeId)) {
            hql.append(" and task.nodeId=:nodeId");
        }
        if (!StringUtils.isEmpty(nodeName)) {
            hql.append(" and task.nodeName=:nodeName");
        }

        if (!StringUtils.isEmpty(processId)) {
            hql.append(" and task.historyProcessInstanceId=:processId");
        }
        if (!StringUtils.isEmpty(packageId)) {
            hql.append(" and task.packageId=:packageId");
        }
        if (!StringUtils.isEmpty(templateId)) {
            hql.append(" and task.templateId=:templateId");
        }
        return hql.toString();
    }


    private Map<String, Object> setTaskParams(String userId, List<String> groupIds, String nodeId, String nodeName, Date createdDatetimeStart, Date createdDatetimeEnd, int taskStatus, String processId, String packageId, String templateId) {
        Map<String, Object> params = new HashMap();


        if (!StringUtils.isEmpty(userId)) {
            params.put("userId", userId);
        }
        if (!groupIds.isEmpty()) {
            params.put("groups", groupIds);
        }

        if (createdDatetimeStart != null) {
            params.put("createdDatetimeStart", createdDatetimeStart);
        }
        if (createdDatetimeEnd != null) {
            params.put("createdDatetimeEnd", createdDatetimeEnd);
        }
        if (!StringUtils.isEmpty(nodeId)) {
            params.put("nodeId", nodeId);
        }
        if (!StringUtils.isEmpty(nodeName)) {
            params.put("nodeName", nodeName);
        }

        if (!StringUtils.isEmpty(processId)) {
            params.put("processId", processId);
        }
        if (!StringUtils.isEmpty(packageId)) {
            params.put("packageId", packageId);
        }
        if (!StringUtils.isEmpty(templateId)) {
            params.put("templateId", templateId);
        }
        return params;
    }

    private List<String> getGroupIds(String userId) {
        List<String> groupIds = new ArrayList();

        if (StringUtils.isEmpty(userId)) {
            return groupIds;
        }

        IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();

        IWfUnit unit = identityAdapter.getUnitByStaffId(userId);
        List<IWfRole> roles = identityAdapter.getRolesByStaffId(userId);

        if (unit != null) {
            groupIds.add(unit.getUnitId());
        }
        for (IWfRole role : roles) {
            groupIds.add(role.getRoleId());
            if (unit != null) {
                groupIds.add(unit.getUnitId() + "," + role.getRoleId());
            }
        }
        return groupIds;
    }


    public Map<String, List<ProcessTraceVO>> getProcessTrace(String processId)
            throws IFinFlowException {
        List<HistoryActivityInstance> list = this.processEngine.getHistoryService().createHistoryActivityInstanceQuery().processId(processId).orderAsc("startTime").list();


        List<ProcessTraceVO> actList = null;
        Map<String, List<ProcessTraceVO>> result = new HashMap();

        NodeVo node = null;

        for (int i = 0; i < list.size(); i++) {
            HistoryActivityInstanceImpl his = (HistoryActivityInstanceImpl) list.get(i);


            String actName = his.getActivityName();

            node = TemplateCacheUtil.getNodeVo(his.getPackageId(), his.getTemplateId(), his.getTemplateVersion(), actName);

            if (0 != node.getDisplay()) {


                actList = (List) result.get(actName);

                if ((actList == null) || (actList.isEmpty())) {
                    actList = new ArrayList();
                    result.put(actName, actList);
                }

                actList.add(BeanUtil.createProcessTraceVo(his));
            }
        }
        return result;
    }

    public List<String> getSkipableTargetsByActivityId(String activityId) throws IFinFlowException {
        return (List) this.processEngine.execute(new ActivitySkipNodeFindCmd(activityId));
    }


    public List<String> getSkipNode(String taskId)
            throws IFinFlowException {
        return (List) this.processEngine.execute(new TaskSkipNodeFindCmd(taskId));
    }

    @Deprecated
    public List<String> getRejectNodeList(String taskId) {
        return (List) this.processEngine.execute(new TaskRejectNodeFindCmd(taskId, this.historyService));
    }

    public List<RejectActivityVO> getRejectList(String taskId) {
        return (List) this.processEngine.execute(new RejectTaskFindCmd(taskId, this.historyService));
    }


    public List<String> getHistoryActivityName(String currentNodeName, ProcessInstance processInstance) {
        List<String> historyActivityNames = new ArrayList();
        historyActivityNames.add(currentNodeName);
        return gethistoryActivitys(historyActivityNames, currentNodeName, processInstance);
    }


    public void setVariablesByTask(String taskId, VariableSet vs)
            throws IFinFlowException {
        if (vs == null) {
            throw new IFinFlowException(104004, new Object[]{"null"});
        }
        this.processEngine.execute(new TaskVariableSetCmd(taskId, vs));
    }


    public void setVariableByTask(String taskId, Variable var)
            throws IFinFlowException {
        if (var == null) {
            throw new IFinFlowException(104004, new Object[]{"null"});
        }
        VariableSet vs = new VariableSet();
        vs.addVariable(var);
        this.processEngine.execute(new TaskVariableSetCmd(taskId, vs));
    }


    public VariableSet getVariableSet(String taskId, Set<String> variableNames) {
        TaskVO task = getTask(taskId);
        return (VariableSet) this.processEngine.execute(new ProcessVariableFindCmd(task.getProcessId(), variableNames));
    }


    public ProcessDefinitionImpl getDefinitionImpl(TaskImpl task) {
        return (ProcessDefinitionImpl) this.repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessInstance().getProcessDefinitionId()).uniqueResult();
    }


    public boolean checkTaskRetraction(String taskId)
            throws IFinFlowException {
        return this.processEngine.execute(new TaskRetractionPathGetCmd(taskId)) != null;
    }


    private List<String> gethistoryActivitys(List<String> historyActivityNames, String activityImplName, ProcessInstance processInstance) {
        ActivityImpl ai = getActivityImplByName(activityImplName, processInstance);

        if (!ai.getIncomingTransitions().isEmpty()) {
            ActivityImpl sourceActivity = (ActivityImpl) ((Transition) ai.getIncomingTransitions().get(0)).getSource();

            String sourceActivityName = sourceActivity.getName();
            if (sourceActivityName != null) {
                if ((!"join".equals(sourceActivityName)) && (!"start".equals(sourceActivityName))) {
                    if (!historyActivityNames.contains(sourceActivityName)) {
                        historyActivityNames.add(sourceActivityName);
                    }
                } else {
                    for (Transition transition : sourceActivity.getIncomingTransitions()) {
                        sourceActivityName = transition.getSource().getName();
                        if (!historyActivityNames.contains(sourceActivityName)) {
                            historyActivityNames.add(sourceActivityName);
                        }

                        gethistoryActivitys(historyActivityNames, sourceActivityName, processInstance);
                    }
                }

                gethistoryActivitys(historyActivityNames, sourceActivityName, processInstance);
            }
        }

        return historyActivityNames;
    }


    public ActivityImpl getActivityImplByName(String activityName, ProcessInstance processInstance) {
        return getProcessDefinition(processInstance).findActivity(activityName);
    }


    public ProcessDefinitionImpl getProcessDefinition(ProcessInstance processInstance) {
        ProcessDefinitionImpl processDefinition = (ProcessDefinitionImpl) this.processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).uniqueResult();


        return processDefinition;
    }


    public void dealOtherBranches(List<String> otherBranches, List<ExecutionImpl> executions) {
        List<Task> tasks = new ArrayList();
        for (String tempExecutionId : otherBranches) {
            tasks.addAll(this.taskService.createTaskQuery().executionId(tempExecutionId).list());
        }


        if ((null != otherBranches) && (!otherBranches.isEmpty())) {
            updateHistoryTaskAndActivityState(otherBranches, 32);

            deleteTemporaryTask(tasks);
        }
    }


    private List<String> findOtherBranches(String executionId, List<ExecutionImpl> executions) {
        List<String> resultList = null;
        int size = executions.size();
        if (size > 0) {
            resultList = new ArrayList();
            for (int i = 0; i < size; i++) {
                if (null != (ExecutionImpl) executions.get(i)) {
                    String tempExecutionId = ((ExecutionImpl) executions.get(i)).getExecutionImplId();

                    if (!tempExecutionId.equals(executionId)) {
                        resultList.add(tempExecutionId);
                    }
                }
            }
        }

        return resultList;
    }


    public List<String> findExecutions() {
        final StringBuffer hql = new StringBuffer();

        hql.append("select hti.executionId");
        hql.append(" from ");
        hql.append(HistoryTaskImpl.class.getName());
        hql.append(" hti");

        List<String> list = (List) this.processEngine.execute(new Command() {
            private static final long serialVersionUID = 1L;

            public List<String> execute(Environment environment) throws Exception {
                return ((Session) environment.get(Session.class)).createQuery(hql.toString()).list();
            }

        });
        return list;
    }


    private boolean updateHistoryTaskAndActivityState(List<String> otherBranches, int taskSate) {
        for (String executionId : otherBranches) {
            updateHistoryTaskState(taskSate, executionId);
            updateHistoryActivityState(taskSate, executionId);
        }
        return true;
    }


    private boolean updateHistoryTaskState(int taskSate, String executionId) {
        final StringBuffer hql = new StringBuffer();
        hql.append(" update " + HistoryTaskImpl.class.getName() + " as hti ");
        hql.append(" set hti.status = " + taskSate + "");
        hql.append(" where hti.executionId = '" + executionId + "'");

        hql.append(" and (hti.status = 1 or hti.status = 2)");
        this.processEngine.execute(new Command<Integer>() {
            private static final long serialVersionUID = 1L;

            public Integer execute(Environment environment) throws Exception {
                return (environment.get(Session.class)).createQuery(hql.toString()).executeUpdate();
            }

        });
        return true;
    }


    private boolean updateHistoryActivityState(int taskSate, String executionId) {
        final StringBuffer hql = new StringBuffer();
        hql.append(" update " + HistoryActivityInstanceImpl.class.getName() + " as o ");

        hql.append(" set o.status = " + taskSate + "");
        hql.append(" where o.executionId = '" + executionId + "'");
        hql.append(" and (o.status = 1 or o.status = 2)");
        this.processEngine.execute(new Command<Integer>() {
            private static final long serialVersionUID = 1L;

            public Integer execute(Environment environment) throws Exception {
                return (environment.get(Session.class)).createQuery(hql.toString()).executeUpdate();
            }
        });
        return true;
    }


    private boolean deleteExecutionImpls(List<String> otherBranches) {
        for (String executionId : otherBranches) {
            ExecutionImpl executionImpl = (ExecutionImpl) this.executionService.findExecutionById(executionId);

            if ((executionImpl != null) &&
                    (executionImpl.getSubProcessInstance() != null)) {
                this.executionService.deleteProcessInstanceCascade(executionImpl.getSubProcessInstance().getExecutionImplId());
            }


            this.processEngine.execute(new DeleteExecutionCmd(executionId));
        }
        return true;
    }

    private void deleteTemporaryTask(List<Task> tasks) {
        for (Task task : tasks) {
            deleteTaskAssociatedObj(task, ParticipationImpl.class.getName());


            this.processEngine.execute(new DeleteTaskImplCmd(task));
        }
    }


    private void deleteTaskAssociatedObj(Task task, String className) {
        StringBuffer tempHql = new StringBuffer("delete from ");
        tempHql.append(className);
        tempHql.append(" o where o.task = '");
        tempHql.append(task.getId());
        tempHql.append("'");
        this.processEngine.getModelService().updateByHql(tempHql.toString());
    }

    public List<String> getAssigneeByTaskId(String taskId) throws IFinFlowException {
        if (StringUtils.isEmpty(taskId)) {
            throw new IFinFlowException(103018);
        }

        return (List) this.processEngine.execute(new GetAssigneeByTaskIdCmd(taskId));
    }


    public String getFirstNodeNameBeforeCurrNode(String taskId)
            throws IFinFlowException {
        if (StringUtils.isEmpty(taskId)) {
            throw new IFinFlowException(103018);
        }

        TaskImpl task = (TaskImpl) this.taskService.getTask(taskId);
        ProcessDefinitionImpl processDefinition = getDefinitionImpl(task);

        List<Activity> actList = (List<Activity>) processDefinition.getActivities();

        for (Activity activity : actList) {
            if ("1".equals(activity.getType())) {
                List<Transition> transitonList = (List<Transition>) activity.getOutgoingTransitions();
                return (transitonList.get(0)).getDestination().getName();
            }
        }
        return null;
    }


    public String getNodeNameBeforeCurrNode(String taskId)
            throws IFinFlowException {
        if (StringUtils.isEmpty(taskId)) {
            throw new IFinFlowException(103018);
        }

        TaskImpl task = (TaskImpl) this.taskService.getTask(taskId);

        List<HistoryActivityInstance> activityList = this.historyService.createHistoryActivityInstanceQuery().processId(task.getProcessInstance().getDbid()).list();


        if (activityList.isEmpty()) {
            throw new IFinFlowException(109002);
        }

        ProcessDefinitionImpl processDefinition = getDefinitionImpl(task);

        ActivityImpl currentActivity = processDefinition.findActivity(task.getActivityName());

        String resultActName = getPriorNode(currentActivity);
        if (null == resultActName) {
            throw new IFinFlowException(109008);
        }

        while (!isExsitInActivityList(resultActName, activityList)) {
            resultActName = getPriorNode(processDefinition.findActivity(resultActName));
        }


        return resultActName;
    }


    public void signal(String processId, String nodeId) {
        Execution execution = (Execution) this.processEngine.execute(new ExecutionQueryCmd(processId, nodeId));

        this.processEngine.execute(new SignalCmd(execution.getExecutionImplId(), null, null));
    }


    public long getTodoTaskCount(WfStaffVO user, String nodeName, String templateId, int status, String... extendValue) {
        return ((Long) this.processEngine.execute(new TodoTaskQueryCountCmd(user == null ? null : user.getStaffId(), nodeName, templateId, status, extendValue))).longValue();
    }


    public List<TaskVO> getTodoTask(WfStaffVO user, String nodeName, String templateId, int status, int startNum, int pageSize, String... extendValue)
            throws IFinFlowException {
        List<TopTaskVO> tasks = (List) this.processEngine.execute(new TodoTaskQueryCmd(user == null ? null : user.getStaffId(), nodeName, templateId, startNum, pageSize, status, extendValue));


        return BeanUtil.createTaskVOs(tasks);
    }


    public void updateDefaultRejectValue(String taskId, String defaultRejectValue) {
        if (StringUtils.isEmpty(taskId)) {
            throw new IFinFlowException(103018);
        }

        TaskImpl task = (TaskImpl) this.taskService.getTask(taskId);
        if (task == null) {
            throw new IFinFlowException(103002, new Object[]{taskId});
        }

        String deploymentId = task.getExecution().getProcessDefinition().getDeploymentId();


        this.modelService.updateDefaultRejectValue(taskId, deploymentId, defaultRejectValue);


        if (TemplateCacheManager.getInstance().getTemplateCacheAllKey().contains(deploymentId)) {

            TemplateCacheManager.getInstance().clearOnly(deploymentId);
        }
    }


    public ProcessVO startProcessWithCommitFirstTask(String packageId, String templateId, int version, WfStaffVO user, VariableSet vs, String subject) {
        ProcessVO process = null;
        process = (ProcessVO) this.processEngine.execute(new ProcessStartCmd(packageId, templateId, version, user, vs, subject));


        ExecutionImpl pro = (ExecutionImpl) this.executionService.findProcessInstanceById(process.getExecutionId());

        List<Task> list = this.taskService.createTaskQuery().executionId(pro.getExecutionImplId()).list();

        for (Task t : list) {
            TaskImpl task = (TaskImpl) t;
            if (task != null) {
                takeTask(task.getDbid(), user);
            } else {
                throw new IFinFlowException(100000);
            }

            completeTask(task, null, 16, user, vs, "");
        }

        return process;
    }

    public void addSubTasks(String processId, String nodeId, List<String> staffIds, String exitType, int exitCount) {
        this.processEngine.execute(new AddSubtaskCmd(processId, nodeId, staffIds, exitType, exitCount));
    }


    public void addSubTasks(String taskId, List<String> staffIds, String exitType, int exitCount)
            throws IFinFlowException {
        this.processEngine.execute(new AddSubtaskCmd(taskId, staffIds, exitType, exitCount));
    }


    public boolean deleteSubTasks(String processId, String nodeId, List<String> staffIds, String exitType, int exitCount)
            throws IFinFlowException {
        return ((Boolean) this.processEngine.execute(new DeleteSubTasksCmd(processId, nodeId, staffIds, exitType, exitCount))).booleanValue();
    }

    public boolean deleteSubTasks(String taskId, List<String> staffIds, String exitType, int exitCount) throws IFinFlowException {
        return ((Boolean) this.processEngine.execute(new DeleteSubTasksCmd(taskId, staffIds, exitType, exitCount))).booleanValue();
    }

    public void updateTaskExtendsField(final String taskId, final String... params) throws IFinFlowException {
        if ((StringUtils.isBlank(taskId)) || (params == null) || (params.length == 0)) {
            return;
        }
        this.processEngine.execute(new Command() {
            private static final long serialVersionUID = 1699783834496583198L;

            public Collection<String> execute(Environment environment) throws Exception {
                Session session = (Session) environment.get(Session.class);
                TaskImpl task = (TaskImpl) session.get(TaskImpl.class, taskId);
                HistoryTaskImpl htask = (HistoryTaskImpl) session.get(HistoryTaskImpl.class, taskId);
                if (task != null) {
                    for (int i = 0; i < params.length; i++) {
                        if (null != params[i]) {
                            switch (i) {
                                case 0:
                                    htask.setUserExtString1(params[i]);
                                    task.setUserExtString1(params[i]);
                                    break;
                                case 1:
                                    htask.setUserExtString2(params[i]);
                                    task.setUserExtString2(params[i]);
                                    break;
                                case 2:
                                    htask.setUserExtString3(params[i]);
                                    task.setUserExtString3(params[i]);
                                    break;
                                case 3:
                                    htask.setUserExtString4(params[i]);
                                    task.setUserExtString4(params[i]);
                                    break;
                                case 4:
                                    htask.setUserExtString5(params[i]);
                                    task.setUserExtString5(params[i]);
                                    break;
                                case 5:
                                    htask.setUserExtString6(params[i]);
                                    task.setUserExtString6(params[i]);
                                    break;
                                case 6:
                                    htask.setUserExtString7(params[i]);
                                    task.setUserExtString7(params[i]);
                                    break;
                                case 7:
                                    htask.setUserExtString8(params[i]);
                                    task.setUserExtString8(params[i]);
                            }

                        }
                    }
                }

                session.update(htask);
                session.update(task);
                return null;
            }
        });
    }

    public List<EntrustTaskHistoryVO> getTaskEntrustHistory(String taskId) throws IFinFlowException {
        List<HistoryComment> res = this.taskService.getTaskComments(taskId);
        return BeanUtil.createEntrustTaskHistoryVO(taskId, res);
    }
}