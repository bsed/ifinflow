package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;
import java.util.Date;

public class ProcessTraceVO implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String activityDbid;
	protected String toActivityDbid;
	protected String toActivityName;
	protected String type;
	protected Integer status;
	protected Date startTime;
	protected Date endTime;
	protected long duration;
	protected String taskId;
	protected String ownerName;
	protected String executorId;
	protected String executorName;
	protected String memo;
	protected Date takeDate;
	protected String nodeName;

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getExecutorId() {
		return this.executorId;
	}

	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}

	public String getExecutorName() {
		return this.executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getToActivityName() {
		return this.toActivityName;
	}

	public void setToActivityName(String toActivityName) {
		this.toActivityName = toActivityName;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getDuration() {
		return this.duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
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

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getTakeDate() {
		return this.takeDate;
	}

	public void setTakeDate(Date takeDate) {
		this.takeDate = takeDate;
	}

	public String getToActivityDbid() {
		return this.toActivityDbid;
	}

	public void setToActivityDbid(String toActivityDbid) {
		this.toActivityDbid = toActivityDbid;
	}

	public String getActivityDbid() {
		return this.activityDbid;
	}

	public void setActivityDbid(String activityDbid) {
		this.activityDbid = activityDbid;
	}
}
