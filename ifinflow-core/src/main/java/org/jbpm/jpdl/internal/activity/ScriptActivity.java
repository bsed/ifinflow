/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.api.model.OpenExecution;
/*    */ import org.jbpm.pvm.internal.el.Expression;
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
/*    */ public class ScriptActivity
/*    */   extends JpdlAutomaticActivity
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected String script;
/*    */   protected String language;
/*    */   protected String variableName;
/*    */   
/*    */   public void perform(OpenExecution execution)
/*    */   {
/* 41 */     Object returnValue = Expression.create(this.script, this.language).evaluate(execution);
/*    */     
/* 43 */     if (this.variableName != null) {
/* 44 */       execution.setVariable(this.variableName, returnValue);
/*    */     }
/*    */   }
/*    */   
/*    */   public void setScript(String script) {
/* 49 */     this.script = script;
/*    */   }
/*    */   
/* 52 */   public void setLanguage(String language) { this.language = language; }
/*    */   
/*    */   public void setVariableName(String variableName) {
/* 55 */     this.variableName = variableName;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/ScriptActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */