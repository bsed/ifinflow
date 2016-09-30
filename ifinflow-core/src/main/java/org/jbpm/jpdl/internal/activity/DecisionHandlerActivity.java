/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.api.JbpmException;
/*    */ import org.jbpm.api.activity.ActivityExecution;
/*    */ import org.jbpm.api.jpdl.DecisionHandler;
/*    */ import org.jbpm.api.model.Activity;
/*    */ import org.jbpm.api.model.Transition;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*    */ import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DecisionHandlerActivity
/*    */   extends JpdlActivity
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   UserCodeReference decisionHandlerReference;
/*    */   
/*    */   public void execute(ActivityExecution execution)
/*    */   {
/* 42 */     execute((ExecutionImpl)execution);
/*    */   }
/*    */   
/*    */   public void execute(ExecutionImpl execution) {
/* 46 */     Activity activity = execution.getActivity();
/*    */     
/* 48 */     String transitionName = null;
/*    */     
/* 50 */     DecisionHandler decisionHandler = null;
/*    */     
/* 52 */     if (this.decisionHandlerReference != null) {
/* 53 */       decisionHandler = (DecisionHandler)this.decisionHandlerReference.getObject(execution);
/*    */     }
/*    */     
/* 56 */     if (decisionHandler == null) {
/* 57 */       throw new JbpmException("no decision handler specified");
/*    */     }
/*    */     
/* 60 */     transitionName = decisionHandler.decide(execution);
/*    */     
/* 62 */     Transition transition = activity.getOutgoingTransition(transitionName);
/* 63 */     if (transition == null) {
/* 64 */       throw new JbpmException("handler in decision '" + activity.getName() + "' returned unexisting outgoing transition name: " + transitionName);
/*    */     }
/*    */     
/* 67 */     execution.historyDecision(transitionName);
/*    */     
/* 69 */     execution.take(transition);
/*    */   }
/*    */   
/*    */   public void setDecisionHandlerReference(UserCodeReference decisionHandlerReference) {
/* 73 */     this.decisionHandlerReference = decisionHandlerReference;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/DecisionHandlerActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */