/*    */ package com.ruimin.ifinflow.engine.pvm.cmd;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.hibernate.Query;
/*    */ import org.hibernate.Session;
/*    */ import org.jbpm.api.cmd.Environment;
/*    */ import org.jbpm.pvm.internal.cmd.AbstractCommand;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FindByHqlCmd
/*    */   extends AbstractCommand<List>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 17 */   String hql = null;
/*    */   
/*    */   public FindByHqlCmd(String hql) {
/* 20 */     this.hql = hql;
/*    */   }
/*    */   
/*    */   public List execute(Environment environment) throws Exception {
/* 24 */     Session session = (Session)environment.get(Session.class);
/* 25 */     Query query = session.createQuery(this.hql);
/* 26 */     return query.list();
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/pvm/cmd/FindByHqlCmd.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */