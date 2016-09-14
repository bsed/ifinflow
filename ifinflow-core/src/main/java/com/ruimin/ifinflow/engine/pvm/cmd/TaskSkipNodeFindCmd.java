package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.util.DefinitionUtil;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.session.RepositorySession;
import org.jbpm.pvm.internal.task.TaskImpl;

public class TaskSkipNodeFindCmd extends AbstractCommand<List<String>> {
	private static final long serialVersionUID = 1L;
	private String taskId;

	public TaskSkipNodeFindCmd(String taskId) {
		this.taskId = taskId;
	}

	public List<String> execute(Environment environment) throws Exception {
		if (StringUtils.isEmpty(this.taskId)) {
			throw new IFinFlowException(109016, new Object[0]);
		}

		DbSession dbSession = (DbSession) environment.get(DbSession.class);
		TaskImpl task = (TaskImpl) dbSession.get(TaskImpl.class, this.taskId);
		if (task == null) {
			throw new IFinFlowException(103003, new Object[] { this.taskId });
		}

		HistoryTaskImpl hisTask = (HistoryTaskImpl) dbSession.get(
				HistoryTaskImpl.class, this.taskId);

		if (hisTask == null) {
			throw new IFinFlowException(103003, new Object[] { this.taskId });
		}

		if (hisTask.getStatus().intValue() != 2) {
			throw new IFinFlowException(103007, new Object[] { this.taskId });
		}
		List<String> result = new ArrayList();

		if (DefinitionUtil.isSkipOut(hisTask.getSkipAuth())) {
			RepositorySession repositorySession = (RepositorySession) environment
					.get(RepositorySession.class);

			ProcessDefinitionImpl pd = repositorySession
					.findProcessDefinitionById(task.getProcessInstance()
							.getProcessDefinitionId());

			if (!pd.hasActivity(task.getActivityName())) {
				pd = DefinitionUtil.getSuperDefinitionImpl(task.getExecution());
			}

			ActivityImpl act = pd.getActivity(task.getActivityName());

			DefinitionUtil.getNextNodeList(act, pd, result);

			if (result.size() <= 1) {
				throw new IFinFlowException(109009, new Object[0]);
			}

			if (task.getActivityName().equals(
					((String) result.get(0)).substring(0,
							((String) result.get(0)).indexOf(";")))) {
				result.remove(0);
			}
		} else {
			throw new IFinFlowException(109010, new Object[0]);
		}

		return getParentExecSkipList(task, result);
	}

	private List<String> getParentExecSkipList(TaskImpl task,
			List<String> result) {
		ExecutionImpl parentExecution = task.getExecution()
				.getSuperProcessExecution();

		if (parentExecution == null) {
			return result;
		}
		ProcessDefinitionImpl pd = parentExecution.getProcessDefinition();
		DefinitionUtil.getNextNodeList(parentExecution.getActivity(), pd,
				result);

		return result;
	}
}
