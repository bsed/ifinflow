package com.ruimin.ifinflow.model.flowmodel.cache;

import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateEventVo;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;
import com.ruimin.ifinflow.model.flowmodel.xml.AssignPolicy;
import com.ruimin.ifinflow.model.flowmodel.xml.Node;
import com.ruimin.ifinflow.model.flowmodel.xml.NodeTimeLimit;
import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplateEvent;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplateTimeLimit;
import com.ruimin.ifinflow.model.flowmodel.xml.VarTaskMapping;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.pvm.internal.cmd.QueryByHqlCmd;

public class TemplateCache {
	private String deploymentId;
	private TemplateVo templateVo;

	protected TemplateVo getTemplateVo(String deploymentId) {
		if (this.templateVo == null) {
			Object[] templateInfo = getTempalteInfo(deploymentId);
			getTemplateVo(templateInfo);
		}
		return this.templateVo;
	}

	protected TemplateVo getTemplateVo(String templateId, String packageId,
			int version) {
		if (this.templateVo == null) {
			Object[] templateInfo = getTempalteInfo(templateId, packageId,
					version);
			getTemplateVo(templateInfo);
		}
		return this.templateVo;
	}

	private void getTemplateVo(Object[] templateInfo) {
		if ((templateInfo != null) && (templateInfo.length > 0)) {
			this.templateVo = new TemplateVo();
			this.templateVo.setPackageId((String) templateInfo[0]);
			this.templateVo.setTemplateId((String) templateInfo[1]);
			this.templateVo.setTemplateName((String) templateInfo[2]);
			this.templateVo.setVersion(((Integer) templateInfo[4]).intValue());
			this.templateVo.setFirstTaskCommit((String) templateInfo[5]);
			String templateHandle = (String) templateInfo[3];

			this.templateVo.setOvertime(getOvertime(templateHandle, 0));

			this.templateVo.setEvents(getTemplateEvent(templateHandle));

			this.templateVo.setNodeMap(getNodeInfo(templateHandle));
		}
	}

	private Object[] getTempalteInfo(String deploymentId) {
		StringBuilder hql = new StringBuilder();
		hql.append("select p.templatePackageId,t.templateId,t.name,t.handle,t.version,t.firstTaskCommit from "
				+ Template.class.getName());

		hql.append(" t,");
		hql.append(TemplatePackage.class.getName());
		hql.append(" p where t.packageHandle = p.handle and t.deploymentId = '");
		hql.append(deploymentId).append("'");

		List<?> list = (List) Configuration.getProcessEngine().execute(
				new QueryByHqlCmd(hql.toString()));

		if (!list.isEmpty()) {
			return (Object[]) list.get(0);
		}
		return null;
	}

	private Object[] getTempalteInfo(String templateId, String packageId,
			int version) {
		StringBuilder hql = new StringBuilder();
		hql.append("select p.templatePackageId,t.templateId,t.name,t.handle,t.version,t.firstTaskCommit from "
				+ Template.class.getName());

		hql.append(" t,").append(TemplatePackage.class.getName())
				.append(" p where t.packageHandle = p.handle")
				.append(" and t.version = ").append(version)
				.append(" and t.templateId = '").append(templateId).append("'")
				.append(" and p.templatePackageId = '").append(packageId)
				.append("'");

		List<?> list = (List) Configuration.getProcessEngine().execute(
				new QueryByHqlCmd(hql.toString()));

		if (!list.isEmpty()) {
			return (Object[]) list.get(0);
		}
		return null;
	}

	private long getOvertime(String handle, int flag) {
		long overtime = 0L;
		StringBuilder hql = new StringBuilder();
		hql.append("select o.consumeDay,o.consumeHour,o.consumeMinute,o.consumeSecond from ");
		if (flag == 0) {
			hql.append(TemplateTimeLimit.class.getName());
			hql.append(" o where o.templateHandle = '");
		} else {
			hql.append(NodeTimeLimit.class.getName());
			hql.append(" o where o.nodeHandle = '");
		}
		hql.append(handle);
		hql.append("'");

		List<Object[]> list = (List) Configuration.getProcessEngine().execute(
				new QueryByHqlCmd(hql.toString()));

		if (!list.isEmpty()) {
			Object[] consumeTime = (Object[]) list.get(0);
			if (consumeTime != null) {
				overtime = ((Integer) consumeTime[0]).intValue() * 24 * 60 * 60
						+ ((Integer) consumeTime[1]).intValue() * 60 * 60
						+ ((Integer) consumeTime[2]).intValue() * 60
						+ ((Integer) consumeTime[3]).intValue();
			}
		}

		return overtime;
	}

	private List<TemplateEventVo> getTemplateEvent(String templateHandle) {
		List<TemplateEventVo> templateEventVos = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append("from " + TemplateEvent.class.getName());
		hql.append(" o where o.templateHandle = '");
		hql.append(templateHandle);
		hql.append("'");

		List<TemplateEvent> templateEvents = (List) Configuration
				.getProcessEngine().execute(new QueryByHqlCmd(hql.toString()));

		TemplateEventVo templateEventVo = null;
		for (TemplateEvent te : templateEvents) {
			templateEventVo = new TemplateEventVo();
			templateEventVo.setAdapterName(te.getAdapterName());
			templateEventVo.setAdapterType(te.getAdapterType());
			templateEventVo.setType(te.getType());
			templateEventVos.add(templateEventVo);
		}
		return templateEventVos;
	}

	private Map<String, NodeVo> getNodeInfo(String templateHandle) {
		Map<String, NodeVo> nodeVos = new HashMap();
		StringBuilder hql = new StringBuilder();
		hql.append("select o.handle, o.name, o.skipAuth, o.rejectAuth, o.kind, o.nodeId, o.rejectdContinue, o.priority, o.url, o.rejectDefault, o.display, o.rejectDefaultNodeId, o.rejectAssignType from ");
		hql.append(Node.class.getName());
		hql.append(" o where o.templateHandle = '");
		hql.append(templateHandle);
		hql.append("'");

		List<Object[]> nodes = (List) Configuration.getProcessEngine().execute(
				new QueryByHqlCmd(hql.toString()));

		NodeVo nodeVo = null;
		if (!nodes.isEmpty()) {
			for (Object[] node : nodes) {
				nodeVo = new NodeVo();
				nodeVo.setName((String) node[1]);
				nodeVo.setSkipAuth((String) node[2]);
				nodeVo.setRejectAuth((String) node[3]);
				nodeVo.setType(((Integer) node[4]).intValue());
				nodeVo.setRejectdContinue((String) node[6]);
				nodeVo.setOvertime(getOvertime(templateHandle, 1));
				nodeVo.setPriority(((Integer) node[7]).intValue());
				nodeVo.setUrl((String) node[8]);
				nodeVo.setRejectDefault((String) node[9]);
				nodeVo.setDisplay(((Integer) node[10]).intValue());
				nodeVo.setRejectDefaultNodeId((String) node[11]);
				nodeVo.setRejectAssignType((String) node[12]);
				nodeVos.put((String) node[5], nodeVo);

				hql = new StringBuilder();
				hql.append("select o.assignMode, o.exitType, o.exitCount, o.handle, o.participantType, o.participantAssign, o.participantHistory, o.result from ");
				hql.append(AssignPolicy.class.getName());
				hql.append(" o where o.handle = '");
				hql.append((String) node[0]);
				hql.append("'");
				List<Object[]> assignPolicyList = (List) Configuration
						.getProcessEngine().execute(
								new QueryByHqlCmd(hql.toString()));

				if (!assignPolicyList.isEmpty()) {
					Object[] assignPolicy = (Object[]) assignPolicyList.get(0);
					if (null != assignPolicy) {
						nodeVo.setAssignMode(((Integer) assignPolicy[0])
								.intValue());
						nodeVo.setExitType((String) assignPolicy[1]);
						nodeVo.setExitCount(((Integer) assignPolicy[2])
								.intValue());
						nodeVo.setAssignHandle((String) assignPolicy[3]);
						nodeVo.setParticipantType(((Integer) assignPolicy[4])
								.intValue());
						nodeVo.setParticipantAssign(((Integer) assignPolicy[5])
								.intValue());
						nodeVo.setParticipantHistory(((Integer) assignPolicy[6])
								.intValue());
						nodeVo.setResult((String) assignPolicy[7]);
					}

					hql = new StringBuilder();
					hql.append("select o.variableName, o.userExtColume from ");
					hql.append(VarTaskMapping.class.getName());
					hql.append(" o where o.nodeHandle = '");
					hql.append((String) node[0]);
					hql.append("' order by o.userExtColume");
					List<String[]> variableNameList = (List) Configuration
							.getProcessEngine().execute(
									new QueryByHqlCmd(hql.toString()));

					nodeVo.setVariableNameList(variableNameList);
				}
			}
		}

		return nodeVos;
	}

	public String getDeploymentId() {
		return this.deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public TemplateVo getTemplateVo() {
		return this.templateVo;
	}

	public void setTemplateVo(TemplateVo templateVo) {
		this.templateVo = templateVo;
	}
}
