package com.ruimin.ifinflow.engine.internal.vo;

import java.io.Serializable;



















public class TaskStatisticsVo
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String nodeId;
  private String nodeName;
  private String duraton;
  public String newStatusCount = "0";
  

  public String assignedStatusCount = "0";
  

  public String suspendedStatusCount = "0";
  

  public String rejectStatusCount = "0";
  

  public String takeBackStatusCount = "0";
  

  public String skipStatusCount = "0";
  

  public String completedStatusCount = "0";
  

  public String cancelStatusCount = "0";
  

  public String abandonStatusCount = "0";
  

  public String timeOutStatusCount = "0";
  

  public String exceptionStatusCount = "0";
  

  private String todoCount = "0";
  
  private String doneCount = "0";
  
  private String rejectCount = "0";
  
  private String suspendCount = "0";
  
  private String allCount = "0";
  
  public String getTodoCount() {
    return this.todoCount;
  }
  
  public void setTodoCount(String todoCount) {
    this.todoCount = todoCount;
  }
  
  public String getDoneCount() {
    return this.doneCount;
  }
  
  public void setDoneCount(String doneCount) {
    this.doneCount = doneCount;
  }
  
  public String getRejectCount() {
    return this.rejectCount;
  }
  
  public void setRejectCount(String rejectCount) {
    this.rejectCount = rejectCount;
  }
  
  public String getAllCount() {
    return this.allCount;
  }
  
  public void setAllCount(String allCount) {
    this.allCount = allCount;
  }
  
  public String getNodeId() {
    return this.nodeId;
  }
  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }
  
  public String getNodeName() {
    return this.nodeName;
  }
  
  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }
  
  public String getNewStatusCount() {
    return this.newStatusCount;
  }
  
  public void setNewStatusCount(String newStatusCount) {
    this.newStatusCount = newStatusCount;
  }
  
  public String getAssignedStatusCount() {
    return this.assignedStatusCount;
  }
  
  public void setAssignedStatusCount(String assignedStatusCount) {
    this.assignedStatusCount = assignedStatusCount;
  }
  
  public String getSuspendedStatusCount() {
    return this.suspendedStatusCount;
  }
  
  public void setSuspendedStatusCount(String suspendedStatusCount) {
    this.suspendedStatusCount = suspendedStatusCount;
  }
  
  public String getRejectStatusCount() {
    return this.rejectStatusCount;
  }
  
  public void setRejectStatusCount(String rejectStatusCount) {
    this.rejectStatusCount = rejectStatusCount;
  }
  
  public String getTakeBackStatusCount() {
    return this.takeBackStatusCount;
  }
  
  public void setTakeBackStatusCount(String takeBackStatusCount) {
    this.takeBackStatusCount = takeBackStatusCount;
  }
  
  public String getSkipStatusCount() {
    return this.skipStatusCount;
  }
  
  public void setSkipStatusCount(String skipStatusCount) {
    this.skipStatusCount = skipStatusCount;
  }
  
  public String getCompletedStatusCount() {
    return this.completedStatusCount;
  }
  
  public void setCompletedStatusCount(String completedStatusCount) {
    this.completedStatusCount = completedStatusCount;
  }
  
  public String getCancelStatusCount() {
    return this.cancelStatusCount;
  }
  
  public void setCancelStatusCount(String cancelStatusCount) {
    this.cancelStatusCount = cancelStatusCount;
  }
  
  public String getAbandonStatusCount() {
    return this.abandonStatusCount;
  }
  
  public void setAbandonStatusCount(String abandonStatusCount) {
    this.abandonStatusCount = abandonStatusCount;
  }
  
  public String getTimeOutStatusCount() {
    return this.timeOutStatusCount;
  }
  
  public void setTimeOutStatusCount(String timeOutStatusCount) {
    this.timeOutStatusCount = timeOutStatusCount;
  }
  
  public String getExceptionStatusCount() {
    return this.exceptionStatusCount;
  }
  
  public void setExceptionStatusCount(String exceptionStatusCount) {
    this.exceptionStatusCount = exceptionStatusCount;
  }
  
  public String getDuraton() {
    return this.duraton;
  }
  
  public void setDuraton(String duraton) {
    this.duraton = duraton;
  }
  
  public String getSuspendCount() {
    return this.suspendCount;
  }
  
  public void setSuspendCount(String suspendCount) {
    this.suspendCount = suspendCount;
  }
}

