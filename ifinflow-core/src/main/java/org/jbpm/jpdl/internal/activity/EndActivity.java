package org.jbpm.jpdl.internal.activity;

import java.util.List;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;

public class EndActivity extends JpdlActivity {
	private static final long serialVersionUID = 1L;
	protected boolean endProcessInstance = true;
	protected String state = null;

	public void execute(ActivityExecution execution) {
		execute((ExecutionImpl) execution);
	}

	public void execute(ExecutionImpl execution) {
		Activity activity = execution.getActivity();
		List<? extends Transition> outgoingTransitions = activity
				.getOutgoingTransitions();
		ActivityImpl parentActivity = (ActivityImpl) activity
				.getParentActivity();

		if ((parentActivity != null)
				&& ("group".equals(parentActivity.getType()))) {

			if ((outgoingTransitions != null)
					&& (outgoingTransitions.size() == 1)) {

				Transition outgoingTransition = (Transition) outgoingTransitions
						.get(0);

				execution.take(outgoingTransition);
			} else {
				execution.setActivity(parentActivity);
				execution.signal();
			}
		} else {
			ExecutionImpl executionToEnd = null;
			if (this.endProcessInstance) {
				executionToEnd = execution.getProcessInstance();
				executionToEnd.setActivity(execution.getActivity());
			} else {
				executionToEnd = execution;
			}

			if (this.state == null) {
				executionToEnd.end();
			} else {
				executionToEnd.end(this.state);
			}
		}
	}

	public void setEndProcessInstance(boolean endProcessInstance) {
		this.endProcessInstance = endProcessInstance;
	}

	public void setState(String state) {
		this.state = state;
	}
}
