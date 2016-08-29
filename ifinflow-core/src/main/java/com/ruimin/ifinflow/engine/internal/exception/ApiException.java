package com.ruimin.ifinflow.engine.internal.exception;

import com.ruimin.ifinflow.engine.internal.log.ELogVO;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class ApiException extends IFinFlowException {
	private static final long serialVersionUID = 1L;
	public static final int API_EXP_PROCESS_START_ERROR = 1;
	public static final int API_EXP_GET_SKIP_NODE_ERROR = 4002;

	public ApiException(int errorCode, Exception ex, ELogVO log) {
		super(errorCode, ex);
		if (log == null) {
			log = new ELogVO(this);
		} else {
			log.setException(this);
		}
	}
}
