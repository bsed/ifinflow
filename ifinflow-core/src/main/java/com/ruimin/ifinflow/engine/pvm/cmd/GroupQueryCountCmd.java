package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;

import java.util.Collection;

public class GroupQueryCountCmd extends AbstractCommand<Long> {
	private static final long serialVersionUID = 1L;
	private String idOrName;

	public GroupQueryCountCmd(String idOrName) {
		this.idOrName = idOrName;
	}

	public Long execute(Environment environment) {
		IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();

		long count = adapter.getGroupCount(this.idOrName);

		return Long.valueOf(count);
	}
}
