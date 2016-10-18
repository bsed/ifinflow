package com.ruimin.ifinflow.util;

import com.ruimin.ifinflow.model.flowmodel.cache.TemplateCache;
import com.ruimin.ifinflow.model.flowmodel.cache.TemplateCacheManager;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;

public class TemplateCacheUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	public static boolean isDisplayNode(ExecutionImpl execution) {
		NodeVo node = getNodeVo(execution);
		return node.getDisplay() != 0;
	}

	public static TemplateVo getTemplateVO(ExecutionImpl execution) {
		return getTemplateVo(getPD(execution));
	}

	public static TemplateVo getTemplateVO(String packageId, String templateId,
			int version) {
		TemplateVo template = TemplateCacheManager
				.getInstance()
				.getTemplateCacheInfo(
						packageId + "_" + templateId + "-" + version)
				.getTemplateVo();

		if (template == null) {
			throw new IFinFlowException(108001, new Object[] { packageId + "_"
					+ templateId + "-" + version });
		}
		return template;
	}

	public static NodeVo getNodeVo(ExecutionImpl execution) {
		return getNodeVo(getPD(execution), execution.getActivityName());
	}

	private static ProcessDefinition getPD(ExecutionImpl execution) {
		ProcessDefinitionImpl pd = execution.getProcessDefinition();
		if (pd.findActivity(execution.getActivityName()) == null) {
			pd = execution.getSubProcessInstance().getProcessDefinition();
		}
		return pd;
	}

	public static NodeVo getNodeVo(TemplateVo template, String nodeId) {
		NodeVo node = template.getNodeVo(nodeId);
		if (node == null) {
			throw new IFinFlowException(108003, new Object[] { nodeId });
		}
		return node;
	}

	public static NodeVo getNodeVo(String packageId, String templateId,
			int version, String nodeId) {
		NodeVo node = getTemplateVO(packageId, templateId, version).getNodeVo(
				nodeId);

		if (node == null) {
			throw new IFinFlowException(108003, new Object[] { nodeId });
		}
		return node;
	}

	public static TemplateVo getTemplateVo(ProcessDefinition pd) {
		String processName = pd.getName();
		String packageId = processName.substring(0,
				processName.lastIndexOf("_"));
		String templateId = processName
				.substring(processName.lastIndexOf("_") + 1);

		packageId = packageId.replace("_", ".");

		return getTemplateVO(packageId, templateId, pd.getVersion());
	}

	public static NodeVo getNodeVo(ProcessDefinition pd, String nodeId) {
		return getNodeVo(getTemplateVo(pd), nodeId);
	}
}
