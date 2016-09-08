 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import java.util.List;
 import org.hibernate.Query;
 import org.hibernate.Session;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.pvm.internal.cmd.AbstractCommand;
 
 
 
 
 
 public class FindByHqlCmd
   extends AbstractCommand<List>
 {
   private static final long serialVersionUID = 1L;
   String hql = null;
   
   public FindByHqlCmd(String hql) {
     this.hql = hql;
   }
   
   public List execute(Environment environment) throws Exception {
     Session session = (Session)environment.get(Session.class);
     Query query = session.createQuery(this.hql);
     return query.list();
   }
 }
