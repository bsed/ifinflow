/*     */ package com.ruimin.ifinflow.engine.flowmodel.vo;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessVO
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected String processId;
/*     */   protected String executionId;
/*     */   protected Date startTime;
/*     */   protected Date endTime;
/*     */   protected Long duration;
/*     */   protected String subject;
/*     */   protected Integer status;
/*     */   protected String initiatorId;
/*     */   protected String initiatorName;
/*     */   protected String ownerUnitId;
/*     */   protected String initiatorRole;
/*     */   protected String packageId;
/*     */   protected String templateId;
/*     */   protected String templeteName;
/*     */   protected Integer templeteVersion;
/*     */   protected Integer overdue;
/*     */   protected Date deadLine;
/*     */   protected String endActivityName;
/*     */   protected int priority;
/*     */   protected String superProcessId;
/*     */   
/*     */   public String getSuperProcessId()
/*     */   {
/*  82 */     return this.superProcessId;
/*     */   }
/*     */   
/*  85 */   public void setSuperProcessId(String superProcessId) { this.superProcessId = superProcessId; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPriority()
/*     */   {
/*  92 */     return this.priority;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPriority(int priority)
/*     */   {
/*  98 */     this.priority = priority;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTemplateId()
/*     */   {
/* 105 */     return this.templateId;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTemplateId(String templateId)
/*     */   {
/* 111 */     this.templateId = templateId;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getTempleteName()
/*     */   {
/* 117 */     return this.templeteName;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTempleteName(String templeteName)
/*     */   {
/* 123 */     this.templeteName = templeteName;
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer getTempleteVersion()
/*     */   {
/* 129 */     return this.templeteVersion;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTempleteVersion(Integer templeteVersion)
/*     */   {
/* 135 */     this.templeteVersion = templeteVersion;
/*     */   }
/*     */   
/*     */   public Integer getOverdue() {
/* 139 */     return this.overdue;
/*     */   }
/*     */   
/*     */   public void setOverdue(Integer overdue) {
/* 143 */     this.overdue = overdue;
/*     */   }
/*     */   
/*     */   public Date getDeadLine() {
/* 147 */     return this.deadLine;
/*     */   }
/*     */   
/*     */   public void setDeadLine(Date deadLine) {
/* 151 */     this.deadLine = deadLine;
/*     */   }
/*     */   
/*     */   public String getPackageId() {
/* 155 */     return this.packageId;
/*     */   }
/*     */   
/*     */   public void setPackageId(String packageId) {
/* 159 */     this.packageId = packageId;
/*     */   }
/*     */   
/*     */   public Integer getStatus() {
/* 163 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(Integer status) {
/* 167 */     this.status = status;
/*     */   }
/*     */   
/*     */   public String getSubject() {
/* 171 */     return this.subject;
/*     */   }
/*     */   
/*     */   public void setSubject(String subject) {
/* 175 */     this.subject = subject;
/*     */   }
/*     */   
/*     */   public String getInitiatorId() {
/* 179 */     return this.initiatorId;
/*     */   }
/*     */   
/*     */   public void setInitiatorId(String initiatorId) {
/* 183 */     this.initiatorId = initiatorId;
/*     */   }
/*     */   
/*     */   public String getInitiatorName() {
/* 187 */     return this.initiatorName;
/*     */   }
/*     */   
/*     */   public void setInitiatorName(String initiatorName) {
/* 191 */     this.initiatorName = initiatorName;
/*     */   }
/*     */   
/*     */   public String getOwnerUnitId() {
/* 195 */     return this.ownerUnitId;
/*     */   }
/*     */   
/*     */   public void setOwnerUnitId(String ownerUnitId) {
/* 199 */     this.ownerUnitId = ownerUnitId;
/*     */   }
/*     */   
/*     */   public String getInitiatorRole() {
/* 203 */     return this.initiatorRole;
/*     */   }
/*     */   
/*     */   public void setInitiatorRole(String initiatorRole) {
/* 207 */     this.initiatorRole = initiatorRole;
/*     */   }
/*     */   
/*     */   public String getProcessId() {
/* 211 */     return this.processId;
/*     */   }
/*     */   
/*     */   public void setProcessId(String processId) {
/* 215 */     this.processId = processId;
/*     */   }
/*     */   
/*     */   public String getExecutionId() {
/* 219 */     return this.executionId;
/*     */   }
/*     */   
/*     */   public void setExecutionId(String executionId) {
/* 223 */     this.executionId = executionId;
/*     */   }
/*     */   
/*     */   public Date getStartTime() {
/* 227 */     return this.startTime;
/*     */   }
/*     */   
/*     */   public void setStartTime(Date startTime) {
/* 231 */     this.startTime = startTime;
/*     */   }
/*     */   
/*     */   public Date getEndTime() {
/* 235 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setEndTime(Date endTime) {
/* 239 */     this.endTime = endTime;
/*     */   }
/*     */   
/*     */   public Long getDuration() {
/* 243 */     return this.duration;
/*     */   }
/*     */   
/*     */   public void setDuration(Long duration) {
/* 247 */     this.duration = duration;
/*     */   }
/*     */   
/*     */   public String getEndActivityName() {
/* 251 */     return this.endActivityName;
/*     */   }
/*     */   
/*     */   public void setEndActivityName(String endActivityName) {
/* 255 */     this.endActivityName = endActivityName;
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/flowmodel/vo/ProcessVO.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */