package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.criterion.type.NullableType;
import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class SimpleExpression implements Criterion {
	private static final long serialVersionUID = -5145639428882010919L;
	private final String propertyName;
	private final Object value;
	private final String op;

	protected SimpleExpression(String propertyName, Object value, String op) {
		this.propertyName = propertyName;
		this.value = value;
		this.op = op;
	}

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		StringBuffer fragment = new StringBuffer();
		fragment.append(this.propertyName).append(" ").append(this.op)
				.append(" ");
		fragment.append("?");
		return fragment.toString();
	}

	public TypedValue[] getTypedValues(Criteria criteria)
			throws IFinFlowException {
		NullableType type = null;
		if (this.value.getClass().getName().equals("java.lang.Integer")) {
			type = Criteria.INTEGER;
		} else if (this.value.getClass().getName().equals("java.lang.Long")) {
			type = Criteria.LONG;
		} else if (this.value.getClass().getName().equals("java.lang.String")) {
			type = Criteria.STRING;
		} else if (this.value.getClass().getName().equals("java.lang.Boolean")) {
			type = Criteria.BOOLEAN;
		} else if (this.value.getClass().getName().equals("java.util.Date")) {
			type = Criteria.DATE;
		}
		return new TypedValue[] { new TypedValue(type, this.value) };
	}

	public String toStaticSqlString() throws IFinFlowException {
		StringBuffer expr = new StringBuffer();
		expr.append(this.propertyName).append(" ").append(this.op).append(" ");
		expr.append(CriterionHelper.getStringRepresentation(this.value));
		return expr.toString();
	}

	public String toString() {
		return this.propertyName + " " + getOp() + " " + this.value;
	}

	protected final String getOp() {
		return this.op;
	}
}
