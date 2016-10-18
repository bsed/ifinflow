package com.ruimin.ifinflow.util.criterion.impl;

import com.ruimin.ifinflow.util.ArrayHelper;
import com.ruimin.ifinflow.util.criterion.Criteria;
import com.ruimin.ifinflow.util.criterion.Criterion;
import com.ruimin.ifinflow.util.criterion.CriterionEntry;
import com.ruimin.ifinflow.util.criterion.Order;
import com.ruimin.ifinflow.util.criterion.OrderEntry;
import com.ruimin.ifinflow.util.criterion.type.Type;
import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CriteriaImpl implements Criteria, Serializable {
	private static final long serialVersionUID = 217242266824271187L;
	private String entityOrClassName;
	private List<CriterionEntry> criterionEntries = new ArrayList();
	private ArrayList<OrderEntry> orderEntries = new ArrayList();

	private Integer maxResult = new Integer(0);
	private Integer firstResult = new Integer(0);
	private Integer totalResult = new Integer(0);

	private Object[] positionalParameterValues;

	private Type[] positionalParameterTypes;

	public CriteriaImpl() {
	}

	public CriteriaImpl(Class<?> persistentClass) {
		this.entityOrClassName = persistentClass.getClass().getName();
	}

	public String getEntityOrClassName() {
		return this.entityOrClassName;
	}

	public void setEntityOrClassName(String entityOrClassName) {
		this.entityOrClassName = entityOrClassName;
	}

	private Iterator<CriterionEntry> iterateExpressionEntries() {
		return this.criterionEntries.iterator();
	}

	private Iterator<OrderEntry> iterateOrderings() {
		return this.orderEntries.iterator();
	}

	public Criteria add(Criteria criteriaInst, Criterion expression) {
		this.criterionEntries.add(new CriterionEntry(expression, criteriaInst));
		return this;
	}

	public Criteria add(Criterion expression) {
		add(this, expression);
		return this;
	}

	public Criteria addOrder(Order ordering) {
		this.orderEntries.add(new OrderEntry(ordering, this));
		return this;
	}

	public Integer getMaxResult() {
		return this.maxResult;
	}

	public Criteria setMaxResult(int maxResult) {
		this.maxResult = new Integer(maxResult);
		return this;
	}

	public Integer getFirstResult() {
		return this.firstResult;
	}

	public Criteria setFirstResult(int firstResult) {
		this.firstResult = new Integer(firstResult);
		return this;
	}

	public Integer getTotalResult() {
		return this.totalResult;
	}

	public Criteria setTotalResult(int totalResult) {
		this.totalResult = new Integer(totalResult);
		return this;
	}

	public Object[] getPositionalParameterValues() throws IFinFlowException {
		if (this.positionalParameterValues == null) {
			getQueryParameters();
		}
		return this.positionalParameterValues;
	}

	public Type[] getPositionalParameterTypes() throws IFinFlowException {
		if (this.positionalParameterValues == null) {
			getQueryParameters();
		}
		return this.positionalParameterTypes;
	}

	public String getStaticWhereCondition() throws IFinFlowException {
		StringBuffer condition = new StringBuffer(30);
		Iterator<CriterionEntry> criterionIterator = iterateExpressionEntries();

		while (criterionIterator.hasNext()) {
			CriterionEntry entry = (CriterionEntry) criterionIterator.next();
			String sqlString = entry.getCriterion().toStaticSqlString();
			condition.append(sqlString);
			if (criterionIterator.hasNext()) {
				condition.append(" and ");
			}
		}
		return condition.toString();
	}

	public String getOrderBy() throws IFinFlowException {
		StringBuffer orderBy = new StringBuffer(30);
		Iterator<OrderEntry> criterionIterator = iterateOrderings();
		while (criterionIterator.hasNext()) {
			OrderEntry oe = (OrderEntry) criterionIterator.next();
			orderBy.append(oe.getOrder().toSqlString(oe.getCriteria()));
			if (criterionIterator.hasNext()) {
				orderBy.append(", ");
			}
		}
		return orderBy.toString();
	}

	private void getQueryParameters() throws IFinFlowException {
		List<Object> values = new ArrayList();
		ArrayList<Type> types = new ArrayList();
		Iterator<CriterionEntry> iter = iterateExpressionEntries();
		while (iter.hasNext()) {
			CriterionEntry ce = (CriterionEntry) iter.next();
			TypedValue[] tv = ce.getCriterion()
					.getTypedValues(ce.getCriteria());

			for (int i = 0; i < tv.length; i++) {
				values.add(tv[i].getValue());
				types.add(tv[i].getType());
			}
		}
		this.positionalParameterValues = values.toArray();
		this.positionalParameterTypes = ArrayHelper.toTypeArray(types);
	}

	protected int bindPositionalParameters(PreparedStatement statement,
			Object[] values, Type[] types) throws SQLException,
			IFinFlowException {
		int span = 0;
		int startIndex = 1;
		for (int i = 0; i < values.length; i++) {
			types[i].nullSafeSet(statement, values[i], startIndex + span);
			span++;
		}
		return span;
	}
}
