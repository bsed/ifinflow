package com.ruimin.ifinflow.engine.pvm.event;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.hibernate.Session;
import org.jbpm.jpdl.internal.assign.AssignTask;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.HistoryEvent;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;










public class HistoryTaskReturn
  extends HistoryEvent
{
  private static final long serialVersionUID = 1L;
  private AssignTask assignTask;
  
  public HistoryTaskReturn(AssignTask assignTask)
  {
    this.assignTask = assignTask;
  }
  
  public void process()
  {
    Session session = (Session)EnvironmentImpl.getFromCurrent(Session.class);
    String historyActivityInstanceDbId = this.execution.getHistoryActivityInstanceDbid();
    
    HistoryTaskInstanceImpl historyTaskInstance = (HistoryTaskInstanceImpl)session.load(HistoryTaskInstanceImpl.class, historyActivityInstanceDbId);
    

    HistoryTaskImpl historyTask = historyTaskInstance.getHistoryTask();
    if (historyTask.getAssignMode().intValue() != 2) {
      throw new IFinFlowException(103011, new Object[0]);
    }
    if (historyTask.getStatus().intValue() != 2) {
      throw new IFinFlowException(103012, new Object[0]);
    }
    
    historyTask.setAssignee(this.assignTask.getOwnerId());
    historyTask.setOwnerId(this.assignTask.getOwnerId());
    historyTask.setOwnerName(null);
    historyTask.setOwnerRoleId(this.assignTask.getOwnerRoleId());
    historyTask.setOwnerUnitId(this.assignTask.getOwnerUnitId());
    historyTask.setOwnerGroupId(this.assignTask.getOwnerGroupId());
    historyTask.setTakeDate(null);
    historyTask.setStatus(Integer.valueOf(1));
    
    historyTaskInstance.setStatus(Integer.valueOf(1));
    session.update(historyTaskInstance);
  }
}

