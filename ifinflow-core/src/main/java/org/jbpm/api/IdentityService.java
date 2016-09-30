package org.jbpm.api;

import java.util.List;
import org.jbpm.api.identity.Group;
import org.jbpm.api.identity.User;

public abstract interface IdentityService {
	public abstract void createUser(String paramString1, String paramString2,
			String paramString3);

	public abstract void createUser(String paramString1, String paramString2,
			String paramString3, String paramString4);

	public abstract User findUserById(String paramString);

	public abstract List<User> findUsers();

	public abstract void deleteUser(String paramString);

	public abstract String createGroup(String paramString);

	public abstract String createGroup(String paramString1, String paramString2);

	public abstract String createGroup(String paramString1,
			String paramString2, String paramString3);

	public abstract Group findGroupById(String paramString);

	public abstract List<Group> findGroupsByUserAndGroupType(
			String paramString1, String paramString2);

	public abstract List<Group> findGroupsByUser(String paramString);

	public abstract List<String> findGroupIdsByUser(String paramString);

	public abstract void deleteGroup(String paramString);

	public abstract void createMembership(String paramString1,
			String paramString2);

	public abstract void createMembership(String paramString1,
			String paramString2, String paramString3);

	public abstract void deleteMembership(String paramString1,
			String paramString2, String paramString3);
}
