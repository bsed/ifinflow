package org.jbpm.api.identity;

public abstract interface Group {
	public static final String TYPE_ROLE = "role_";
	public static final String TYPE_ORG = "org_";

	public abstract String getId();

	public abstract String getName();

	public abstract String getType();
}
