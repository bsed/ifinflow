package com.ruimin.ifinflow.engine.flowmodel;

import com.ruimin.ifinflow.engine.flowmodel.util.VariableUtil;
import com.ruimin.ifinflow.util.DatetimeUtil;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Variable implements Serializable {
	private static final long serialVersionUID = -3660194822596305964L;
	private String name;
	private int kind;
	private Object value;
	private String bizName;

	public Variable() {
	}

	public Variable(String name, int kind) throws IFinFlowException {
		this(name, kind, null, null);
	}

	public Variable(String name, int kind, Object value)
			throws IFinFlowException {
		this(name, kind, value, null);
	}

	public Variable(String name, int kind, Object value, String bizName)
			throws IFinFlowException {
		this.name = name;
		this.kind = kind;
		this.bizName = bizName;
		setValue(value);
	}

	public static String getKindString(int kind) {
		switch (kind) {
		case 1:
			return "STRING";
		case 2:
			return "INTEGER";
		case 4:
			return "LONG";
		case 3:
			return "FLOAT";
		case 5:
			return "DOUBLE";
		case 7:
			return "DATETIME";
		case 8:
			return "BOOLEAN";
		case 6:
			return "CHAR";
		case 9:
			return "LIST";
		case 10:
			return "MAP";
		}
		return "UNDEFINED";
	}

	public static Object convertToCompatibleValue(String varName, int kind,
			Object value) throws IFinFlowException {
		switch (kind) {
		case 1:
			if ((value instanceof String)) {
				return value;
			}
			throw new IFinFlowException(104002, new Object[] { varName,
					"String", value.getClass().getName() });

		case 2:
			if ((value instanceof Integer))
				return value;
			if ((value instanceof Number)) {
				return new Integer(((Number) value).intValue());
			}
			throw new IFinFlowException(104002, new Object[] { varName,
					"Integer", value.getClass().getName() });

		case 4:
			if ((value instanceof Long))
				return value;
			if ((value instanceof Number)) {
				return new Long(((Number) value).longValue());
			}
			throw new IFinFlowException(104002, new Object[] { varName, "Long",
					value.getClass().getName() });

		case 3:
			if ((value instanceof Float))
				return value;
			if ((value instanceof Number)) {
				return new Float(((Number) value).floatValue());
			}
			throw new IFinFlowException(104002, new Object[] { varName,
					"FLoat", value.getClass().getName() });

		case 5:
			if ((value instanceof Double))
				return value;
			if ((value instanceof Number)) {
				return new Double(((Number) value).doubleValue());
			}
			throw new IFinFlowException(104002, new Object[] { varName,
					"Double", value.getClass().getName() });

		case 7:
			if ((value instanceof Date)) {
				return value;
			}
			throw new IFinFlowException(104002, new Object[] { varName, "Date",
					value.getClass().getName() });

		case 8:
			if ((value instanceof Boolean)) {
				return value;
			}
			throw new IFinFlowException(104002, new Object[] { varName,
					"Boolean", value.getClass().getName() });

		case 6:
			if ((value instanceof Character))
				return value;
			if ((value instanceof String))
				return new Character(((String) value).charAt(0));
			if ((value instanceof Number)) {
				return new Character((char) ((Number) value).intValue());
			}
			throw new IFinFlowException(104002, new Object[] { varName, "Char",
					value.getClass().getName() });

		case 9:
			if ((value instanceof List)) {
				return value;
			}
			throw new IFinFlowException(104002, new Object[] { varName, "List",
					value.getClass().getName() });

		case 10:
			if ((value instanceof Map)) {
				return value;
			}
			throw new IFinFlowException(104002, new Object[] { varName, "Map",
					value.getClass().getName() });
		}

		throw new IFinFlowException(104003,
				new Object[] { Integer.valueOf(kind) });
	}

	public static Object valueOf(int kind, String value)
			throws IFinFlowException {
		Object valueObj = null;
		switch (kind) {
		case 8:
			if ((value == null) || (value.trim().equals(""))) {
				valueObj = new Boolean(false);
			} else if ("T".equalsIgnoreCase(value.trim())) {
				valueObj = Boolean.valueOf(true);
			} else {
				valueObj = new Boolean(value);
			}
			break;
		case 1:
			valueObj = value;
			break;
		case 7:
			if ((value == null) || (value.trim().equals(""))) {
				valueObj = null;
			} else {
				valueObj = DatetimeUtil.string2Date(value);
			}
			break;

		case 5:
			if ((value == null) || (value.trim().equals(""))) {
				valueObj = new Double(0.0D);
			} else
				valueObj = new Double(value);
			break;

		case 3:
			if ((value == null) || (value.trim().equals(""))) {
				valueObj = new Float(0.0F);
			} else
				valueObj = new Float(value);
			break;

		case 2:
			if ((value == null) || (value.trim().equals(""))) {
				valueObj = new Integer(0);
			} else
				valueObj = new Integer(value);
			break;

		case 4:
			if ((value == null) || (value.trim().equals(""))) {
				valueObj = new Long(0L);
			} else
				valueObj = new Long(value);
			break;

		case 6:
			if ((value == null) || (value.length() == 0)) {
				valueObj = new Character('\000');
			} else {
				char c = value.charAt(0);
				if (c != '\\') {
					valueObj = new Character(c);
				} else {
					c = value.charAt(1);
					switch (c) {
					case 'n':
						valueObj = new Character('\n');
					case 't':
						valueObj = new Character('\t');
					case 'b':
						valueObj = new Character('\b');
					case 'r':
						valueObj = new Character('\r');
					case 'f':
						valueObj = new Character('\f');
					case '\\':
						valueObj = new Character('\\');
					case '\'':
						valueObj = new Character('\'');
					case '"':
						valueObj = new Character('"');
					}
					if ((c >= '0') && (c <= '7')) {
						String s = value.substring(1);
						c = (char) Integer.parseInt(s, 8);
						valueObj = new Character(c);
					}
				}
			}
			break;
		case 9:
			if ((value == null) || (value.trim().equals(""))) {
				valueObj = new ArrayList();
			} else {
				valueObj = VariableUtil.getListData(value);
			}
			break;
		case 10:
			if ((value == null) || (value.trim().equals(""))) {
				valueObj = new HashMap();
			} else {
				valueObj = VariableUtil.getMapData(value);
			}
			break;
		default:
			throw new IFinFlowException(104003,
					new Object[] { Integer.valueOf(kind) });
		}
		return valueObj;
	}

	public Object getValue() {
		return this.value;
	}

	public String getValueString() throws IFinFlowException {
		switch (this.kind) {
		case 1:
			return (String) this.value;

		case 2:
			if (this.value != null) {
				return ((Integer) this.value).toString();
			}
			return "0";

		case 4:
			if (this.value != null) {
				return ((Long) this.value).toString();
			}
			return "0";

		case 3:
			if (this.value != null) {
				return ((Float) this.value).toString();
			}
			return "0";

		case 5:
			if (this.value != null) {
				return ((Double) this.value).toString();
			}
			return "0";

		case 7:
			if (this.value != null) {
				return DatetimeUtil.toDatetimeString((Date) this.value);
			}
			return null;

		case 8:
			if (this.value != null) {
				return ((Boolean) this.value).toString();
			}
			return "false";

		case 6:
			if (this.value != null) {
				return ((Character) this.value).toString();
			}
			return "";
		case 9:
			if (this.value != null) {
				return ((List) this.value).toString();
			}
			return null;

		case 10:
			if (this.value != null) {
				return ((Map) this.value).toString();
			}
			return null;
		}
		return null;
	}

	public int getKind() {
		return this.kind;
	}

	public String getName() {
		return this.name;
	}

	public void setValue(Object value) throws IFinFlowException {
		if (this.kind == 0) {
			this.kind = VariableUtil.getVariableKind(value);
		}
		if (value == null) {
			this.value = VariableUtil.getDefaultValueByKind(this.kind);
			return;
		}
		switch (this.kind) {
		case 1:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 2:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 4:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 3:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 5:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 7:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 8:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 6:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 9:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
			break;
		case 10:
			this.value = convertToCompatibleValue(this.name, this.kind, value);
		}

	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBizName() {
		return this.bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		try {
			buf.append("[");
			buf.append("[Name = " + getName() + "]");
			buf.append("[Kind = " + getKind() + "]");
			String vStr = getValueString();
			buf.append("[Value = " + vStr + "]");
			buf.append("[BizName = " + getBizName() + "]");
			buf.append("]\n");
			return buf.toString();
		} catch (IFinFlowException e) {
			e.printStackTrace();
			buf.append("[Value = EXCEPTION_OCCURRED]");
			buf.append("[BizName = " + getBizName() + "]");
			buf.append("]\n");
		}
		return buf.toString();
	}

	public boolean equals(Object other) {
		if (!(other instanceof Variable))
			return false;
		Variable castOther = (Variable) other;
		return new EqualsBuilder().append(getName(), castOther.getName())
				.append(getKind(), castOther.getKind())
				.append(getValue(), castOther.getValue())
				.append(this.bizName, castOther.bizName).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getName()).append(getKind())
				.append(getValue()).toHashCode();
	}
}
