package com.ruimin.ifinflow.util.criterion.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FloatType extends NullableType {
	private static final long serialVersionUID = -5909150921839860886L;

	public Serializable getDefaultValue() {
		return new Float(0.0D);
	}

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setFloat(index, ((Float) value).floatValue());
	}

	public int sqlType() {
		return 6;
	}
}
