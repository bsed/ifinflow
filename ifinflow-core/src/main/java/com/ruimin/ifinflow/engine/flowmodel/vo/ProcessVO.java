package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;
import java.util.Date;

public class ProcessVO implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String processId;
	protected String executionId;
	protected Date startTime;
	protected Date endTime;
	protected Long duration;
	protected String subject;
	protected Integer status;
	protected String initiatorId;
	protected String initiatorName;
	protected String ownerUnitId;
	protected String initiatorRole;
	protected String packageId;
	protected String templateId;
	protected String templeteName;
	protected Integer templeteVersion;
	protected Integer overdue;
	protected Date deadLine;
	protected String endActivityName;
	protected int priority;
	protected String superProcessId;

	public String getSuperProcessId() {
		return this.superProcessId;
	}

	public void setSuperProcessId(String superProcessId) {
		this.superProcessId = superProcessId;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTempleteName() {
		return this.templeteName;
	}

	public void setTempleteName(String templeteName) {
		this.templeteName = templeteName;
	}

	public Integer getTempleteVersion() {
		return this.templeteVersion;
	}

	public void setTempleteVersion(Integer templeteVersion) {
		this.templeteVersion = templeteVersion;
	}

	public Integer getOverdue() {
		return this.overdue;
	}

	public void setOverdue(Integer overdue) {
		this.overdue = overdue;
	}

	public Date getDeadLine() {
		return this.deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public String getPackageId() {
		return this.packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getInitiatorId() {
		return this.initiatorId;
	}

	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}

	public String getInitiatorName() {
		return this.initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

	public String getOwnerUnitId() {
		return this.ownerUnitId;
	}

	public void setOwnerUnitId(String ownerUnitId) {
		this.ownerUnitId = ownerUnitId;
	}

	public String getInitiatorRole() {
		return this.initiatorRole;
	}

	public void setInitiatorRole(String initiatorRole) {
		this.initiatorRole = initiatorRole;
	}

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getExecutionId() {
		return this.executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getDuration() {
		return this.duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getEndActivityName() {
		return this.endActivityName;
	}

	public void setEndActivityName(String endActivityName) {
		this.endActivityName = endActivityName;
	}
}
