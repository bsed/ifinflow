package org.jbpm.api.task;

import java.io.Serializable;

public abstract interface Swimlane extends Serializable {
	public abstract String getId();

	public abstract String getName();

	public abstract String getAssignee();
}
