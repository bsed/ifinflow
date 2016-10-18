package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.StringHelper;
import com.ruimin.ifinflow.util.criterion.type.Type;
import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class SQLCriterion implements Criterion {
	private static final long serialVersionUID = -7412215850159136543L;
	private final String sql;
	private final TypedValue[] typedValues;

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		return StringHelper.replace(this.sql, "{alias}", "");
	}

	public TypedValue[] getTypedValues(Criteria criteria)
			throws IFinFlowException {
		return this.typedValues;
	}

	public String toStaticSqlString() throws IFinFlowException {
		return this.sql;
	}

	public String toString() {
		return this.sql;
	}

	protected SQLCriterion(String sql, Object[] values, Type[] types) {
		this.sql = sql;
		this.typedValues = new TypedValue[values.length];
		for (int i = 0; i < this.typedValues.length; i++) {
			this.typedValues[i] = new TypedValue(types[i], values[i]);
		}
	}
}
