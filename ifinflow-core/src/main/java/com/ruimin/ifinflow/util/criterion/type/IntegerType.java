package com.ruimin.ifinflow.util.criterion.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntegerType extends NullableType {
	private static final long serialVersionUID = -8122784761232115757L;
	private static final Integer ZERO = new Integer(0);

	public Serializable getDefaultValue() {
		return ZERO;
	}

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setInt(index, ((Integer) value).intValue());
	}

	public int sqlType() {
		return 4;
	}
}
