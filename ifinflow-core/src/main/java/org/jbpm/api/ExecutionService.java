package org.jbpm.api;

import java.util.Map;
import java.util.Set;

public abstract interface ExecutionService
{
  public abstract ProcessInstance startProcessInstanceById(String paramString);
  
  public abstract ProcessInstance startProcessInstanceById(String paramString1, String paramString2);
  
  public abstract ProcessInstance startProcessInstanceById(String paramString, Map<String, ?> paramMap);
  
  public abstract ProcessInstance startProcessInstanceById(String paramString1, Map<String, ?> paramMap, String paramString2);
  
  public abstract ProcessInstance startProcessInstanceByKey(String paramString);
  
  public abstract ProcessInstance startProcessInstanceByKey(String paramString1, String paramString2);
  
  public abstract ProcessInstance startProcessInstanceByKey(String paramString, Map<String, ?> paramMap);
  
  public abstract ProcessInstance startProcessInstanceByKey(String paramString1, Map<String, ?> paramMap, String paramString2);
  
  public abstract Execution findExecutionById(String paramString);
  
  public abstract ProcessInstance findProcessInstanceById(String paramString);
  
  public abstract ProcessInstance signalExecutionById(String paramString);
  
  public abstract ProcessInstance signalExecutionById(String paramString1, String paramString2);
  
  public abstract ProcessInstance signalExecutionById(String paramString1, String paramString2, Map<String, ?> paramMap);
  
  public abstract ProcessInstance signalExecutionById(String paramString, Map<String, ?> paramMap);
  
  public abstract ProcessInstanceQuery createProcessInstanceQuery();
  
  public abstract void setVariable(String paramString1, String paramString2, Object paramObject);
  
  public abstract void setVariables(String paramString, Map<String, ?> paramMap);
  
  public abstract void createVariable(String paramString1, String paramString2, Object paramObject, boolean paramBoolean);
  
  public abstract void createVariables(String paramString, Map<String, ?> paramMap, boolean paramBoolean);
  
  public abstract Object getVariable(String paramString1, String paramString2);
  
  public abstract Set<String> getVariableNames(String paramString);
  
  public abstract Map<String, Object> getVariables(String paramString, Set<String> paramSet);
  
  public abstract void endProcessInstance(String paramString1, String paramString2);
  
  public abstract void deleteProcessInstance(String paramString);
  
  public abstract void deleteProcessInstanceCascade(String paramString);
}
