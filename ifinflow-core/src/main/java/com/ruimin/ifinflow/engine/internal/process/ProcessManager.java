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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessManager
/*     */ {
/*     */   private ProcessEngine processEngine;
/*     */   private TaskService taskService;
/*     */   private ExecutionService executionService;
/*     */   private ManagementService managementService;
/*     */   private IdentityService identityService;
/*     */   private HistoryService historyService;
/*     */   private RepositoryService repositoryService;
/*     */   
/*     */   public ProcessManager(ProcessEngine processEngine)
/*     */   {
/*  55 */     this.processEngine = processEngine;
/*  56 */     this.repositoryService = processEngine.getRepositoryService();
/*  57 */     this.executionService = processEngine.getExecutionService();
/*  58 */     this.taskService = processEngine.getTaskService();
/*  59 */     this.identityService = processEngine.getIdentityService();
/*  60 */     this.managementService = processEngine.getManagementService();
/*  61 */     this.historyService = processEngine.getHistoryService();
/*     */   }
/*     */   
/*     */ 
/*     */   public ProcessVO startProcess(String deployId, WfStaffVO user, VariableSet vs, String subject)
/*     */     throws IFinFlowException
/*     */   {
/*  68 */     return (ProcessVO)this.processEngine.execute(new ProcessStartByDeployIdCmd(deployId, user, vs, subject));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProcessVO startProcess(String packageId, String templateId, int version, WfStaffVO user, VariableSet vs, String subject)
/*     */     throws IFinFlowException
/*     */   {
/*  94 */     return (ProcessVO)this.processEngine.execute(new ProcessStartCmd(packageId, templateId, version, user, vs, subject));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void cancelProcess(String processId, WfStaffVO user)
/*     */     throws IFinFlowException
/*     */   {
/* 111 */     this.processEngine.execute(new CancelProcessCmd(processId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void suspendProcessByTaskId(String taskId)
/*     */     throws IFinFlowException
/*     */   {
/* 124 */     ExecutionImpl process = getProcessByTaskId(taskId);
/* 125 */     if (process == null) {
/* 126 */       throw new IFinFlowException(102001, new Object[] { taskId });
/*     */     }
/* 128 */     this.processEngine.execute(new ProcessSuspendCmd(process.getDbid()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resumeProcessByTaskId(String taskId)
/*     */     throws IFinFlowException
/*     */   {
/* 141 */     ExecutionImpl process = getProcessByTaskId(taskId);
/* 142 */     if (process == null) {
/* 143 */       throw new IFinFlowException(102001, new Object[] { taskId });
/*     */     }
/* 145 */     this.processEngine.execute(new ProcessResumeCmd(process.getDbid()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateProcessSubject(String processId, String subject)
/*     */     throws IFinFlowException
/*     */   {
/* 160 */     HistoryProcessInstanceImpl hpii = (HistoryProcessInstanceImpl)this.historyService.createHistoryProcessInstanceQuery().processId(processId).uniqueResult();
/*     */     
/*     */ 
/* 163 */     if (hpii == null) {
/* 164 */       throw new IFinFlowException(102001, new Object[] { processId });
/*     */     }
/* 166 */     hpii.setSubject(subject);
/* 167 */     this.processEngine.execute(new ProcessUpdateCmd(hpii));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVariables(String executionId, VariableSet vs)
/*     */     throws IFinFlowException
/*     */   {
/* 183 */     if (vs == null)
/*     */     {
/* 185 */       throw new IFinFlowException(104004, new Object[] { "null" });
/*     */     }
/* 187 */     this.processEngine.execute(new ProcessVariableSetCmd(executionId, vs));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVariable(String executionId, Variable var)
/*     */     throws IFinFlowException
/*     */   {
/* 201 */     if (var == null)
/*     */     {
/* 203 */       throw new IFinFlowException(104004, new Object[] { "null" });
/*     */     }
/* 205 */     VariableSet vs = new VariableSet();
/* 206 */     vs.addVariable(var);
/*     */     
/* 208 */     this.processEngine.execute(new ProcessVariableSetCmd(executionId, vs));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public VariableSet getVariableSet(String processId)
/*     */   {
/* 221 */     VariableSet vs = (VariableSet)this.processEngine.execute(new ProcessVariableFindCmd(processId));
/*     */     
/* 223 */     return vs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Variable getVariable(String processId, String variableName)
/*     */   {
/* 236 */     Set<String> variableNames = new HashSet();
/* 237 */     variableNames.add(variableName);
/* 238 */     VariableSet vs = (VariableSet)this.processEngine.execute(new ProcessVariableFindCmd(processId, variableNames));
/*     */     
/* 240 */     return vs.getVariable(variableName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public VariableSet getVariableSet(String executionId, Set<String> variableNames)
/*     */   {
/* 254 */     return (VariableSet)this.processEngine.execute(new ProcessVariableFindCmd(executionId, variableNames));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ExecutionImpl getExecutionImpl(String processId)
/*     */   {
/* 261 */     return (ExecutionImpl)this.executionService.createProcessInstanceQuery().dbid(processId).uniqueResult();
/*     */   }
/*     */   
/*     */ 
/*     */   public List<ExecutionImpl> getSubProcessInstance(String processId)
/*     */   {
/* 267 */     if (processId == null) {
/* 268 */       throw new IFinFlowException(102005, new Object[0]);
/*     */     }
/* 270 */     String hql = "from " + ExecutionImpl.class.getName() + " where superProcessExecution.dbid = '" + processId + "'";
/* 271 */     return (List)this.processEngine.execute(new FindByHqlCmd(hql));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExecutionImpl getProcessByTaskId(String taskId)
/*     */     throws IFinFlowException
/*     */   {
/* 283 */     TaskImpl task = (TaskImpl)this.taskService.getTask(taskId);
/* 284 */     if (task == null)
/*     */     {
/*     */ 
/* 287 */       throw new IFinFlowException(103002, new Object[] { taskId });
/*     */     }
/* 289 */     return task.getProcessInstance();
/*     */   }
/*     */   
/*     */   public void resolveException(String processId, WfStaffVO user, VariableSet vs) throws IFinFlowException
/*     */   {
/* 294 */     CompositeCmd compositeCmd = new CompositeCmd();
/*     */     
/*     */ 
/* 297 */     this.processEngine.execute(compositeCmd);
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/internal/process/ProcessManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */