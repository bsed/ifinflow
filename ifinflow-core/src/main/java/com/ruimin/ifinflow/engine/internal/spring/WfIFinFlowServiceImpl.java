
package com.ruimin.ifinflow.engine.internal.spring;


import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.engine.external.spring.WfIFinFlowService;
import com.ruimin.ifinflow.engine.flowmodel.Variable;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.util.BeanUtil;
import com.ruimin.ifinflow.engine.flowmodel.vo.*;
import com.ruimin.ifinflow.engine.internal.cal.vo.BusinessCalendarVo;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.engine.internal.log.LogCategory;
import com.ruimin.ifinflow.engine.internal.log.LogVO;
import com.ruimin.ifinflow.engine.internal.log.WorkflowLog;
import com.ruimin.ifinflow.engine.internal.log.vo.AppLog;
import com.ruimin.ifinflow.engine.internal.process.ProcessManager;
import com.ruimin.ifinflow.engine.internal.task.TaskManager;
import com.ruimin.ifinflow.engine.pvm.cmd.*;
import com.ruimin.ifinflow.model.flowmodel.external.IModelService;
import com.ruimin.ifinflow.model.flowmodel.manage.xml.XmlHandle;
import com.ruimin.ifinflow.model.flowmodel.xml.AssignPolicy;
import com.ruimin.ifinflow.model.flowmodel.xml.Node;
import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import com.ruimin.ifinflow.model.util.FileUtil;
import com.ruimin.ifinflow.model.util.JbpmUtil;
import com.ruimin.ifinflow.util.criterion.impl.CriteriaImpl;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.jbpm.api.*;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.pvm.internal.cal.BusinessCalendar;
import org.jbpm.pvm.internal.cal.BusinessCalendarImpl;
import org.jbpm.pvm.internal.cmd.SaveTaskCmd;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.w3c.dom.Element;

import java.util.*;


public class WfIFinFlowServiceImpl
        implements WfIFinFlowService {
    /*   93 */   private static final Logger logger = Logger.getLogger(WfIFinFlowServiceImpl.class);

    private ProcessEngine processEngine;

    private TaskService taskService;

    private ExecutionService executionService;

    private HistoryService historyService;

    private IModelService<?> modelService;
    private TaskManager taskManager;
    private ProcessManager processManager;


    public void setProcessEngine(ProcessEngine processEngine) {

        this.processEngine = processEngine;

        if (processEngine == null) {

            this.processEngine = JbpmUtil.buildProcessEngine();

        }

        this.taskManager = new TaskManager(this.processEngine);

        this.processManager = new ProcessManager(this.processEngine);

        this.executionService = processEngine.getExecutionService();

        this.taskService = processEngine.getTaskService();

        this.historyService = processEngine.getHistoryService();

        this.modelService = processEngine.getModelService();

    }


    public ProcessVO startProcess(String packageId, String templeteId, int version, WfStaffVO user, VariableSet vs, String subject)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("startProcess", new String[]{"packageId", "templeteId", "version", "user", "vs", "subject"}, new Object[]{packageId, templeteId, Integer.valueOf(version), user, vs, subject}));


        ProcessVO processvo = null;

        try {

            LogVO vo = new LogVO(LogCategory.API, "调用发起流程API", user.getStaffId(), user.getStaffName());


            WorkflowLog.dbLog(Level.INFO, vo);


            processvo = this.processManager.startProcess(packageId, templeteId, version, user, vs, subject);

        } catch (JbpmException e) {

            throw new IFinFlowException(102002, e, packageId, templeteId, Integer.valueOf(version));

        }


        return processvo;

    }


    public ProcessVO startProcess(String packageId, String templeteId, WfStaffVO user, VariableSet vs, String subject)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("startProcess", new String[]{"packageId", "templeteId", "user", "vs", "subject"}, new Object[]{packageId, templeteId, user, vs, subject}));


        ProcessVO processvo = null;

        try {

            LogVO vo = new LogVO(LogCategory.API, "调用发起流程API", user.getStaffId(), user.getStaffName());


            WorkflowLog.dbLog(Level.INFO, vo);


            processvo = this.processManager.startProcess(packageId, templeteId, 0, user, vs, subject);

        } catch (JbpmException e) {

            throw new IFinFlowException(102002, e, packageId, templeteId, "默认最新版本号");

        }


        return processvo;

    }


    public ProcessVO startProcess(String deployId, WfStaffVO user, VariableSet vs, String subject)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("startProcess", new String[]{"deployId", "user", "vs", "subject"}, new Object[]{deployId, user, vs, subject}));


        ProcessVO processvo = null;

        if (StringUtils.isEmpty(deployId)) {

            throw new IFinFlowException(102003, deployId);

        }

        processvo = this.processManager.startProcess(deployId, user, vs, subject);

        return processvo;

    }


    public void cancelProcess(String processId, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("cancelProcess", new String[]{"processId", "user"}, new Object[]{processId, user}));


            this.processManager.cancelProcess(processId, user);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102011, e);

        }

    }


    public void cancelProcessByTaskId(String taskId, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("cancelProcessByTaskId", new String[]{"taskId", "user"}, new Object[]{taskId, user}));


            ProcessVO process = getProcessByTaskId(taskId);

            this.processManager.cancelProcess(process.getProcessId(), user);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102011, e);

        }

    }


    public void suspendProcess(String processId, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("suspendProcess", new String[]{"processId", "user"}, new Object[]{processId, user}));


            this.processEngine.execute(new ProcessSuspendCmd(processId));

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102012, e);

        }

    }


    public void suspendProcessByTaskId(String taskId, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("suspendProcessByTaskId", new String[]{"taskId", "user"}, new Object[]{taskId, user}));


            this.processManager.suspendProcessByTaskId(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102012, e);

        }

    }


    public void resumeProcess(String processId, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("resumeProcess", new String[]{"processId", "user"}, new Object[]{processId, user}));


            this.processEngine.execute(new ProcessResumeCmd(processId));

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102013, e);

        }

    }


    public void resumeProcessByTaskId(String taskId, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("resumeProcessByTaskId", new String[]{"taskId", "user"}, new Object[]{taskId, user}));


            this.processManager.resumeProcessByTaskId(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102013, e);

        }

    }


    public void updateProcessSubject(String processId, String subject)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("updateProcessSubject", new String[]{"processId", "subject"}, new Object[]{processId, subject}));


            this.processManager.updateProcessSubject(processId, subject);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102020, e);

        }

    }


    public void takeTask(String taskId, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("takeTask", new String[]{"taskId", "user"}, new Object[]{taskId, user}));


            this.taskManager.takeTask(taskId, user);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103015, e, taskId);

        }

    }


    public int takeTasks(TaskType taskType, WfStaffVO user, int count) throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("takeTasks", new String[]{"taskids", "user"}, new Object[]{taskType, user, Integer.valueOf(count)}));


        return this.processEngine.execute(new TasksAssigneeCmd(taskType, user != null ? user.getWfUserInfo() : null, count)).intValue();

    }


    public void commitTask(String taskId, WfStaffVO user, VariableSet variables)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("commitTask", new String[]{"taskId", "user", "variables"}, new Object[]{taskId, user, variables}));


            commitTask(taskId, user, variables, null);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            if (!(e instanceof StaleStateException)) {

                throw new IFinFlowException(103019, e, taskId);

            }

        }

    }


    public void commitTask(String taskId, WfStaffVO user, VariableSet vs, String memo)
            throws IFinFlowException {

        logger.info("completeTask begin ===============================================");


        WorkflowLog.dbLog(Level.INFO, new AppLog("commitTask", new String[]{"taskId", "user", "vs", "memo"}, new Object[]{taskId, user, vs, memo}));


        TaskImpl task = (TaskImpl) this.taskService.getTask(taskId);

        if (task == null) {

            throw new IFinFlowException(103002, taskId);

        }

        try {

            this.taskManager.completeTask(task, null, 16, user, vs, memo);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            if (!(e instanceof StaleStateException)) {

                throw new IFinFlowException(103019, e, taskId);

            }

        }


        logger.info("completeTask end =============================================");

    }


    public void rejectTask(String taskId, String targetName, WfStaffVO user, VariableSet vs)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("rejectTask", new String[]{"taskId", "targetName", "user", "vs"}, new Object[]{taskId, targetName, user, vs}));


            rejectTask(taskId, targetName, user, vs, null);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103020, e, taskId);

        }

    }


    public void rejectTask(String taskId, String targetName, WfStaffVO user, VariableSet vs, String memo)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("rejectTask", new String[]{"taskId", "targetName", "user", "vs", "memo"}, new Object[]{taskId, targetName, user, vs, memo}));


            this.taskManager.rejectTask(taskId, targetName, user, vs, memo);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103020, e, taskId);

        }

    }


    public void retractTask(String taskId, WfStaffVO user, VariableSet vs)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("rejectTask", new String[]{"taskId", "user", "vs"}, new Object[]{taskId, user, vs}));


            this.taskManager.retractTask(taskId, user, vs);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103021, e, taskId);

        }

    }


    public void returnTask(String taskId, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("returnTask", new String[]{"taskId", "user"}, new Object[]{taskId, user}));


            this.taskManager.returnTask(taskId, user);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103004, e, taskId);

        }

    }


    public void skipTask(String taskId, String targetName, WfStaffVO user, VariableSet vs)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("skipTask", new String[]{"taskId", "targetName", "user", "vs"}, new Object[]{taskId, targetName, user, vs}));


            skipTask(taskId, targetName, user, vs, null);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103022, e, taskId);

        }

    }


    public void skipTask(String taskId, String targetName, WfStaffVO user, VariableSet vs, String memo)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("skipTask", new String[]{"taskId", "targetName", "user", "vs", "memo"}, new Object[]{taskId, targetName, user, vs, memo}));


            this.taskManager.skipTask(taskId, targetName, user, vs, memo);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103022, e, taskId);

        }

    }


    public void entrustTask(String taskId, WfStaffVO oldUser, WfStaffVO toUser)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("entrustTask", new String[]{"taskId", "oldUser", "toUser"}, new Object[]{taskId, oldUser, toUser}));


            this.taskManager.entrustTask(taskId, oldUser, toUser, "");

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103005, e, taskId);

        }

    }


    public void entrustTask(String taskId, WfStaffVO oldUser, WfStaffVO toUser, String memo) throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("entrustTask", new String[]{"taskId", "oldUser", "toUser"}, new Object[]{taskId, oldUser, toUser}));


            this.taskManager.entrustTask(taskId, oldUser, toUser, memo);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103005, e, taskId);

        }

    }


    public void setTaskPriority(String taskId, WfStaffVO user, int priority)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("setTaskPriority", new String[]{"taskId", "user", "priority"}, new Object[]{taskId, user, Integer.valueOf(priority)}));


        if ((priority > 0) && (priority < 11)) {

            try {

                TaskImpl task = (TaskImpl) this.taskService.getTask(taskId);

                task.setPriority(priority);

                this.processEngine.execute(new SaveTaskCmd(task));

            } catch (IFinFlowException e) {

                throw e;

            } catch (Exception e) {

                throw new IFinFlowException(101010, e);

            }

        } else {

            throw new IFinFlowException(110007);

        }

    }


    public void signal(String processId, String nodeId) {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("signal", new String[]{"processId", "nodeId"}, new Object[]{processId, nodeId}));


            this.taskManager.signal(processId, nodeId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(101009, e);

        }

    }


    public List<ProcessExceptionVO> searchProcessException(CriteriaImpl criteria)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchProcessException", new String[]{"criteria"}, new Object[]{criteria}));


            return (List) this.processEngine.execute(new CriteriaQueryCmd(getConditionString(criteria, ProcessExceptionVO.class.getName(), false), criteria.getFirstResult().intValue(), criteria.getMaxResult().intValue()));


        } catch (IFinFlowException e) {


            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(101008, e);

        }

    }


    public List<TaskVO> getTodoTask(WfStaffVO user, int startNum, int pageSize)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getTodoTask", new String[]{"user", "startNum", "pageSize"}, new Object[]{user, Integer.valueOf(startNum), Integer.valueOf(pageSize)}));


            return this.taskManager.getTodoTask(user, startNum, pageSize);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103036, e);

        }

    }


    public long getTodoTaskCount(WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getTodoTaskCount", new String[]{"user"}, new Object[]{user}));


            return this.taskManager.getTodoTaskCount(user);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103035, e);

        }

    }


    public List<TaskVO> getDoneTask(WfStaffVO user, int startNum, int pageSize)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getDoneTask", new String[]{"user", "startNum", "pageSize"}, new Object[]{user, Integer.valueOf(startNum), Integer.valueOf(pageSize)}));


            return this.taskManager.getDoneTask(user, startNum, pageSize);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103034, e);

        }

    }


    public long getDoneTaskCount(WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getDoneTaskCount", new String[]{"user"}, new Object[]{user}));


            return this.taskManager.getDoneTaskCount(user);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103033, e);

        }

    }


    public List<TaskVO> searchTask(WfStaffVO user, int startNum, int pageSize)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTask", new String[]{"user", "startNum", "pageSize"}, new Object[]{user, Integer.valueOf(startNum), Integer.valueOf(pageSize)}));


            return this.taskManager.searchTask(user, startNum, pageSize);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103031, e);

        }

    }


    public long searchTaskCount(WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTaskCount", new String[]{"user"}, new Object[]{user}));


            return this.taskManager.searchTaskCount(user);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103024, e);

        }

    }


    public List<TaskVO> searchTask(String staffId, String nodeName, Date createdDatetimeStart, Date createdDatetimeEnd, int taskStatus, int startNumber, int pageSize)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTask", new String[]{"staffId", "nodeName", "createdDatetimeStart", "createdDatetimeEnd", "taskStatus", "startNumber", "pageSize"}, new Object[]{staffId, nodeName, createdDatetimeStart, createdDatetimeEnd, Integer.valueOf(taskStatus), Integer.valueOf(startNumber), Integer.valueOf(pageSize)}));


            return this.taskManager.searchTask(staffId, null, nodeName, createdDatetimeStart, createdDatetimeEnd, taskStatus, startNumber, pageSize, null, null, null);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103031, e);

        }

    }


    public long searchTaskCount(String staffId, String nodeName, Date createdDatetimeStart, Date createdDatetimeEnd, int taskStatus)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTaskCount", new String[]{"staffId", "nodeName", "createdDatetimeStart", "createdDatetimeEnd", "taskStatus"}, new Object[]{staffId, nodeName, createdDatetimeStart, createdDatetimeEnd, Integer.valueOf(taskStatus)}));


            return this.taskManager.searchTaskCount(staffId, null, nodeName, createdDatetimeStart, createdDatetimeEnd, taskStatus, null, null, null);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103032, e);

        }

    }


    public List<TaskVO> searchTask(String staffId, String nodeId, Date createdDatetimeStart, Date createdDatetimeEnd, int taskStatus, int startNumber, int pageSize, String processId, String packageId, String templateId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTask", new String[]{"staffId", "nodeId", "createdDatetimeStart", "createdDatetimeEnd", "taskStatus", "startNumber", "pageSize", "processId", "packageId", "templateId"}, new Object[]{staffId, nodeId, createdDatetimeStart, createdDatetimeEnd, Integer.valueOf(taskStatus), Integer.valueOf(startNumber), Integer.valueOf(pageSize), processId, packageId, templateId}));


            return this.taskManager.searchTask(staffId, nodeId, null, createdDatetimeStart, createdDatetimeEnd, taskStatus, startNumber, pageSize, processId, packageId, templateId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103031, e);

        }

    }


    public long searchTaskCount(String staffId, String nodeId, Date createdDatetimeStart, Date createdDatetimeEnd, int taskStatus, String processId, String packageId, String templateId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTaskCount", new String[]{"staffId", "nodeId", "createdDatetimeStart", "createdDatetimeEnd", "taskStatus", "processId", "packageId", "templateId"}, new Object[]{staffId, nodeId, createdDatetimeStart, createdDatetimeEnd, Integer.valueOf(taskStatus), processId, packageId, templateId}));


            return this.taskManager.searchTaskCount(staffId, nodeId, null, createdDatetimeStart, createdDatetimeEnd, taskStatus, processId, packageId, templateId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103031, e);

        }

    }


    public TaskVO getTask(String taskId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getTask", new String[]{"taskId"}, new Object[]{taskId}));


            HistoryTaskImpl task = (HistoryTaskImpl) this.historyService.createHistoryTaskQuery().taskId(taskId).uniqueResult();


            if (task != null) {

                HistoryProcessInstance hpi = this.historyService.createHistoryProcessInstanceQuery().processId(task.getHistoryProcessInstanceId()).uniqueResult();


                return BeanUtil.createTaskVO(task, hpi);

            }

            return null;

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103030, e, taskId);

        }

    }


    public List<TaskVO> getTodoTasksByProcessId(String processId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getTasksByProcessId", new String[]{"processId"}, new Object[]{processId}));


            List<HistoryTask> list = this.historyService.createHistoryTaskQuery().orderDesc("priority").orderDesc("createTime").processId(processId).statusCondetion("in(1,2) ").list();


            return BeanUtil.createTaskVOs(list);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103029, e, processId);

        }

    }


    public List<TaskVO> getAllTasksByProcessId(String processId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getTasksByProcessId", new String[]{"processId"}, new Object[]{processId}));


            List<HistoryTask> list = this.historyService.createHistoryTaskQuery().orderDesc("priority").orderDesc("createTime").processId(processId).list();


            return BeanUtil.createTaskVOs(list);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103028, e, processId);

        }

    }


    public List<TaskVO> searchTask(CriteriaImpl criteria)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTask", new String[]{"criteria"}, new Object[]{criteria}));


            return BeanUtil.createTaskVOs(this.processEngine.execute(new CriteriaQueryCmd(getConditionString(criteria, HistoryTaskImpl.class.getName(), false), criteria.getFirstResult().intValue(), criteria.getMaxResult().intValue())));


        } catch (IFinFlowException e) {


            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103027, e);

        }

    }


    public String searchTaskUrl(String taskId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTaskUrl", new String[]{"taskId"}, new Object[]{taskId}));


            TaskVO task = getTask(taskId);

            if (task == null) {

                throw new IFinFlowException(103003, taskId);

            }

            return task.getSourceUrl();

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(101007, e, taskId);

        }

    }


    public ProcessVO getProcess(final String processId) throws IFinFlowException {
        try {
            WorkflowLog.dbLog(Level.INFO, new AppLog("getProcess", new String[]{"processId"}, new Object[]{processId}));
            if (StringUtils.isEmpty(processId)) {
                throw new IFinFlowException(102005);
            }
            return BeanUtil.createProcessVO(this.processEngine.execute(new Command<HistoryProcessInstance>() {
                public HistoryProcessInstance execute(Environment environment) throws Exception {
                    Session session = environment.get(Session.class);
                    HistoryProcessInstanceImpl process = (HistoryProcessInstanceImpl) session.get(HistoryProcessInstanceImpl.class, processId);
                    session.refresh(process);
                    return process;
                }
            }));
        } catch (IFinFlowException e) {
            throw e;
        } catch (Exception e) {
            throw new IFinFlowException(102016, e, processId);
        }
    }


    public List<ProcessVO> getProcess(String packageId, String templeteId, int version, int startNum, int pageSize)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getProcessByTemplete", new String[]{"packageId", "templeteId", "version", "startNum", "pageSize"}, new Object[]{packageId, templeteId, Integer.valueOf(version), Integer.valueOf(startNum), Integer.valueOf(pageSize)}));


            if (version <= 0) {

                version = this.modelService.getLastVersion(packageId, templeteId);

            }


            HistoryProcessInstanceQuery query = this.historyService.createHistoryProcessInstanceQuery().packageId(packageId).templeteId(templeteId).templeteVersion(version);


            if ((startNum >= 0) && (pageSize > 0)) {

                query.page(startNum, pageSize).list();

            }

            List<HistoryProcessInstance> list = query.list();

            return BeanUtil.createProcessVOs(list);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102016, e);

        }

    }


    public long getProcessCount(String packageId, String templeteId, int version)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getProcessByTempleteCount", new String[]{"packageId", "templeteId", "version"}, new Object[]{packageId, templeteId, Integer.valueOf(version)}));


            if (version <= 0) {

                version = this.modelService.getLastVersion(packageId, templeteId);

            }

            return this.historyService.createHistoryProcessInstanceQuery().packageId(packageId).templeteId(templeteId).templeteVersion(version).count();

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102019, e, packageId, templeteId, Integer.valueOf(version));

        }

    }


    public ProcessVO getProcessByTaskId(String taskId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getProcessByTaskId", new String[]{"taskId"}, new Object[]{taskId}));


            HistoryTaskImpl task = (HistoryTaskImpl) this.historyService.createHistoryTaskQuery().taskId(taskId).uniqueResult();


            HistoryProcessInstance process = task.getHistoryProcessInstance();

            return BeanUtil.createProcessVO(process);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102018, e, taskId);

        }

    }


    public List<ProcessVO> getSubProcess(String processId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getSubProcess", new String[]{"processId"}, new Object[]{processId}));


            List<ExecutionImpl> list = this.processManager.getSubProcessInstance(processId);


            return BeanUtil.createProcessVOsByExecutions(list);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102017, e, processId);

        }

    }


    public List<ProcessVO> searchProcess(CriteriaImpl criteria)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchProcess", new String[]{"criteria"}, new Object[]{criteria}));


            return BeanUtil.createProcessVOs(this.processEngine.execute(new CriteriaQueryCmd(getConditionString(criteria, HistoryProcessInstanceImpl.class.getName(), false), criteria.getFirstResult().intValue(), criteria.getMaxResult().intValue())));


        } catch (IFinFlowException e) {


            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102016, e);

        }

    }


    public List<ActivityVO> searchActivity(CriteriaImpl criteria)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchActivity", new String[]{"criteria"}, new Object[]{criteria}));


            List<HistoryActivityInstance> hisActivitys = (List) this.processEngine.execute(new CriteriaQueryCmd(getConditionString(criteria, HistoryActivityInstanceImpl.class.getName(), false), criteria.getFirstResult().intValue(), criteria.getMaxResult().intValue()));


            return BeanUtil.createActivityVOs(hisActivitys);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(101006, e);

        }

    }


    public void setProcessVariable(String processId, Variable var, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("setProcessVariable", new String[]{"processId", "var", "user"}, new Object[]{processId, var, user}));


            this.processManager.setVariable(processId, var);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(104011, e);

        }

    }


    public void setProcessVariables(String processId, VariableSet vs, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("setProcessVariables", new String[]{"processId", "vs", "user"}, new Object[]{processId, vs, user}));


            this.processManager.setVariables(processId, vs);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(104010, e);

        }

    }


    public void setProcessVariableByTaskId(String taskId, Variable var, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("setProcessVariableByTaskId", new String[]{"taskId", "var", "user"}, new Object[]{taskId, var, user}));


            this.taskManager.setVariableByTask(taskId, var);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(104011, e);

        }

    }


    public void setProcessVariablesByTaskId(String taskId, VariableSet vs, WfStaffVO user)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("setProcessVariablesByTaskId", new String[]{"taskId", "vs", "user"}, new Object[]{taskId, vs, user}));


            this.taskManager.setVariablesByTask(taskId, vs);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(104010, e);

        }

    }


    public VariableSet getProcessVariables(String processId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getProcessVariables", new String[]{"processId"}, new Object[]{processId}));


            return this.processManager.getVariableSet(processId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(104009, e);

        }

    }


    public VariableSet getProcessVariables(String processId, Set<String> variableNames)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getProcessVariables", new String[]{"processId", "variableNames"}, new Object[]{processId, variableNames}));


            return this.processManager.getVariableSet(processId, variableNames);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(104009, e);

        }

    }


    public VariableSet getProcessVariablesByTaskId(String taskId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getProcessVariablesByTaskId", new String[]{"taskId"}, new Object[]{taskId}));


            return this.taskManager.getVariableSet(taskId, null);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(104009, e);

        }

    }


    public VariableSet getProcessVariablesByTaskId(String taskId, Set<String> variableNames)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getProcessVariablesByTaskId", new String[]{"taskId", "variableNames"}, new Object[]{taskId, variableNames}));


            return this.taskManager.getVariableSet(taskId, variableNames);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(104009, e);

        }

    }


    public List<String> getPackageList()
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getPackageList", new String[0], new Object[0]));


            List<TemplatePackage> list = this.modelService.getAllPackage();

            List<String> packList = new ArrayList();

            for (TemplatePackage tpackage : list) {

                packList.add(tpackage.getTemplatePackageId());

            }

            return packList;

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(107007, e);

        }

    }


    public List<DefinitionVO> searchDeployedTemplates(CriteriaImpl criteria)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("searchDeployedTemplates", new String[]{"criteria"}, new Object[]{criteria}));


        return BeanUtil.createDefinitionVOs(this.processEngine.execute(new CriteriaQueryCmd(getConditionString(criteria, Template.class.getName(), false), criteria.getFirstResult().intValue(), criteria.getMaxResult().intValue())));

    }


    public Map<String, List<ProcessTraceVO>> getProcessTrace(String processId)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("getProcessTrace", new String[]{"processId"}, new Object[]{processId}));


        if (StringUtils.isEmpty(processId)) {

            throw new IFinFlowException(102005);

        }

        Map<String, List<ProcessTraceVO>> map = new HashMap();

        try {

            map = this.taskManager.getProcessTrace(processId);


        } catch (IFinFlowException e) {


            throw e;

        }

        return map;

    }


    public List<String> getActivityNames(String processId) {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getActivityNames", new String[]{"processId"}, new Object[]{processId}));


            if (StringUtils.isEmpty(processId)) {

                throw new IFinFlowException(102005);

            }

            HistoryProcessInstanceImpl hisProcess = (HistoryProcessInstanceImpl) this.historyService.createHistoryProcessInstanceQuery().processId(processId).uniqueResult();


            if (hisProcess == null) {

                throw new IFinFlowException(102001, processId);

            }


            List<String> res = new ArrayList();


            if (hisProcess.getStatus().equals(Integer.valueOf(16))) {

                res.add(hisProcess.getEndActivityName());

            } else {

                List<HistoryActivityInstance> list = this.historyService.createHistoryActivityInstanceQuery().processId(processId).status(" in(1,2,3,256,512)").list();


                for (HistoryActivityInstance act : list) {

                    res.add(act.getActivityName());

                }

            }

            return res;

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102015, e, processId);

        }

    }


    public boolean checkTaskRetraction(String taskId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("checkTaskRetraction", new String[]{"taskId"}, new Object[]{taskId}));


            return this.taskManager.checkTaskRetraction(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103026, e, taskId);

        }

    }


    public List<String> getRejectableTargets(String taskId) throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getRejectableTargets", new String[]{"taskId"}, new Object[]{taskId}));


            return this.taskManager.getRejectNodeList(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103025, e, taskId);

        }

    }


    public List<RejectActivityVO> getRejectableTargetList(String taskId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getRejectableTargets", new String[]{"taskId"}, new Object[]{taskId}));


            return this.taskManager.getRejectList(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103025, e, taskId);

        }

    }

    public List<String> getSkipableTargets(String taskId)
            throws IFinFlowException {

        List<String> list = new ArrayList();

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getSkipableTargets", new String[]{"taskId"}, new Object[]{taskId}));


            list = this.taskManager.getSkipNode(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103016, e, taskId);

        }

        return list;

    }


    private String getConditionString(CriteriaImpl criteria, String className, boolean countFlag)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("getConditionString", new String[]{"criteria", "className"}, new Object[]{criteria, className}));


        String condition = criteria.getStaticWhereCondition();

        String orderBy = criteria.getOrderBy();

        StringBuilder hql = new StringBuilder();

        if (countFlag) {

            hql.append("select count(*) ");

        }

        hql.append("from ");

        hql.append(className);


        if (StringUtils.isNotEmpty(condition)) {

            if (HistoryTaskImpl.class.getName().equals(className)) {

                hql.append(" where ((assignMode = 3 and superTaskId is not null ) or assignMode <> 3 ) and ");


                hql.append(condition);

            } else {

                hql.append(" where ");

                hql.append(condition);


            }


        } else if (HistoryTaskImpl.class.getName().equals(className)) {

            hql.append(" where ((assignMode = 3 and superTaskId is not null ) or assignMode <> 3 )");

        }


        if (StringUtils.isNotEmpty(orderBy)) {

            hql.append(" order by ");

            hql.append(orderBy);

        }

        return hql.toString();

    }


    public long searchTaskCount(CriteriaImpl criteria)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchTaskCount", new String[]{"criteria"}, new Object[]{criteria}));


            return this.processEngine.execute(new QueryCountCmd(getConditionString(criteria, HistoryTaskImpl.class.getName(), true))).longValue();

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103024, e);

        }

    }


    public long searchProcessCount(CriteriaImpl criteria)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchProcessCount", new String[]{"criteria"}, new Object[]{criteria}));


            return this.processEngine.execute(new QueryCountCmd(getConditionString(criteria, HistoryProcessInstanceImpl.class.getName(), true))).longValue();

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(102014, e);

        }

    }

    public long searchActivityCount(CriteriaImpl criteria)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("searchActivityCount", new String[]{"criteria"}, new Object[]{criteria}));


            return this.processEngine.execute(new QueryCountCmd(getConditionString(criteria, HistoryActivityInstanceImpl.class.getName(), true))).longValue();


        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(101005, e);

        }

    }


    public List<String> getAssigneeByTaskId(String taskId)
            throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getTemplateNodeVO", new String[]{"taskId"}, new Object[]{taskId}));


            return this.taskManager.getAssigneeByTaskId(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103023, e, taskId);

        }

    }


    public List<TemplateNodeVO> getTemplateNodeList(String packageId, String templateId, int version, String nodeType) {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getTemplateNodeVO", new String[]{"packageId", "templateId", "version"}, new Object[]{packageId, templateId, Integer.valueOf(version)}));


            List<TemplateNodeVO> list = new ArrayList();

            List<Node> l = this.modelService.findNodeList(packageId, templateId, version, nodeType);


            TemplateNodeVO tnvo = null;

            for (Node n : l) {

                tnvo = new TemplateNodeVO();

                tnvo.setNodeId(n.getNodeId());

                tnvo.setNodeName(n.getName());

                list.add(tnvo);

            }


            return list;

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(109013, e);

        }

    }


    public String getNodeNameBeforeCurrNode(String taskId)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("getNodeNameBeforeCurrNode", new String[]{"taskId"}, new Object[]{taskId}));

        try {

            return this.taskManager.getNodeNameBeforeCurrNode(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(109012, e);

        }

    }


    public String getFirstNodeNameBeforeCurrNode(String taskId)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("getFirstNodeNameBeforeCurrNode", new String[]{"taskId"}, new Object[]{taskId}));


        try {

            return this.taskManager.getFirstNodeNameBeforeCurrNode(taskId);

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(109011, e);

        }

    }


    public List<TaskVO> getTodoTask(WfStaffVO user, String nodeName, String templateId, int status, int startNum, int pageSize, String... extendValue)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("getTodoTask", new String[]{"user", "nodeName", "templateId", "status", "startNum", "pageSize", "extendValue"}, new Object[]{user, nodeName, templateId, Integer.valueOf(status), Integer.valueOf(startNum), Integer.valueOf(pageSize), Arrays.toString(extendValue)}));


        return this.taskManager.getTodoTask(user, nodeName, templateId, status, startNum, pageSize, extendValue);

    }


    public long getTodoTaskCount(WfStaffVO user, String nodeName, String templateId, int status, String... extendValue)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("getTodoTaskCount", new String[]{"user", "nodeName", "templateId", "status", "extendValue"}, new Object[]{user, nodeName, templateId, Integer.valueOf(status), Arrays.toString(extendValue)}));


        return this.taskManager.getTodoTaskCount(user, nodeName, templateId, status, extendValue);

    }


    public byte[] getTemplatePicture(String packageId, String templateId, int version)
            throws IFinFlowException {

        return this.modelService.getTemplatePicture(packageId, templateId, version);

    }


    public void updateDefaultRejectValue(String taskId, String defaultRejectValue)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("setDefaultRejectValue", new String[]{"taskId", "defaultRejectValue"}, new Object[]{taskId, defaultRejectValue}));


        this.taskManager.updateDefaultRejectValue(taskId, defaultRejectValue);

    }


    public void updateBusinessCalendar(BusinessCalendarVo businessCalendarVo)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("setBusinessCalendar", new String[]{"businessCalendarVo"}, new Object[]{businessCalendarVo}));


        if (businessCalendarVo != null) {

            StringBuilder businessCalendarStr = new StringBuilder();

            businessCalendarStr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("<jbpm-configuration>").append("<process-engine-context>").append("<business-calendar>");


            setWorkDay(businessCalendarVo, businessCalendarStr);

            String[] holidays = businessCalendarVo.getHolidays();

            for (int i = 0; i < holidays.length; i++) {

                businessCalendarStr.append("<holiday period=\"").append(holidays[i]).append("\"/>");

            }


            String nonworkingdays = businessCalendarVo.getNonworkingdays();

            if (StringUtils.isNotBlank(nonworkingdays)) {

                businessCalendarStr.append("<nonworkingdays value=\"").append(nonworkingdays).append("\"/>");

            }


            businessCalendarStr.append("</business-calendar>").append("</process-engine-context>").append("</jbpm-configuration>");


            FileUtil.string2File(businessCalendarStr.toString(), "ifinflow.businesscalendar.cfg.xml");

        }

    }


    public boolean isHoliday(Date date)
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("isHoliday", new String[]{"date"}, new Object[]{date}));


        return ((BusinessCalendarImpl) this.processEngine.get(BusinessCalendar.class)).isHoliday(date);

    }


    public BusinessCalendarVo getBusinessCalendar()
            throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("getBusinessCalendar", new String[0], new Object[0]));


        BusinessCalendarVo businesscalendar = null;


        String businesscalendarStr = FileUtil.file2String("ifinflow.businesscalendar.cfg.xml", "UTF-8");


        Element root = XmlHandle.getRootElement(businesscalendarStr);

        if (null == root) {

            throw new IFinFlowException(110008);

        }


        Element businessCalendarEle = XmlUtil.element(XmlUtil.element(root, "process-engine-context"), "business-calendar");


        if (businessCalendarEle != null) {

            businesscalendar = new BusinessCalendarVo();


            setBusinessCalendarPros(businesscalendar, businessCalendarEle, "sunday");


            setBusinessCalendarPros(businesscalendar, businessCalendarEle, "monday");


            setBusinessCalendarPros(businesscalendar, businessCalendarEle, "tuesday");


            setBusinessCalendarPros(businesscalendar, businessCalendarEle, "wednesday");


            setBusinessCalendarPros(businesscalendar, businessCalendarEle, "thursday");


            setBusinessCalendarPros(businesscalendar, businessCalendarEle, "friday");


            setBusinessCalendarPros(businesscalendar, businessCalendarEle, "saturday");


            List<Element> holidayElementList = XmlUtil.elements(businessCalendarEle, "holiday");


            int holidaySize = holidayElementList.size();

            if ((holidayElementList != null) && (holidaySize > 0)) {

                StringBuilder holidays = new StringBuilder();

                for (int i = 0; i < holidaySize; i++) {

                    String period = XmlUtil.attribute(holidayElementList.get(i), "period");


                    if (StringUtils.isNotBlank(period)) {

                        holidays.append(period).append(",");

                    }

                }

                int index = holidays.lastIndexOf(",");

                if (index > 0) {

                    holidays.deleteCharAt(index);

                    businesscalendar.setHolidayStr(holidays.toString());

                }

            }


            Element nonworkingdaysEle = XmlUtil.element(businessCalendarEle, "nonworkingdays");


            if (nonworkingdaysEle != null) {

                String nonworkingdays = XmlUtil.attribute(nonworkingdaysEle, "value");

                if (StringUtils.isNotBlank(nonworkingdays)) {

                    businesscalendar.setNonworkingdays(nonworkingdays);

                }

            }

        }

        return businesscalendar;

    }


    private void setBusinessCalendarPros(BusinessCalendarVo businesscalendar, Element businessCalendarEle, String dayOfWeekName) {

        Element dayElement = XmlUtil.element(businessCalendarEle, dayOfWeekName);


        if (dayElement != null) {

            String dayHours = XmlUtil.attribute(dayElement, "hours");

            if (StringUtils.isNotBlank(dayHours)) {

                if ("sunday".equals(dayOfWeekName)) {

                    businesscalendar.setSunday(dayHours);

                } else if ("monday".equals(dayOfWeekName)) {

                    businesscalendar.setMonday(dayHours);

                } else if ("tuesday".equals(dayOfWeekName)) {

                    businesscalendar.setTuesday(dayHours);

                } else if ("wednesday".equals(dayOfWeekName)) {

                    businesscalendar.setWednesday(dayHours);

                } else if ("thursday".equals(dayOfWeekName)) {

                    businesscalendar.setThursday(dayHours);

                } else if ("friday".equals(dayOfWeekName)) {

                    businesscalendar.setFriday(dayHours);

                } else if ("saturday".equals(dayOfWeekName)) {

                    businesscalendar.setSaturday(dayHours);

                }

            }

        }

    }


    private void setWorkDay(BusinessCalendarVo businessCalendarVo, StringBuilder businessCalendarStr) {

        if (StringUtils.isNotBlank(businessCalendarVo.getSunday())) {

            businessCalendarStr.append("<sunday hours=\"").append(businessCalendarVo.getSunday()).append("\" />");

        }


        if (StringUtils.isNotBlank(businessCalendarVo.getMonday())) {

            businessCalendarStr.append("<monday hours=\"").append(businessCalendarVo.getMonday()).append("\" />");

        }


        if (StringUtils.isNotBlank(businessCalendarVo.getTuesday())) {

            businessCalendarStr.append("<tuesday hours=\"").append(businessCalendarVo.getTuesday()).append("\" />");

        }


        if (StringUtils.isNotBlank(businessCalendarVo.getWednesday())) {

            businessCalendarStr.append("<wednesday hours=\"").append(businessCalendarVo.getWednesday()).append("\" />");

        }


        if (StringUtils.isNotBlank(businessCalendarVo.getThursday())) {

            businessCalendarStr.append("<thursday hours=\"").append(businessCalendarVo.getThursday()).append("\" />");

        }


        if (StringUtils.isNotBlank(businessCalendarVo.getFriday())) {

            businessCalendarStr.append("<friday hours=\"").append(businessCalendarVo.getFriday()).append("\" />");

        }


        if (StringUtils.isNotBlank(businessCalendarVo.getSaturday())) {

            businessCalendarStr.append("<saturday hours=\"").append(businessCalendarVo.getSaturday()).append("\" />");

        }

    }


    public void addTask(String processId, String nodeId, List<String> staffIds, String exitType, int exitCount)
            throws IFinFlowException {

        this.taskManager.addSubTasks(processId, nodeId, staffIds, exitType, exitCount);

    }


    public void addTask(String taskId, List<String> staffIds, String exitType, int exitCount)
            throws IFinFlowException {

        this.taskManager.addSubTasks(taskId, staffIds, exitType, exitCount);

    }


    public boolean deleteTask(String processId, String nodeId, List<String> staffIds, String exitType, int exitCount)
            throws IFinFlowException {

        return this.taskManager.deleteSubTasks(processId, nodeId, staffIds, exitType, exitCount);

    }


    public boolean deleteTask(String taskId, List<String> staffIds, String exitType, int exitCount)
            throws IFinFlowException {

        return this.taskManager.deleteSubTasks(taskId, staffIds, exitType, exitCount);

    }


    public void updateTaskExtendsField(String taskId, String... param)
            throws IFinFlowException {

        this.taskManager.updateTaskExtendsField(taskId, param);

    }


    public TaskVO getTaskvoWithAssign(String taskId) throws IFinFlowException {

        try {

            WorkflowLog.dbLog(Level.INFO, new AppLog("getTaskvoWithAssign", new String[]{"taskId"}, new Object[]{taskId}));


            HistoryTaskImpl task = (HistoryTaskImpl) this.historyService.createHistoryTaskQuery().taskId(taskId).uniqueResult();


            if (task != null) {

                HistoryProcessInstance hpi = this.historyService.createHistoryProcessInstanceQuery().processId(task.getHistoryProcessInstanceId()).uniqueResult();


                TaskVO taskvo = BeanUtil.createTaskVO(task, hpi);


                if (task.getStatus().intValue() == 1) {

                    taskvo.setAssignRoleId(taskvo.getOwnerRoleId());

                    taskvo.setAssignUnitId(taskvo.getOwnerUnitId());

                } else if (task.getStatus().intValue() == 2) {

                    String packageId = taskvo.getPackageId();

                    String templateId = taskvo.getTemplateId();

                    int version = taskvo.getTemplateVersion();

                    String nodeId = taskvo.getNodeId();


                    AssignPolicy asp = this.processEngine.execute(new QueryTaskAssign(packageId, templateId, version, nodeId));


                    String unitId = null;

                    String assignResult = asp.getResult();

                    String roleFlag = "(岗位)=";

                    String orgFlag = "(机构)=";

                    String orgLevelFlag = "(机构级别)=";

                    if (((asp.getParticipantType() == 6) || (asp.getParticipantType() == 4)) && (!assignResult.contains("(机构)="))) {


                        unitId = UserExtendsReference.getIdentityAdapter().getUnitByStaffId(hpi.getInitiatorId()).getUnitId();

                    } else if (assignResult.indexOf("(机构级别)=") >= 0) {

                        unitId = assignResult.substring(assignResult.indexOf("(机构)=") + "(机构)=".length(), assignResult.indexOf("(机构级别)="));


                        String orgLevel = assignResult.substring(assignResult.indexOf("(机构级别)=") + "(机构级别)=".length(), assignResult.indexOf("(岗位)="));


                        unitId = UserExtendsReference.getIdentityAdapter().getUnit(unitId, orgLevel).getUnitId();

                    } else {

                        unitId = assignResult.substring(assignResult.indexOf("(机构)=") + "(机构)=".length(), assignResult.indexOf("(岗位)="));

                    }


                    taskvo.setAssignUnitId(unitId);

                    taskvo.setAssignRoleId(assignResult.substring(assignResult.indexOf("(岗位)=") + "(岗位)=".length()));

                }


                return taskvo;

            }

            return null;

        } catch (IFinFlowException e) {

            throw e;

        } catch (Exception e) {

            throw new IFinFlowException(103030, e, taskId);

        }

    }


    public void updateTaskAssign(final String taskId, final String staffId) throws IFinFlowException {

        this.processEngine.execute(new Command() {

            public Collection<String> execute(Environment environment) throws Exception {

                Session session = environment.get(Session.class);

                IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();

                IWfStaff staff = identityAdapter.getStaffById(staffId);

                if (staff == null) {

                    throw new IFinFlowException(105005, staffId);

                }

                IWfUnit unit = identityAdapter.getUnitByStaffId(staffId);


                if ((taskId != null) && (!"".equals(taskId.trim()))) {

                    String hql = "update " + TaskImpl.class.getName() + " t set t.status=" + 2 + ",t.assignee='" + staffId + "',t.ownerId='" + staffId + "',t.ownerUnitId='" + unit.getUnitId() + "' where t.dbid='" + taskId + "'";

                    session.createQuery(hql).executeUpdate();

                    hql = "update " + HistoryTaskImpl.class.getName() + " t set t.status=" + 2 + ",t.assignee='" + staffId + "',t.ownerId='" + staffId + "',t.ownerName='" + staff.getStaffName() + "',t.ownerUnitId='" + unit.getUnitId() + "' where t.dbid='" + taskId + "'";

                    session.createQuery(hql).executeUpdate();

                }

                return null;

            }

        });

    }


    public List<EntrustTaskHistoryVO> getTaskEntrustHistory(String taskId) throws IFinFlowException {

        WorkflowLog.dbLog(Level.INFO, new AppLog("getTaskEntrustHistory", new String[]{"taskId"}, new Object[]{taskId}));


        return this.taskManager.getTaskEntrustHistory(taskId);

    }

}