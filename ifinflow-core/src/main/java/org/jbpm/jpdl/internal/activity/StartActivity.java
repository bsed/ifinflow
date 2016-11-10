/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.api.activity.ActivityExecution;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*    */ import org.jbpm.pvm.internal.task.FormBehaviour;
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
/*    */ public class StartActivity
/*    */   extends JpdlActivity
/*    */   implements FormBehaviour
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   String formResourceName;
/*    */   
/*    */   public void execute(ActivityExecution execution)
/*    */   {
/* 41 */     execute((ExecutionImpl)execution);
/*    */   }
/*    */   
/*    */   public String getFormResourceName() {
/* 45 */     return this.formResourceName;
/*    */   }
/*    */   
/* 48 */   public void setFormResourceName(String formResourceName) { this.formResourceName = formResourceName; }
/*    */   
/*    */   public void execute(ExecutionImpl execution)
/*    */   {
/* 52 */     execution.historyAutomatic();
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/StartActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */