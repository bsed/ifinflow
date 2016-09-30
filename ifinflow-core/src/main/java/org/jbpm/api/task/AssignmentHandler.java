package org.jbpm.api.task;

import java.io.Serializable;
import org.jbpm.api.model.OpenExecution;

public abstract interface AssignmentHandler extends Serializable {
	public abstract void assign(Assignable paramAssignable,
			OpenExecution paramOpenExecution) throws Exception;
}
