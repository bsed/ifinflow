/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.api.JbpmException;
/*    */ import org.jbpm.api.activity.ActivityExecution;
/*    */ import org.jbpm.api.model.Activity;
/*    */ import org.jbpm.api.model.Transition;
/*    */ import org.jbpm.pvm.internal.el.Expression;
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
/*    */ public class DecisionExpressionActivity
/*    */   extends JpdlActivity
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected Expression expression;
/*    */   
/*    */   public void execute(ActivityExecution execution)
/*    */   {
/* 41 */     execute((ExecutionImpl)execution);
/*    */   }
/*    */   
/*    */   public void execute(ExecutionImpl execution) {
/* 45 */     Activity activity = execution.getActivity();
/* 46 */     String transitionName = null;
/*    */     
/* 48 */     Object result = this.expression.evaluate(execution);
/* 49 */     if ((result != null) && (!(result instanceof String)))
/*    */     {
/*    */ 
/* 52 */       throw new JbpmException("expression '" + this.expression + "' in decision '" + activity.getName() + "' returned " + result.getClass().getName() + " instead of a transitionName (String): " + result);
/*    */     }
/* 54 */     transitionName = (String)result;
/*    */     
/* 56 */     Transition transition = activity.getOutgoingTransition(transitionName);
/* 57 */     if (transition == null) {
/* 58 */       throw new JbpmException("expression '" + this.expression + "' in decision '" + activity.getName() + "' returned unexisting outgoing transition name: " + transitionName);
/*    */     }
/*    */     
/* 61 */     execution.historyDecision(transitionName);
/*    */     
/* 63 */     execution.take(transition);
/*    */   }
/*    */   
/*    */   public void setExpression(Expression expression) {
/* 67 */     this.expression = expression;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/DecisionExpressionActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */