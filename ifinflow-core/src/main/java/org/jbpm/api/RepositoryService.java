package org.jbpm.api;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import org.jbpm.api.model.ActivityCoordinates;

public abstract interface RepositoryService
{
  public abstract NewDeployment createDeployment();
  
  public abstract void suspendDeployment(String paramString);
  
  public abstract void resumeDeployment(String paramString);
  
  public abstract void deleteDeployment(String paramString);
  
  public abstract void deleteDeploymentCascade(String paramString);
  
  public abstract Set<String> getResourceNames(String paramString);
  
  public abstract InputStream getResourceAsStream(String paramString1, String paramString2);
  
  public abstract ProcessDefinitionQuery createProcessDefinitionQuery();
  
  public abstract DeploymentQuery createDeploymentQuery();
  
  public abstract List<String> getStartActivityNames(String paramString);
  
  public abstract String getStartFormResourceName(String paramString1, String paramString2);
  
  public abstract ActivityCoordinates getActivityCoordinates(String paramString1, String paramString2);
}
