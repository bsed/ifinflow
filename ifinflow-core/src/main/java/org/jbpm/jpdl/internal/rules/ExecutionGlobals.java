/*    */ package org.jbpm.jpdl.internal.rules;
/*    */ 
/*    */ import org.drools.runtime.Globals;
/*    */ import org.jbpm.api.Execution;
/*    */ import org.jbpm.internal.log.Log;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*    */ 
/*    */ public class ExecutionGlobals implements Globals
/*    */ {
/* 10 */   private static final Log log = Log.getLog(ExecutionGlobals.class.getName());
/*    */   
/*    */   ExecutionImpl execution;
/* 13 */   Outcome outcome = new Outcome();
/*    */   
/*    */   public ExecutionGlobals(Execution execution) {
/* 16 */     this.execution = ((ExecutionImpl)execution);
/*    */   }
/*    */   
/*    */   public Object get(String variableName) {
/* 20 */     if ("execution".equals(variableName)) {
/* 21 */       log.info("returning execution");
/* 22 */       return this.execution;
/*    */     }
/* 24 */     if ("outcome".equals(variableName)) {
/* 25 */       log.info("returning outcome");
/* 26 */       return this.outcome;
/*    */     }
/* 28 */     Object variableValue = this.execution.getVariable(variableName);
/* 29 */     log.info("returning variable " + variableName + ": " + variableValue);
/* 30 */     return variableValue;
/*    */   }
/*    */   
/*    */   public void set(String variableName, Object value) {
/* 34 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void setDelegate(Globals globals) {
/* 38 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/* 41 */   public Outcome getOutcome() { return this.outcome; }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/rules/ExecutionGlobals.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */