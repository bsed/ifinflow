package com.ruimin.ifinflow.util.criterion.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LongType extends NullableType {
	private static final long serialVersionUID = 6992440557387527218L;
	private static final Long ZERO = new Long(0L);

	public Serializable getDefaultValue() {
		return ZERO;
	}

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setLong(index, ((Long) value).longValue());
	}

	public int sqlType() {
		return -5;
	}
}
