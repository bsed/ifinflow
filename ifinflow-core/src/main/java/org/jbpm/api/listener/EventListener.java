package org.jbpm.api.listener;

import java.io.Serializable;

public abstract interface EventListener extends Serializable {
	public abstract void notify(
			EventListenerExecution paramEventListenerExecution)
			throws Exception;
}
