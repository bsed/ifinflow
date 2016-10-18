package com.ruimin.ifinflow.util.criterion;

import java.io.Serializable;

public class CriterionEntry implements Serializable {
	private static final long serialVersionUID = -5462364297308945132L;
	private final Criterion criterion;
	private final Criteria criteria;

	public CriterionEntry(Criterion criterion, Criteria criteria) {
		this.criteria = criteria;
		this.criterion = criterion;
	}

	public Criterion getCriterion() {
		return this.criterion;
	}

	public Criteria getCriteria() {
		return this.criteria;
	}

	public String toString() {
		return this.criterion.toString();
	}
}
