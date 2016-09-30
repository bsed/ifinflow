package org.jbpm.api;

import java.util.List;

public abstract interface ProcessInstanceQuery
{
  public static final String PROPERTY_KEY = "key";
  
  public abstract ProcessInstanceQuery processDefinitionId(String paramString);
  
  public abstract ProcessInstanceQuery processInstanceId(String paramString);
  
  public abstract ProcessInstanceQuery processInstanceKey(String paramString);
  
  public abstract ProcessInstanceQuery suspended();
  
  public abstract ProcessInstanceQuery notSuspended();
  
  public abstract ProcessInstanceQuery orderAsc(String paramString);
  
  public abstract ProcessInstanceQuery orderDesc(String paramString);
  
  public abstract ProcessInstanceQuery page(int paramInt1, int paramInt2);
  
  public abstract List<ProcessInstance> list();
  
  public abstract ProcessInstance uniqueResult();
  
  public abstract long count();
  
  public abstract ProcessInstanceQuery dbid(String paramString);
}

