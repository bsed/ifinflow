package com.ruimin.ifinflow.engine.pvm.cmd;

import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.session.DbSession;

import java.util.Collection;

public class ProcessUpdateCmd extends AbstractCommand<Void> {
	private static final long serialVersionUID = 1L;
	private HistoryProcessInstanceImpl hpii;

	public ProcessUpdateCmd(HistoryProcessInstanceImpl hpii) {
		this.hpii = hpii;
	}

	public Void execute(Environment environment) {
		DbSession dbSession = (DbSession) environment.get(DbSession.class);
		dbSession.update(this.hpii);
		return null;
	}
}
