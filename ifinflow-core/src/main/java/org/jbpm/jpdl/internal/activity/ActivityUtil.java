package org.jbpm.jpdl.internal.activity;

import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;
import com.ruimin.ifinflow.util.TemplateCacheUtil;
import java.io.Serializable;
import java.util.Date;
import org.jbpm.api.JbpmException;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;

public class ActivityUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void activityStartCopyValue(ExecutionImpl execution,
			HistoryActivityInstanceImpl historyActivityInstance) {
		TemplateVo templateVO = TemplateCacheUtil.getTemplateVO(execution);

		historyActivityInstance.setPackageId(templateVO.getPackageId());
		historyActivityInstance.setTemplateId(templateVO.getTemplateId());
		historyActivityInstance.setTemplateVersion(templateVO.getVersion());

		NodeVo nodeVO = templateVO.getNodeVo(historyActivityInstance
				.getActivityName());

		if (nodeVO != null) {
			historyActivityInstance.setType(String.valueOf(nodeVO.getType()));
			historyActivityInstance.setSkipAuth(nodeVO.getSkipAuth());
			historyActivityInstance.setRejectAuth(nodeVO.getRejectAuth());
			historyActivityInstance.setOverdue(Integer.valueOf((int) nodeVO
					.getOvertime()));
			historyActivityInstance.setDeadLine(new Date(
					historyActivityInstance.getStartTime().getTime()
							+ historyActivityInstance.getOverdue().intValue()));
		} else {
			throw new JbpmException("缓存中未找到节点信息");
		}
	}
}
