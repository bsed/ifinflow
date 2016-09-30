package org.jbpm.jpdl.internal.activity;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.pvm.internal.el.Expression;
import org.jbpm.pvm.internal.el.UelValueExpression;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.wire.Descriptor;
import org.jbpm.pvm.internal.wire.WireContext;

public class AssignActivity extends JpdlAutomaticActivity {
	private static final long serialVersionUID = 1L;
	protected Expression fromExpression;
	protected String fromVariable;
	protected Descriptor fromDescriptor;
	protected UelValueExpression toExpression;
	protected String toVariable;

	void perform(OpenExecution execution) throws Exception {
		Object value = null;

		if (this.fromExpression != null) {
			value = this.fromExpression.evaluate(execution);
		} else if (this.fromVariable != null) {
			value = execution.getVariable(this.fromVariable);
		} else if (this.fromDescriptor != null) {
			value = WireContext.create(this.fromDescriptor,
					(ExecutionImpl) execution);
		}

		if (this.toExpression != null) {
			this.toExpression.setValue(execution, value);
		} else {
			execution.setVariable(this.toVariable, value);
		}
	}

	public void setFromDescriptor(Descriptor descriptor) {
		this.fromDescriptor = descriptor;
	}

	public void setFromExpression(Expression expression) {
		this.fromExpression = expression;
	}

	public void setFromVariable(String variableName) {
		this.fromVariable = variableName;
	}

	public void setToExpression(UelValueExpression expression) {
		this.toExpression = expression;
	}

	public void setToVariable(String variableName) {
		this.toVariable = variableName;
	}
}
