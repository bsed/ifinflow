package com.ruimin.ifinflow.engine.external.adapter;

import com.ruimin.ifinflow.engine.external.model.IWfGroup;
import com.ruimin.ifinflow.engine.external.model.IWfRole;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.List;

public interface IdentityAdapter {
	IWfStaff getStaffById(String paramString);

	IWfRole getRoleById(String paramString);

	List<IWfRole> getRolesByStaffId(String paramString);

	IWfUnit getUnitById(String paramString);

	IWfUnit getUnitByStaffId(String paramString);

	List<IWfStaff> getStaffsByUintId(String paramString);

	List<IWfStaff> getStaffsByUnitIds(String... paramVarArgs);

	List<IWfStaff> getStaffsByids(String... paramVarArgs);

	List<IWfStaff> getStaffsByRole(String paramString);

	List<IWfStaff> getStaffsByRoles(String... paramVarArgs);

	List<String> getStaffIdsByRolesWithUnit(String paramString,
			String... paramVarArgs);

	List<String> getStaffIdsByRole(String... paramVarArgs);

	List<String> getStaffIdsByUnit(String... paramVarArgs);

	List<?> getIdentityInfoPage(String paramString1, String paramString2,
			int paramInt1, int paramInt2);

	long getIdentityInfoPageCount(String paramString1, String paramString2);

	List<IWfStaff> getAllStaffs();

	List<String> getAllUnitLevel();

	IWfGroup createGroup(String paramString1, String paramString2)
			throws IFinFlowException;

	void updateGroup(String paramString1, String paramString2)
			throws IFinFlowException;

	void deleteGroup(String paramString) throws IFinFlowException;

	void createUserGroup(String paramString1, String paramString2)
			throws IFinFlowException;

	void deleteUserGroup(String paramString1, String paramString2)
			throws IFinFlowException;

	List<IWfGroup> getGroup(String paramString, int paramInt1, int paramInt2)
			throws IFinFlowException;

	long getGroupCount(String paramString) throws IFinFlowException;

	List<IWfStaff> getStaffsByGroupId(String paramString)
			throws IFinFlowException;

	List<String> getStaffIdsByGroupId(String paramString)
			throws IFinFlowException;

	void deleteUserGroupByGroupId(String paramString) throws IFinFlowException;

	IWfGroup getGroup(String paramString);

	long queryGroupCount(String paramString1, String paramString2)
			throws IFinFlowException;

	List<IWfGroup> queryGroup(String paramString1, String paramString2,
			int paramInt1, int paramInt2) throws IFinFlowException;

	List<IWfGroup> getGroupsByStaffId(String paramString)
			throws IFinFlowException;

	List<?> getIdentityInfoPage(String paramString1, String paramString2,
			String paramString3, int paramInt1, int paramInt2);

	long getIdentityInfoPageCount(String paramString1, String paramString2,
			String paramString3);

	IWfUnit getUnit(String paramString1, String paramString2);
}
