/*     */ package org.jbpm.jpdl.internal.activity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.el.PropertyNotFoundException;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.hibernate.Session;
/*     */ import org.jbpm.api.JbpmException;
/*     */ import org.jbpm.api.activity.ActivityExecution;
/*     */ import org.jbpm.api.model.Activity;
/*     */ import org.jbpm.internal.log.Log;
/*     */ import org.jbpm.pvm.internal.client.ClientProcessDefinition;
/*     */ import org.jbpm.pvm.internal.el.Expression;
/*     */ import org.jbpm.pvm.internal.env.EnvironmentImpl;
/*     */ import org.jbpm.pvm.internal.env.ExecutionContext;
/*     */ import org.jbpm.pvm.internal.exception.ExceptionUtil;
/*     */ import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
/*     */ import org.jbpm.pvm.internal.model.ActivityImpl;
/*     */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*     */ import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.model.TransitionImpl;
/*     */ import org.jbpm.pvm.internal.session.RepositorySession;
/*     */ import org.jbpm.pvm.internal.task.SwimlaneImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SubProcessActivity
/*     */   extends JpdlExternalActivity
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  58 */   private static final Log log = Log.getLog(SubProcessActivity.class.getName());
/*     */   
/*     */   protected String subProcessKey;
/*     */   
/*     */   protected String subProcessId;
/*     */   protected Map<String, String> swimlaneMappings;
/*     */   protected List<SubProcessInParameterImpl> inParameters;
/*     */   protected List<SubProcessOutParameterImpl> outParameters;
/*     */   protected Expression outcomeExpression;
/*     */   protected Map<Object, String> outcomeVariableMappings;
/*     */   
/*     */   public void execute(ActivityExecution execution)
/*     */   {
/*  71 */     ExecutionImpl executionImpl = (ExecutionImpl)execution;
/*     */     
/*  73 */     RepositorySession repositorySession = (RepositorySession)EnvironmentImpl.getFromCurrent(RepositorySession.class);
/*     */     
/*  75 */     ClientProcessDefinition processDefinition = null;
/*     */     
/*  77 */     if (this.subProcessId != null) {
/*  78 */       Expression subProcessKeyExpression = Expression.create(this.subProcessId, "uel-value");
/*  79 */       String subProcessIdEval = (String)subProcessKeyExpression.evaluate(execution);
/*  80 */       processDefinition = repositorySession.findProcessDefinitionById(subProcessIdEval);
/*     */       
/*  82 */       if (processDefinition == null) {
/*  83 */         throw new JbpmException("cannot find process definition by id: [" + this.subProcessId + "(" + subProcessIdEval + ")" + "]");
/*     */       }
/*     */     }
/*     */     else {
/*  87 */       String subProcessKeyEval = null;
/*     */       try {
/*  89 */         Expression subProcessKeyExpression = Expression.create(this.subProcessKey, "uel-value");
/*  90 */         subProcessKeyEval = (String)subProcessKeyExpression.evaluate(execution);
/*  91 */         if (subProcessKeyEval == null) {
/*  92 */           throw new JbpmException("Subprocess key '" + this.subProcessKey + "' resolved to null.");
/*     */         }
/*     */       } catch (PropertyNotFoundException e) {
/*  95 */         throw new JbpmException("Subprocess key '" + this.subProcessKey + "' could not be resolved.");
/*     */       }
/*     */       
/*  98 */       processDefinition = repositorySession.findProcessDefinitionByKey(subProcessKeyEval);
/*  99 */       if (processDefinition == null) {
/* 100 */         throw new JbpmException("Subprocess '" + subProcessKeyEval + "' could not be found.");
/*     */       }
/*     */       
/* 103 */       if (processDefinition == null) {
/* 104 */         throw new JbpmException("cannot find process definition by key: [" + this.subProcessKey + "(" + subProcessKeyEval + ")" + "]");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */     ExecutionImpl subProcessInstance = (ExecutionImpl)processDefinition.createProcessInstance(null, execution);
/* 116 */     EnvironmentImpl environment = EnvironmentImpl.getCurrent();
/* 117 */     Session session = (Session)environment.get(Session.class);
/*     */     
/* 119 */     HistoryProcessInstanceImpl hpii = (HistoryProcessInstanceImpl)session.get(HistoryProcessInstanceImpl.class, subProcessInstance.getDbid());
/* 120 */     hpii.setSuperProcessExecution(((ExecutionImpl)execution.getProcessInstance()).getDbid());
/*     */     
/* 122 */     String subProcessActivityName = executionImpl.getActivityName();
/* 123 */     hpii.setActivityName(subProcessActivityName);
/* 124 */     session.saveOrUpdate(hpii);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */     for (String swimlaneName : this.swimlaneMappings.keySet()) {
/* 131 */       String subSwimlaneName = (String)this.swimlaneMappings.get(swimlaneName);
/* 132 */       SwimlaneImpl subSwimlane = subProcessInstance.createSwimlane(subSwimlaneName);
/* 133 */       SwimlaneImpl swimlane = executionImpl.getSwimlane(swimlaneName);
/* 134 */       if (swimlane != null) {
/* 135 */         subSwimlane.initialize(swimlane);
/*     */       }
/*     */     }
/*     */     
/* 139 */     for (SubProcessInParameterImpl inParameter : this.inParameters) {
/* 140 */       inParameter.produce(executionImpl, subProcessInstance);
/*     */     }
/*     */     
/* 143 */     execution.waitForSignal();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 148 */     log.debug("SubProcessActivity 148 子流程提交时execution.getActivityName=" + subProcessInstance.getActivityName());
/* 149 */     ExceptionUtil.saveTmpExecution(subProcessInstance.getExecutionImplId(), subProcessInstance.getActivityName());
/*     */   }
/*     */   
/*     */ 
/*     */   public void signal(ActivityExecution execution, String signalName, Map<String, ?> parameters)
/*     */     throws Exception
/*     */   {
/* 156 */     signal((ExecutionImpl)execution, signalName, parameters);
/*     */   }
/*     */   
/*     */   public void signal(ExecutionImpl execution, String signalName, Map<String, ?> parameters) throws Exception {
/* 160 */     ExecutionImpl subProcessInstance = execution.getSubProcessInstance();
/*     */     
/* 162 */     String transitionName = null;
/*     */     
/* 164 */     ExecutionContext originalExecutionContext = null;
/* 165 */     ExecutionContext subProcessExecutionContext = null;
/* 166 */     EnvironmentImpl environment = EnvironmentImpl.getCurrent();
/* 167 */     if (environment != null) {
/* 168 */       originalExecutionContext = (ExecutionContext)environment.removeContext("execution");
/* 169 */       subProcessExecutionContext = new ExecutionContext(subProcessInstance);
/* 170 */       environment.setContext(subProcessExecutionContext);
/*     */     }
/* 172 */     Activity activity = null;
/*     */     try {
/* 174 */       subProcessInstance.setSuperProcessExecution(null);
/* 175 */       execution.setSubProcessInstance(null);
/*     */       
/*     */ 
/* 178 */       for (SubProcessOutParameterImpl outParameter : this.outParameters) {
/* 179 */         outParameter.consume(execution, subProcessInstance);
/*     */       }
/*     */       
/* 182 */       activity = execution.getActivity();
/* 183 */       String subProcessActivityName = subProcessInstance.getActivityName();
/*     */       
/* 185 */       if (this.outcomeExpression != null) {
/* 186 */         Object value = this.outcomeExpression.evaluate(execution);
/*     */         
/* 188 */         if (((value instanceof String)) && (activity.hasOutgoingTransition((String)value)))
/*     */         {
/*     */ 
/*     */ 
/* 192 */           transitionName = (String)value;
/*     */         }
/*     */         else {
/* 195 */           transitionName = (String)this.outcomeVariableMappings.get(value);
/*     */         }
/*     */       }
/* 198 */       else if (activity.hasOutgoingTransition(subProcessActivityName)) {
/* 199 */         transitionName = subProcessActivityName;
/*     */       }
/*     */     }
/*     */     finally {
/* 203 */       if (subProcessExecutionContext != null) {
/* 204 */         environment.removeContext(subProcessExecutionContext);
/*     */       }
/* 206 */       if (originalExecutionContext != null) {
/* 207 */         environment.setContext(originalExecutionContext);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 212 */     int status = -1;
/* 213 */     ActivityImpl act = (ActivityImpl)activity;
/* 214 */     if (act.isReject()) {
/* 215 */       status = 4;
/* 216 */       act.setReject(false);
/* 217 */     } else if (execution.getActivity().isSkip()) {
/* 218 */       status = 6;
/* 219 */       act.setSkip(false);
/*     */     }
/* 221 */     if (status > 0) {
/* 222 */       execution.historyActivityEnd(status, signalName);
/*     */     } else {
/* 224 */       execution.historyActivityEnd();
/*     */     }
/*     */     
/* 227 */     if (transitionName != null) {
/* 228 */       execution.take(transitionName);
/*     */     }
/* 230 */     else if (!StringUtils.isEmpty(signalName)) {
/* 231 */       TransitionImpl tran = act.createOutgoingTransition();
/* 232 */       String destName = signalName.substring(signalName.lastIndexOf(" to ") + " to ".length());
/*     */       
/*     */ 
/* 235 */       ActivityImpl toAct = act.getProcessDefinition().findActivity(destName);
/* 236 */       tran.setName(signalName);
/* 237 */       tran.setDestination(toAct);
/* 238 */       act.addOutgoingTransition(tran);
/* 239 */       execution.take(tran);
/*     */     } else {
/* 241 */       execution.takeDefaultTransition();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSwimlaneMappings(Map<String, String> swimlaneMappings) {
/* 246 */     this.swimlaneMappings = swimlaneMappings;
/*     */   }
/*     */   
/* 249 */   public void setOutcomeVariableMappings(Map<Object, String> outcomeVariableMappings) { this.outcomeVariableMappings = outcomeVariableMappings; }
/*     */   
/*     */   public void setSubProcessKey(String subProcessKey) {
/* 252 */     this.subProcessKey = subProcessKey;
/*     */   }
/*     */   
/* 255 */   public void setSubProcessId(String subProcessId) { this.subProcessId = subProcessId; }
/*     */   
/*     */   public void setOutcomeExpression(Expression outcomeExpression) {
/* 258 */     this.outcomeExpression = outcomeExpression;
/*     */   }
/*     */   
/* 261 */   public List<SubProcessInParameterImpl> getInParameters() { return this.inParameters; }
/*     */   
/*     */   public void setInParameters(List<SubProcessInParameterImpl> inParameters) {
/* 264 */     this.inParameters = inParameters;
/*     */   }
/*     */   
/* 267 */   public List<SubProcessOutParameterImpl> getOutParameters() { return this.outParameters; }
/*     */   
/*     */   public void setOutParameters(List<SubProcessOutParameterImpl> outParameters) {
/* 270 */     this.outParameters = outParameters;
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/SubProcessActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */