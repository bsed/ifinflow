package org.jbpm.api.activity;

import org.jbpm.api.model.OpenExecution;

public abstract interface ActivityExecution extends OpenExecution {
	public abstract String getActivityName();

	public abstract void waitForSignal();

	public abstract void takeDefaultTransition();

	public abstract void take(String paramString);

	public abstract void execute(String paramString);

	public abstract void end();

	public abstract void end(String paramString);

	public abstract void setPriority(int paramInt);
}
