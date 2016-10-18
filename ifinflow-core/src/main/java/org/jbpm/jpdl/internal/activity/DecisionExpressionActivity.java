package org.jbpm.jpdl.internal.activity;

import org.jbpm.api.JbpmException;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.el.Expression;
import org.jbpm.pvm.internal.model.ExecutionImpl;

public class DecisionExpressionActivity extends JpdlActivity {
	private static final long serialVersionUID = 1L;
	protected Expression expression;

	public void execute(ActivityExecution execution) {
		execute((ExecutionImpl) execution);
	}

	public void execute(ExecutionImpl execution) {
		Activity activity = execution.getActivity();
		String transitionName = null;

		Object result = this.expression.evaluate(execution);
		if ((result != null) && (!(result instanceof String))) {

			throw new JbpmException("expression '" + this.expression
					+ "' in decision '" + activity.getName() + "' returned "
					+ result.getClass().getName()
					+ " instead of a transitionName (String): " + result);
		}
		transitionName = (String) result;

		Transition transition = activity.getOutgoingTransition(transitionName);
		if (transition == null) {
			throw new JbpmException("expression '" + this.expression
					+ "' in decision '" + activity.getName()
					+ "' returned unexisting outgoing transition name: "
					+ transitionName);
		}

		execution.historyDecision(transitionName);

		execution.take(transition);
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}
