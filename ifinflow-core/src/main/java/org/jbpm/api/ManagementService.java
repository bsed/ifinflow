package org.jbpm.api;

public abstract interface ManagementService
{
  public abstract void executeJob(String paramString);
  
  public abstract JobQuery createJobQuery();
  
  public abstract boolean deleteJob(String paramString);
}
