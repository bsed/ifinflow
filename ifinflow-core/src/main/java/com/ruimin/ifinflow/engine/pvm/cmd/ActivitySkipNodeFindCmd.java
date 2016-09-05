package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.util.DefinitionUtil;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.ArrayList;
import java.util.List;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.history.HistoryActivityInstanceQuery;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.query.HistoryActivityInstanceQueryImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.session.RepositorySession;

public class ActivitySkipNodeFindCmd extends AbstractCommand<List<String>> {
	private static final long serialVersionUID = 1L;
	private String activityId;

	public ActivitySkipNodeFindCmd(String activityId) {
		this.activityId = activityId;
	}

	public List<String> execute(Environment environment) throws Exception {
		DbSession dbSession = (DbSession) environment.get(DbSession.class);

		HistoryActivityInstanceImpl hisact = (HistoryActivityInstanceImpl) dbSession
				.createHistoryActivityInstanceQuery()
				.activityName(this.activityId).status(" =1").uniqueResult();

		List<String> result = new ArrayList();

		if (DefinitionUtil.isSkipOut(hisact.getSkipAuth())) {
			RepositorySession repositorySession = (RepositorySession) environment
					.get(RepositorySession.class);

			ProcessDefinitionImpl pd = repositorySession
					.findProcessDefinitionById(hisact
							.getHistoryProcessInstance()
							.getProcessDefinitionId());

			ActivityImpl act = hisact.getActivity();
			DefinitionUtil.getNextNodeList(act, pd, result);
			if (result.size() <= 2) {
				throw new IFinFlowException(109009, new Object[0]);
			}

			result.remove(0);

			result.remove(0);
		} else {
			throw new IFinFlowException(109010, new Object[0]);
		}
		return result;
	}
}
