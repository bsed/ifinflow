package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class LogicalExpression implements Criterion {
	private static final long serialVersionUID = -2044421035289535424L;
	private final Criterion lhs;
	private final Criterion rhs;
	private final String op;

	protected LogicalExpression(Criterion lhs, Criterion rhs, String op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}

	public TypedValue[] getTypedValues(Criteria criteria)
			throws IFinFlowException {
		TypedValue[] lhstv = this.lhs.getTypedValues(criteria);
		TypedValue[] rhstv = this.rhs.getTypedValues(criteria);
		TypedValue[] result = new TypedValue[lhstv.length + rhstv.length];
		System.arraycopy(lhstv, 0, result, 0, lhstv.length);
		System.arraycopy(rhstv, 0, result, lhstv.length, rhstv.length);
		return result;
	}

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		return '(' + this.lhs.toSqlString(criteria) + ' ' + getOp() + ' '
				+ this.rhs.toSqlString(criteria) + ')';
	}

	public String getOp() {
		return this.op;
	}

	public String toStaticSqlString() throws IFinFlowException {
		StringBuffer expr = new StringBuffer();
		expr.append("(").append(this.lhs.toStaticSqlString());
		expr.append(" ").append(this.op).append(" ");
		expr.append(this.rhs.toStaticSqlString()).append(")");
		return expr.toString();
	}

	public String toString() {
		return this.lhs.toString() + ' ' + getOp() + ' ' + this.rhs.toString();
	}
}
