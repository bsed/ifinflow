package org.jbpm.api;

import java.util.List;
import org.jbpm.api.job.Job;

public abstract interface JobQuery
{
  public static final String PROPERTY_DUEDATE = "dueDate";
  public static final String PROPERTY_STATE = "state";
  
  public abstract JobQuery messages();
  
  public abstract JobQuery timers();
  
  public abstract JobQuery processInstanceId(String paramString);
  
  public abstract JobQuery exception(boolean paramBoolean);
  
  public abstract JobQuery orderAsc(String paramString);
  
  public abstract JobQuery orderDesc(String paramString);
  
  public abstract JobQuery page(int paramInt1, int paramInt2);
  
  public abstract List<Job> list();
  
  public abstract Job uniqueResult();
  
  public abstract long count();
  
  public abstract JobQuery executionDbid(String paramString);
}
