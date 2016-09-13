 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.pvm.internal.cmd.AbstractCommand;

 import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
 import org.jbpm.pvm.internal.session.RepositorySession;
 import org.jbpm.pvm.internal.task.TaskImpl;

	import java.util.Collection;


 
 
 
 
 
 
 public class ProcessDefinitionFindByTaskId
   extends AbstractCommand<ProcessDefinitionImpl>
 {
   private static final long serialVersionUID = 1L;
   private TaskImpl task;
   
   public ProcessDefinitionFindByTaskId(TaskImpl task)
   {
     this.task = task;
   }
   
   public ProcessDefinitionImpl execute(Environment environment) throws Exception
   {
     if (this.task == null) {
       throw new IFinFlowException(103009, new Object[0]);
     }
     RepositorySession repositorySession = (RepositorySession)environment.get(RepositorySession.class);
     return repositorySession.findProcessDefinitionById(this.task.getProcessInstance().getProcessDefinitionId());
   }
 }

