package org.jbpm.jpdl.internal.activity;

import org.jbpm.api.JbpmException;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;

public class DecisionHandlerActivity extends JpdlActivity {
	private static final long serialVersionUID = 1L;
	UserCodeReference decisionHandlerReference;

	public void execute(ActivityExecution execution) {
		execute((ExecutionImpl) execution);
	}

	public void execute(ExecutionImpl execution) {
		Activity activity = execution.getActivity();

		String transitionName = null;

		DecisionHandler decisionHandler = null;

		if (this.decisionHandlerReference != null) {
			decisionHandler = (DecisionHandler) this.decisionHandlerReference
					.getObject(execution);
		}

		if (decisionHandler == null) {
			throw new JbpmException("no decision handler specified");
		}

		transitionName = decisionHandler.decide(execution);

		Transition transition = activity.getOutgoingTransition(transitionName);
		if (transition == null) {
			throw new JbpmException("handler in decision '"
					+ activity.getName()
					+ "' returned unexisting outgoing transition name: "
					+ transitionName);
		}

		execution.historyDecision(transitionName);

		execution.take(transition);
	}

	public void setDecisionHandlerReference(
			UserCodeReference decisionHandlerReference) {
		this.decisionHandlerReference = decisionHandlerReference;
	}
}
