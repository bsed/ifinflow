package org.jbpm.api;

import java.util.List;

public abstract interface ProcessDefinitionQuery
{
  public static final String PROPERTY_ID = "idProperty.stringValue";
  public static final String PROPERTY_KEY = "keyProperty.stringValue";
  public static final String PROPERTY_NAME = "idProperty.objectName";
  public static final String PROPERTY_VERSION = "versionProperty.longValue";
  public static final String PROPERTY_DEPLOYMENT_TIMESTAMP = "deployment.timestamp";
  
  public abstract ProcessDefinitionQuery processDefinitionId(String paramString);
  
  public abstract ProcessDefinitionQuery processDefinitionKey(String paramString);
  
  public abstract ProcessDefinitionQuery processDefinitionNameLike(String paramString);
  
  public abstract ProcessDefinitionQuery processDefinitionName(String paramString);
  
  public abstract ProcessDefinitionQuery deploymentId(String paramString);
  
  public abstract ProcessDefinitionQuery suspended();
  
  public abstract ProcessDefinitionQuery notSuspended();
  
  public abstract ProcessDefinitionQuery orderAsc(String paramString);
  
  public abstract ProcessDefinitionQuery orderDesc(String paramString);
  
  public abstract ProcessDefinitionQuery page(int paramInt1, int paramInt2);
  
  public abstract List<ProcessDefinition> list();
  
  public abstract ProcessDefinition uniqueResult();
  
  public abstract long count();
}

