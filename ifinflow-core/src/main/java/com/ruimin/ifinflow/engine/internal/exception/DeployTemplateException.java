package com.ruimin.ifinflow.engine.internal.exception;

import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class DeployTemplateException extends IFinFlowException {
	private static final long serialVersionUID = 1L;

	public DeployTemplateException(Throwable e, String templateId) {
		super(102009, e, new Object[] { templateId });
		setStackTrace(e.getStackTrace());
	}
}
