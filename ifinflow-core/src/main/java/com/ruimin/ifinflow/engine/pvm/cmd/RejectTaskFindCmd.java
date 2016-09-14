package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.flowmodel.vo.RejectActivityVO;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;

import com.ruimin.ifinflow.util.DefinitionUtil;
import com.ruimin.ifinflow.util.TemplateCacheUtil;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.HistoryService;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.history.HistoryActivityInstance;

import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;

import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.session.RepositorySession;
import org.jbpm.pvm.internal.task.TaskImpl;


public class RejectTaskFindCmd extends AbstractCommand<List<RejectActivityVO>> {
    private static final long serialVersionUID = 1L;
    private String taskId;
    private HistoryService historyService;

    public RejectTaskFindCmd(String taskId, HistoryService historyService) {
        this.taskId = taskId;
        this.historyService = historyService;
    }

    public List<RejectActivityVO> execute(Environment environment) throws Exception {
        List<RejectActivityVO> resultList = null;

        DbSession dbSession = (DbSession) environment.get(DbSession.class);
        TaskImpl task = (TaskImpl) dbSession.get(TaskImpl.class, this.taskId);
        if (task == null) {
            throw new IFinFlowException(103003, new Object[]{this.taskId});
        }

        RepositorySession repositorySession = (RepositorySession) environment.get(RepositorySession.class);

        ProcessDefinitionImpl procDef = repositorySession.findProcessDefinitionById(task.getProcessInstance().getProcessDefinitionId());


        ExecutionImpl ei = task.getProcessInstance();
        ProcessDefinitionImpl superProcDef = null;
        if (!procDef.hasActivity(task.getActivityName())) {
            superProcDef = DefinitionUtil.getSuperDefinitionImpl(task.getExecution());
            ei = ei.getSuperProcessExecution().getProcessInstance();
        }
        List<String> priorNodeListInPd = null;
        if (superProcDef == null) {
            priorNodeListInPd = getPriorNodeListByProcDef(task.getActivityName(), procDef);
        } else {
            priorNodeListInPd = getPriorNodeListByProcDef(task.getActivityName(), superProcDef);
        }


        List<HistoryActivityInstance> activityList = this.historyService.createHistoryActivityInstanceQuery().processId(ei.getDbid()).list();
        if ((activityList == null) || (activityList.isEmpty())) {
            throw new IFinFlowException(109002, new Object[0]);
        }

        List<HistoryActivityInstance> tmpActivityList = null;

        ExecutionImpl parentExecution = task.getExecution().getParent();
        Collection<ExecutionImpl> tmpExecutions;
        Iterator i$;
        if (parentExecution != null) {
            Collection<ExecutionImpl> executions = parentExecution.getExecutions();
            tmpExecutions = null;
            if ((executions != null) && (!executions.isEmpty())) {
                tmpExecutions = new ArrayList();
                tmpExecutions.addAll(executions);
                tmpExecutions.remove(task.getExecution());

                tmpActivityList = new ArrayList();
                tmpActivityList.addAll(activityList);

                for (HistoryActivityInstance hai : tmpActivityList) {
                    String actExecutionId = hai.getExecutionId();
                    if ((tmpExecutions != null) && (!tmpExecutions.isEmpty())) {
                        for (ExecutionImpl tmpExecution : tmpExecutions) {
                            if ((tmpExecution != null) &&
                                    (StringUtils.equals(actExecutionId, tmpExecution.getExecutionImplId())))
                                activityList.remove(hai);
                        }
                    }
                }
            }
        }
        List<String> rejectList = getPriorNodeList(priorNodeListInPd, activityList);
        ExecutionImpl superExecution = task.getProcessInstance().getSuperProcessExecution();
        if (superExecution != null) {
            activityList = this.historyService.createHistoryActivityInstanceQuery().processId(superExecution.getProcessInstance().getDbid()).list();

            for (int i = activityList.size() - 1; i >= 0; i--) {
                if (((HistoryActivityInstanceImpl) activityList.get(i)).getType().equals("12")) {
                    priorNodeListInPd = getPriorNodeListByProcDef(((HistoryActivityInstance) activityList.get(i)).getActivityName(), superExecution.getProcessInstance().getProcessDefinition());

                    rejectList.addAll(getPriorNodeList(priorNodeListInPd, activityList));
                    break;
                }
            }
        }
        resultList = new ArrayList();
        for (String activity : rejectList) {
            String[] act = activity.split(";");
            RejectActivityVO vo = new RejectActivityVO();
            vo.setActivityId(act[0]);
            vo.setActivityName(act[1]);
            if (act.length > 2) {
                vo.setOwnerId(act[2]);
            } else {
                vo.setOwnerId("");
            }

            resultList.add(vo);
        }


        return resultList;
    }

    private List<String> getPriorNodeListByProcDef(String nodeId, ProcessDefinitionImpl procDef) {
        List<String> priorNodeListInPd = new ArrayList();

        getPriorNodeListInProcDef(procDef.getActivity(nodeId), procDef, priorNodeListInPd);
        if ((priorNodeListInPd.isEmpty()) && (priorNodeListInPd.size() <= 1)) {
            throw new IFinFlowException(109008, new Object[0]);
        }
        if (((String) priorNodeListInPd.get(0)).contains(nodeId)) {
            if (!procDef.getActivity(nodeId).getType().equals("12")) {
                priorNodeListInPd.remove(0);
            }
        }

        return priorNodeListInPd;
    }

    private List<String> getPriorNodeList(List<String> priorNodesInPd, List<HistoryActivityInstance> activityList) {
        int size = priorNodesInPd.size();
        List<String> resultList = new ArrayList();

        for (int i = 0; i < size; i++) {
            isExistInAtivityList((String) priorNodesInPd.get(i), activityList, resultList);
        }
        return resultList;
    }

    private void isExistInAtivityList(String node, List<HistoryActivityInstance> activityList, List<String> resultList) {
        boolean result = false;
        String activityName = node.split(";")[0];
        Date tmpEndTime = null;
        String ownerId = "";
        HistoryTaskInstanceImpl htask = null;
        for (HistoryActivityInstance hisactivity : activityList) {
            if (activityName.equals(hisactivity.getActivityName())) {
                if ((activityName.startsWith("TaskNode_")) && ((tmpEndTime == null) || (tmpEndTime.before(hisactivity.getEndTime())))) {
                    htask = (HistoryTaskInstanceImpl) hisactivity;
                    tmpEndTime = hisactivity.getEndTime();
                    ownerId = htask.getHistoryTask().getOwnerId();
                    result = true;
                } else {
                    resultList.add(node + ";");
                    break;
                }
            }
        }
        if (result) {
            resultList.add(node + ";" + (ownerId == null ? "" : ownerId));
            result = false;
        }
    }

    private void getPriorNodeListInProcDef(ActivityImpl activityImpl, ProcessDefinitionImpl procDef, List<String> result) {
        String nodeId = activityImpl.getName();
        NodeVo nodevo = TemplateCacheUtil.getTemplateVo(procDef).getNodeVo(nodeId);


        if ((!StringUtils.equals("6", activityImpl.getType())) && (!StringUtils.equals("1", activityImpl.getType())) && (isRejectable(nodevo))) {


            StringBuilder sb = new StringBuilder();
            sb.append(nodeId);
            sb.append(";");
            sb.append(nodevo.getName());

            if (!result.contains(sb.toString())) {
                result.add(sb.toString());
            } else {
                return;
            }
        }

        List<? extends Transition> transitionList = activityImpl.getIncomingTransitions();


        if ((!transitionList.isEmpty()) && (!StringUtils.equals("1", activityImpl.getType()))) {
            for (Transition tran : transitionList) {
                if (!StringUtils.equals("1", ((ActivityImpl) tran.getSource()).getType())) {
                    getPriorNodeListInProcDef((ActivityImpl) tran.getSource(), procDef, result);
                }
            }
        }
    }

    private boolean isRejectable(NodeVo nodevo) {
        String rejectAuth = nodevo.getRejectAuth();
        if ((StringUtils.isNotBlank(rejectAuth)) &&
                (rejectAuth.length() == 2) && (rejectAuth.endsWith("1"))) {
            return true;
        }

        return false;
    }
}