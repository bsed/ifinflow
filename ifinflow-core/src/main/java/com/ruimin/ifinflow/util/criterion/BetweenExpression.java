package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.criterion.type.NullableType;
import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.hibernate.util.StringHelper;

public class BetweenExpression implements Criterion {
	private static final long serialVersionUID = -3451279905726747479L;
	private final String propertyName;
	private final Object lo;
	private final Object hi;

	protected BetweenExpression(String propertyName, Object lo, Object hi) {
		this.propertyName = propertyName;
		this.lo = lo;
		this.hi = hi;
	}

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		return StringHelper.join(" and ", StringHelper.suffix(
				new String[] { this.propertyName }, " between ? and ?"));
	}

	public TypedValue[] getTypedValues(Criteria criteria)
			throws IFinFlowException {
		NullableType typelo = null;
		if (this.lo.getClass().getName().equals("java.lang.Integer")) {
			typelo = Criteria.INTEGER;
		} else if (this.lo.getClass().getName().equals("java.lang.Long")) {
			typelo = Criteria.LONG;
		} else if (this.lo.getClass().getName().equals("java.lang.String")) {
			typelo = Criteria.STRING;
		} else if (this.lo.getClass().getName().equals("java.lang.Boolean")) {
			typelo = Criteria.BOOLEAN;
		} else if (this.lo.getClass().getName().equals("java.util.Date")) {
			typelo = Criteria.DATE;
		}
		NullableType typehi = null;
		if (this.hi.getClass().getName().equals("java.lang.Integer")) {
			typehi = Criteria.INTEGER;
		} else if (this.hi.getClass().getName().equals("java.lang.Long")) {
			typehi = Criteria.LONG;
		} else if (this.hi.getClass().getName().equals("java.lang.String")) {
			typehi = Criteria.STRING;
		} else if (this.hi.getClass().getName().equals("java.lang.Boolean")) {
			typehi = Criteria.BOOLEAN;
		} else if (this.hi.getClass().getName().equals("java.util.Date")) {
			typehi = Criteria.DATE;
		}
		return new TypedValue[] { new TypedValue(typelo, this.lo),
				new TypedValue(typehi, this.hi) };
	}

	public String toString() {
		return this.propertyName + " between " + this.lo + " and " + this.hi;
	}

	public String toStaticSqlString() throws IFinFlowException {
		StringBuffer expr = new StringBuffer();
		expr.append(this.propertyName).append(" between ");
		expr.append(CriterionHelper.getStringRepresentation(this.lo)).append(
				" and ");
		expr.append(CriterionHelper.getStringRepresentation(this.hi));
		return expr.toString();
	}
}
