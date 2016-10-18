package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;

public class Order implements Serializable {
	private static final long serialVersionUID = 8630694230578274173L;
	private boolean ascending;
	private String propertyName;

	public boolean getAscending() {
		return this.ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Order() {
	}

	public String toString() {
		return this.propertyName + ' ' + (this.ascending ? "asc" : "desc");
	}

	protected Order(String propertyName, boolean ascending) {
		this.propertyName = propertyName;
		this.ascending = ascending;
	}

	public String toSqlString(Criteria criteria) throws IFinFlowException {
		StringBuffer fragment = new StringBuffer();
		fragment.append(this.propertyName);
		fragment.append(this.ascending ? " asc" : " desc");
		return fragment.toString();
	}

	public static Order asc(String propertyName) {
		return new Order(propertyName, true);
	}

	public static Order desc(String propertyName) {
		return new Order(propertyName, false);
	}
}
