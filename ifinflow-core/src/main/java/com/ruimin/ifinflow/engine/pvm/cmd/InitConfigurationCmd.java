package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.internal.config.Configuration;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.env.EnvironmentImpl;

import java.util.Collection;

public class InitConfigurationCmd implements Command<Object> {
	private static final long serialVersionUID = 1L;
	private int CMD;

	public InitConfigurationCmd(int cmd) {
		this.CMD = cmd;
	}

	public Collection<String> execute(Environment environment) throws Exception {
		Session session = (Session) EnvironmentImpl
				.getFromCurrent(Session.class);
		if (this.CMD == Configuration.INIT) {
			Configuration.init(session);
		} else if (this.CMD == Configuration.RELOAD) {
			Configuration.reload(session);
		} else if (this.CMD == Configuration.CLEAR) {
			Configuration.clear();
		}

		return null;
	}
}
