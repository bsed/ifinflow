package com.ruimin.ifinflow.util;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfRole;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.model.WfUserInfo;

public class IdentityUtil {
	public static WfUserInfo getWfUserInfo(String staffId) {
		if (StringUtils.isBlank(staffId)) {
			throw new IFinFlowException(105005, new Object[] { staffId });
		}

		WfUserInfo user = new WfUserInfo();
		IdentityAdapter identityAdapter = UserExtendsReference
				.getIdentityAdapter();
		IWfStaff staff = identityAdapter.getStaffById(staffId);
		List<IWfRole> roles = identityAdapter.getRolesByStaffId(staffId);
		IWfUnit unit = identityAdapter.getUnitByStaffId(staffId);

		if ((staff == null) || (staff.getStaffId() == null)) {
			throw new IFinFlowException(105005, new Object[] { staffId });
		}

		user.setStaffName(staff.getStaffName());
		if (unit != null) {
			user.setUnitId(unit.getUnitId());
		}

		if ((roles == null) || (roles.isEmpty())) {
			StringBuffer roleIds = new StringBuffer();
			for (IWfRole r : roles) {
				roleIds.append(r.getRoleId()).append(",");
			}
			if (roleIds.length() > 0) {
				user.setRoleId(roleIds.substring(0, roleIds.length() - 1));
			}
		}

		return user;
	}
}
