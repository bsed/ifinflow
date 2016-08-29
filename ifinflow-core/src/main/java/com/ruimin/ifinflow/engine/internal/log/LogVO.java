package com.ruimin.ifinflow.engine.internal.log;

import com.ruimin.ifinflow.engine.internal.entity.IFinFlowLog;
import com.ruimin.ifinflow.model.util.UUIDGenerator;
import java.util.Date;

public class LogVO extends IFinFlowLog {
	private LogCategory category;
	private Throwable exception;

	public LogVO(LogCategory category) {
		this.category = category;
		setPoid(UUIDGenerator.generate(this));
		setLogTime(new Date());
		setCategoryId(category.getId());
	}

	public LogVO(LogCategory category, String logTitle, String operatorId,
			String operatorName) {
		this(category);
		setLogTitle(logTitle);
		setOperatorId(operatorId);
		setOperatorName(operatorName);
	}

	public LogCategory getCategory() {
		return this.category;
	}

	public void setCategory(LogCategory category) {
		this.category = category;
		setCategoryId(category.getId());
	}

	public Throwable getException() {
		return this.exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	IFinFlowLog me() {
		IFinFlowLog log = new IFinFlowLog();
		log.setActionName(getActionName());
		log.setActionType(getActionType());
		log.setCategoryId(getCategoryId());
		log.setLogContent(getLogContent());
		log.setLogTime(getLogTime());
		log.setLogTitle(getLogTitle());
		log.setMpid(getMpid());
		log.setOperatorId(getOperatorId());
		log.setOperatorName(getOperatorName());
		log.setPoid(getPoid());
		log.setProcessPoid(getProcessPoid());
		log.setRemark(getRemark());
		return log;
	}
}
