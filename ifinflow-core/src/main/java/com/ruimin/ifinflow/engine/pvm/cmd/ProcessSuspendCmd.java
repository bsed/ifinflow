/*    */ package com.ruimin.ifinflow.engine.pvm.cmd;
/*    */ 
/*    */ import org.jbpm.api.JbpmException;
/*    */
/*    */ import org.jbpm.api.cmd.Environment;
/*    */ import org.jbpm.pvm.internal.cmd.AbstractCommand;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*    */
/*    */ import org.jbpm.pvm.internal.session.DbSession;

import java.util.Collection;

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
/*    */ public class ProcessSuspendCmd
/*    */   extends AbstractCommand<Void>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   String processId;
/*    */   String taskId;
/*    */   
/* 27 */   public ProcessSuspendCmd(String processId) { this.processId = processId; }
/*    */   
/*    */   public Void execute(Environment environment) throws Exception {
/* 30 */     DbSession dbSession = (DbSession)environment.get(DbSession.class);
/* 31 */     ExecutionImpl process = null;
/* 32 */     process = (ExecutionImpl)dbSession.createProcessInstanceQuery().dbid(this.processId).uniqueResult();
/*    */     
/* 34 */     if (process == null) {
/* 35 */       throw new JbpmException("没找到对应流程实例，processId=" + this.processId);
/*    */     }
/* 37 */     process.suspend();
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/pvm/cmd/ProcessSuspendCmd.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */