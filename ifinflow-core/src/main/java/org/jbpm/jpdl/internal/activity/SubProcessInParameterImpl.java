/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.pvm.internal.el.Expression;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
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
/*    */ public class SubProcessInParameterImpl
/*    */   extends SubProcessParameterImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public void produce(ExecutionImpl superExecution, ExecutionImpl subProcessInstance)
/*    */   {
/* 52 */     Object value = null;
/* 53 */     if (this.variableName != null) {
/* 54 */       value = superExecution.getVariablePrototype(this.variableName);
/* 55 */       if (value != null) {
/* 56 */         Variable var = (Variable)value;
/* 57 */         subProcessInstance.setVariable(this.subVariableName, var.getBizName(), var.getValue(superExecution), var.getKind());
/*    */       }
/*    */     }
/*    */     else {
/* 61 */       value = this.expression.evaluateInScope(superExecution);
/* 62 */       subProcessInstance.setVariable(this.subVariableName, value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/SubProcessInParameterImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */