package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfGroup;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import java.util.Collection;
import java.util.List;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;

public class GroupQueryPageCmd extends AbstractCommand<List<IWfGroup>> {
	private static final long serialVersionUID = 1L;
	private String idOrName;
	private int startNum;
	private int pageSize;

	public GroupQueryPageCmd(String idOrName, int startNum, int pageSize) {
		this.idOrName = idOrName;
		this.startNum = startNum;
		this.pageSize = pageSize;
	}

	public List<IWfGroup> execute(Environment environment) {
		IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();

		List<IWfGroup> groups = adapter.getGroup(this.idOrName, this.startNum,
				this.pageSize);

		return groups;
	}
}

