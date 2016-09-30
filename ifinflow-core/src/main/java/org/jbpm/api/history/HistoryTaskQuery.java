package org.jbpm.api.history;

import java.util.Date;
import java.util.List;

public abstract interface HistoryTaskQuery {
	public static final String PROPERTY_ID = "dbid";
	public static final String PROPERTY_CREATETIME = "createTime";
	public static final String PROPERTY_ENDTIME = "endTime";
	public static final String PROPERTY_EXECUTIONID = "executionId";
	public static final String PROPERTY_OUTCOME = "outcome";
	public static final String PROPERTY_ASSIGNEE = "assignee";
	public static final String PROPERTY_STATE = "state";
	public static final String PROPERTY_DURATION = "duration";

	public abstract HistoryTaskQuery taskId(String paramString);

	public abstract HistoryTaskQuery executionId(String paramString);

	public abstract HistoryTaskQuery assignee(String paramString);

	public abstract HistoryTaskQuery state(String paramString);

	public abstract HistoryTaskQuery outcome(String paramString);

	public abstract HistoryTaskQuery orderAsc(String paramString);

	public abstract HistoryTaskQuery orderDesc(String paramString);

	public abstract HistoryTaskQuery page(int paramInt1, int paramInt2);

	public abstract HistoryTaskQuery startedAfter(Date paramDate);

	public abstract HistoryTaskQuery startedBefore(Date paramDate);

	public abstract HistoryTaskQuery tookLessThen(long paramLong);

	public abstract HistoryTaskQuery tookLongerThen(long paramLong);

	public abstract List<HistoryTask> list();

	public abstract HistoryTask uniqueResult();

	public abstract long count();

	public abstract HistoryTaskQuery nodeId(String paramString);

	public abstract HistoryTaskQuery processId(String paramString);

	public abstract HistoryTaskQuery statusCondetion(String paramString);

	public abstract HistoryTaskQuery ownerId(String paramString);
}
