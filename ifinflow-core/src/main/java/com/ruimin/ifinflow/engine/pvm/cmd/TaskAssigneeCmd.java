package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.engine.pvm.event.WorkloadUpdate;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.identity.User;
import org.jbpm.api.model.WfUserInfo;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.HistoryEvent;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.identity.spi.IdentitySession;
import org.jbpm.pvm.internal.query.TaskQueryUtil;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.jbpm.pvm.internal.task.TopTaskVO;
import org.jbpm.pvm.internal.util.Clock;

import java.util.Collection;

public class TaskAssigneeCmd extends AbstractCommand<Void> {
	private static final long serialVersionUID = 1L;
	protected String taskId;
	protected WfUserInfo user;
	protected boolean take;

	public TaskAssigneeCmd(String taskId, WfUserInfo user) {
		this.taskId = taskId;
		this.user = user;
	}

	public TaskAssigneeCmd(String taskId, WfUserInfo user, boolean take) {
		this.taskId = taskId;
		this.user = user;
		this.take = take;
	}

	public Void execute(Environment environment) throws Exception {
		if ((this.user != null)
				&& (StringUtils.isEmpty(this.user.getStaffId()))) {
			throw new IFinFlowException(101001, new Object[0]);
		}
		if (StringUtils.isBlank(this.taskId)) {
			throw new IFinFlowException(103050, new Object[0]);
		}
		DbSession dbSession = (DbSession) environment.get(DbSession.class);

		IdentitySession identitySession = (IdentitySession) EnvironmentImpl
				.getFromCurrent(IdentitySession.class);

		User staf = identitySession.findUserById(this.user.getStaffId());
		if (staf == null) {
			throw new IFinFlowException(105005,
					new Object[] { this.user.getStaffId() });
		}

		Session session = (Session) environment.get(Session.class);
		Query query = TaskQueryUtil.getTodoTaskQuery(session, false,
				this.user.getStaffId(), null, null, this.taskId, 1, -1, 0,
				new String[0]);

		TopTaskVO toptask = (TopTaskVO) query.uniqueResult();
		if (toptask == null) {
			throw new IFinFlowException(103006, new Object[] { this.taskId,
					this.user.getStaffId() });
		}

		TaskImpl task = (TaskImpl) session.get(TaskImpl.class, this.taskId);
		HistoryTaskImpl histask = (HistoryTaskImpl) session.get(
				HistoryTaskImpl.class, this.taskId);

		IdentityAdapter ida = UserExtendsReference.getIdentityAdapter();
		IWfUnit unit = ida.getUnitByStaffId(staf.getId());
		if (unit != null) {
			histask.setOwnerUnitId(unit.getUnitId());
			task.setOwnerUnitId(unit.getUnitId());
		}
		task.setAssigneeOnly(staf.getId());
		task.setOwnerId(staf.getId());
		task.setStatus(2);
		task.setTakeDate(Clock.getTime());
		dbSession.update(task);

		if (task.getAssignMode() == 2) {
			HistoryEvent.fire(new WorkloadUpdate(staf.getId(), staf
					.getGivenName(), 1));
		}

		histask.setStatus(Integer.valueOf(2));

		histask.setAssignee(staf.getId());
		histask.setOwnerId(staf.getId());
		histask.setOwnerName(staf.getGivenName());

		histask.setTakeDate(task.getTakeDate());
		dbSession.update(histask);

		return null;
	}
}
