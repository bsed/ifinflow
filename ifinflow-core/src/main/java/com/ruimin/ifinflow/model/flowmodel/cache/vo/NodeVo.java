package com.ruimin.ifinflow.model.flowmodel.cache.vo;

import java.io.Serializable;
import java.util.List;



























































































public class NodeVo
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String name;
  private int assignMode;
  private String skipAuth;
  private String rejectAuth;
  private long overtime;
  private int type;
  private String exitType;
  private int exitCount;
  private List<String[]> variableNameList;
  private String rejectdContinue;
  private String assignHandle;
  private int participantType;
  private int participantAssign;
  private int participantHistory;
  private String result;
  private int priority;
  private String url;
  private String rejectDefault;
  private int display;
  private String rejectDefaultNodeId;
  private String rejectAssignType;
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public int getAssignMode() {
    return this.assignMode;
  }
  
  public void setAssignMode(int assignMode) {
    this.assignMode = assignMode;
  }
  
  public String getSkipAuth() {
    return this.skipAuth;
  }
  
  public void setSkipAuth(String skipAuth) {
    this.skipAuth = skipAuth;
  }
  
  public String getRejectAuth() {
    return this.rejectAuth;
  }
  
  public void setRejectAuth(String rejectAuth) {
    this.rejectAuth = rejectAuth;
  }
  
  public long getOvertime() {
    return this.overtime;
  }
  
  public void setOvertime(long overtime) {
    this.overtime = overtime;
  }
  
  public int getType() {
    return this.type;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public String getExitType() {
    return this.exitType;
  }
  
  public void setExitType(String exitType) {
    this.exitType = exitType;
  }
  
  public int getExitCount() {
    return this.exitCount;
  }
  
  public void setExitCount(int exitCount) {
    this.exitCount = exitCount;
  }
  
  public List<String[]> getVariableNameList() {
    return this.variableNameList;
  }
  
  public void setVariableNameList(List<String[]> variableNameList) {
    this.variableNameList = variableNameList;
  }
  
  public String getRejectdContinue() {
    return this.rejectdContinue;
  }
  
  public void setRejectdContinue(String rejectdContinue) {
    this.rejectdContinue = rejectdContinue;
  }
  
  public String getAssignHandle() {
    return this.assignHandle;
  }
  
  public void setAssignHandle(String assignHandle) {
    this.assignHandle = assignHandle;
  }
  
  public int getParticipantType() {
    return this.participantType;
  }
  
  public void setParticipantType(int participantType) {
    this.participantType = participantType;
  }
  
  public int getParticipantAssign() {
    return this.participantAssign;
  }
  
  public void setParticipantAssign(int participantAssign) {
    this.participantAssign = participantAssign;
  }
  
  public int getParticipantHistory() {
    return this.participantHistory;
  }
  
  public void setParticipantHistory(int participantHistory) {
    this.participantHistory = participantHistory;
  }
  
  public String getResult() {
    return this.result;
  }
  
  public void setResult(String result) {
    this.result = result;
  }
  
  public int getPriority() {
    return this.priority;
  }
  
  public void setPriority(int priority) {
    this.priority = priority;
  }
  
  public String getUrl() {
    return this.url;
  }
  
  public void setUrl(String url) {
    this.url = url;
  }
  
  public String getRejectDefault() {
    return this.rejectDefault;
  }
  
  public void setRejectDefault(String rejectDefault) {
    this.rejectDefault = rejectDefault;
  }
  



  public int getDisplay()
  {
    return this.display;
  }
  
  public void setDisplay(int display) {
    this.display = display;
  }
  
  public String getRejectDefaultNodeId() {
    return this.rejectDefaultNodeId;
  }
  
  public void setRejectDefaultNodeId(String rejectDefaultNodeId) {
    this.rejectDefaultNodeId = rejectDefaultNodeId;
  }
  
  public String getRejectAssignType() {
    return this.rejectAssignType;
  }
  
  public void setRejectAssignType(String rejectAssignType) {
    this.rejectAssignType = rejectAssignType;
  }
}

