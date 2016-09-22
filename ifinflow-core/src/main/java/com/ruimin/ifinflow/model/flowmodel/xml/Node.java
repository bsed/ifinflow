 package com.ruimin.ifinflow.model.flowmodel.xml;
 
 import java.io.Serializable;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class Node
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private String handle;
   private String templateHandle;
   private String nodeId;
   private String name;
   private String remark;
   private int kind;
   private String url;
   private String oprateAuth;
   private String skipAuth;
   private String rejectAuth;
   private String rejectDefault;
   private String rejectdContinue;
   private String adapterType;
   private String adapterName;
   private String matchId;
   private int priority;
   private int display = 1;
   
 
 
   private String rejectDefaultNodeId;
   
 
 
   private String rejectAssignType;
   
 
 
   public String getRejectAuth()
   {
     return this.rejectAuth;
   }
   
   public void setRejectAuth(String rejectAuth) {
     this.rejectAuth = rejectAuth;
   }
   
   public String getRejectDefault() {
     return this.rejectDefault;
   }
   
   public void setRejectDefault(String rejectDefault) {
     this.rejectDefault = rejectDefault;
   }
   
   public String getRejectdContinue() {
     return this.rejectdContinue;
   }
   
   public void setRejectdContinue(String rejectdContinue) {
     this.rejectdContinue = rejectdContinue;
   }
   
   public String getAdapterType() {
     return this.adapterType;
   }
   
   public void setAdapterType(String adapterType) {
     this.adapterType = adapterType;
   }
   
   public String getAdapterName() {
     return this.adapterName;
   }
   
   public void setAdapterName(String adapterName) {
     this.adapterName = adapterName;
   }
   
   public String getMatchId() {
     return this.matchId;
   }
   
   public void setMatchId(String matchId) {
     this.matchId = matchId;
   }
   
   public int getPriority() {
     return this.priority;
   }
   
   public void setPriority(int priority) {
     this.priority = priority;
   }
   
   public String getHandle() {
     return this.handle;
   }
   
   public void setHandle(String handle) {
     this.handle = handle;
   }
   
   public String getTemplateHandle() {
     return this.templateHandle;
   }
   
   public void setTemplateHandle(String templateHandle) {
     this.templateHandle = templateHandle;
   }
   
   public String getNodeId() {
     return this.nodeId;
   }
   
   public void setNodeId(String nodeId) {
     this.nodeId = nodeId;
   }
   
   public String getName() {
     return this.name;
   }
   
   public void setName(String name) {
     this.name = name;
   }
   
   public String getRemark() {
     return this.remark;
   }
   
   public void setRemark(String remark) {
     this.remark = remark;
   }
   
   public int getKind() {
     return this.kind;
   }
   
   public void setKind(int kind) {
     this.kind = kind;
   }
   
   public String getUrl() {
     return this.url;
   }
   
   public void setUrl(String url) {
     this.url = url;
   }
   
   public String getOprateAuth() {
     return this.oprateAuth;
   }
   
   public void setOprateAuth(String oprateAuth) {
     this.oprateAuth = oprateAuth;
   }
   
   public String getSkipAuth() {
     return this.skipAuth;
   }
   
   public void setSkipAuth(String skipAuth) {
     this.skipAuth = skipAuth;
   }
   
   public int getDisplay() {
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

