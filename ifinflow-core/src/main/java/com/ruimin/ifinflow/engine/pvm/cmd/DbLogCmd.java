 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import com.ruimin.ifinflow.engine.internal.entity.IFinFlowJProcessExcep;
 import com.ruimin.ifinflow.engine.internal.entity.IFinFlowLog;
 import org.hibernate.Session;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.pvm.internal.cmd.AbstractCommand;
 import org.jbpm.pvm.internal.cmd.CommandService;
 import org.jbpm.pvm.internal.env.BasicEnvironment;

 public class DbLogCmd
   extends AbstractCommand<Object>
 {
   private static final long serialVersionUID = 1L;
   private Object log;

   public DbLogCmd(IFinFlowLog log)
   {
     this.log = log;
   }

   public DbLogCmd(IFinFlowJProcessExcep log) {
     this.log = log;
   }

   public Object execute(Environment environment) throws Exception {
     CommandService commandService = (CommandService)environment.get("newTxRequiredCommandService");
     return commandService.execute(new LogCmd());
   }

   class LogCmd extends AbstractCommand<Object> { LogCmd() {}

     public Object execute(Environment environment) throws Exception { Session session = (Session)BasicEnvironment.getFromCurrent(Session.class);
       session.save(DbLogCmd.this.log);
       return DbLogCmd.this.log;
     }
   }
 }