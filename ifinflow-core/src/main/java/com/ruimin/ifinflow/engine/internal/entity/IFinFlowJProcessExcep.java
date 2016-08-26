package com.ruimin.ifinflow.engine.internal.entity;

import java.io.Serializable;
import java.util.Date;

public class IFinFlowJProcessExcep implements Serializable {
	private static final long serialVersionUID = 1L;
	private String dbid;
	private String packageId;
	private String hproci;
	private String hmprocl;
	private String hacti;
	private String expCode;
	private String expMessage;
	private int status;
	private String nodeId;
	private String nodeName;
	private String nodeKind;
	private String templateId;
	private String templateName;
	private Integer templateVersion;
	private String subject;
	private Date createdDateTime;
	private String resolverId;
	private String resolverName;
	private Date resolvedTime;
	private int exceptionPos;
	private int executionStatus;
	private String executionId;
	private String operationName;

	public String getDbid() {
		return this.dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getPackageId() {
		return this.packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getHproci() {
		return this.hproci;
	}

	public void setHproci(String hproci) {
		this.hproci = hproci;
	}

	public String getHmprocl() {
		return this.hmprocl;
	}

	public void setHmprocl(String hmprocl) {
		this.hmprocl = hmprocl;
	}

	public String getHacti() {
		return this.hacti;
	}

	public void setHacti(String hacti) {
		this.hacti = hacti;
	}

	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	public String getExpMessage() {
		return this.expMessage;
	}

	public void setExpMessage(String expMessage) {
		this.expMessage = expMessage;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getNodeKind() {
		return this.nodeKind;
	}

	public void setNodeKind(String nodeKind) {
		this.nodeKind = nodeKind;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getTemplateVersion() {
		return this.templateVersion;
	}

	public void setTemplateVersion(Integer templateVersion) {
		this.templateVersion = templateVersion;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getCreatedDateTime() {
		return this.createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getResolverId() {
		return this.resolverId;
	}

	public void setResolverId(String resolverId) {
		this.resolverId = resolverId;
	}

	public String getResolverName() {
		return this.resolverName;
	}

	public void setResolverName(String resolverName) {
		this.resolverName = resolverName;
	}

	public Date getResolvedTime() {
		return this.resolvedTime;
	}

	public void setResolvedTime(Date resolvedTime) {
		this.resolvedTime = resolvedTime;
	}

	public int getExceptionPos() {
		return this.exceptionPos;
	}

	public void setExceptionPos(int exceptionPos) {
		this.exceptionPos = exceptionPos;
	}

	public int getExecutionStatus() {
		return this.executionStatus;
	}

	public void setExecutionStatus(int executionStatus) {
		this.executionStatus = executionStatus;
	}

	public String getExecutionId() {
		return this.executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getOperationName() {
		return this.operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
}
