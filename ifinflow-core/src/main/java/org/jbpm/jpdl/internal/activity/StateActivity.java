/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.jbpm.api.activity.ActivityExecution;
/*    */ import org.jbpm.api.model.Transition;
/*    */ import org.jbpm.pvm.internal.model.ActivityImpl;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
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
/*    */ public class StateActivity
/*    */   extends JpdlExternalActivity
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public void execute(ActivityExecution execution)
/*    */   {
/* 39 */     execute((ExecutionImpl)execution);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void execute(ExecutionImpl execution)
/*    */   {
/* 46 */     execution.waitForSignal();
/*    */   }
/*    */   
/*    */   public void signal(ActivityExecution execution, String signalName, Map<String, ?> parameters) throws Exception {
/* 50 */     signal((ExecutionImpl)execution, signalName, parameters);
/*    */   }
/*    */   
/*    */   public void signal(ExecutionImpl execution, String signalName, Map<String, ?> parameters) throws Exception {
/* 54 */     ActivityImpl activity = execution.getActivity();
/*    */     
/* 56 */     if (parameters != null) {
/* 57 */       execution.setVariables(parameters);
/*    */     }
/*    */     
/* 60 */     execution.fire(signalName, activity);
/*    */     
/* 62 */     Transition transition = null;
/* 63 */     if ((signalName == null) && (activity.getOutgoingTransitions() != null) && (activity.getOutgoingTransitions().size() == 1))
/*    */     {
/*    */ 
/*    */ 
/* 67 */       transition = (Transition)activity.getOutgoingTransitions().get(0);
/*    */     } else {
/* 69 */       transition = activity.findOutgoingTransition(signalName);
/*    */     }
/*    */     
/* 72 */     if (transition != null) {
/* 73 */       execution.historyActivityEnd(signalName);
/* 74 */       execution.take(transition);
/*    */     } else {
/* 76 */       execution.waitForSignal();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/StateActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */