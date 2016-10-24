package com.ruimin.ifinflow.util.criterion.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShortType extends NullableType {
	private static final long serialVersionUID = 5824266877000870785L;
	private static final Short ZERO = new Short((short) 0);

	public Serializable getDefaultValue() {
		return ZERO;
	}

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setShort(index, ((Short) value).shortValue());
	}

	public int sqlType() {
		return 5;
	}
}
