/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.jbpm.api.activity.ActivityExecution;
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
/*    */ public class RulesFact
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected String variableName;
/*    */   protected Expression expression;
/*    */   protected String language;
/*    */   
/*    */   public String getVariableName()
/*    */   {
/* 41 */     return this.variableName;
/*    */   }
/*    */   
/*    */   public void setVariableName(String variableName) {
/* 45 */     this.variableName = variableName;
/*    */   }
/*    */   
/*    */   public Expression getExpression() {
/* 49 */     return this.expression;
/*    */   }
/*    */   
/*    */   public void setExpression(Expression expression) {
/* 53 */     this.expression = expression;
/*    */   }
/*    */   
/*    */   public String getLanguage() {
/* 57 */     return this.language;
/*    */   }
/*    */   
/*    */   public void setLanguage(String language) {
/* 61 */     this.language = language;
/*    */   }
/*    */   
/*    */   public Object getObject(ActivityExecution execution) {
/* 65 */     if (this.variableName != null) {
/* 66 */       return execution.getVariable(this.variableName);
/*    */     }
/* 68 */     if (this.expression != null) {
/* 69 */       return this.expression.evaluate(execution);
/*    */     }
/*    */     
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/RulesFact.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */