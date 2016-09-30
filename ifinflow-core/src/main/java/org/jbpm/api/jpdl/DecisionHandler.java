package org.jbpm.api.jpdl;

import java.io.Serializable;
import org.jbpm.api.model.OpenExecution;

public abstract interface DecisionHandler extends Serializable {
	public abstract String decide(OpenExecution paramOpenExecution);
}
