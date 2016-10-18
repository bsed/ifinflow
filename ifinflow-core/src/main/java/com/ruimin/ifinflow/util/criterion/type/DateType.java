package com.ruimin.ifinflow.util.criterion.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;

public class DateType extends NullableType {
	private static final long serialVersionUID = 3400322614864932061L;

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		Date sqlDate;
		if ((value instanceof java.sql.Date)) {
			sqlDate = (java.sql.Date) value;
		} else {
			sqlDate = new java.sql.Date(((java.util.Date) value).getTime());
		}
		st.setTimestamp(index, new Timestamp(sqlDate.getTime()));
	}

	public int sqlType() {
		return 91;
	}
}