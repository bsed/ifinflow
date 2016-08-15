package com.ruimin.ifinflow.engine.assign.entity;

import java.io.Serializable;

public class IFinFlowUserGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String relationId;
	private IFinFlowUserEntity user;
	private IFinFlowGroupEntity group;
	private String groupId;
	private String userId;

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRelationId() {
		return this.relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public IFinFlowUserEntity getUser() {
		return this.user;
	}

	public void setUser(IFinFlowUserEntity user) {
		this.user = user;
	}

	public IFinFlowGroupEntity getGroup() {
		return this.group;
	}

	public void setGroup(IFinFlowGroupEntity group) {
		this.group = group;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}