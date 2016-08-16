package com.ruimin.ifinflow.engine.external.adapter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AssignCandidate implements Serializable {
	private static final long serialVersionUID = 1L;
	private Set<String> staffIds = null;

	private String unitId;

	private String roleId;

	private String groupId;

	public void addStaffId(String staffId) {
		if (this.staffIds == null) {
			this.staffIds = new HashSet();
		}
		this.staffIds.add(staffId);
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Set<String> getStaffIds() {
		return this.staffIds;
	}

	public String getUnitId() {
		return this.unitId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
