package com.ruimin.ifinflow.engine.pvm.activity;

import java.util.Map;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.activity.ExternalActivityBehaviour;

public class VirtualActivity
  implements ExternalActivityBehaviour
{
  private static final long serialVersionUID = 1L;
  
  public void execute(ActivityExecution execution)
    throws Exception
  {}
  
  public void signal(ActivityExecution execution, String signalName, Map<String, ?> parameters)
    throws Exception
  {}
}


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/pvm/activity/VirtualActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */