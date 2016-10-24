package com.ruimin.ifinflow.util.criterion.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DoubleType extends NullableType {
	private static final long serialVersionUID = -5025817949656544631L;

	public Serializable getDefaultValue() {
		return new Double(0.0D);
	}

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setDouble(index, ((Double) value).doubleValue());
	}

	public int sqlType() {
		return 8;
	}
}
