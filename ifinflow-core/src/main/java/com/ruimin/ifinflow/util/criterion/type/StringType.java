package com.ruimin.ifinflow.util.criterion.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringType extends NullableType {
	private static final long serialVersionUID = 4345452171621348507L;

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setString(index, (String) value);
	}

	public int sqlType() {
		return 12;
	}
}
