package org.jbpm.api.history;

import java.util.Date;
import java.util.List;

public abstract interface HistoryActivityInstance {
	public abstract String getDbid();

	public abstract String getActivityName();

	public abstract Date getStartTime();

	public abstract Date getEndTime();

	public abstract long getDuration();

	public abstract String getExecutionId();

	public abstract List<String> getTransitionNames();

	public abstract Integer getStatus();

	public abstract String getPackageId();

	public abstract String getRejectAuth();

	public abstract String getSkipAuth();

	public abstract String getSouRejectName();

	public abstract Integer getOverdue();

	public abstract Date getDeadLine();

	public abstract boolean isSkipFromHere();

	public abstract boolean isSkipToHere();

	public abstract boolean isRejectFromHere();

	public abstract boolean isRejectToHere();

	public abstract String getTemplateId();

	public abstract int getTemplateVersion();

	public abstract String getNodeName();
}
