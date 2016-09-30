package org.jbpm.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jbpm.api.history.HistoryComment;
import org.jbpm.api.task.Participation;
import org.jbpm.api.task.Task;

public abstract interface TaskService
{
  public abstract Task newTask();
  
  public abstract Task newTask(String paramString);
  
  public abstract String saveTask(Task paramTask);
  
  public abstract Task getTask(String paramString);
  
  public abstract void assignTask(String paramString1, String paramString2);
  
  public abstract void takeTask(String paramString1, String paramString2);
  
  public abstract void completeTask(String paramString);
  
  public abstract void completeTask(String paramString, Map<String, ?> paramMap);
  
  public abstract void completeTask(String paramString1, String paramString2);
  
  public abstract void completeTask(String paramString1, String paramString2, Map<String, ?> paramMap);
  
  public abstract void deleteTask(String paramString);
  
  public abstract void deleteTaskCascade(String paramString);
  
  public abstract void deleteTask(String paramString1, String paramString2);
  
  public abstract void addTaskParticipatingUser(String paramString1, String paramString2, String paramString3);
  
  public abstract void addTaskParticipatingGroup(String paramString1, String paramString2, String paramString3);
  
  public abstract List<Participation> getTaskParticipations(String paramString);
  
  public abstract void removeTaskParticipatingUser(String paramString1, String paramString2, String paramString3);
  
  public abstract void removeTaskParticipatingGroup(String paramString1, String paramString2, String paramString3);
  
  public abstract TaskQuery createTaskQuery();
  
  public abstract List<Task> findPersonalTasks(String paramString);
  
  public abstract List<Task> findGroupTasks(String paramString);
  
  public abstract List<Task> getSubTasks(String paramString);
  
  public abstract HistoryComment addTaskComment(String paramString1, String paramString2);
  
  public abstract List<HistoryComment> getTaskComments(String paramString);
  
  public abstract HistoryComment addReplyComment(String paramString1, String paramString2);
  
  public abstract void deleteComment(String paramString);
  
  public abstract void setVariables(String paramString, Map<String, ?> paramMap);
  
  public abstract Object getVariable(String paramString1, String paramString2);
  
  public abstract Set<String> getVariableNames(String paramString);
  
  public abstract Map<String, Object> getVariables(String paramString, Set<String> paramSet);
  
  public abstract Set<String> getOutcomes(String paramString);
}

