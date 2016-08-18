package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;

public class RejectActivityVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String activityId;
	private String activityName;
	private String ownerId;

	public String getActivityId() {
		return this.activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return this.activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
