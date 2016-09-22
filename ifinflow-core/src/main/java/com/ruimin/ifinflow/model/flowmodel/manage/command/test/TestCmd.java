 package com.ruimin.ifinflow.model.flowmodel.manage.command.test;
 
 import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;


 public class TestCmd
   implements Command<Void>
 {
   public Void execute(Environment environment)
     throws Exception
   {
     Template t = (Template)((Session)environment.get(Session.class)).createQuery("from Template o where o.deploymentId = 1").uniqueResult();
     return null;
   }
 }