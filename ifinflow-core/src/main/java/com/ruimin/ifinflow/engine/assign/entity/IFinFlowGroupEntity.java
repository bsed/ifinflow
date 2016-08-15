package com.ruimin.ifinflow.engine.assign.entity;

import com.ruimin.ifinflow.engine.external.model.IWfGroup;
import java.util.List;
import java.util.Set;

public class IFinFlowGroupEntity implements IWfGroup {
	private static final long serialVersionUID = 1L;
	private String dbid;
	private String groupId;
	private String groupName;
	private Set<IFinFlowUserGroupEntity> userGroup;
	private List<String> staffIds;

	public List<String> getStaffIds() {
		return this.staffIds;
	}

	public void setStaffIds(List<String> staffIds) {
		this.staffIds = staffIds;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Set<IFinFlowUserGroupEntity> getUserGroup() {
		return this.userGroup;
	}

	public void setUserGroup(Set<IFinFlowUserGroupEntity> userGroup) {
		this.userGroup = userGroup;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("IFinFlowGroupEntity {");
		buf.append("[groupId = " + this.groupId + "]");
		buf.append("[groupName = " + this.groupName + "]");
		buf.append("}\n");
		return buf.toString();
	}

	public String getDbid() {
		return this.dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
}
