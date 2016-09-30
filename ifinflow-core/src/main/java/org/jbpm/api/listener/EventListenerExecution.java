package org.jbpm.api.listener;

import org.jbpm.api.model.OpenExecution;

public abstract interface EventListenerExecution extends OpenExecution {
	public abstract void setPriority(int paramInt);
}
