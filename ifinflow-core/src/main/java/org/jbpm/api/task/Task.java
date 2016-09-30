package org.jbpm.api.task;

import java.io.Serializable;
import java.util.Date;

public abstract interface Task extends Serializable {
	public static final String STATE_OPEN = "open";
	public static final String STATE_COMPLETED = "completed";
	public static final String STATE_SUSPENDED = "suspended";

	public abstract String getId();

	public abstract String getName();

	public abstract void setName(String paramString);

	public abstract String getDescription();

	public abstract void setDescription(String paramString);

	public abstract String getAssignee();

	public abstract void setAssignee(String paramString);

	public abstract Date getCreateTime();

	public abstract Date getDuedate();

	public abstract void setDuedate(Date paramDate);

	public abstract int getPriority();

	public abstract void setPriority(int paramInt);

	public abstract Integer getProgress();

	public abstract void setProgress(Integer paramInteger);

	public abstract String getExecutionId();

	public abstract String getActivityName();

	public abstract String getFormResourceName();
}
