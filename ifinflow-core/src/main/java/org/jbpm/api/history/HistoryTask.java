package org.jbpm.api.history;

import java.util.Date;

public abstract interface HistoryTask {
	public static final String STATE_COMPLETED = "completed";
	public static final String STATE_OBSOLETE = "obsolete";
	public static final String STATE_REJECTED = "rejected";
	public static final String STATE_SKIPPED = "skipped";
	public static final String STATE_CANCELED = "canceled";

	public abstract String getId();

	public abstract String getExecutionId();

	public abstract Date getCreateTime();

	public abstract Date getEndTime();

	public abstract long getDuration();

	public abstract String getState();

	public abstract String getAssignee();

	public abstract String getOutcome();

	public abstract int getPriority();

	public abstract String getPackageId();

	public abstract String getTemplateId();

	public abstract int getTemplateVersion();

	public abstract String getSubject();

	public abstract String getSourceUrl();

	public abstract Integer getStatus();

	public abstract String getNodeId();

	public abstract String getNodeName();

	public abstract Integer getAssignMode();

	public abstract String getSkipAuth();

	public abstract String getRejectAuth();

	public abstract String getOwnerId();

	public abstract String getOwnerName();

	public abstract String getOwnerUnitId();

	public abstract String getOwnerRoleId();

	public abstract String getExecutorId();

	public abstract String getExecutorName();

	public abstract String getExecutorUnit();

	public abstract String getExecutorRole();

	public abstract Date getTakeDate();

	public abstract Integer getOverdue();

	public abstract Date getDeadLine();

	public abstract String getUserExtString1();

	public abstract String getUserExtString2();

	public abstract String getUserExtString3();

	public abstract String getUserExtString4();

	public abstract String getUserExtString5();

	public abstract String getUserExtString6();

	public abstract String getUserExtString7();

	public abstract String getUserExtString8();
}
