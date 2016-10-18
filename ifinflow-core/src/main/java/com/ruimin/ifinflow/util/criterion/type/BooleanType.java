package com.ruimin.ifinflow.util.criterion.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BooleanType extends NullableType {
	private static final long serialVersionUID = -1798995899369819438L;

	public Serializable getDefaultValue() {
		return Boolean.FALSE;
	}

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setBoolean(index, ((Boolean) value).booleanValue());
	}

	public int sqlType() {
		return -7;
	}
}
