package org.jbpm.api;

import java.util.List;
import org.jbpm.api.task.Task;

public abstract interface TaskQuery
{
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_ASSIGNEE = "assignee";
  public static final String PROPERTY_CREATEDATE = "createTime";
  public static final String PROPERTY_DUEDATE = "duedate";
  public static final String PROPERTY_PRIORITY = "priority";
  public static final String PROPERTY_PROGRESS = "progress";
  
  public abstract TaskQuery assignee(String paramString);
  
  public abstract TaskQuery unassigned();
  
  public abstract TaskQuery candidate(String paramString);
  
  public abstract TaskQuery executionId(String paramString);
  
  public abstract TaskQuery processInstanceId(String paramString);
  
  public abstract TaskQuery processDefinitionId(String paramString);
  
  public abstract TaskQuery activityName(String paramString);
  
  public abstract TaskQuery suspended();
  
  public abstract TaskQuery notSuspended();
  
  public abstract TaskQuery page(int paramInt1, int paramInt2);
  
  public abstract TaskQuery orderAsc(String paramString);
  
  public abstract TaskQuery orderDesc(String paramString);
  
  public abstract long count();
  
  public abstract List<Task> list();
  
  public abstract Task uniqueResult();
}

