package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.util.StringHelper;

public class Junction implements Criterion {
	private static final long serialVersionUID = 2295460811927922880L;
	private final ArrayList<Criterion> criteria = new ArrayList();
	private final String op;

	protected Junction(String op) {
		this.op = op;
	}

	public Junction add(Criterion criterion) {
		this.criteria.add(criterion);
		return this;
	}

	public String getOp() {
		return this.op;
	}

	public TypedValue[] getTypedValues(Criteria crit) throws IFinFlowException {
		ArrayList<TypedValue> typedValues = new ArrayList();

		Iterator<Criterion> iter = this.criteria.iterator();

		while (iter.hasNext()) {

			TypedValue[] subvalues = ((Criterion) iter.next())
					.getTypedValues(crit);

			for (int i = 0; i < subvalues.length; i++) {

				typedValues.add(subvalues[i]);
			}
		}
		return typedValues.toArray(new TypedValue[typedValues.size()]);
	}

	public String toSqlString(Criteria crit) throws IFinFlowException {

		if (this.criteria.size() == 0) {
			return "1=1";
		}
		StringBuffer buffer = new StringBuffer().append('(');
		Iterator<Criterion> iter = this.criteria.iterator();
		while (iter.hasNext()) {
			buffer.append(((Criterion) iter.next()).toSqlString(crit));
			if (iter.hasNext())
				buffer.append(' ').append(this.op).append(' ');
		}
		buffer.append(')');
		return buffer.toString();

	}

	public String toStaticSqlString() throws IFinFlowException {

		if (this.criteria.size() == 0) {
			return "1=1";
		}
		StringBuffer expr = new StringBuffer();
		int count = this.criteria.size();
		expr.append("(");
		for (int i = 0; i < count; i++) {
			expr.append(CriterionHelper.getStringRepresentation(this.criteria
					.get(i)));
			if (i + 1 < count) {
				expr.append(" ").append(this.op).append(" ");
			}
		}
		expr.append(")");
		return expr.toString();
	}

	public String toString() {
		return '(' + StringHelper.join(
				new StringBuilder().append(' ').append(this.op).append(' ')
						.toString(), this.criteria.iterator()) + ')';
	}
}