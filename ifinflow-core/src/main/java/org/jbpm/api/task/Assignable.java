package org.jbpm.api.task;

public abstract interface Assignable {
	public abstract void setAssignee(String paramString);

	public abstract void addCandidateUser(String paramString);

	public abstract void addCandidateGroup(String paramString);

	public abstract void setAssigneeOnly(String paramString);
}
