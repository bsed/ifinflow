package com.ruimin.ifinflow.engine.pvm.cmd;

import org.jbpm.api.JbpmException;

import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import org.jbpm.pvm.internal.session.DbSession;

import java.util.Collection;

public class ProcessSuspendCmd extends AbstractCommand<Void> {
	private static final long serialVersionUID = 1L;
	String processId;
	String taskId;

	public ProcessSuspendCmd(String processId) {
		this.processId = processId;
	}

	public Void execute(Environment environment) throws Exception {
		DbSession dbSession = (DbSession) environment.get(DbSession.class);
		ExecutionImpl process = null;
		process = (ExecutionImpl) dbSession.createProcessInstanceQuery()
				.dbid(this.processId).uniqueResult();

		if (process == null) {
			throw new JbpmException("没找到对应流程实例，processId=" + this.processId);
		}
		process.suspend();
		return null;
	}
}
