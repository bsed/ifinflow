package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;
import java.util.Date;

public class ActivityVO implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String ID = "id";
	public static final String PROCESSID = "historyProcessInstance.dbid";
	public static final String NODE_ID = "nodeId";
	public static final String TYPE = "type";
	public static final String STATUS = "status";
	public static final String PACKAGEID = "packageId";
	public static final String TEMPLATEID = "templateId";
	public static final String TEMPLATEVERSION = "templateVersion";
	public static final String REJECT_AUTH = "rejectAuth";
	public static final String SKIP_AUTH = "skipAuth";
	public static final String OVERDUE = "overdue";
	public static final String DEAD_LINE = "deadLine";
	public static final String SOUREJECT_NAME = "souRejectName";
	private String id;
	private String processId;
	private String nodeId;
	private String type;
	private Integer status;
	private String packageId;
	private String templateId;
	private int templateVersion;
	private String rejectAuth;
	private String skipAuth;
	private Integer overdue;
	private Date deadLine;
	private String souRejectName;
	protected Date startTime;
	protected Date endTime;
	protected long duration;

	public long getDuration() {
		return this.duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
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

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPackageId() {
		return this.packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getRejectAuth() {
		return this.rejectAuth;
	}

	public void setRejectAuth(String rejectAuth) {
		this.rejectAuth = rejectAuth;
	}

	public String getSkipAuth() {
		return this.skipAuth;
	}

	public void setSkipAuth(String skipAuth) {
		this.skipAuth = skipAuth;
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

	public String getSouRejectName() {
		return this.souRejectName;
	}

	public void setSouRejectName(String souRejectName) {
		this.souRejectName = souRejectName;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getTemplateVersion() {
		return this.templateVersion;
	}

	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}
}
