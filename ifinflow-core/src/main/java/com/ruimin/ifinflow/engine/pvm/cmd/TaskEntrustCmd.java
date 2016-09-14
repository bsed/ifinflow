package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.TaskService;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.task.TaskImpl;

import java.util.Collection;

public class TaskEntrustCmd implements Command<Void> {
	private static final long serialVersionUID = 1L;
	private String taskId;
	private String toUser;
	private String oldUser;
	private String memo;

	public TaskEntrustCmd(String taskId, String oldUser, String toUser) {
		this.taskId = taskId;
		this.toUser = toUser;
		this.oldUser = oldUser;
	}

	public TaskEntrustCmd(String taskId, String oldUser, String toUser,
			String memo) {
		this.taskId = taskId;
		this.toUser = toUser;
		this.oldUser = oldUser;
		this.memo = memo;
	}

	public Void execute(Environment environment) throws Exception {
		if (StringUtils.isBlank(this.taskId)) {
			throw new IFinFlowException(103052, new Object[0]);
		}

		DbSession dbSession = (DbSession) environment.get(DbSession.class);
		TaskImpl task = (TaskImpl) dbSession.get(TaskImpl.class, this.taskId);

		if (task == null) {
			throw new IFinFlowException(103053, new Object[] { this.taskId });
		}

		if (2 != task.getStatus()) {
			throw new IFinFlowException(103013, new Object[0]);
		}

		if (!task.getAssignee().equals(this.oldUser)) {
			throw new IFinFlowException(103054, new Object[] { this.oldUser,
					task.getAssignee(), this.taskId });
		}

		TaskService taskService = (TaskService) environment
				.get(TaskService.class);

		taskService.addTaskParticipatingUser(task.getId(), task.getAssignee(),
				"replaced-assignee");

		taskService.addTaskComment(task.getId(), "任务由 " + task.getAssignee()
				+ "移交给" + this.toUser + ",移交原因:" + this.memo);

		task.updateAssignee(this.oldUser, this.toUser, true);
		return null;
	}
}
