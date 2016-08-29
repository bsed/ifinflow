package com.ruimin.ifinflow.engine.internal.exception;

import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class UndoDeployTemplateException extends IFinFlowException {
	private static final long serialVersionUID = 1L;

	public UndoDeployTemplateException(Throwable e, String packageId,
			String templateId, String version) {
		super(102010, new Object[] { packageId, templateId, version });
		setStackTrace(e.getStackTrace());
	}

	public UndoDeployTemplateException(String pkgId, String templateId,
			String version) {
		super(102010, new Object[] { pkgId, templateId, version });
	}
}
