package org.jbpm.api.model;

import java.util.Map;
import java.util.Set;
import org.jbpm.api.Execution;

public abstract interface OpenExecution extends Execution {
	public abstract Activity getActivity();

	public abstract void setState(String paramString);

	public abstract OpenExecution getSubProcessInstance();

	public abstract Object getVariable(String paramString);

	public abstract void setVariable(String paramString, Object paramObject);

	public abstract void setVariables(Map<String, ?> paramMap);

	public abstract boolean hasVariable(String paramString);

	public abstract boolean removeVariable(String paramString);

	public abstract void removeVariables();

	public abstract boolean hasVariables();

	public abstract Set<String> getVariableKeys();

	public abstract Map<String, ?> getVariables();

	public abstract void createVariable(String paramString, Object paramObject);

	public abstract void setPriority(int paramInt);

	public abstract OpenProcessInstance getProcessInstance();

	public abstract OpenExecution getParent();

	public abstract OpenExecution getExecution(String paramString);

	public abstract OpenExecution findActiveExecutionIn(String paramString);
}
