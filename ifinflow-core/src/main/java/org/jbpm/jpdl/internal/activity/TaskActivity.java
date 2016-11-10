/*     */ package org.jbpm.jpdl.internal.activity;
/*     */ 
/*     */ import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
/*     */ import com.ruimin.ifinflow.engine.external.model.IWfStaff;
/*     */ import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
/*     */ import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
/*     */ import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;
/*     */ import com.ruimin.ifinflow.util.ActivityUtil;
/*     */ import com.ruimin.ifinflow.util.TemplateCacheUtil;
/*     */ import com.ruimin.ifinflow.util.exception.IFinFlowException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.hibernate.Criteria;
/*     */ import org.hibernate.Session;
/*     */ import org.hibernate.criterion.Order;
/*     */ import org.hibernate.criterion.Restrictions;
/*     */ import org.jbpm.api.JbpmException;
/*     */ import org.jbpm.api.activity.ActivityExecution;
/*     */ import org.jbpm.api.model.Transition;
/*     */ import org.jbpm.jpdl.internal.assign.AssignTask;
/*     */ import org.jbpm.pvm.internal.cal.Duration;
/*     */ import org.jbpm.pvm.internal.el.Expression;
/*     */ import org.jbpm.pvm.internal.env.EnvironmentImpl;
/*     */ import org.jbpm.pvm.internal.history.HistoryEvent;
/*     */ import org.jbpm.pvm.internal.history.events.TaskActivityStart;
/*     */ import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
/*     */ import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
/*     */ import org.jbpm.pvm.internal.model.ActivityImpl;
/*     */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*     */ import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.model.TransitionImpl;
/*     */ import org.jbpm.pvm.internal.session.DbSession;
/*     */ import org.jbpm.pvm.internal.task.MultiParticipationImpl;
/*     */ import org.jbpm.pvm.internal.task.ParticipationImpl;
/*     */ import org.jbpm.pvm.internal.task.SwimlaneDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.task.SwimlaneImpl;
/*     */ import org.jbpm.pvm.internal.task.TaskDefinitionImpl;
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
/*     */ public class TaskActivity
/*     */   extends JpdlExternalActivity
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected TaskDefinitionImpl taskDefinition;
/*     */   private static final String REJECT_SKIP_FLAG = " to ";
/*     */   
/*     */   public void execute(ActivityExecution execution)
/*     */   {
/*  80 */     execute((ExecutionImpl)execution);
/*     */   }
/*     */   
/*     */   public void execute(ExecutionImpl execution) {
/*  84 */     DbSession dbSession = (DbSession)EnvironmentImpl.getFromCurrent(DbSession.class);
/*  85 */     TaskImpl task = dbSession.createTask();
/*  86 */     task.setTaskDefinition(this.taskDefinition);
/*  87 */     task.setExecution(execution);
/*  88 */     task.setProcessInstance(execution.getProcessInstance());
/*  89 */     task.setSignalling(true);
/*     */     
/*     */ 
/*  92 */     if (this.taskDefinition.getName() != null) {
/*  93 */       task.setName(this.taskDefinition.getName());
/*     */     } else {
/*  95 */       task.setName(execution.getActivityName());
/*     */     }
/*     */     
/*  98 */     Expression descriptionExpression = this.taskDefinition.getDescription();
/*  99 */     if (descriptionExpression != null) {
/* 100 */       String description = (String)descriptionExpression.evaluate(task);
/* 101 */       task.setDescription(description);
/*     */     }
/* 103 */     task.setPriority(this.taskDefinition.getPriority());
/* 104 */     task.setFormResourceName(this.taskDefinition.getFormResourceName());
/*     */     
/*     */ 
/* 107 */     String dueDateDescription = this.taskDefinition.getDueDateDescription();
/* 108 */     if (dueDateDescription != null) {
/* 109 */       task.setDuedate(Duration.calculateDueDate(dueDateDescription));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */     setExtends(execution, task);
/*     */     
/* 121 */     Session session = (Session)EnvironmentImpl.getFromCurrent(Session.class);
/*     */     
/* 123 */     AssignTask assignTask = new AssignTask();
/*     */     
/* 125 */     HistoryActivityInstanceImpl hai = ActivityUtil.getHistoryActIns(execution.getHistoryActivityInstanceDbid());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 130 */     if (task.isAssignLast(hai))
/*     */     {
/* 132 */       ExecutionImpl processIns = execution.getProcessInstance();
/* 133 */       if (processIns == null) {
/* 134 */         processIns = execution;
/*     */       }
/*     */       
/* 137 */       HistoryTaskImpl ht = (HistoryTaskImpl)session.createCriteria(HistoryTaskImpl.class).add(Restrictions.eq("historyProcessInstanceId", processIns.getDbid())).add(Restrictions.eq("nodeId", task.getActivityName())).addOrder(Order.desc("endTime")).setFirstResult(0).setMaxResults(1).uniqueResult();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */       if (ht == null) {
/* 144 */         throw new IFinFlowException(103057, new Object[] { task.getId() });
/*     */       }
/* 146 */       assignTask.setOwnerId(ht.getExecutorId());
/* 147 */       task.setAssigneeOnly(ht.getExecutorId());
/* 148 */       task.setOwnerName(ht.getExecutorName());
/*     */       
/* 150 */       task.setStatus(2);
/*     */     } else {
/*     */       try {
/* 153 */         assignTask.assignTask(execution, this.taskDefinition, task);
/*     */         
/* 155 */         if (assignTask.getAssignMode() != 2) {
/* 156 */           task.setAssigneeOnly(assignTask.getOwnerId());
/*     */         }
/*     */       } catch (IFinFlowException e) {
/* 159 */         throw e;
/*     */       } catch (Exception e) {
/* 161 */         IFinFlowException te = new IFinFlowException(106008, e);
/*     */         
/* 163 */         throw te;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 168 */     task.setOwnerId(assignTask.getOwnerId());
/* 169 */     task.setOwnerUnitId(assignTask.getOwnerUnitId());
/* 170 */     task.setOwnerRoleId(assignTask.getOwnerRoleId());
/* 171 */     task.setOwnerGroupId(assignTask.getOwnerGroupId());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 177 */     if (task.isAutoComplete()) {
/* 178 */       task.setState("completed");
/* 179 */       task.setStatus(16);
/*     */       
/*     */ 
/* 182 */       IWfStaff user = UserExtendsReference.getIdentityAdapter().getStaffById(task.getOwnerId());
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
/* 197 */       if (user != null) {
/* 198 */         task.setOwnerId(user.getStaffId());
/* 199 */         task.setOwnerName(user.getStaffName());
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 207 */       String ownerId = task.getOwnerId();
/* 208 */       if ((2 == task.getAssignMode()) && (ownerId != null) && (ownerId.indexOf(",") >= 0))
/*     */       {
/* 210 */         String[] userIds = ownerId.replace(" ", "").replace("[", "").replace("]", "").split(",");
/*     */         
/* 212 */         for (String userId : userIds) {
/* 213 */           task.addMParticipation(new MultiParticipationImpl(userId));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 219 */       dbSession.save(task);
/*     */     }
/*     */     
/* 222 */     SwimlaneDefinitionImpl swimlaneDefinition = this.taskDefinition.getSwimlaneDefinition();
/*     */     
/* 224 */     if (swimlaneDefinition != null) {
/* 225 */       SwimlaneImpl swimlane = execution.getInitializedSwimlane(swimlaneDefinition);
/*     */       
/* 227 */       task.setSwimlane(swimlane);
/*     */       
/*     */ 
/* 230 */       task.setAssignee(swimlane.getAssignee());
/* 231 */       for (ParticipationImpl participant : swimlane.getParticipations()) {
/* 232 */         task.addParticipation(participant.getUserId(), participant.getGroupId(), participant.getType());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 237 */     execution.initializeAssignments(this.taskDefinition, task);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 244 */     TaskActivityStart tas = new TaskActivityStart(task);
/* 245 */     tas.setOwnerId(assignTask.getOwnerId());
/* 246 */     tas.setOwnerUnitId(assignTask.getOwnerUnitId());
/* 247 */     tas.setOwnerRoleId(assignTask.getOwnerRoleId());
/* 248 */     tas.setOwnerGroupId(assignTask.getOwnerGroupId());
/* 249 */     HistoryEvent.fire(tas, execution);
/*     */     
/*     */ 
/*     */ 
/* 253 */     if (!task.isAutoComplete())
/*     */     {
/* 255 */       execution.waitForSignal();
/*     */     } else {
/* 257 */       execution.setFirstAutoTaskHasCommit(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void multiAssignTask(TaskImpl task) {}
/*     */   
/*     */ 
/*     */   public void signal(ActivityExecution execution, String signalName, Map<String, ?> parameters)
/*     */     throws Exception
/*     */   {
/* 269 */     signal((ExecutionImpl)execution, signalName, parameters);
/*     */   }
/*     */   
/*     */   public void signal(ExecutionImpl execution, String signalName, Map<String, ?> parameters) throws Exception
/*     */   {
/* 274 */     ActivityImpl activity = execution.getActivity();
/*     */     
/* 276 */     if (parameters != null) {
/* 277 */       execution.setVariables(parameters);
/*     */     }
/*     */     
/* 280 */     execution.fire(signalName, activity);
/*     */     
/* 282 */     DbSession taskDbSession = (DbSession)EnvironmentImpl.getFromCurrent(DbSession.class);
/*     */     
/* 284 */     TaskImpl task = taskDbSession.findTaskByExecution(execution);
/* 285 */     if (task != null) {
/* 286 */       task.setSignalling(false);
/*     */     }
/*     */     
/* 289 */     Transition transition = null;
/* 290 */     List<? extends Transition> outgoingTransitions = activity.getOutgoingTransitions();
/*     */     
/*     */ 
/* 293 */     if ((outgoingTransitions != null) && (!outgoingTransitions.isEmpty()))
/*     */     {
/* 295 */       boolean noOutcomeSpecified = "jbpm_no_task_outcome_specified_jbpm".equals(signalName);
/*     */       
/* 297 */       if ((noOutcomeSpecified) && (activity.findOutgoingTransition(signalName) == null))
/*     */       {
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
/* 309 */         transition = activity.findOutgoingTransition(null);
/*     */       } else {
/* 311 */         transition = activity.findOutgoingTransition(signalName);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 317 */       if (transition == null)
/*     */       {
/* 319 */         if (signalName == null)
/*     */         {
/* 321 */           throw new JbpmException("No unnamed transitions were found for the task '" + getTaskDefinition().getName() + "'");
/*     */         }
/*     */         
/* 324 */         if (noOutcomeSpecified)
/*     */         {
/* 326 */           if (outgoingTransitions.size() == 1)
/*     */           {
/*     */ 
/* 329 */             transition = (Transition)outgoingTransitions.get(0);
/*     */           } else {
/* 331 */             throw new JbpmException("No unnamed transitions were found for the task '" + getTaskDefinition().getName() + "'");
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 337 */         else if (!signalName.contains(" to "))
/*     */         {
/* 339 */           throw new JbpmException("No transition named '" + signalName + "' was found.");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 344 */       ActivityImpl outAct = null;
/* 345 */       if (StringUtils.isNotBlank(signalName)) {
/* 346 */         String destName = signalName.substring(signalName.lastIndexOf(" to ") + " to ".length());
/*     */         
/*     */ 
/*     */ 
/* 350 */         boolean endProcessInstance = false;
/*     */         
/* 352 */         outAct = execution.getProcessDefinition().findActivity(destName);
/*     */         
/* 354 */         if ((signalName.contains(" to ")) && (outAct == null)) {
/* 355 */           endProcessInstance = true;
/*     */         }
/* 357 */         ExecutionImpl executionToEnd = null;
/* 358 */         if (endProcessInstance) {
/* 359 */           executionToEnd = execution.getProcessInstance();
/* 360 */           executionToEnd.setActivity(execution.getActivity());
/* 361 */           executionToEnd.end("ended", signalName);
/*     */         }
/*     */       }
/*     */       
/* 365 */       if ((task != null) && (!task.isCompleted()))
/*     */       {
/* 367 */         task.skip(transition.getName());
/*     */       }
/* 369 */       if ((transition == null) && (signalName.contains(" to "))) {
/* 370 */         TransitionImpl tran = activity.createOutgoingTransition();
/* 371 */         tran.setName(signalName);
/* 372 */         tran.setDestination(outAct);
/* 373 */         activity.addOutgoingTransition(tran);
/* 374 */         transition = tran;
/*     */       }
/*     */       
/* 377 */       if (transition != null) {
/* 378 */         execution.take(transition);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public TaskDefinitionImpl getTaskDefinition() {
/* 384 */     return this.taskDefinition;
/*     */   }
/*     */   
/*     */   public void setTaskDefinition(TaskDefinitionImpl taskDefinition) {
/* 388 */     this.taskDefinition = taskDefinition;
/*     */   }
/*     */   
/*     */ 
/*     */   private static TaskImpl setExtends(ExecutionImpl execution, TaskImpl task)
/*     */   {
/* 394 */     TemplateVo template = TemplateCacheUtil.getTemplateVO(execution);
/* 395 */     NodeVo node = TemplateCacheUtil.getNodeVo(template, execution.getActivityName());
/* 396 */     task.setPackageId(template.getPackageId());
/* 397 */     task.setTemplateId(template.getTemplateId());
/* 398 */     task.setTemplateVersion(template.getVersion());
/* 399 */     task.setAssignMode(node.getAssignMode());
/* 400 */     task.setParticipantMode(node.getParticipantType());
/*     */     
/* 402 */     List<String[]> userExtStr = node.getVariableNameList();
/* 403 */     if (userExtStr != null) {
/* 404 */       int num = userExtStr.size();
/* 405 */       if (num > 0) {
/* 406 */         Object varValue = null;
/* 407 */         Object[] mapping = null;
/* 408 */         String extStrName = null;
/* 409 */         String varName = null;
/* 410 */         for (int i = 0; i < num; i++) {
/* 411 */           mapping = (Object[])userExtStr.get(i);
/*     */           
/* 413 */           varName = String.valueOf(mapping[0]);
/* 414 */           varValue = execution.getVariable(varName);
/*     */           
/* 416 */           extStrName = String.valueOf(mapping[1]);
/* 417 */           if ("userexpandstring1".equals(extStrName)) {
/* 418 */             task.setUserExtString1(String.valueOf(varValue));
/*     */           }
/* 420 */           else if ("userexpandstring2".equals(extStrName)) {
/* 421 */             task.setUserExtString2(String.valueOf(varValue));
/*     */           }
/* 423 */           else if ("userexpandstring3".equals(extStrName)) {
/* 424 */             task.setUserExtString3(String.valueOf(varValue));
/*     */           }
/* 426 */           else if ("userexpandstring4".equals(extStrName)) {
/* 427 */             task.setUserExtString4(String.valueOf(varValue));
/*     */           }
/* 429 */           else if ("userexpandstring5".equals(extStrName)) {
/* 430 */             task.setUserExtString5(String.valueOf(varValue));
/*     */           }
/* 432 */           else if ("userexpandstring6".equals(extStrName)) {
/* 433 */             task.setUserExtString6(String.valueOf(varValue));
/*     */           }
/* 435 */           else if ("userexpandstring7".equals(extStrName)) {
/* 436 */             task.setUserExtString7(String.valueOf(varValue));
/*     */           }
/* 438 */           else if ("userexpandstring8".equals(extStrName)) {
/* 439 */             task.setUserExtString8(String.valueOf(varValue));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 445 */     return task;
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/TaskActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */