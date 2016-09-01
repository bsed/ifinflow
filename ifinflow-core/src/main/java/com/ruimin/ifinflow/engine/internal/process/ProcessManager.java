/*     */ package com.ruimin.ifinflow.engine.internal.process;
/*     */ 
/*     */ import com.ruimin.ifinflow.engine.flowmodel.Variable;
/*     */ import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
/*     */ import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;
/*     */ import com.ruimin.ifinflow.engine.flowmodel.vo.WfStaffVO;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.CancelProcessCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.FindByHqlCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.ProcessResumeCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.ProcessStartByDeployIdCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.ProcessStartCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.ProcessSuspendCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.ProcessUpdateCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.ProcessVariableFindCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.ProcessVariableSetCmd;
/*     */ import com.ruimin.ifinflow.util.exception.IFinFlowException;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.jbpm.api.ExecutionService;
/*     */ import org.jbpm.api.HistoryService;
/*     */ import org.jbpm.api.IdentityService;
/*     */ import org.jbpm.api.ManagementService;
/*     */ import org.jbpm.api.ProcessEngine;
/*     */ import org.jbpm.api.ProcessInstanceQuery;
/*     */ import org.jbpm.api.RepositoryService;
/*     */ import org.jbpm.api.TaskService;
/*     */ import org.jbpm.api.history.HistoryProcessInstanceQuery;
/*     */ import org.jbpm.pvm.internal.cmd.CompositeCmd;
/*     */ import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
/*     */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*     */ import org.jbpm.pvm.internal.task.TaskImpl;
 
 
 
 
 
 
 
 
 
 
 public class ProcessManager
 {
   private ProcessEngine processEngine;
   private TaskService taskService;
   private ExecutionService executionService;
   private ManagementService managementService;
   private IdentityService identityService;
   private HistoryService historyService;
   private RepositoryService repositoryService;
   
   public ProcessManager(ProcessEngine processEngine)
   {
     this.processEngine = processEngine;
     this.repositoryService = processEngine.getRepositoryService();
     this.executionService = processEngine.getExecutionService();
     this.taskService = processEngine.getTaskService();
     this.identityService = processEngine.getIdentityService();
     this.managementService = processEngine.getManagementService();
     this.historyService = processEngine.getHistoryService();
   }
   
 
   public ProcessVO startProcess(String deployId, WfStaffVO user, VariableSet vs, String subject)
     throws IFinFlowException
   {
     return (ProcessVO)this.processEngine.execute(new ProcessStartByDeployIdCmd(deployId, user, vs, subject));
   }
   
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   public ProcessVO startProcess(String packageId, String templateId, int version, WfStaffVO user, VariableSet vs, String subject)
     throws IFinFlowException
   {
     return (ProcessVO)this.processEngine.execute(new ProcessStartCmd(packageId, templateId, version, user, vs, subject));
   }
   
 
 
 
 
 
 
 
 
 
 
 
   public void cancelProcess(String processId, WfStaffVO user)
     throws IFinFlowException
   {
     this.processEngine.execute(new CancelProcessCmd(processId));
   }
   
 
 
 
 
 
 
 
   public void suspendProcessByTaskId(String taskId)
     throws IFinFlowException
   {
     ExecutionImpl process = getProcessByTaskId(taskId);
     if (process == null) {
       throw new IFinFlowException(102001, new Object[] { taskId });
     }
     this.processEngine.execute(new ProcessSuspendCmd(process.getDbid()));
   }
   
 
 
 
 
 
 
 
   public void resumeProcessByTaskId(String taskId)
     throws IFinFlowException
   {
     ExecutionImpl process = getProcessByTaskId(taskId);
     if (process == null) {
       throw new IFinFlowException(102001, new Object[] { taskId });
     }
     this.processEngine.execute(new ProcessResumeCmd(process.getDbid()));
   }
   
 
 
 
 
 
 
 
 
 
   public void updateProcessSubject(String processId, String subject)
     throws IFinFlowException
   {
     HistoryProcessInstanceImpl hpii = (HistoryProcessInstanceImpl)this.historyService.createHistoryProcessInstanceQuery().processId(processId).uniqueResult();
     
 
     if (hpii == null) {
       throw new IFinFlowException(102001, new Object[] { processId });
     }
     hpii.setSubject(subject);
     this.processEngine.execute(new ProcessUpdateCmd(hpii));
   }
   
 
 
 
 
 
 
 
 
 
 
   public void setVariables(String executionId, VariableSet vs)
     throws IFinFlowException
   {
     if (vs == null)
     {
       throw new IFinFlowException(104004, new Object[] { "null" });
     }
     this.processEngine.execute(new ProcessVariableSetCmd(executionId, vs));
   }
   
 
 
 
 
 
 
 
 
   public void setVariable(String executionId, Variable var)
     throws IFinFlowException
   {
     if (var == null)
     {
       throw new IFinFlowException(104004, new Object[] { "null" });
     }
     VariableSet vs = new VariableSet();
     vs.addVariable(var);
     
     this.processEngine.execute(new ProcessVariableSetCmd(executionId, vs));
   }
   
 
 
 
 
 
 
 
 
   public VariableSet getVariableSet(String processId)
   {
     VariableSet vs = (VariableSet)this.processEngine.execute(new ProcessVariableFindCmd(processId));
     
     return vs;
   }
   
 
 
 
 
 
 
 
 
   public Variable getVariable(String processId, String variableName)
   {
     Set<String> variableNames = new HashSet();
     variableNames.add(variableName);
     VariableSet vs = (VariableSet)this.processEngine.execute(new ProcessVariableFindCmd(processId, variableNames));
     
     return vs.getVariable(variableName);
   }
   
 
 
 
 
 
 
 
 
 
   public VariableSet getVariableSet(String executionId, Set<String> variableNames)
   {
     return (VariableSet)this.processEngine.execute(new ProcessVariableFindCmd(executionId, variableNames));
   }
   
 
 
   public ExecutionImpl getExecutionImpl(String processId)
   {
     return (ExecutionImpl)this.executionService.createProcessInstanceQuery().dbid(processId).uniqueResult();
   }
   
 
   public List<ExecutionImpl> getSubProcessInstance(String processId)
   {
     if (processId == null) {
       throw new IFinFlowException(102005, new Object[0]);
     }
     String hql = "from " + ExecutionImpl.class.getName() + " where superProcessExecution.dbid = '" + processId + "'";
     return (List)this.processEngine.execute(new FindByHqlCmd(hql));
   }
   
 
 
 
 
 
 
   public ExecutionImpl getProcessByTaskId(String taskId)
     throws IFinFlowException
   {
     TaskImpl task = (TaskImpl)this.taskService.getTask(taskId);
     if (task == null)
     {
 
       throw new IFinFlowException(103002, new Object[] { taskId });
     }
     return task.getProcessInstance();
   }
   
   public void resolveException(String processId, WfStaffVO user, VariableSet vs) throws IFinFlowException
   {
     CompositeCmd compositeCmd = new CompositeCmd();
     
 
     this.processEngine.execute(compositeCmd);
   }
 }

