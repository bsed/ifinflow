 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import org.hibernate.LockMode;
 import org.hibernate.Query;
 import org.hibernate.Session;
 import org.jbpm.api.cmd.Command;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.pvm.internal.model.TmpExecution;

	import java.util.Collection;


 
 
 
 public class UpdateTmpExecutionAppIdCmd
   implements Command<Void>
 {
   private static final long serialVersionUID = 1L;
   private String sql;
   private int executionCount;
   
   public UpdateTmpExecutionAppIdCmd(String taskDbid, int executionCount)
   {
     this.sql = taskDbid;
     this.executionCount = executionCount;
   }
   
   public Void execute(Environment environment) throws Exception {
     Session session = (Session)environment.get(Session.class);
     
     String tranSql = "from " + TmpExecution.class.getName() + " as tmpExecution";
     Query qu = session.createQuery(tranSql);
     qu.setLockMode("tmpExecution", LockMode.UPGRADE);
     qu.list();
     
     Query query = session.createSQLQuery(this.sql);
     if (this.executionCount > 0) {
       query.setInteger("count", this.executionCount);
     }
     query.executeUpdate();
     return null;
   }
 }

