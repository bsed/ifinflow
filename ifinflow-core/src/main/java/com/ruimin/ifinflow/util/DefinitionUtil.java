package com.ruimin.ifinflow.util;

import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.jbpm.pvm.internal.util.CollectionUtil;

public class DefinitionUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void getNextNodeList(ActivityImpl act,
			ProcessDefinitionImpl pd, List<String> result) {
		String nodeId = act.getName();
		NodeVo nodevo = TemplateCacheUtil.getTemplateVo(pd).getNodeVo(nodeId);

		if (nodevo == null) {
			throw new IFinFlowException(108003, new Object[] { nodeId });
		}

		if ((isSkipIn(nodevo.getSkipAuth())) && (1 == nodevo.getDisplay())) {
			if (!result.contains(act.getName() + ";" + nodevo.getName())) {
				result.add(act.getName() + ";" + nodevo.getName());
			} else {
				return;
			}
		}

		List<TransitionImpl> tranList = null;

		if ((StringUtils.equals("5", act.getType()))
				|| (StringUtils.equals("7", act.getType()))) {

			tranList = CollectionUtil.checkList(act.getOutgoingTransitions(),
					TransitionImpl.class);
		} else {
			tranList = new ArrayList();
			tranList.add(act.getDefaultOutgoingTransition());
		}

		for (TransitionImpl tran : tranList) {
			if (tran != null) {
				getNextNodeList(tran.getDestination(), pd, result);
			}
		}
	}

	public static boolean isSkipIn(String skipAuth) {
		if (("01".equals(skipAuth)) || ("11".equals(skipAuth))) {
			return true;
		}
		return false;
	}

	public static boolean isSkipOut(String skipAuth) {
		if (("10".equals(skipAuth)) || ("11".equals(skipAuth))) {
			return true;
		}
		return false;
	}

	public static String addOutGoingTransition(ProcessDefinitionImpl pd,
			ActivityImpl fromActivity, String targetName)
			throws IFinFlowException {
		String path = fromActivity.getName() + " to " + targetName;
		ActivityImpl toAct = pd.getActivity(targetName);
		if (toAct == null) {
			throw new IFinFlowException(109015, new Object[0]);
		}
		TransitionImpl tran = toAct.findOutgoingTransition(path);

		if (tran == null) {
			tran = fromActivity.createOutgoingTransition();
			tran.setName(path);
			tran.setDestination(toAct);
			fromActivity.addOutgoingTransition(tran);
		}
		return path;
	}

	public static String addOutGoingTransition(ProcessDefinitionImpl pd,
			ActivityImpl fromActivity, String targetName,
			ProcessDefinitionImpl superPd) throws IFinFlowException {
		if (!pd.hasActivity(targetName)) {
			pd = superPd;
		}

		return addOutGoingTransition(pd, fromActivity, targetName);
	}

	public static NodeVo getCacshByTask(TaskImpl task) throws IFinFlowException {
		return TemplateCacheUtil.getNodeVo(task.getExecution());
	}

	public static ProcessDefinitionImpl getSuperDefinitionImpl(
			ExecutionImpl execution) {
		ExecutionImpl parentExecution = execution.getSuperProcessExecution();
		ProcessDefinitionImpl parentProcDef = null;
		if (parentExecution != null) {
			parentProcDef = (ProcessDefinitionImpl) Configuration
					.getProcessEngine()
					.getRepositoryService()
					.createProcessDefinitionQuery()
					.processDefinitionId(
							parentExecution.getProcessDefinitionId())
					.uniqueResult();
		}

		return parentProcDef;
	}
}
