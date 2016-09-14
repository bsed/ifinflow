package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.flowmodel.Variable;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryVariableImpl;
import org.jbpm.pvm.internal.util.CollectionUtil;

public class TaskHistoryVariableFindCmd extends AbstractCommand<VariableSet> {
	private static final long serialVersionUID = 1L;
	protected String taskId;
	protected String processId;

	public TaskHistoryVariableFindCmd(String taskId, String processId) {
		this.taskId = taskId;
		this.processId = processId;
	}

	public VariableSet execute(Environment environment) throws Exception {
		if ((StringUtils.isEmpty(this.taskId))
				&& (StringUtils.isEmpty(this.processId))) {
			throw new IFinFlowException(103008, new Object[0]);
		}

		Session session = (Session) environment.get(Session.class);
		List<HistoryVariableImpl> vars = new ArrayList();

		StringBuffer hql = new StringBuffer("from ");
		hql.append(HistoryVariableImpl.class.getName());
		if ((StringUtils.isEmpty(this.taskId))
				&& (!StringUtils.isEmpty(this.processId))) {
			hql.append(" va where va.historyTask.dbid=?");
			vars = CollectionUtil.checkList(session.createQuery(hql.toString())
					.setString(0, this.taskId).list(),
					HistoryVariableImpl.class);
		}

		if ((!StringUtils.isEmpty(this.taskId))
				&& (StringUtils.isEmpty(this.processId))) {
			hql.append(" va where va.processInstanceId=? ");
			vars = CollectionUtil.checkList(session.createQuery(hql.toString())
					.setString(0, this.processId).list(),
					HistoryVariableImpl.class);
		}

		if ((!StringUtils.isEmpty(this.taskId))
				&& (!StringUtils.isEmpty(this.processId))) {
			hql.append(" va where va.processInstanceId=?  and va.historyTask.dbid=?");
			vars = CollectionUtil.checkList(session.createQuery(hql.toString())
					.setString(0, this.processId).setString(1, this.taskId)
					.list(), HistoryVariableImpl.class);
		}

		VariableSet vs = new VariableSet();
		for (HistoryVariableImpl va : vars) {
			vs.addVariable(new Variable(va.getVariableName(), va.getKind()
					.intValue(), va.getValue(), va.getBizName()));
		}

		return vs;
	}
}
