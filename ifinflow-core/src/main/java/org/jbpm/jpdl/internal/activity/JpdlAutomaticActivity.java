/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.api.activity.ActivityExecution;
/*    */ import org.jbpm.api.listener.EventListener;
/*    */ import org.jbpm.api.listener.EventListenerExecution;
/*    */ import org.jbpm.api.model.OpenExecution;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*    */ 
/*    */ public abstract class JpdlAutomaticActivity extends JpdlActivity implements EventListener
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public void execute(ActivityExecution execution) throws Exception
/*    */   {
/* 15 */     perform(execution);
/* 16 */     ((ExecutionImpl)execution).historyAutomatic();
/*    */   }
/*    */   
/*    */   public void notify(EventListenerExecution execution) throws Exception {
/* 20 */     perform(execution);
/*    */   }
/*    */   
/*    */   abstract void perform(OpenExecution paramOpenExecution)
/*    */     throws Exception;
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/JpdlAutomaticActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */