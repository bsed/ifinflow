package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.DatabaseUtil;
import com.ruimin.ifinflow.util.DatetimeUtil;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.Date;

public class CriterionHelper {
	public static final int DB_TYPE_SQLSERVER = 1;
	public static final int DB_TYPE_ORACLE = 2;
	public static final int DB_TYPE_DB2 = 3;
	public static final int DB_TYPE_SYBASE = 4;
	public static final int DB_TYPE_INFORMIX = 5;
	public static final int DB_TYPE_MYSQL = 6;

	public static String getStringRepresentation(Object value)
			throws IFinFlowException {
		String valueString = null;
		if (value == null) {
			return "null";
		}
		if ((value instanceof String)) {
			valueString = "'" + (String) value + "'";
		} else if ((value instanceof Integer)) {
			valueString = ((Integer) value).toString();
		} else if ((value instanceof Date)) {
			if (DatabaseUtil.getDbType() == 3) {
				String datetimeStr = DatetimeUtil
						.toDatetimeString((Date) value);
				valueString = "TO_DATE('" + datetimeStr
						+ "', 'YYYY-MM-DD HH24:MI:SS')";
			} else if (DatabaseUtil.getDbType() == 2) {
				String datetimeStr = DatetimeUtil
						.toDatetimeString((Date) value);
				valueString = "TO_DATE('" + datetimeStr
						+ "', 'YYYY-MM-DD HH24:MI:SS')";
			} else if (DatabaseUtil.getDbType() == 1) {
				String datetimeStr = DatetimeUtil
						.toDatetimeString((Date) value);
				valueString = "convert(datetime, '" + datetimeStr + "', 120)";
			} else if (DatabaseUtil.getDbType() == 4) {
				String datetimeStr = DatetimeUtil
						.toDatetimeString((Date) value);
				valueString = "convert(datetime, '" + datetimeStr + "', 120)";
			} else if (DatabaseUtil.getDbType() == 6) {
				String datetimeStr = DatetimeUtil
						.toDatetimeString((Date) value);
				valueString = "str_to_date('" + datetimeStr
						+ "', '%Y-%m-%d %T')";
			} else if (DatabaseUtil.getDbType() == 5) {
				String datetimeStr = DatetimeUtil
						.toDatetimeString((Date) value);
				valueString = "TO_DATE('" + datetimeStr
						+ "', 'YYYY-MM-DD HH24:MI:SS')";
			} else {
				throw new IFinFlowException(1, new Object[0]);
			}
		} else if ((value instanceof Long)) {
			valueString = ((Long) value).toString();
		} else if ((value instanceof Boolean)) {
			valueString = ((Boolean) value).toString();
		} else if ((value instanceof Criterion)) {
			valueString = ((Criterion) value).toStaticSqlString();
		} else {
			throw new IFinFlowException(101002, new Object[] { value.getClass()
					.getName() });
		}
		return valueString;
	}
}
