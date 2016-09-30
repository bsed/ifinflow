package org.jbpm.api.history;

import java.util.Date;
import java.util.List;

public abstract interface HistoryProcessInstanceQuery {
	public static final String PROPERTY_STARTTIME = "startTime";
	public static final String PROPERTY_ENDTIME = "endTime";
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_STATE = "state";
	public static final String PROPERTY_DURATION = "duration";
	public static final String PROPERTY_KEY = "key";

	public abstract HistoryProcessInstanceQuery processInstanceId(
			String paramString);

	public abstract HistoryProcessInstanceQuery processDefinitionId(
			String paramString);

	public abstract HistoryProcessInstanceQuery processInstanceKey(
			String paramString);

	public abstract HistoryProcessInstanceQuery state(String paramString);

	public abstract HistoryProcessInstanceQuery orderAsc(String paramString);

	public abstract HistoryProcessInstanceQuery orderDesc(String paramString);

	public abstract HistoryProcessInstanceQuery page(int paramInt1,
			int paramInt2);

	public abstract HistoryProcessInstanceQuery ended();

	public abstract HistoryProcessInstanceQuery endedBefore(Date paramDate);

	public abstract HistoryProcessInstanceQuery endedAfter(Date paramDate);

	public abstract List<HistoryProcessInstance> list();

	public abstract HistoryProcessInstance uniqueResult();

	public abstract long count();

	public abstract HistoryProcessInstanceQuery processId(String paramString);

	public abstract HistoryProcessInstanceQuery packageId(String paramString);

	public abstract HistoryProcessInstanceQuery templeteId(String paramString);

	public abstract HistoryProcessInstanceQuery templeteVersion(int paramInt);
}
