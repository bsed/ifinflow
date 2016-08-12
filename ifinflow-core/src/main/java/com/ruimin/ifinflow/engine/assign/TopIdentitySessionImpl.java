package com.ruimin.ifinflow.engine.assign;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfRole;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.jbpm.api.identity.Group;
import org.jbpm.api.identity.User;
import org.jbpm.pvm.internal.identity.impl.GroupImpl;
import org.jbpm.pvm.internal.identity.impl.UserImpl;
import org.jbpm.pvm.internal.identity.spi.IdentitySession;

public class TopIdentitySessionImpl implements IdentitySession {
	protected Session session;
	private IdentityAdapter identityAdapter;

	public TopIdentitySessionImpl() {
		this.identityAdapter = UserExtendsReference.getIdentityAdapter();
	}

	public User findUserById(String userId) {
		IWfStaff staff = this.identityAdapter.getStaffById(userId);

		return staff2User(new UserImpl(), staff);
	}

	public List<User> findUsersById(String... userIds) {
		List<IWfStaff> list = this.identityAdapter.getStaffsByids(userIds);
		return staffList2Users(list);
	}

	public List<User> findUsers() {
		return new ArrayList();
	}

	public List<User> findUsersByGroup(String groupId) {
		List<User> users = new ArrayList();
		if (groupId.startsWith("UNIT_")) {
			users = staffList2Users(this.identityAdapter
					.getStaffsByUintId(groupId.replace("UNIT_", "")));

		} else if (groupId.startsWith("ROLE_")) {
			users = staffList2Users(this.identityAdapter
					.getStaffsByRole(groupId.replace("ROLE_", "")));
		}

		return users;
	}

	public Group findGroupById(String groupId) {
		if (groupId.startsWith("UNIT_")) {
			return unit2Group(new GroupImpl(),
					this.identityAdapter.getUnitById(groupId.replace("UNIT_",
							"")));
		}

		if (groupId.startsWith("ROLE_")) {
			return role2Group(new GroupImpl(),
					this.identityAdapter.getRoleById(groupId.replace("ROLE_",
							"")));
		}

		return null;
	}

	public List<Group> findGroupsByUserAndGroupType(String userId,
			String groupType) {
		return findGroupsByUser(userId);
	}

	public List<Group> findGroupsByUser(String userId) {
		List<Group> list = new ArrayList();

		list.add(unit2Group(new GroupImpl(),
				this.identityAdapter.getUnitByStaffId(userId)));

		list.addAll(roleList2GroupList(this.identityAdapter
				.getRolesByStaffId(userId)));

		return list;
	}

	private List<User> staffList2Users(List<IWfStaff> list) {
		List<User> userList = new ArrayList();
		UserImpl user = null;
		for (IWfStaff staff : list) {
			userList.add(staff2User(user, staff));
		}
		return userList;
	}

	private User staff2User(UserImpl user, IWfStaff staff) {
		if (user == null) {
			user = new UserImpl();
		}
		if (staff != null) {
			user.setId(staff.getStaffId());
			user.setBusinessEmail(staff.getBusinessEmail());
			user.setGivenName(staff.getStaffName());
		}
		return user;
	}

	private Group unit2Group(GroupImpl group, IWfUnit iWfUnit) {
		if (group == null) {
			group = new GroupImpl();
		}
		if (iWfUnit != null) {
			group.setId(iWfUnit.getUnitId());
			group.setName(iWfUnit.getUnitName());
			group.setType(iWfUnit.getUnitKind());
		}
		return group;
	}

	private List<Group> roleList2GroupList(List<IWfRole> list2) {
		List<Group> list = new ArrayList();
		GroupImpl group = null;
		for (IWfRole role : list2) {
			list.add(role2Group(group, role));
		}
		return list;
	}

	private Group role2Group(GroupImpl group, IWfRole iWfRole) {
		if (group == null) {
			group = new GroupImpl();
		}
		if (iWfRole != null) {
			group.setId(iWfRole.getRoleId());
			group.setName(iWfRole.getRoleName());
			group.setType("ROLE");
		}
		return group;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String createUser(String userId, String givenName,
			String familyName, String businessEmail) {
		return null;
	}

	public void deleteUser(String userId) {
	}

	public String createGroup(String groupName, String groupType,
			String parentGroupId) {
		return null;
	}

	public void deleteGroup(String groupId) {
	}

	public void createMembership(String userId, String groupId, String role) {
	}

	public void deleteMembership(String userId, String groupId, String role) {
	}
}
