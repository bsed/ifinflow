package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfGroup;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import java.util.Collection;
import java.util.List;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;

public class GroupQueryIdAndNamePageCmd extends AbstractCommand<List<IWfGroup>> {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private int startNum;
	private int pageSize;

	public GroupQueryIdAndNamePageCmd(String id, String name, int startNum,
			int pageSize) {
		this.id = id;
		this.name = name;
		this.startNum = startNum;
		this.pageSize = pageSize;
	}

	public List<IWfGroup> execute(Environment environment) {
		IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();

		List<IWfGroup> groups = adapter.queryGroup(this.id, this.name,
				this.startNum, this.pageSize);

		return groups;
	}
}
