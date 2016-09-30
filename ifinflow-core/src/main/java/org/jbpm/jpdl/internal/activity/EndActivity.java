/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.jbpm.api.activity.ActivityExecution;
/*    */ import org.jbpm.api.model.Activity;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EndActivity
/*    */   extends JpdlActivity
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 39 */   protected boolean endProcessInstance = true;
/* 40 */   protected String state = null;
/*    */   
/*    */   public void execute(ActivityExecution execution) {
/* 43 */     execute((ExecutionImpl)execution);
/*    */   }
/*    */   
/*    */   public void execute(ExecutionImpl execution) {
/* 47 */     Activity activity = execution.getActivity();
/* 48 */     List<? extends Transition> outgoingTransitions = activity.getOutgoingTransitions();
/* 49 */     ActivityImpl parentActivity = (ActivityImpl)activity.getParentActivity();
/*    */     
/* 51 */     if ((parentActivity != null) && ("group".equals(parentActivity.getType())))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 56 */       if ((outgoingTransitions != null) && (outgoingTransitions.size() == 1))
/*    */       {
/*    */ 
/* 59 */         Transition outgoingTransition = (Transition)outgoingTransitions.get(0);
/*    */         
/*    */ 
/* 62 */         execution.take(outgoingTransition);
/*    */       }
/*    */       else {
/* 65 */         execution.setActivity(parentActivity);
/* 66 */         execution.signal();
/*    */       }
/*    */     }
/*    */     else {
/* 70 */       ExecutionImpl executionToEnd = null;
/* 71 */       if (this.endProcessInstance) {
/* 72 */         executionToEnd = execution.getProcessInstance();
/* 73 */         executionToEnd.setActivity(execution.getActivity());
/*    */       } else {
/* 75 */         executionToEnd = execution;
/*    */       }
/*    */       
/* 78 */       if (this.state == null) {
/* 79 */         executionToEnd.end();
/*    */       } else {
/* 81 */         executionToEnd.end(this.state);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void setEndProcessInstance(boolean endProcessInstance) {
/* 87 */     this.endProcessInstance = endProcessInstance;
/*    */   }
/*    */   
/* 90 */   public void setState(String state) { this.state = state; }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/EndActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */