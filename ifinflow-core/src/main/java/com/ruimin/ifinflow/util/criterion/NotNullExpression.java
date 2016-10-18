package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.StringHelper;
import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class NotNullExpression implements Criterion {
	private static final long serialVersionUID = -2846219395195061228L;
	private final String propertyName;
	private static final TypedValue[] NO_VALUES = new TypedValue[0];

	protected NotNullExpression(String propertyName) {
		this.propertyName = propertyName;
	}

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		String result = StringHelper.join(" or ", StringHelper.suffix(
				new String[] { this.propertyName }, " is not null"));

		return result;
	}

	public TypedValue[] getTypedValues(Criteria criteria)
			throws IFinFlowException {
		return NO_VALUES;
	}

	public String toStaticSqlString() throws IFinFlowException {
		String result = StringHelper.join(" or ", StringHelper.suffix(
				new String[] { this.propertyName }, " is not null"));

		return result;
	}

	public String toString() {
		return this.propertyName + " is not null";
	}
}
