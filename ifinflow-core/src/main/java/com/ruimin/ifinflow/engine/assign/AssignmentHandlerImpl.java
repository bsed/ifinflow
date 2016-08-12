package com.ruimin.ifinflow.engine.assign;

import com.ruimin.ifinflow.engine.external.adapter.AssignCandidate;
import com.ruimin.ifinflow.engine.external.adapter.IAssignAdapter;
import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskVO;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.engine.pvm.cmd.WorkloadMinQueryCmd;
import com.ruimin.ifinflow.engine.pvm.event.WorkloadUpdate;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;
import com.ruimin.ifinflow.util.TemplateCacheUtil;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.Configuration;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.jbpm.api.history.HistoryTaskQuery;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.HistoryEvent;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.query.HistoryProcessInstanceQueryImpl;
import org.jbpm.pvm.internal.session.DbSession;

public class AssignmentHandlerImpl implements AssignmentHandler {
	private static final long serialVersionUID = 1L;
	private String top_sys_assign_param;
	private String nodeId;
	private String exceptUserId;
	private HistoryProcessInstanceImpl hisprocess;
	private int historyType;
	private int assignMode;
	private int participateType;
	private int paramType;
	private String paramValue;

	public void assign(Assignable assignable, OpenExecution execution)
			throws IFinFlowException {
	}

	public void assign(Assignable assignable, ExecutionImpl execution)
			throws IFinFlowException {
		if (StringUtils.isEmpty(this.top_sys_assign_param)) {
			IFinFlowException te = new IFinFlowException(105004, new Object[0]);
			throw te;
		}
		this.nodeId = execution.getActivityName();
		parseParam(this.top_sys_assign_param);

		DbSession dbsession = (DbSession) EnvironmentImpl
				.getFromCurrent(DbSession.class);

		if (1 == this.historyType) {
			this.hisprocess = getProcess(execution, dbsession);
			assignable.setAssigneeOnly(this.hisprocess.getInitiatorId());
			return;
		}

		if (2 == this.historyType) {
			this.exceptUserId = getLastTaskExecutor(execution);
		}

		assignByParamType(assignable, execution);
	}

	private void assignByParamType(Assignable assignable,
			ExecutionImpl execution) {
		if (1 == this.paramType) {
			if (4 == this.participateType) {
				String orgId = null;
				if (this.paramValue.indexOf("(机构)=") >= 0) {
					orgId = this.paramValue
							.substring(this.paramValue.indexOf("(机构)=")
									+ "(机构)=".length(),
									this.paramValue.indexOf("(岗位)="));
				} else {
					if (this.hisprocess == null) {
						this.hisprocess = getProcess(execution,
								(DbSession) EnvironmentImpl
										.getFromCurrent(DbSession.class));
					}
					orgId = this.hisprocess.getOwnerUnitId();
				}
				String roleIds = this.paramValue.substring(this.paramValue
						.indexOf("(岗位)=") + "(岗位)=".length());

				if (!StringUtils.isEmpty(roleIds)) {
					if (roleIds.indexOf("*") > 0) {
						assignByValue(assignable, orgId, execution,
								roleIds.split("\\*"));
					} else {
						assignByValue(assignable, orgId, execution,
								new String[] { roleIds });
					}

				}
			} else if (this.paramValue.indexOf(";") > 0) {
				assignByValue(assignable, null, execution,
						this.paramValue.split(";"));
			} else {
				assignByValue(assignable, null, execution,
						new String[] { this.paramValue });
			}

			return;
		}

		if (2 == this.paramType) {
			org.jbpm.pvm.internal.type.Variable var = execution
					.getVariablePrototype(this.paramValue);
			if (var == null) {
				throw new IFinFlowException(104004,
						new Object[] { this.paramValue });
			}
			if (var.getKind().intValue() == 1) {
				String sv = (String) var.getValue(execution);
				if ((sv == null) || ("".equals(sv.trim()))) {
					throw new IFinFlowException(104001, new Object[] {
							var.getKey(), this.nodeId });
				}
				assignByValue(assignable, null, execution, new String[] { sv });
			} else {
				throw new IFinFlowException(104006, new Object[0]);
			}
			return;
		}

		if (3 == this.paramType) {
			if (!StringUtils.isEmpty(this.paramValue)) {
				IAssignAdapter iAssignAdapter = null;
				try {
					Class<?> clazz = Class.forName(this.paramValue);
					iAssignAdapter = (IAssignAdapter) clazz.newInstance();
				} catch (ClassNotFoundException e) {
					throw new IFinFlowException(106001, e, new Object[] {
							this.nodeId, this.paramValue });
				} catch (InstantiationException e) {
					throw new IFinFlowException(106002, e, new Object[] {
							this.nodeId, this.paramValue });
				} catch (IllegalAccessException e) {
					throw new IFinFlowException(106002, e, new Object[] {
							this.nodeId, this.paramValue });
				}

				VariableSet vs = new VariableSet();
				Map<String, org.jbpm.pvm.internal.type.Variable> map = execution
						.getVariablesPrototype();

				Set<Map.Entry<String, org.jbpm.pvm.internal.type.Variable>> set = map
						.entrySet();

				org.jbpm.pvm.internal.type.Variable var = null;
				for (Map.Entry<String, org.jbpm.pvm.internal.type.Variable> entry : set) {
					var = (org.jbpm.pvm.internal.type.Variable) entry
							.getValue();
					vs.addVariable(new com.ruimin.ifinflow.engine.flowmodel.Variable(
							var.getKey(), var.getKind().intValue(), var
									.getValue(execution), var.getBizName()));
				}

				TemplateVo templateVO = TemplateCacheUtil
						.getTemplateVO(execution);
				NodeVo nodeVO = TemplateCacheUtil.getNodeVo(templateVO,
						execution.getActivityName());

				TaskVO task = new TaskVO();
				task.setNodeName(nodeVO.getName());
				task.setSourceUrl(nodeVO.getUrl());
				AssignCandidate assignCandidate = null;
				try {
					assignCandidate = iAssignAdapter.assign(vs, task);
				} catch (Exception e) {
					throw new IFinFlowException(106007, e, new Object[] {
							task.getNodeId(), this.paramValue });
				}
				assignAdapter(assignable, execution, assignCandidate);
			}

			return;
		}
	}

	private void assignByValue(Assignable assignable, String orgId,
			ExecutionImpl execution, String... value) {
		String singleStaff = null;
		IdentityAdapter identityAdapter = UserExtendsReference
				.getIdentityAdapter();

		if (1 == this.participateType) {
			if (1 == this.assignMode) {
				if (value.length == 1) {
					singleStaff = value[0];
				} else {
					singleStaff = getMinWorkStaffByIds(value);
				}

				if (StringUtils.isEmpty(singleStaff)) {
					throw new IFinFlowException(105001,
							new Object[] { this.nodeId });
				}

				IWfStaff sstaff = identityAdapter.getStaffById(singleStaff);
				if (sstaff == null) {
					throw new IFinFlowException(103041, new Object[] {
							this.nodeId, singleStaff });
				}
				assignable.setAssigneeOnly(singleStaff);

				HistoryEvent.fire(new WorkloadUpdate(singleStaff, singleStaff,
						1));
				return;
			}
			if (2 == this.assignMode) {
				if (value.length <= 0) {
					throw new IFinFlowException(105001,
							new Object[] { this.nodeId });
				}
				checkStaff(identityAdapter, value);
				for (String userId : value) {
					assignable.addCandidateUser(userId);
				}
				return;
			}
			if (3 == this.assignMode) {
				String staffstr = Arrays.toString(value);
				if (staffstr.length() > 2) {
					staffstr = staffstr.substring(1, staffstr.length() - 1);
				}
				assignable.setAssigneeOnly(staffstr);
			}

		} else if (2 == this.participateType) {
			if (1 == this.assignMode) {
				singleStaff = getMinWorkStaffByRole(value);
				if (StringUtils.isEmpty(singleStaff)) {
					throw new IFinFlowException(105001,
							new Object[] { this.nodeId });
				}
				assignable.setAssigneeOnly(singleStaff);
				return;
			}
			if (2 == this.assignMode) {
				List<String> ids = identityAdapter.getStaffIdsByRole(value);
				if ((ids != null) && (ids.size() > 0)) {
					for (String roleId : value) {
						assignable.addCandidateGroup(roleId);
					}
				} else {
					throw new IFinFlowException(103042, new Object[] {
							Arrays.toString(value), this.nodeId });
				}
				return;
			}
			if (3 == this.assignMode) {
				List<String> list = identityAdapter.getStaffIdsByRole(value);
				String staffstr = list.toString();
				if (staffstr.length() > 2) {
					staffstr = staffstr.substring(1, staffstr.length() - 1);
				}
				assignable.setAssigneeOnly(staffstr);
			}

		} else if (3 == this.participateType) {
			if (1 == this.assignMode) {
				singleStaff = getMinWorkStaffByOrg(value);
				if (StringUtils.isEmpty(singleStaff)) {
					throw new IFinFlowException(105001,
							new Object[] { this.nodeId });
				}
				assignable.setAssigneeOnly(singleStaff);
				return;
			}
			if (2 == this.assignMode) {
				List<String> ids = identityAdapter.getStaffIdsByUnit(value);
				if ((ids != null) && (ids.size() > 0)) {
					for (String unitId : value) {
						assignable.addCandidateGroup(unitId);
					}
				} else {
					throw new IFinFlowException(103043, new Object[] {
							Arrays.toString(value), this.nodeId });
				}
				return;
			}
			if (3 == this.assignMode) {
				List<String> list = identityAdapter.getStaffIdsByUnit(value);
				String staffstr = list.toString();
				if (staffstr.length() > 2) {
					staffstr = staffstr.substring(1, staffstr.length() - 1);
				}
				assignable.setAssigneeOnly(staffstr);
			}

		} else if (4 == this.participateType) {
			if (1 == this.assignMode) {
				singleStaff = getMinWorkStaffByOrgRole(orgId, value);
				if (StringUtils.isEmpty(singleStaff)) {
					throw new IFinFlowException(105001,
							new Object[] { this.nodeId });
				}
				assignable.setAssigneeOnly(singleStaff);
				return;
			}
			if (2 == this.assignMode) {
				if (UserExtendsReference.getIdentityAdapter()
						.getStaffIdsByRolesWithUnit(orgId, value).isEmpty()) {
					throw new IFinFlowException(105002, new Object[] { orgId,
							Arrays.toString(value), this.nodeId });
				}

				StringBuffer orgRule = new StringBuffer(orgId);
				for (String roleId : value) {
					orgRule.append("," + roleId);
				}
				assignable.addCandidateGroup(orgRule.toString());
				return;
			}
			if (3 == this.assignMode) {
				List<String> list = identityAdapter.getStaffIdsByRolesWithUnit(
						orgId, value);

				String staffstr = list.toString();
				if (staffstr.length() > 2) {
					staffstr = staffstr.substring(1, staffstr.length() - 1);
				}
				assignable.setAssigneeOnly(staffstr);
				return;
			}
		}
	}

	public void assignAdapter(Assignable assignable, ExecutionImpl execution,
			AssignCandidate assignCandidate) {
		Set<String> staffIds = assignCandidate.getStaffIds();
		String roleId = assignCandidate.getRoleId();
		String unitId = assignCandidate.getUnitId();

		if (2 == this.assignMode) {
			if ((staffIds != null) && (staffIds.size() > 0)) {
				for (String userId : staffIds) {
					assignable.addCandidateUser(userId);
				}
			}
			if (!StringUtils.isEmpty(roleId)) {
				assignable.addCandidateGroup(roleId);
			}
			if (!StringUtils.isEmpty(unitId)) {
				assignable.addCandidateGroup(unitId);
			}
			return;
		}
		List<String> staffs = new ArrayList();
		if ((staffIds != null) && (!staffIds.isEmpty())) {
			staffs.addAll(staffIds);
		}
		IdentityAdapter identityAdapter = UserExtendsReference
				.getIdentityAdapter();

		if (!StringUtils.isEmpty(roleId)) {
			List<String> list = identityAdapter
					.getStaffIdsByRole(new String[] { roleId });
			staffs.addAll(list);
		}
		if (!StringUtils.isEmpty(unitId)) {
			List<String> list = identityAdapter
					.getStaffIdsByUnit(new String[] { unitId });
			staffs.addAll(list);
		}

		if (1 == this.assignMode) {
			String singleStaff = null;
			if (!staffs.isEmpty()) {
				singleStaff = getMinWorkStaffByIds((String[]) staffs
						.toArray(new String[staffs.size()]));

				if (StringUtils.isEmpty(singleStaff)) {
					throw new IFinFlowException(105001, new Object[0]);
				}
			}
			assignable.setAssigneeOnly(singleStaff);
			return;
		}

		if (3 == this.assignMode) {
			String staffstr = staffs.toString();
			if (staffstr.length() > 2) {
				staffstr = staffstr.substring(1, staffstr.length() - 1);
			}
			assignable.setAssigneeOnly(staffstr);
			return;
		}
	}

	private String getMinWorkStaffByIds(String... ids) {
		List<String> list = Arrays.asList(ids);
		if (list.contains(this.exceptUserId)) {
			list.remove(this.exceptUserId);
		}
		if (list.size() == 1) {
			return (String) list.get(0);
		}
		ids = (String[]) list.toArray(new String[list.size()]);

		return (String) Configuration.getProcessEngine().execute(
				new WorkloadMinQueryCmd(ids));
	}

	private String getMinWorkStaffByRole(String... ids) {
		IdentityAdapter identityAdapter = UserExtendsReference
				.getIdentityAdapter();

		List<IWfStaff> iWfStaffs = identityAdapter.getStaffsByRoles(ids);

		return getStaffIds(iWfStaffs);
	}

	private String getMinWorkStaffByOrg(String... ids) {
		IdentityAdapter identityAdapter = UserExtendsReference
				.getIdentityAdapter();

		List<IWfStaff> iWfStaffs = identityAdapter.getStaffsByUnitIds(ids);

		return getStaffIds(iWfStaffs);
	}

	private String getMinWorkStaffByOrgRole(String orgId, String... roleId) {
		IdentityAdapter identityAdapter = UserExtendsReference
				.getIdentityAdapter();

		List<String> staffs = identityAdapter.getStaffIdsByRolesWithUnit(orgId,
				roleId);

		return getStaffByIds(staffs);
	}

	private String getStaffIds(List<IWfStaff> iWfStaffs) {
		if (iWfStaffs.isEmpty()) {
			throw new IFinFlowException(105002, new Object[] { this.nodeId });
		}
		String[] staffIds = new String[iWfStaffs.size()];
		int i = 0;
		for (IWfStaff staff : iWfStaffs) {
			if (!staff.getStaffId().equals(this.exceptUserId)) {
				staffIds[i] = staff.getStaffId();
				i++;
			}
		}
		return (String) Configuration.getProcessEngine().execute(
				new WorkloadMinQueryCmd(staffIds));
	}

	private String getStaffByIds(List<String> staffs) {
		if (staffs.contains(this.exceptUserId)) {
			staffs.remove(this.exceptUserId);
		}
		if (staffs.isEmpty()) {
			throw new IFinFlowException(105002, new Object[] { this.nodeId });
		}
		return (String) Configuration.getProcessEngine().execute(
				new WorkloadMinQueryCmd((String[]) staffs
						.toArray(new String[staffs.size()])));
	}

	private String getLastTaskExecutor(ExecutionImpl execution) {
		ProcessDefinitionImpl pd = execution.getProcessDefinition();
		ActivityImpl act = pd.getActivity(this.nodeId);
		TransitionImpl tran = (TransitionImpl) act.getIncomingTransitions()
				.get(0);

		ActivityImpl source = tran.getSource();
		if ("3".equals(source.getType())) {
			HistoryTaskImpl task = (HistoryTaskImpl) ((HistoryService) Configuration
					.getProcessEngine().get(HistoryService.class))
					.createHistoryTaskQuery()
					.executionId(execution.getExecutionImplId())
					.nodeId(source.getName()).orderDesc("createTime")
					.page(0, 1).uniqueResult();

			if (task != null) {
				return task.getExecutorId();
			}
		}
		return null;
	}

	private void parseParam(String top_sys_assign_param)
			throws IFinFlowException {
		String[] params = top_sys_assign_param.split(",");

		if ((params.length != 5) && (params.length != 4)) {
			throw new IFinFlowException(105004, new Object[0]);
		}

		this.assignMode = ("".equals(params[0]) ? 999 : Integer
				.parseInt(params[0]));

		this.participateType = ("".equals(params[1]) ? 999 : Integer
				.parseInt(params[1]));

		this.paramType = ("".equals(params[2]) ? 999 : Integer
				.parseInt(params[2]));

		this.historyType = ("".equals(params[3]) ? 999 : Integer
				.parseInt(params[3]));

		if (params.length == 5) {
			this.paramValue = params[4];
			if (StringUtils.isEmpty(this.paramValue)) {
				throw new IFinFlowException(105003, new Object[0]);
			}
		}
	}

	private HistoryProcessInstanceImpl getProcess(ExecutionImpl execution,
			DbSession dbsession) {
		String processId = execution.getProcessInstance().getDbid();

		ExecutionImpl superPro = execution.getSuperProcessExecution();
		if (superPro != null) {
			ExecutionImpl parent = superPro.getParent();
			if (parent != null) {
				processId = parent.getDbid();
			} else {
				processId = superPro.getDbid();
			}
		} else {
			ExecutionImpl parent = execution.getParent();
			if (parent != null) {
				processId = parent.getDbid();
			}
		}

		return (HistoryProcessInstanceImpl) dbsession
				.createHistoryProcessInstanceQuery().processId(processId)
				.uniqueResult();
	}

	private void checkStaff(IdentityAdapter identityAdapters, String[] ids) {
		List<IWfStaff> ls = identityAdapters.getStaffsByids(ids);
		int num = ids.length - ls.size();
		if (num != 0) {
			List<String> idList = Arrays.asList(ids);
			for (IWfStaff st : ls) {
				if (idList.contains(st.getStaffId())) {
					idList.remove(st.getStaffId());
				}
			}
			throw new IFinFlowException(103041, new Object[] { this.nodeId,
					Arrays.toString(idList.toArray(new String[num])) });
		}
	}
}
