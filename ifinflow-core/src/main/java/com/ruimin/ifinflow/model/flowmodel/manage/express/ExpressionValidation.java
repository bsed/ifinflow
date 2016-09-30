package com.ruimin.ifinflow.model.flowmodel.manage.express;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.PrintStream;
import java.util.List;
import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExpressionValidation {
	private static Log log = LogFactory.getLog(ExpressionValidation.class);

	public static boolean match(String toDeal) throws IFinFlowException {
		Stack<Integer> stack = new Stack();
		stack.add(Integer.valueOf(0));
		int length = toDeal.length();
		for (int i = 0; i < length; i++) {
			char c = toDeal.charAt(i);
			if (c == '(') {
				stack.add(Integer.valueOf(i + 1));
			} else if (c == ')') {
				int index = ((Integer) stack.pop()).intValue();
				if (index == 0) {
					log.error("左边第" + (i + 1) + "位括号没对应左括号");
					stack.add(Integer.valueOf(0));
					throw new IFinFlowException(110001,
							new Object[] { Integer.valueOf(i + 1) });
				}
			}
		}

		if (stack != null) {
			while (!stack.isEmpty()) {
				int index = ((Integer) stack.pop()).intValue();
				if (index != 0) {
					log.error("左边第" + index + "位括号没对应右括号");
					throw new IFinFlowException(110002,
							new Object[] { Integer.valueOf(index) });
				}
			}
		}

		return true;
	}

	public static String[] compileAtomExps(String toDeal) {
		toDeal = toDeal.replaceAll("\\&\\&", "\\|\\|").replaceAll("\\(", "")
				.replaceAll("\\)", "");

		String[] atomExps = null;
		if (StringUtils.isNotEmpty(toDeal)) {
			atomExps = toDeal.split("\\|\\|");
		}
		return atomExps;
	}

	public static boolean isNumeric(String toDeal) {
		return toDeal.matches("\\d+\\.{0,1}(\\d*)");
	}

	private static String getExpressOperator(String toDeal) {
		String resultSign = null;
		String[] signs = { "==", "!=", ">=", ">", "<=", "<" };
		for (String sign : signs) {
			int index = toDeal.indexOf(sign);
			if (index > 0) {
				resultSign = toDeal.substring(index, index + sign.length());
				break;
			}
		}
		return resultSign;
	}

	public static boolean checkExpress(String toDeal, List<String> vars) {
		String[] atomExps = compileAtomExps(toDeal);

		for (String atomExp : atomExps) {
			String operator = getExpressOperator(atomExp);

			String leftExp = atomExp.substring(0, atomExp.indexOf(operator))
					.trim();
			if (!leftExp.startsWith("#{")) {
				throw new IFinFlowException(110003, new Object[] { leftExp });
			}
			if (!isVariable(leftExp, vars)) {
				throw new IFinFlowException(110004, new Object[] { leftExp });
			}

			String rightExp = atomExp.substring(atomExp.indexOf(operator)
					+ operator.length());
			String[] splits = rightExp.split("[+-[*]/]");
			for (String splitStr : splits) {
				splitStr = splitStr.trim();

				if (splitStr.startsWith("#{")) {
					if (!isVariable(splitStr, vars)) {
						throw new IFinFlowException(110004,
								new Object[] { splitStr });
					}

				} else if ((!isNumeric(splitStr))
						&& ((!splitStr.startsWith("\"")) || (!splitStr
								.endsWith("\"")))) {
					throw new IFinFlowException(110005,
							new Object[] { splitStr });
				}
			}
		}

		return true;
	}

	public static boolean isVariable(String toDeal, List<String> vars) {
		for (String var : vars) {
			StringBuilder tempVar = new StringBuilder();
			if (toDeal.equals("#{" + var + "}")) {
				tempVar = null;
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.isNotBlank(null));
	}
}
