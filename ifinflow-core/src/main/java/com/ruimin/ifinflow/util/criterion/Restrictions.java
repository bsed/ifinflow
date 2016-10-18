package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.ArrayHelper;
import com.ruimin.ifinflow.util.criterion.type.Type;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Restrictions implements Serializable {
	private static final long serialVersionUID = -7218861016911843812L;

	public static SimpleExpression eq(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, "=");
	}

	public static SimpleExpression ne(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, "<>");
	}

	public static SimpleExpression like(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, " like ");
	}

	public static SimpleExpression gt(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, ">");
	}

	public static SimpleExpression lt(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, "<");
	}

	public static SimpleExpression le(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, "<=");
	}

	public static SimpleExpression ge(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, ">=");
	}

	public static Criterion between(String propertyName, Object lo, Object hi) {
		return new BetweenExpression(propertyName, lo, hi);
	}

	public static Criterion in(String propertyName, Object[] values) {
		return new InExpression(propertyName, values);
	}

	public static Criterion in(String propertyName, Collection<?> values) {
		return new InExpression(propertyName, values.toArray());
	}

	public static Criterion isNull(String propertyName) {
		return new NullExpression(propertyName);
	}

	public static Criterion isNotNull(String propertyName) {
		return new NotNullExpression(propertyName);
	}

	public static LogicalExpression and(Criterion lhs, Criterion rhs) {
		return new LogicalExpression(lhs, rhs, "and");
	}

	public static LogicalExpression or(Criterion lhs, Criterion rhs) {
		return new LogicalExpression(lhs, rhs, "or");
	}

	public static Criterion not(Criterion expression) {
		return new NotExpression(expression);
	}

	public static Criterion sqlRestriction(String sql, Object[] values,
			Type[] types) {
		return new SQLCriterion(sql, values, types);
	}

	public static Criterion sqlRestriction(String sql, Object value, Type type) {
		return new SQLCriterion(sql, new Object[] { value },
				new Type[] { type });
	}

	public static Criterion sqlRestriction(String sql) {
		return new SQLCriterion(sql, ArrayHelper.EMPTY_OBJECT_ARRAY,
				ArrayHelper.EMPTY_TYPE_ARRAY);
	}

	public static Conjunction conjunction() {
		return new Conjunction();
	}

	public static Disjunction disjunction() {
		return new Disjunction();
	}

	public static Criterion allEq(Map<?, ?> propertyNameValues) {
		Conjunction conj = conjunction();
		Iterator<?> iter = propertyNameValues.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry me = (Map.Entry) iter.next();
			conj.add(eq((String) me.getKey(), me.getValue()));
		}
		return conj;
	}
}
