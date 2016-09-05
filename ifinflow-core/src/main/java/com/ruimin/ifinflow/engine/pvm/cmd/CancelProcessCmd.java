package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.session.RepositorySession;

public class CancelProcessCmd
  extends AbstractCommand<Void>
{
  private static final long serialVersionUID = 1L;
  String processId;
  
  public CancelProcessCmd(String processId)
  {
    this.processId = processId;
  }
  
  public Void execute(Environment environment) throws Exception
  {
    Session session = (Session)EnvironmentImpl.getFromCurrent(Session.class);
    
    ExecutionImpl processInstance = (ExecutionImpl)session.load(ExecutionImpl.class, this.processId);
    
    String processDefinitionId = processInstance.getProcessDefinitionId();
    if (processDefinitionId == null) {
      throw new IFinFlowException(102001, new Object[] { this.processId });
    }
    RepositorySession repositorySession = (RepositorySession)EnvironmentImpl.getFromCurrent(RepositorySession.class);
    
    ProcessDefinitionImpl processDefinition = repositorySession.findProcessDefinitionById(processDefinitionId);
    if (processDefinition == null) {
      throw new IFinFlowException(102007, new Object[] { processDefinitionId });
    }
    
    processInstance.fire("cancelprocess", processDefinition);
    

    DbSession dbSession = (DbSession)EnvironmentImpl.getFromCurrent(DbSession.class);
    dbSession.cancelProcessInstance(this.processId);
    return null;
  }
}