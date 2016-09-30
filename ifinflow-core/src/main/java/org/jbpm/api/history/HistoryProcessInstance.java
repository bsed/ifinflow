package org.jbpm.api.history;

import java.util.Date;

public abstract interface HistoryProcessInstance {
	public static final String STATE_ENDED = "ended";
	public static final String STATE_ACTIVE = "active";

	public abstract String getProcessInstanceId();

	public abstract String getProcessDefinitionId();

	public abstract String getKey();

	public abstract String getState();

	public abstract Date getStartTime();

	public abstract Date getEndTime();

	public abstract Long getDuration();

	public abstract String getEndActivityName();

	public abstract String getDbid();

	public abstract String getPackageId();

	public abstract String getSubject();

	public abstract Integer getStatus();

	public abstract String getInitiatorId();

	public abstract String getInitiatorName();

	public abstract String getOwnerUnitId();

	public abstract String getInitiatorRole();

	public abstract String getTemplateId();

	public abstract String getTempleteName();

	public abstract Integer getTempleteVersion();

	public abstract Integer getOverdue();

	public abstract Date getDeadLine();
}
