package org.jbpm.api;

public class JbpmException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JbpmException() {
	}

	public JbpmException(String msg, Throwable cause) {
		super(msg);
		super.initCause(cause);
	}

	public JbpmException(String msg) {
		super(msg);
	}

	public JbpmException(Throwable cause) {
		super.initCause(cause);
	}
}
