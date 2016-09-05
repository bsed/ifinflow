package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.id.DbidGenerator;
import org.jbpm.pvm.internal.task.MultiTaskExists;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.jbpm.pvm.internal.util.Clock;

public class AddSubtaskCmd implements Command<Void> {
	private static final long serialVersionUID = 2341508299588517223L;
	private String processId;
	private String nodeId;
	private String taskId;
	private String exitType;
	private int exitCount;
	private List<String> staffIds;

	public AddSubtaskCmd(String taskId, List<String> staffIds, String exitType,
			int exitCount) {
		this.taskId = taskId;
		this.exitType = exitType;
		this.exitCount = exitCount;
		this.staffIds = staffIds;
	}

	public AddSubtaskCmd(String processId, String nodeId,
			List<String> staffIds, String exitType, int exitCount) {
		this.processId = processId;
		this.nodeId = nodeId;
		this.exitType = exitType;
		this.exitCount = exitCount;
		this.staffIds = staffIds;
	}

	public Void execute(Environment environment) throws Exception {
		Session session = (Session) environment.get(Session.class);

		TaskImpl task = null;
		if (null != this.taskId) {
			task = (TaskImpl) session.get(TaskImpl.class, this.taskId);
			if (null == task) {
				throw new IFinFlowException(103003,
						new Object[] { this.taskId });
			}
			if (null != task.getSuperTask()) {
				task = task.getSuperTask();
			}
		} else if (null != this.processId) {
			task = (TaskImpl) session
					.createCriteria(TaskImpl.class)
					.add(Restrictions.eq("processId", this.processId))
					.add(Restrictions.eq("activityName", this.nodeId))
					.add(Restrictions.in(
							"status",
							new Integer[] { Integer.valueOf(1),
									Integer.valueOf(2) }))
					.add(Restrictions.isNull("superTask")).uniqueResult();

			if (null == task) {
				throw new IFinFlowException(103056, new Object[] {
						this.processId, this.nodeId });
			}
		}

		if (task.getAssignMode() != 3) {
			throw new IFinFlowException(103055, new Object[] { task.getDbid() });
		}

		HistoryTaskImpl htask = (HistoryTaskImpl) session.load(
				HistoryTaskImpl.class, task.getDbid());
		HistoryProcessInstance hpi = htask.getHistoryProcessInstance();

		List<IWfStaff> staffs = UserExtendsReference
				.getIdentityAdapter()
				.getStaffsByids((String[]) this.staffIds.toArray(new String[0]));

		TaskImpl subTask = null;
		int i = task.getSubTasks().size();
		for (IWfStaff s : staffs) {
			subTask = new TaskImpl();
			subTask.setDbid(((DbidGenerator) EnvironmentImpl
					.getFromCurrent(DbidGenerator.class)).uuid());

			subTask.setCreateTime(Clock.getTime());
			subTask.setName(task.getName() + " sub " + i++);
			subTask.setAssigneeOnly(s.getStaffId());
			subTask.setTaskDefinition(task.getTaskDefinition());
			subTask.setExecution(task.getExecution());
			subTask.setProcessInstance(task.getProcessInstance());
			subTask.setPackageId(task.getPackageId());
			subTask.setTemplateId(task.getTemplateId());
			subTask.setTemplateVersion(task.getTemplateVersion());
			subTask.setPriority(task.getPriority());
			subTask.setOwnerId(s.getStaffId());
			subTask.setAssignMode(task.getAssignMode());
			subTask.setParticipantMode(task.getParticipantMode());
			subTask.setUserExtString1(task.getUserExtString1());
			subTask.setUserExtString2(task.getUserExtString2());
			subTask.setUserExtString3(task.getUserExtString3());
			subTask.setUserExtString4(task.getUserExtString4());
			subTask.setUserExtString5(task.getUserExtString5());
			subTask.setUserExtString6(task.getUserExtString6());
			subTask.setUserExtString7(task.getUserExtString7());
			subTask.setUserExtString8(task.getUserExtString8());
			subTask.setSuperTask(task);
			subTask.setFormResourceName(task.getFormResourceName());
			session.save(subTask);

			HistoryTaskImpl hist = new HistoryTaskImpl(subTask);
			hist.setSourceUrl(task.getFormResourceName());

			hist.setExecutionId(task.getExecutionId());
			hist.setHistoryProcessInstance(hpi);
			hist.setSubject(hpi.getSubject());
			hist.setRejectAuth(htask.getRejectAuth());
			hist.setSkipAuth(htask.getSkipAuth());
			hist.setOverdue(htask.getOverdue());
			hist.setDeadLine(htask.getDeadLine());
			hist.setNodeId(htask.getNodeId());
			hist.setNodeName(htask.getNodeName());
			hist.setAssignee(subTask.getAssignee());
			hist.setOwnerId(subTask.getOwnerId());
			hist.setOwnerRoleId(subTask.getOwnerRoleId());
			hist.setOwnerUnitId(subTask.getOwnerUnitId());
			hist.setOwnerGroupId(subTask.getOwnerGroupId());

			hist.setOwnerName(subTask.getOwnerName());

			hist.setSuperTaskId(task.getDbid());
			session.save(hist);
		}

		if ((StringUtils.isNotBlank(this.exitType)) && (this.exitCount > 0)) {
			MultiTaskExists te = (MultiTaskExists) session.get(
					MultiTaskExists.class, task.getDbid());

			if (te == null) {
				te = new MultiTaskExists();
				te.setTaskId(task.getDbid());
			}
			te.setExitCount(this.exitCount);
			te.setExitType(this.exitType);
			session.save(te);
		}

		return null;
	}
}
