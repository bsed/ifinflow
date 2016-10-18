package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.criterion.type.NullableType;
import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.ArrayList;
import org.hibernate.util.StringHelper;

public class InExpression implements Criterion {
	private static final long serialVersionUID = 1853540962498163547L;
	private final String propertyName;
	private final Object[] values;

	protected InExpression(String propertyName, Object[] values) {
		this.propertyName = propertyName;
		this.values = values;
	}

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		String singleValueParam = StringHelper.repeat("?, ", 0) + "?";
		String params = this.values.length > 0 ? StringHelper.repeat(
				new StringBuilder().append(singleValueParam).append(", ")
						.toString(), this.values.length - 1)
				+ singleValueParam : "";

		String cols = StringHelper.join(", ",
				new String[] { this.propertyName });
		return cols + " in (" + params + ')';
	}

	public TypedValue[] getTypedValues(Criteria criteria)
			throws IFinFlowException {
		ArrayList<TypedValue> list = new ArrayList();
		for (int j = 0; j < this.values.length; j++) {
			NullableType type = null;
			if (this.values[j].getClass().getName().equals("java.lang.Integer")) {
				type = Criteria.INTEGER;
			} else if (this.values[j].getClass().getName()
					.equals("java.lang.Long")) {
				type = Criteria.LONG;
			} else if (this.values[j].getClass().getName()
					.equals("java.lang.String")) {
				type = Criteria.STRING;
			} else if (this.values[j].getClass().getName()
					.equals("java.lang.Boolean")) {
				type = Criteria.BOOLEAN;
			} else if (this.values[j].getClass().getName()
					.equals("java.util.Date")) {
				type = Criteria.DATE;
			}
			list.add(new TypedValue(type, this.values[j]));
		}
		return (TypedValue[]) list.toArray(new TypedValue[list.size()]);
	}

	public String toStaticSqlString() throws IFinFlowException {
		StringBuffer expr = new StringBuffer();
		expr.append(this.propertyName).append(" in (");
		for (int i = 0; i < this.values.length; i++) {
			expr.append(CriterionHelper.getStringRepresentation(this.values[i]));
			if (i + 1 < this.values.length) {
				expr.append(", ");
			}
		}
		expr.append(")");
		return expr.toString();
	}

	public String toString() {
		return this.propertyName + " in (" + StringHelper.toString(this.values)
				+ ')';
	}
}
