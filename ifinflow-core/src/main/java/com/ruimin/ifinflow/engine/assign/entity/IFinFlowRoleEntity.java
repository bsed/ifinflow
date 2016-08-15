package com.ruimin.ifinflow.engine.assign.entity;

import com.ruimin.ifinflow.engine.external.model.IWfRole;
import java.util.Set;

public class IFinFlowRoleEntity implements IWfRole {
	private static final long serialVersionUID = 3596880223516666658L;
	String roleId;
	String roleName;
	Set<IFinFlowUserRoleEntity> staffRoles;

	public Set<IFinFlowUserRoleEntity> getStaffRoles() {
		return this.staffRoles;
	}

	public void setStaffRoles(Set<IFinFlowUserRoleEntity> staffRoles) {
		this.staffRoles = staffRoles;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("WfRoleVO{");
		buf.append("[roleId = " + this.roleId + "]");
		buf.append("[roleName = " + this.roleName + "]");
		buf.append("}\n");
		return buf.toString();
	}
}
