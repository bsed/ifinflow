package com.ruimin.ifinflow.engine.internal.adapter;

import com.ruimin.ifinflow.engine.external.adapter.IOvertimeAdapter;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.engine.internal.log.LogCategory;
import com.ruimin.ifinflow.engine.internal.log.WorkflowLog;
import com.ruimin.ifinflow.engine.internal.log.vo.AppLog;
import com.ruimin.ifinflow.util.ActivityUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Level;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.query.HistoryProcessInstanceQueryImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.task.TaskImpl;

public class CallOvertimeAdapter implements EventListener {
	private static final long serialVersionUID = 1L;
	private String classType;
	private String extendsclass;

	public void notify(EventListenerExecution execution) throws Exception {
		notify((ExecutionImpl) execution);
	}

	public void notify(ExecutionImpl execution) throws Exception {
		IOvertimeAdapter iba = UserExtendsReference.getOvertimeAdapter(
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

		AppLog log = new AppLog(LogCategory.OVERTIME, this.extendsclass
				+ ".invoke", new String[] { "invokeParams" },
				new Object[] { vs });
		log.setProcessPoid(execution.getProcessInstance().getDbid());
		log.setActionType("4");
		log.setActionName(execution.getActivityName());
		log.setMpid(execution.getProcessInstance().getDbid());
		WorkflowLog.dbLog(Level.INFO, log);

		vs = iba.invoke(vs);

		if (vs != null) {
			execution.setVariables(vs.getList());
		}

		updateStatus(execution);
	}

	private void updateStatus(ExecutionImpl execution) {
		DbSession dbSession = (DbSession) EnvironmentImpl
				.getFromCurrent(DbSession.class);

		HistoryProcessInstanceImpl hisProc = (HistoryProcessInstanceImpl) dbSession
				.createHistoryProcessInstanceQuery()
				.processInstanceId(
						execution.getProcessInstance().getExecutionImplId())
				.uniqueResult();

		ActivityImpl activity = execution.getActivity();

		HistoryActivityInstanceImpl hisAct = ActivityUtil
				.getHistoryActIns(execution.getHistoryActivityInstanceDbid());

		if (hisAct != null) {
			if ("3" == activity.getType()) {
				HistoryTaskInstanceImpl taskInstance = (HistoryTaskInstanceImpl) hisAct;
				HistoryTaskImpl task = taskInstance.getHistoryTask();
				task.setStatus(Integer.valueOf(256));
				dbSession.update(task);

				TaskImpl ttask = (TaskImpl) dbSession.get(TaskImpl.class,
						task.getId());
				ttask.setStatus(256);
				Set<Task> subs = ttask.getSubTasks();
				if (!subs.isEmpty()) {
					Iterator<Task> it = subs.iterator();
					while (it.hasNext()) {
						TaskImpl sub = (TaskImpl) it.next();
						sub.setStatus(256);
					}
				}
				dbSession.update(ttask);
			}

			hisAct.setStatus(Integer.valueOf(256));
			dbSession.update(hisAct);
		}

		hisProc.setStatus(Integer.valueOf(256));
		dbSession.update(hisProc);
	}
}
