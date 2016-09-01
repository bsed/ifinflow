package com.ruimin.ifinflow.engine.pvm.activity;

import com.ruimin.ifinflow.engine.external.adapter.IBusinessAdapter;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.util.BeanUtil;
import com.ruimin.ifinflow.engine.flowmodel.util.VariableUtil;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.engine.internal.log.LogCategory;
import com.ruimin.ifinflow.engine.internal.log.WorkflowLog;
import com.ruimin.ifinflow.engine.internal.log.vo.AppLog;
import java.util.Map;
import org.apache.log4j.Level;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.activity.ExternalActivityBehaviour;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.query.HistoryProcessInstanceQueryImpl;
import org.jbpm.pvm.internal.session.DbSession;

public class RuleActivity implements ExternalActivityBehaviour {
	private static final long serialVersionUID = 1L;
	private String classType;
	private String extendsclass;

	public void execute(ActivityExecution execution) throws Exception {
		execute((ExecutionImpl) execution);
	}

	public void execute(ExecutionImpl execution) throws Exception {
		IBusinessAdapter iba = UserExtendsReference.getBusinessAdapter(
				this.extendsclass, this.classType, execution.getActivityName());

		VariableSet vs = VariableUtil.getVariableSet(execution);

		AppLog log = new AppLog(LogCategory.AUTONODE, this.extendsclass
				+ ".invoke", new String[] { "invokeParams" },
				new Object[] { vs });
		log.setProcessPoid(execution.getDbid());
		log.setActionType("4");
		log.setActionName(execution.getActivityName());
		ExecutionImpl parent = execution;
		while (parent.getParent() != null) {
			parent = parent.getParent();
		}
		log.setMpid(parent.getDbid());
		WorkflowLog.dbLog(Level.INFO, log);

		DbSession session = (DbSession) EnvironmentImpl.getFromCurrent(
				DbSession.class, false);

		HistoryProcessInstance hpii = session
				.createHistoryProcessInstanceQuery()
				.processId(execution.getDbid()).uniqueResult();

		ProcessVO process = BeanUtil.createProcessVO(hpii);

		vs = iba.invoke(vs, process);

		if (vs != null) {
			execution.setVariables(vs.getList());
		}
	}

	public void signal(ActivityExecution execution, String signalName,
			Map<String, ?> parameters) throws Exception {
	}

	public void signal(ExecutionImpl execution, String signalName,
			Map<String, ?> parameters) throws Exception {
	}
}
