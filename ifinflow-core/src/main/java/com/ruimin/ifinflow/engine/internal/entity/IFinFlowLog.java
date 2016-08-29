package com.ruimin.ifinflow.engine.internal.entity;

import java.io.Serializable;
import java.util.Date;

public class IFinFlowLog implements Serializable {
	private String poid;
	private String categoryId;
	private Date logTime;
	private String logTitle;
	private String logContent;
	private String mpid;
	private String processPoid;
	private String actionName;
	private String actionType;
	private String operatorId;
	private String operatorName;
	private String remark;

	public String getPoid() {
		return this.poid;
	}

	public void setPoid(String poid) {
		this.poid = poid;
	}

	public String getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getLogTitle() {
		return this.logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public String getLogContent() {
		return this.logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getMpid() {
		return this.mpid;
	}

	public void setMpid(String mpid) {
		this.mpid = mpid;
	}

	public String getProcessPoid() {
		return this.processPoid;
	}

	public void setProcessPoid(String processPoid) {
		this.processPoid = processPoid;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getOperatorId() {
		return this.operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return this.operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
