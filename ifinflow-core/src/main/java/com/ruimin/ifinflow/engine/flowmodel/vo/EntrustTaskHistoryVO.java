package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;
import java.util.Date;

public class EntrustTaskHistoryVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String taskId;
	private Date operateTime;
	private String message;

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}