package org.jbpm.api.activity;

import java.io.Serializable;

public abstract interface ActivityBehaviour
  extends Serializable
{
  public abstract void execute(ActivityExecution paramActivityExecution)
    throws Exception;
}

