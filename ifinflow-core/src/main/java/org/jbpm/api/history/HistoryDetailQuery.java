package org.jbpm.api.history;

import java.util.Date;
import java.util.List;

public abstract interface HistoryDetailQuery {
	public static final String PROPERTY_USERID = "userId";
	public static final String PROPERTY_TIME = "time";

	public abstract HistoryDetailQuery processInstanceId(String paramString);

	public abstract HistoryDetailQuery activityInstanceId(String paramString);

	public abstract HistoryDetailQuery taskId(String paramString);

	public abstract HistoryDetailQuery timeAfter(Date paramDate);

	public abstract HistoryDetailQuery timeBefore(Date paramDate);

	public abstract HistoryDetailQuery comments();

	public abstract HistoryDetailQuery orderAsc(String paramString);

	public abstract HistoryDetailQuery orderDesc(String paramString);

	public abstract HistoryDetailQuery page(int paramInt1, int paramInt2);

	public abstract List<HistoryDetail> list();

	public abstract HistoryDetail uniqueResult();

	public abstract long count();
}
