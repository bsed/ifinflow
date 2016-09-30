package org.jbpm.api.history;

import java.io.Serializable;
import java.util.Date;

public abstract interface HistoryDetail
  extends Serializable
{
  public abstract String getId();
  
  public abstract String getUserId();
  
  public abstract Date getTime();
}

