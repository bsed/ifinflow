package com.ruimin.ifinflow.util;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.hibernate.Session;
import org.jbpm.api.HistoryService;
import org.jbpm.api.history.HistoryActivityInstanceQuery;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;

public class ActivityUtil {
	public static HistoryActivityInstanceImpl getLastHistoryActIns(
			HistoryActivityInstanceImpl hisActIns) {
		HistoryActivityInstanceImpl lastHAI = (HistoryActivityInstanceImpl) hisActIns
				.getLastHistoryActivityInstance();

		if ((lastHAI == null) && (hisActIns.getLastActivityDbid() != null)) {
			lastHAI = (HistoryActivityInstanceImpl) ((Session) EnvironmentImpl
					.getFromCurrent(Session.class)).get(
					HistoryActivityInstanceImpl.class,
					hisActIns.getLastActivityDbid());

		} else {
			lastHAI = (HistoryActivityInstanceImpl) ((HistoryService) EnvironmentImpl
					.getFromCurrent(HistoryService.class))
					.createHistoryActivityInstanceQuery()
					.activityName(hisActIns.getActivityName())
					.processId(hisActIns.getHistoryProcessInstance().getDbid())
					.orderDesc("endTime").page(0, 1).uniqueResult();
		}

		return lastHAI;
	}

	public static HistoryActivityInstanceImpl getHistoryActIns(
			String hisActInsDbid) {
		try {
			return (HistoryActivityInstanceImpl) ((Session) EnvironmentImpl
					.getFromCurrent(Session.class)).get(
					HistoryActivityInstanceImpl.class, hisActInsDbid);

		} catch (Exception e) {
			throw new IFinFlowException(102025, e,
					new Object[] { hisActInsDbid });
		}
	}
}
