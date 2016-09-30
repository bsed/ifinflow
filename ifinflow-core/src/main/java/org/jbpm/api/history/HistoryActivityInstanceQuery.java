package org.jbpm.api.history;

import java.util.Date;
import java.util.List;

public abstract interface HistoryActivityInstanceQuery {
	public static final String PROPERTY_STARTTIME = "startTime";
	public static final String PROPERTY_ENDTIME = "endTime";
	public static final String PROPERTY_EXECUTIONID = "executionId";
	public static final String PROPERTY_ACTIVITYNAME = "activityName";
	public static final String PROPERTY_DURATION = "duration";

	public abstract HistoryActivityInstanceQuery processDefinitionId(
			String paramString);

	public abstract HistoryActivityInstanceQuery executionId(String paramString);

	public abstract HistoryActivityInstanceQuery processInstanceId(
			String paramString);

	public abstract HistoryActivityInstanceQuery startedAfter(Date paramDate);

	public abstract HistoryActivityInstanceQuery startedBefore(Date paramDate);

	public abstract HistoryActivityInstanceQuery activityName(String paramString);

	public abstract HistoryActivityInstanceQuery tookLongerThen(long paramLong);

	public abstract HistoryActivityInstanceQuery tookLessThen(long paramLong);

	public abstract HistoryActivityInstanceQuery orderAsc(String paramString);

	public abstract HistoryActivityInstanceQuery orderDesc(String paramString);

	public abstract HistoryActivityInstanceQuery page(int paramInt1,
			int paramInt2);

	public abstract List<HistoryActivityInstance> list();

	public abstract HistoryActivityInstance uniqueResult();

	public abstract long count();

	public abstract HistoryActivityInstanceQuery processId(String paramString);

	public abstract HistoryActivityInstanceQuery status(String paramString);

	public abstract HistoryActivityInstanceQuery historyProcessInstance(
			String paramString);
}
