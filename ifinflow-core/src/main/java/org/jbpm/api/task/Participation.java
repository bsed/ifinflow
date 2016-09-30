package org.jbpm.api.task;

public abstract interface Participation {
	public static final String CANDIDATE = "candidate";
	public static final String OWNER = "owner";
	public static final String CLIENT = "client";
	public static final String VIEWER = "viewer";
	public static final String REPLACED_ASSIGNEE = "replaced-assignee";

	public abstract String getId();

	public abstract String getUserId();

	public abstract String getGroupId();

	public abstract String getType();
}
