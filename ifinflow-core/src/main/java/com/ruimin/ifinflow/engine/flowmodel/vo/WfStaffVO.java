package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;
import org.jbpm.api.model.WfUserInfo;

public class WfStaffVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String staffId = null;

	private String staffName = null;

	private String unitId = null;

	private String unitName = null;

	private String roleId = null;

	private String roleName = null;

	private String groupId = null;

	private String groupName = null;

	public WfStaffVO() {
	}

	public WfStaffVO(String staffId, String staffName, String roleId,
			String unitId) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.roleId = roleId;
		this.unitId = unitId;
	}

	public WfStaffVO(String staffId, String staffName, String roleId,
			String unitId, String groupId) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.roleId = roleId;
		this.unitId = unitId;
		this.groupId = groupId;
	}

	public String getUnitId() {
		return this.unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
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

	public String getStaffId() {
		return this.staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return this.staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
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

	public WfUserInfo getWfUserInfo() {
		return new WfUserInfo(this.staffId, this.staffName, this.roleId,
				this.unitId, this.groupId);
	}
}
