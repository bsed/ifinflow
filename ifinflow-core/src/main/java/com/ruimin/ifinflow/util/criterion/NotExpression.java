package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class NotExpression implements Criterion {
	private static final long serialVersionUID = 5083620472459339653L;
	private Criterion criterion;

	protected NotExpression(Criterion criterion) {
		this.criterion = criterion;
	}

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		return "not " + this.criterion.toSqlString(criteria);
	}

	public TypedValue[] getTypedValues(Criteria criteria)
			throws IFinFlowException {
		return this.criterion.getTypedValues(criteria);
	}

	public String toStaticSqlString() throws IFinFlowException {
		StringBuffer expr = new StringBuffer();
		expr.append("not ").append(this.criterion.toStaticSqlString());
		return expr.toString();
	}

	public String toString() {
		return "not " + this.criterion.toString();
	}
}
