package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;

import java.util.Collection;

public class SaveHistoryTaskCmd extends AbstractCommand<Void> {
	private static final long serialVersionUID = 1L;
	protected String historyTaskId;
	String hisActId;

	public SaveHistoryTaskCmd(String historyTaskId, String hisActId) {
		this.historyTaskId = historyTaskId;
		this.hisActId = hisActId;
	}

	public Void execute(Environment environment) throws Exception {
		Session session = (Session) environment.get(Session.class);
		session.createSQLQuery(
				"update IFF_J_ACTIVITY set HTASK_ = '" + this.historyTaskId
						+ "' where dbid_ = '" + this.hisActId + "'")
				.executeUpdate();

		return null;
	}
}
