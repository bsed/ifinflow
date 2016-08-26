package com.ruimin.ifinflow.engine.internal.adapter;

import java.util.Map;
import java.util.Set;

import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;
import org.jbpm.internal.log.Log;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.session.DbSession;

import com.ruimin.ifinflow.engine.external.adapter.IBusinessAdapter;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.util.BeanUtil;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;

public class CallBusinessAdapter implements EventListener {
	private static Log log = Log.getLog(CallBusinessAdapter.class.getName());

	private static final long serialVersionUID = 1L;

	private String classType;

	private String extendsclass;

	public void notify(EventListenerExecution execution) throws Exception {
		try {
			notify((ExecutionImpl) execution);
		} catch (Exception e) {
			log.error("业务适配器调用异常", e);
			throw e;
		}
	}

	public void notify(ExecutionImpl execution) throws Exception {
		IBusinessAdapter iba = UserExtendsReference.getBusinessAdapter(
				this.extendsclass, this.classType, execution.getActivityName());

		VariableSet vs = new VariableSet();
		Map<String, org.jbpm.pvm.internal.type.Variable> map = execution
				.getVariablesPrototype();

		Set<Map.Entry<String, org.jbpm.pvm.internal.type.Variable>> set = map
				.entrySet();

		org.jbpm.pvm.internal.type.Variable var = null;
		for (Map.Entry<String, org.jbpm.pvm.internal.type.Variable> entry : set) {
			var = (org.jbpm.pvm.internal.type.Variable) entry.getValue();
			vs.addVariable(new com.ruimin.ifinflow.engine.flowmodel.Variable(
					var.getKey(), var.getKind().intValue(), var
							.getValue(execution), var.getBizName()));
		}

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
}
