package com.ruimin.ifinflow.util.criterion.type;

import java.io.Serializable;

public final class TypedValue implements Serializable {
	private static final long serialVersionUID = -8193780208428225239L;
	private final Type type;
	private final Object value;

	public TypedValue(Type type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Object getValue() {
		return this.value;
	}

	public Type getType() {
		return this.type;
	}

	public String toString() {
		return this.value == null ? "null" : this.value.toString();
	}
}
