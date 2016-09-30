package org.jbpm.api;

import java.util.Map;
import java.util.Set;
import org.jbpm.api.history.HistoryActivityInstanceQuery;
import org.jbpm.api.history.HistoryDetailQuery;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.jbpm.api.history.HistoryTaskQuery;

public abstract interface HistoryService
{
  public abstract HistoryProcessInstanceQuery createHistoryProcessInstanceQuery();
  
  public abstract HistoryActivityInstanceQuery createHistoryActivityInstanceQuery();
  
  public abstract HistoryTaskQuery createHistoryTaskQuery();
  
  public abstract HistoryDetailQuery createHistoryDetailQuery();
  
  public abstract Map<String, Number> avgDurationPerActivity(String paramString);
  
  public abstract Map<String, Number> choiceDistribution(String paramString1, String paramString2);
  
  public abstract Set<String> getVariableNames(String paramString);
  
  public abstract Object getVariable(String paramString1, String paramString2);
  
  public abstract Map<String, ?> getVariables(String paramString, Set<String> paramSet);
}

