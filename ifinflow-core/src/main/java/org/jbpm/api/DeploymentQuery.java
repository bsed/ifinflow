package org.jbpm.api;

import java.util.List;

public abstract interface DeploymentQuery
{
  public static final String PROPERTY_TIMESTAMP = "timestamp";
  public static final String PROPERTY_STATE = "state";
  
  public abstract DeploymentQuery deploymentId(String paramString);
  
  public abstract DeploymentQuery suspended();
  
  public abstract DeploymentQuery notSuspended();
  
  public abstract DeploymentQuery orderAsc(String paramString);
  
  public abstract DeploymentQuery orderDesc(String paramString);
  
  public abstract DeploymentQuery page(int paramInt1, int paramInt2);
  
  public abstract List<Deployment> list();
  
  public abstract Deployment uniqueResult();
  
  public abstract long count();
}
