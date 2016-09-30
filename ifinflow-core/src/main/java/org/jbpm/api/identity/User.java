package org.jbpm.api.identity;

public abstract interface User {
	public abstract String getId();

	public abstract String getGivenName();

	public abstract String getFamilyName();

	public abstract String getBusinessEmail();
}
