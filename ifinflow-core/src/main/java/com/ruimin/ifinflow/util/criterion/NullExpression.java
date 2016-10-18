package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.StringHelper;
import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class NullExpression implements Criterion {
	private static final long serialVersionUID = 245936921368860536L;
	private final String propertyName;
	private static final TypedValue[] NO_VALUES = new TypedValue[0];

	protected NullExpression(String propertyName) {
		this.propertyName = propertyName;
	}

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		String result = StringHelper.join(" and ", StringHelper.suffix(
				new String[] { this.propertyName }, " is null"));

		return result;
	}

	public TypedValue[] getTypedValues(Criteria criteria)
			throws IFinFlowException {
		return NO_VALUES;
	}

	public String toStaticSqlString() throws IFinFlowException {
		String result = StringHelper.join(" and ", StringHelper.suffix(
				new String[] { this.propertyName }, " is null"));

		return result;
	}

	public String toString() {
		return this.propertyName + " is null";
	}
}
