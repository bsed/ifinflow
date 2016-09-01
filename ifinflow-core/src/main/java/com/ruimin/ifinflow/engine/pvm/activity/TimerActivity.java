package com.ruimin.ifinflow.engine.pvm.activity;

import java.util.List;
import java.util.Map;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.activity.ExternalActivityBehaviour;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;

public class TimerActivity implements ExternalActivityBehaviour {
	private static final long serialVersionUID = 1L;

	public void execute(ActivityExecution execution) throws Exception {
		execute((ExecutionImpl) execution);
	}

	public void execute(ExecutionImpl execution) throws Exception {
		execution.waitForSignal();
	}

	public void signal(ActivityExecution execution, String signalName,
			Map<String, ?> parameters) throws Exception {
		signal((ExecutionImpl) execution, signalName, parameters);
	}

	public void signal(ExecutionImpl execution, String signalName,
			Map<String, ?> parameters) throws Exception {
		ActivityImpl activity = execution.getActivity();

		if (parameters != null) {
			execution.setVariables(parameters);
		}

		execution.fire(signalName, activity);

		Transition transition = null;
		if ((signalName == null) && (activity.getOutgoingTransitions() != null)
				&& (activity.getOutgoingTransitions().size() == 1)) {
			transition = (Transition) activity.getOutgoingTransitions().get(0);
		} else {
			transition = activity.findOutgoingTransition(signalName);
		}

		execution.historyActivityEnd(signalName);
		execution.take(transition);
	}
}
