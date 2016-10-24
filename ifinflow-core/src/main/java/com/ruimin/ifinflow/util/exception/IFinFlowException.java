package com.ruimin.ifinflow.util.exception;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.Stack;

public class IFinFlowException extends RuntimeException {
	private static final long serialVersionUID = 6717005263209699611L;
	private static Properties msgProps;

	static {
		try {
			msgProps = new Properties();
			InputStream input = IFinFlowException.class
					.getResourceAsStream("/com/ruimin/ifinflow/util/exception/message.properties");

			msgProps.load(input);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected int code = -1;

	protected String message = null;

	protected Stack<Throwable> exceptionStack = new Stack();

	private Object[] params = null;

	public IFinFlowException(int errorCode, Object... params) {
		this.code = errorCode;
		this.params = params;
	}

	public IFinFlowException(int errorCode, Throwable e, Object... params) {
		this.code = errorCode;
		this.params = params;
		this.message = getMessage();
		this.exceptionStack.push(e);
	}

	public IFinFlowException(int errorCode, Exception ex) {
		this.code = errorCode;
		this.message = ex.getMessage();
		this.exceptionStack.push(ex);
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		if ((this.code != -1) && (this.message == null)) {
			try {
				String msg = msgProps.getProperty("M" + this.code);
				this.message = MessageFormat.format(msg, this.params);
			} catch (Exception e) {
				this.message = msgProps.getProperty("M100000");
				e.printStackTrace();
			}
		}
		return this.message;
	}

	public void pushException(Exception exception) {
		this.exceptionStack.push(exception);
	}

	public Exception popException() {
		return (Exception) this.exceptionStack.pop();
	}

	public Throwable getRootException() {
		if (this.exceptionStack != null) {
			return (Throwable) this.exceptionStack.get(0);
		}
		return null;
	}

	public Throwable peekException() {
		return (Throwable) this.exceptionStack.peek();
	}

	public String toString() {
		StringBuffer msg = new StringBuffer();
		msg.append("{{>>>Begin of IFinFlowException<<<\n");
		msg.append("[ExceptionCode: " + this.code + "] ");
		msg.append("[ExcetpionMessage: ");
		msg.append(getMessage());
		msg.append("]\n");
		for (int stackSize = this.exceptionStack.size(); stackSize > 0; stackSize--) {
			Object expObject = this.exceptionStack.get(stackSize - 1);
			if ((expObject instanceof Exception)) {
				Exception exp = (Exception) expObject;
				if ((exp instanceof IFinFlowException)) {
					msg.append("Caused by ");
					msg.append(exp.getClass().getName());
					msg.append(": ");
					msg.append("[ExceptionCode: " + this.code + "] ");
					msg.append("[ExcetpionMessage: ");
					msg.append(((IFinFlowException) exp).getMessage());
					msg.append("]\n");
				} else {
					msg.append("Caused by ");
					msg.append(ExceptionMsgUtil.getStacktrace(exp));
				}
			} else if ((expObject instanceof ThreadDeath)) {
				msg.append("Caused by java.lang.ThreadDeath: ")
						.append(((ThreadDeath) expObject).getMessage())
						.append("\n");

				msg.append(ExceptionMsgUtil
						.getStacktrace(((ThreadDeath) expObject).getCause()));
			} else {
				msg.append("Caused by ");
				msg.append(ExceptionMsgUtil
						.getStacktrace((Throwable) expObject));
			}
		}

		msg.append(">>>End of IFinFlowException<<<}}\n");
		return msg.toString();
	}

	public void printStackTrace() {
		System.out.println(toString());
	}
}
