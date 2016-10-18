package com.ruimin.ifinflow.util.criterion.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CharacterType extends NullableType {
	private static final long serialVersionUID = -6638515890569581609L;

	public Serializable getDefaultValue() {
		throw new UnsupportedOperationException("not a valid id type");
	}

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setString(index, value.toString());
	}

	public int sqlType() {
		return 1;
	}
}
