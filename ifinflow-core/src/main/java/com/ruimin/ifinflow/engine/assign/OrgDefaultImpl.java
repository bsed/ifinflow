package com.ruimin.ifinflow.engine.assign;

import com.ruimin.ifinflow.engine.assign.entity.IFinFlowGroupEntity;
import com.ruimin.ifinflow.engine.assign.entity.IFinFlowRoleEntity;
import com.ruimin.ifinflow.engine.assign.entity.IFinFlowUnitEntity;
import com.ruimin.ifinflow.engine.assign.entity.IFinFlowUserEntity;
import com.ruimin.ifinflow.engine.assign.entity.IFinFlowUserGroupEntity;
import com.ruimin.ifinflow.engine.assign.entity.IFinFlowUserRoleEntity;
import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfGroup;
import com.ruimin.ifinflow.engine.external.model.IWfRole;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.id.DbidGenerator;
import org.jbpm.pvm.internal.util.CollectionUtil;

public class OrgDefaultImpl implements IdentityAdapter {
	private Session session;

	public OrgDefaultImpl(Session session) {

		this.session = session;

	}

	public IFinFlowUserEntity getStaffById(String staffId) {

		IFinFlowUserEntity staff = (IFinFlowUserEntity) this.session.get(
				IFinFlowUserEntity.class, staffId);

		return staff;

	}

	public List<IFinFlowUserEntity> getStaffs() {

		List<?> staffs = this.session.createCriteria(IFinFlowUserEntity.class)
				.add(Restrictions.eq("deleteFlag", "0")).list();

		return CollectionUtil.checkList(staffs, IFinFlowUserEntity.class);

	}

	public List<IWfRole> getRolesByStaffId(String staffId) {

		String hql = "select distinct r from com.ruimin.ifinflow.engine.assign.entity.IFinFlowUserRoleEntity m join m.staff s join m.role r where s.staffId = :staffId";

		List<?> roles = this.session.createQuery(hql)
				.setParameter("staffId", staffId).list();

		return CollectionUtil.checkList(roles, IWfRole.class);

	}

	public IFinFlowUnitEntity getUnitByStaffId(String staffId) {

		String hql = "select distinct s.unit from com.ruimin.ifinflow.engine.assign.entity.IFinFlowUserEntity s where s.staffId = :staffId";

		return (IFinFlowUnitEntity) this.session.createQuery(hql)
				.setParameter("staffId", staffId).uniqueResult();

	}

	public IFinFlowRoleEntity getRoleById(String roleId) {

		return (IFinFlowRoleEntity) this.session.get(IFinFlowRoleEntity.class,
				roleId);

	}

	public List<IWfStaff> getStaffsByids(String... staffIds) {

		List<?> staffs = this.session.createCriteria(IFinFlowUserEntity.class)
				.add(Restrictions.in("staffId", staffIds)).list();

		return CollectionUtil.checkList(staffs, IWfStaff.class);

	}

	public List<IWfStaff> getStaffsByRole(String roleId) {

		List<?> staffs = this.session
				.createCriteria(IFinFlowUserRoleEntity.class)
				.createAlias("role", "r")
				.add(Restrictions.eq("r.roleId", roleId))
				.setProjection(Projections.property("staff")).list();

		return CollectionUtil.checkList(staffs, IWfStaff.class);

	}

	public List<IWfStaff> getStaffsByUintId(String unitId) {

		List<?> staffs = this.session.createCriteria(IFinFlowUserEntity.class)
				.add(Restrictions.eq("unit.unitId", unitId)).list();

		return CollectionUtil.checkList(staffs, IWfStaff.class);

	}

	public List<IWfStaff> getStaffsByRolesWithUnit(String unitId,
			String... roleId) {

		List<IWfStaff> listByRole = getStaffsByRoles(roleId);

		List<IWfStaff> listByUnit = getStaffsByUintId(unitId);

		List<IWfStaff> result = new ArrayList();

		for (int i = 0; i < listByRole.size(); i++) {

			if (listByUnit.contains(listByRole.get(i))) {

				result.add(listByRole.get(i));

			}

		}

		return result;

	}

	public List<String> getStaffIdsByRole(String... roleIds) {

		Query query = this.session.createQuery("select ur.staff.staffId from "
				+ IFinFlowUserRoleEntity.class.getName()
				+ " ur where ur.role.roleId in (:roleIds)");

		query.setParameterList("roleIds", roleIds);

		return CollectionUtil.checkList(query.list(), String.class);

	}

	public List<String> getStaffIdsByUnit(String... unitIds) {

		Query query = this.session.createQuery("select ur.staffId from "
				+ IFinFlowUserEntity.class.getName()
				+ " ur where ur.unit.unitId in (:unitIds)");

		query.setParameterList("unitIds", unitIds);

		return CollectionUtil.checkList(query.list(), String.class);

	}

	public IFinFlowUnitEntity getUnitById(String unitId) {

		return (IFinFlowUnitEntity) this.session
				.createCriteria(IFinFlowUnitEntity.class)
				.add(Restrictions.eq("unitId", unitId)).uniqueResult();

	}

	public List<?> getIdentityInfoPage(String param, String type, int startNum,
			int pageSize) {

		return getIdentityInfoPage(param, null, type, startNum, pageSize);

	}

	public long getIdentityInfoPageCount(String param, String type) {

		return getIdentityInfoPageCount(param, null, type);

	}

	public void setSession(Session session) {

		this.session = session;

	}

	public List<IWfStaff> getStaffsByUnitIds(String... unitIds) {

		List<?> staffs = this.session.createCriteria(IFinFlowUserEntity.class)
				.add(Restrictions.in("unit.unitId", unitIds)).list();

		return CollectionUtil.checkList(staffs, IWfStaff.class);

	}

	public List<IWfStaff> getStaffsByRoles(String... roleIds) {

		List<?> staffs = this.session
				.createCriteria(IFinFlowUserRoleEntity.class)
				.createAlias("role", "r")
				.add(Restrictions.in("r.roleId", roleIds))
				.setProjection(Projections.property("staff")).list();

		return CollectionUtil.checkList(staffs, IWfStaff.class);

	}

	public List<IWfStaff> getAllStaffs() {

		return this.session.createCriteria(IFinFlowUserEntity.class).list();

	}

	public List<String> getAllUnitLevel() {

		List<String> list = new ArrayList();

		list.add("1");

		list.add("2");

		list.add("3");

		return list;

	}

	public IWfGroup createGroup(String id, String name)
			throws IFinFlowException {

		List<?> groups = this.session.createCriteria(IFinFlowGroupEntity.class)
				.add(Restrictions.eq("groupId", id)).list();

		if (!groups.isEmpty()) {

			throw new IFinFlowException(105007, new Object[] { id });

		}

		IFinFlowGroupEntity t = new IFinFlowGroupEntity();

		t.setGroupId(id);

		t.setGroupName(name);

		String dbid = ((DbidGenerator) EnvironmentImpl
				.getFromCurrent(DbidGenerator.class)).uuid();

		t.setDbid(dbid);

		try {

			this.session.save(t);

		} catch (HibernateException e) {

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "createGroup" });

		}

		return t;

	}

	public void updateGroup(String id, String name) throws IFinFlowException {

		try {

			IFinFlowGroupEntity t = (IFinFlowGroupEntity) this.session
					.createCriteria(IFinFlowGroupEntity.class)
					.add(Restrictions.eq("groupId", id)).uniqueResult();

			if (t == null) {

				throw new IFinFlowException(105006, new Object[] { id });

			}

			t.setGroupName(name);

			this.session.update(t);

		} catch (HibernateException e) {

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "updateGroup" });

		}

	}

	public void deleteGroup(String id) throws IFinFlowException {

		try {

			IFinFlowGroupEntity g = (IFinFlowGroupEntity) getGroup(id);

			Set<IFinFlowUserGroupEntity> set = g.getUserGroup();

			if (set != null) {

				for (IFinFlowUserGroupEntity ug : set) {

					this.session.delete(ug);

				}

			}

			this.session.delete(g);

		} catch (HibernateException e) {

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "deleteGroup" });

		}

	}

	public List<IWfGroup> getGroup(String param, int startNum, int pageSize)
			throws IFinFlowException {

		if (pageSize == 0) {

			pageSize = 10;

		}

		List<IWfGroup> list = new ArrayList();

		try {

			Criteria criteria = this.session
					.createCriteria(IFinFlowGroupEntity.class);

			if (!StringUtils.isEmpty(param)) {

				criteria.add(Restrictions.or(Restrictions.eq("groupId", param),
						Restrictions.ilike("groupName", param,
								MatchMode.ANYWHERE)));

			}

			if ((startNum >= 0) && (pageSize > 0)) {

				criteria.setFirstResult(startNum).setMaxResults(
						startNum + pageSize);

			}

			List<IFinFlowGroupEntity> groups = criteria.list();

			list = criteria.list();

			List<String> staffIds = null;

			Set<IFinFlowUserGroupEntity> ugs = null;

			IFinFlowUserEntity user = null;

			for (IFinFlowGroupEntity g : groups) {

				staffIds = new ArrayList();

				ugs = g.getUserGroup();

				for (IFinFlowUserGroupEntity ug : ugs) {

					user = ug.getUser();

					if (user != null) {

						staffIds.add(user.getStaffId());

					}

				}

				g.setStaffIds(staffIds);
			}
		} catch (HibernateException e) {
			List<String> staffIds;

			Set<IFinFlowUserGroupEntity> ugs;

			IFinFlowUserEntity user;

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "getGroup" });

		}

		return list;

	}

	public long getGroupCount(String param) throws IFinFlowException {

		long count = 0L;

		try {

			StringBuffer hql = new StringBuffer();

			hql.append("select count(g.groupId) from ");

			hql.append(IFinFlowGroupEntity.class.getName());

			hql.append(" g where 1=1 ");

			if (!StringUtils.isEmpty(param)) {

				hql.append(" and (g.groupId=:groupId or g.groupName like :name)");

			}

			Query query = this.session.createQuery(hql.toString());

			if (!StringUtils.isEmpty(param)) {

				query.setParameter("groupId", param)
						.setParameter("name", param);

			}

			count = ((Long) query.uniqueResult()).longValue();

		} catch (HibernateException e) {

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "queryGroupCount" });

		}

		return count;

	}

	public List<IWfGroup> queryGroup(String id, String name, int startNum,
			int pageSize) throws IFinFlowException {

		if (pageSize == 0) {

			pageSize = 10;

		}

		List<IWfGroup> list = new ArrayList();

		try {

			Criteria criteria = this.session
					.createCriteria(IFinFlowGroupEntity.class);

			if (!StringUtils.isEmpty(id)) {

				criteria.add(Restrictions.eq("groupId", id));

			}

			if (!StringUtils.isEmpty(name)) {

				criteria.add(Restrictions.like("groupName", "%" + name + "%",
						MatchMode.ANYWHERE));

			}

			if ((startNum >= 0) && (pageSize > 0)) {

				criteria.setFirstResult(startNum).setMaxResults(
						startNum + pageSize);

			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "queryGroup" });

		}

		return list;

	}

	public long queryGroupCount(String id, String name)
			throws IFinFlowException {

		long count = 0L;

		try {

			StringBuffer hql = new StringBuffer();

			hql.append("select count(g.groupId) from ")
					.append(IFinFlowGroupEntity.class.getName())
					.append(" g where 1=1 ");

			if (!StringUtils.isEmpty(id)) {

				hql.append(" and g.groupId=:groupId");

			}

			if (!StringUtils.isEmpty(name)) {

				hql.append(" and g.groupName like :name");

			}

			Query query = this.session.createQuery(hql.toString());

			if (!StringUtils.isEmpty(id)) {

				query.setParameter("groupId", id);

			}

			if (!StringUtils.isEmpty(name)) {

				query.setParameter("name", "%" + name + "%");

			}

			count = ((Long) query.uniqueResult()).longValue();

		} catch (HibernateException e) {

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "getGroupCount" });

		}

		return count;

	}

	public List<IWfStaff> getStaffsByGroupId(String groupId)
			throws IFinFlowException {

		List<?> staffs = this.session
				.createCriteria(IFinFlowUserGroupEntity.class)
				.createAlias("group", "g")
				.add(Restrictions.eq("g.groupId", groupId))
				.setProjection(Projections.property("user")).list();

		return CollectionUtil.checkList(staffs, IWfStaff.class);

	}

	public void createUserGroup(String userId, String groupId)
			throws IFinFlowException {

		try {

			IFinFlowGroupEntity group = (IFinFlowGroupEntity) getGroup(groupId);

			if (group == null) {

				throw new IFinFlowException(105006, new Object[] { groupId });

			}

			IFinFlowUserEntity user = getStaffById(userId);

			if (user == null) {

				throw new IFinFlowException(105005, new Object[] { userId });

			}

			IFinFlowUserGroupEntity ug = new IFinFlowUserGroupEntity();

			ug.setGroup(group);

			ug.setUser(user);

			String dbid = ((DbidGenerator) EnvironmentImpl
					.getFromCurrent(DbidGenerator.class)).uuid();

			ug.setRelationId(dbid);

			this.session.save(ug);

		} catch (HibernateException e) {

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "createUserGroup" });

		}

	}

	public void deleteUserGroup(String userId, String groupId)
			throws IFinFlowException {

		try {

			StringBuffer hql = new StringBuffer();

			hql.append("delete from ")
					.append(IFinFlowUserGroupEntity.class.getName())
					.append(" ug where ug.userId=:userId");

			Query query = this.session.createQuery(hql.toString());

			query.setParameter("userId", userId);

			query.executeUpdate();

		} catch (HibernateException e) {

			throw new IFinFlowException(100003, e, new Object[] {
					OrgDefaultImpl.class.getName(), "deleteUserGroup" });

		}

	}

	public IWfGroup getGroup(String id) {

		return (IWfGroup) this.session
				.createCriteria(IFinFlowGroupEntity.class)
				.add(Restrictions.eq("groupId", id)).uniqueResult();

	}

	public void deleteUserGroupByGroupId(String groupId)
			throws IFinFlowException {

		IFinFlowGroupEntity t = (IFinFlowGroupEntity) this.session
				.createCriteria(IFinFlowGroupEntity.class)
				.add(Restrictions.eq("groupId", groupId)).uniqueResult();

		if (t == null) {

			throw new IFinFlowException(105006, new Object[] { groupId });

		}

		StringBuffer hql = new StringBuffer();

		hql.append("delete from ")
				.append(IFinFlowUserGroupEntity.class.getName())
				.append(" ug where ug.groupId=:groupId");

		Query query = this.session.createQuery(hql.toString());

		query.setParameter("groupId", t.getDbid());

		query.executeUpdate();

	}

	public List<?> getIdentityInfoPage(String param, String loginUserId,
			String type, int startNum, int pageSize) {

		if (pageSize == 0) {

			pageSize = 10;

		}

		String orgId = null;

		if (!StringUtils.isEmpty(loginUserId)) {

			IFinFlowUserEntity user = (IFinFlowUserEntity) this.session
					.createCriteria(IFinFlowUserEntity.class)
					.add(Restrictions.eq("staffId", loginUserId))
					.setMaxResults(1).uniqueResult();

			orgId = user.getUnit().getUnitId();

		}

		Criteria criteria = null;

		if ("1".equals(type)) {

			criteria = this.session.createCriteria(IFinFlowUserEntity.class)
					.add(Restrictions.eq("deleteFlag", "0"));

			if (!StringUtils.isEmpty(param)) {

				criteria.add(Restrictions.or(Restrictions.eq("staffId", param),
						Restrictions.ilike("staffName", param,
								MatchMode.ANYWHERE)));

			}

			if (!StringUtils.isEmpty(orgId)) {

				criteria.add(Restrictions.eq("unit.unitId", orgId));

			}

		}

		if ("2".equals(type)) {

			criteria = this.session.createCriteria(IFinFlowRoleEntity.class);

			if (!StringUtils.isEmpty(param)) {

				criteria.add(Restrictions.or(Restrictions.eq("roleId", param),
						Restrictions.ilike("roleName", param,
								MatchMode.ANYWHERE)));

			}

		}

		if ("3".equals(type)) {

			criteria = this.session.createCriteria(IFinFlowUnitEntity.class);

			if (!StringUtils.isEmpty(param)) {

				criteria.add(Restrictions.or(Restrictions.eq("unitId", param),
						Restrictions.ilike("unitName", param,
								MatchMode.ANYWHERE)));

			}

		}

		if ((startNum >= 0) && (pageSize > 0)) {

			criteria.setFirstResult(startNum).setMaxResults(pageSize);

		}

		return criteria.list();

	}

	public long getIdentityInfoPageCount(String param, String loginUserId,
			String type) {

		String orgId = null;

		if (!StringUtils.isEmpty(loginUserId)) {

			IFinFlowUserEntity user = (IFinFlowUserEntity) this.session
					.createCriteria(IFinFlowUserEntity.class)
					.add(Restrictions.eq("staffId", loginUserId))
					.setMaxResults(1).uniqueResult();

			orgId = user.getUnit().getUnitId();

		}

		StringBuffer hql = new StringBuffer();

		if ("1".equals(type)) {

			hql.append("select count(s.id) from com.ruimin.ifinflow.engine.assign.entity.IFinFlowUserEntity s where s.deleteFlag = '0'");

			if (!StringUtils.isEmpty(param)) {

				hql.append(" and (s.staffId = :staffId or s.staffName like :staffName)");

			}

			if (!StringUtils.isEmpty(orgId)) {

				hql.append(" and s.unit.unitId=:unitId");

			}

			Query query = this.session.createQuery(hql.toString());

			if (!StringUtils.isEmpty(param)) {

				query.setParameter("staffId", param).setParameter("staffName",
						"%" + param + "%");

			}

			if (!StringUtils.isEmpty(orgId)) {

				query.setParameter("unitId", orgId);

			}

			return ((Long) query.uniqueResult()).longValue();

		}

		if ("2".equals(type)) {

			if (StringUtils.isEmpty(param)) {

				hql.append("select count(s.roleId) from com.ruimin.ifinflow.engine.assign.entity.IFinFlowRoleEntity s ");

				return ((Long) this.session.createQuery(hql.toString())
						.uniqueResult()).longValue();

			}

			hql.append("select count(s.roleId) from com.ruimin.ifinflow.engine.assign.entity.IFinFlowRoleEntity s where s.roleId = :roleId or s.roleName like :roleName");

			return ((Long) this.session.createQuery(hql.toString())
					.setParameter("roleId", param)
					.setParameter("roleName", "%" + param + "%").uniqueResult())
					.longValue();

		}

		if ("3".equals(type)) {

			if (StringUtils.isEmpty(param)) {

				hql.append("select count(s.unitId) from com.ruimin.ifinflow.engine.assign.entity.IFinFlowUnitEntity s ");

				return ((Long) this.session.createQuery(hql.toString())
						.uniqueResult()).longValue();

			}

			hql.append("select count(s.unitId) from com.ruimin.ifinflow.engine.assign.entity.IFinFlowUnitEntity s where s.unitId = :unitId or s.unitName like :unitName");

			return ((Long) this.session.createQuery(hql.toString())
					.setParameter("unitId", param)
					.setParameter("unitName", "%" + param + "%").uniqueResult())
					.longValue();

		}

		return 0L;

	}

	public IWfUnit getUnit(String unitId, String unitLevel) {

		IFinFlowUnitEntity unit = getUnitById(unitId);

		if (unit == null) {

			throw new IFinFlowException(105009, new Object[] { unitId });

		}

		if (StringUtils.isEmpty(unit.getUnitLevel())) {

			throw new IFinFlowException(105010,
					new Object[] { unit.getUnitId() });

		}

		int res = new BigDecimal(unitLevel).compareTo(new BigDecimal(unit
				.getUnitLevel()));

		if ((res < 0) && (!StringUtils.isEmpty(unit.getParentUnitId()))) {

			unit = (IFinFlowUnitEntity) getUnit(unit.getParentUnitId(),
					unitLevel);

		} else if ((StringUtils.isEmpty(unit.getParentUnitId())) && (res != 0)) {

			throw new IFinFlowException(103047, new Object[0]);

		}

		return unit;

	}

	public List<String> getStaffIdsByRolesWithUnit(String unitId,
			String... roleId) {

		List<String> listByRole = getStaffIdsByRole(roleId);

		List<String> listByUnit = getStaffIdsByUnit(new String[] { unitId });

		List<String> result = new ArrayList();

		for (int i = 0; i < listByRole.size(); i++) {

			if (listByUnit.contains(listByRole.get(i))) {

				result.add(listByRole.get(i));

			}

		}

		return result;

	}

	public List<String> getStaffIdsByGroupId(String groupId)
			throws IFinFlowException {

		Query query = this.session.createQuery("select ur.user.staffId from "
				+ IFinFlowUserGroupEntity.class.getName()
				+ " ur where ur.group.groupId in (:groupId)");

		query.setParameter("groupId", groupId);

		return CollectionUtil.checkList(query.list(), String.class);

	}

	public List<IWfGroup> getGroupsByStaffId(String staffId)
			throws IFinFlowException {

		String hql = "select distinct g from "
				+ IFinFlowUserGroupEntity.class.getName()
				+ " m join m.user s join m.group g where s.staffId = :staffId";

		List<?> groups = this.session.createQuery(hql)
				.setParameter("staffId", staffId).list();

		return CollectionUtil.checkList(groups, IWfGroup.class);

	}

}