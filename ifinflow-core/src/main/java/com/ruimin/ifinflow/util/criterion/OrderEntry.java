package com.ruimin.ifinflow.util.criterion;

import java.io.Serializable;

public class OrderEntry implements Serializable {
	private static final long serialVersionUID = -6487237210901975642L;
	private final Order order;
	private final Criteria criteria;

	public OrderEntry(Order order, Criteria criteria) {
		this.criteria = criteria;
		this.order = order;
	}

	public Order getOrder() {
		return this.order;
	}

	public Criteria getCriteria() {
		return this.criteria;
	}

	public String toString() {
		return this.order.toString();
	}
}
