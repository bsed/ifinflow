/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.pvm.internal.el.Expression;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*    */ import org.jbpm.pvm.internal.model.ScopeInstanceImpl;
/*    */ import org.jbpm.pvm.internal.type.Variable;
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
/*    */ public class SubProcessOutParameterImpl
/*    */   extends SubProcessParameterImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public void consume(ExecutionImpl superExecution, ScopeInstanceImpl subProcessInstance)
/*    */   {
/* 54 */     Object value = null;
/* 55 */     if (this.variableName != null) {
/* 56 */       value = subProcessInstance.getVariablePrototype(this.subVariableName);
/* 57 */       if (value != null) {
/* 58 */         Variable var = (Variable)value;
/* 59 */         superExecution.setVariable(this.variableName, var.getBizName(), var.getValue(subProcessInstance), var.getKind());
/*    */       }
/*    */     }
/*    */     else {
/* 63 */       value = this.expression.evaluateInScope(subProcessInstance);
/* 64 */       superExecution.setVariable(this.variableName, value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/SubProcessOutParameterImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */