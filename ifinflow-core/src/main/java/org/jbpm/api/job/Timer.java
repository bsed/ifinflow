package org.jbpm.api.job;

public abstract interface Timer extends Job {
	public abstract String getSignalName();

	public abstract String getEventName();

	public abstract String getRepeat();
}
