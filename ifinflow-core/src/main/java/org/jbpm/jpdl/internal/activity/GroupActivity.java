package org.jbpm.jpdl.internal.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jbpm.api.JbpmException;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.model.ExecutionImpl;

public class GroupActivity extends JpdlExternalActivity {
	private static final long serialVersionUID = 1L;

	public void execute(ActivityExecution execution) {
		execute((ExecutionImpl) execution);
	}

	public void execute(ExecutionImpl execution) {
		Activity activity = execution.getActivity();
		List<Activity> startActivities = findStartActivities(activity);
		ExecutionImpl concurrentRoot;
		if (startActivities.size() == 1) {
			execution.execute((Activity) startActivities.get(0));
		} else {
			concurrentRoot = null;
			if ("active-root".equals(execution.getState())) {
				concurrentRoot = execution;
			} else if ("active-concurrent".equals(execution.getState())) {
				concurrentRoot = execution.getParent();
			} else {
				throw new JbpmException("illegal state");
			}

			for (Activity startActivity : startActivities) {
				ExecutionImpl concurrentExecution = concurrentRoot
						.createExecution();
				concurrentExecution.setState("active-concurrent");
				concurrentExecution.execute(startActivity);
			}
		}
	}

	private List<Activity> findStartActivities(Activity activity) {
		List<Activity> startActivities = new ArrayList();
		List<? extends Activity> nestedActivities = activity.getActivities();
		for (Activity nestedActivity : nestedActivities) {
			if ((nestedActivity.getIncomingTransitions() == null)
					|| (nestedActivity.getIncomingTransitions().isEmpty())) {

				startActivities.add(nestedActivity);
			}
		}
		return startActivities;
	}

	public void signal(ActivityExecution execution, String signalName,
			Map<String, ?> parameters) throws Exception {
		signal((ExecutionImpl) execution, signalName, parameters);
	}

	public void signal(ExecutionImpl execution, String signalName,
			Map<String, ?> parameters) throws Exception {
		Transition transition = null;
		Activity activity = execution.getActivity();
		List<? extends Transition> outgoingTransitions = activity
				.getOutgoingTransitions();

		int nbrOfOutgoingTransitions = outgoingTransitions != null ? outgoingTransitions
				.size() : 0;
		if ((signalName == null) && (nbrOfOutgoingTransitions == 1)) {

			transition = (Transition) outgoingTransitions.get(0);
		} else {
			transition = activity.getOutgoingTransition(signalName);
		}

		execution.take(transition);
	}
}
