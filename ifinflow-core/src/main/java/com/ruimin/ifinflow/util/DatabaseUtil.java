package com.ruimin.ifinflow.util;

import com.ruimin.ifinflow.engine.internal.config.Configuration;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class DatabaseUtil {
	public static final int DB_TYPE_SQLSERVER = 1;
	public static final int DB_TYPE_ORACLE = 2;
	public static final int DB_TYPE_DB2 = 3;
	public static final int DB_TYPE_SYBASE = 4;
	public static final int DB_TYPE_INFORMIX = 5;
	public static final int DB_TYPE_MYSQL = 6;
	public static final String DB_ORACLE = "oracle";
	public static final String DB_SYBASE = "sybase";
	public static final String DB_SQLSERVER = "sqlserver";
	public static final String DB_DB2 = "db2";
	public static final String DB_INFORMIX = "informix";
	public static final String DB_MYSQL = "mysql";
	private static int dbType = 0;

	public static int getDbType() {
		if (dbType == 0) {
			try {
				setDbType();
			} catch (IFinFlowException wk) {
				wk.printStackTrace();
			}
		}
		return dbType;
	}

	public static void setDbType(String type) throws IFinFlowException {
		if ((type == null) || (type.trim().equals("")))
			throw new IFinFlowException(100001, new Object[0]);
		type = type.trim();
		if (type.equalsIgnoreCase("oracle")) {
			dbType = 2;
		} else if (type.equalsIgnoreCase("sybase")) {
			dbType = 4;
		} else if (type.equalsIgnoreCase("sqlserver")) {
			dbType = 1;
		} else if (type.equalsIgnoreCase("db2")) {
			dbType = 3;
		} else if (type.equalsIgnoreCase("informix")) {
			dbType = 5;
		} else if (type.equalsIgnoreCase("mysql")) {
			dbType = 6;
		} else {
			throw new IFinFlowException(1, new Object[0]);
		}
	}

	public static void setDbType() throws IFinFlowException {
		String databaseType = Configuration.getValue("Database");
		if ((databaseType == null) || (databaseType.trim().equals("")))
			throw new IFinFlowException(100001, new Object[0]);
		databaseType = databaseType.trim();
		if (databaseType.equalsIgnoreCase("oracle")) {
			dbType = 2;
		} else if (databaseType.equalsIgnoreCase("sybase")) {
			dbType = 4;
		} else if (databaseType.equalsIgnoreCase("sqlserver")) {
			dbType = 1;
		} else if (databaseType.equalsIgnoreCase("db2")) {
			dbType = 3;
		} else if (databaseType.equalsIgnoreCase("informix")) {
			dbType = 5;
		} else if (databaseType.equalsIgnoreCase("mysql")) {
			dbType = 6;
		} else {
			throw new IFinFlowException(100001, new Object[0]);
		}
	}

	public static void resetDatabaseType() throws Exception {
		dbType = 0;
	}
}
