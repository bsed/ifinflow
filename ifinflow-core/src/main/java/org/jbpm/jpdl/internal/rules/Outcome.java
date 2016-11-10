/*    */ package org.jbpm.jpdl.internal.rules;
/*    */ 
/*    */ import org.jbpm.internal.log.Log;
/*    */ 
/*    */ public class Outcome
/*    */ {
/*  7 */   private static final Log log = Log.getLog(Outcome.class.getName());
/*    */   
/*  9 */   String outcome = null;
/*    */   
/*    */   public void set(String outcome) {
/* 12 */     log.info("outcome is being set to " + outcome);
/* 13 */     this.outcome = outcome;
/*    */   }
/*    */   
/*    */   public boolean isDefined() {
/* 17 */     return this.outcome != null;
/*    */   }
/*    */   
/*    */   public String get() {
/* 21 */     log.info("outcome " + this.outcome + " is being fetched");
/* 22 */     return this.outcome;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/rules/Outcome.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */