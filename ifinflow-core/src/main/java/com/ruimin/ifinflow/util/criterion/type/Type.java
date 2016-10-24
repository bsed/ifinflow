package com.ruimin.ifinflow.util.criterion.type;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract interface Type extends Serializable {
	public abstract void nullSafeSet(PreparedStatement paramPreparedStatement,
			Object paramObject, int paramInt) throws IFinFlowException,
			SQLException;
}
