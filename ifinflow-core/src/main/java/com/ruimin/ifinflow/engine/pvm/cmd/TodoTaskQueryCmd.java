package com.ruimin.ifinflow.engine.pvm.cmd;

import java.util.Collection;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.query.TaskQueryUtil;
import org.jbpm.pvm.internal.task.TopTaskVO;
import org.jbpm.pvm.internal.util.CollectionUtil;

public class TodoTaskQueryCmd extends AbstractCommand<List<TopTaskVO>> {
	private static final long serialVersionUID = 1L;
	protected String userId;
	protected int startNum;
	protected int pageSize;
	protected String nodeName;
	protected String templateId;
	protected int status;
	protected String[] extendValue;

	public TodoTaskQueryCmd(String userId, int startNum, int pageSize) {
		this.userId = userId;
		this.startNum = startNum;
		this.pageSize = pageSize;
	}

	public TodoTaskQueryCmd(String userId, String nodeName, String templateId,
			int startNum, int pageSize, int status, String... extendValue) {
		this.userId = userId;
		this.nodeName = nodeName;
		this.templateId = templateId;
		this.startNum = startNum;
		this.pageSize = pageSize;
		this.status = status;
		this.extendValue = extendValue;
	}

	public List<TopTaskVO> execute(Environment environment) throws Exception {
		Session session = (Session) environment.get(Session.class);

		Query query = TaskQueryUtil.getTodoTaskQuery(session, false,
				this.userId, this.nodeName, this.templateId, null, this.status,
				this.startNum, this.pageSize, this.extendValue);

		return CollectionUtil.checkList(query.list(), TopTaskVO.class);
	}
}