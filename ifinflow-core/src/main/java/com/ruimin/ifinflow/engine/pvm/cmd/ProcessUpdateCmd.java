/*    */ package com.ruimin.ifinflow.engine.pvm.cmd;
/*    */ 
/*    */ import org.jbpm.api.cmd.Environment;
/*    */ import org.jbpm.pvm.internal.cmd.AbstractCommand;
/*    */ import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
/*    */ import org.jbpm.pvm.internal.session.DbSession;

import java.util.Collection;

/*    */
/*    */ 
/*    */ public class ProcessUpdateCmd
/*    */   extends AbstractCommand<Void>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private HistoryProcessInstanceImpl hpii;
/*    */   
/* 15 */   public ProcessUpdateCmd(HistoryProcessInstanceImpl hpii) { this.hpii = hpii; }
/*    */   
/*    */   public Void execute(Environment environment) {
/* 18 */     DbSession dbSession = (DbSession)environment.get(DbSession.class);
/* 19 */     dbSession.update(this.hpii);
/* 20 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/pvm/cmd/ProcessUpdateCmd.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */