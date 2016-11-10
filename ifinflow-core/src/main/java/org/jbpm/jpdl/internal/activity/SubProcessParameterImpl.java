/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class SubProcessParameterImpl
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected String subVariableName;
/*    */   protected String variableName;
/*    */   protected Expression expression;
/*    */   
/*    */   public String getVariableName()
/*    */   {
/* 41 */     return this.variableName;
/*    */   }
/*    */   
/* 44 */   public void setVariableName(String variable) { this.variableName = variable; }
/*    */   
/*    */   public Expression getExpression() {
/* 47 */     return this.expression;
/*    */   }
/*    */   
/* 50 */   public void setExpression(Expression expression) { this.expression = expression; }
/*    */   
/*    */   public String getSubVariableName() {
/* 53 */     return this.subVariableName;
/*    */   }
/*    */   
/* 56 */   public void setSubVariableName(String name) { this.subVariableName = name; }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/SubProcessParameterImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */