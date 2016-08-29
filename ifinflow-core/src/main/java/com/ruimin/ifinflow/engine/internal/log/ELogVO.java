package com.ruimin.ifinflow.engine.internal.log;

import com.ruimin.ifinflow.engine.internal.entity.IFinFlowJProcessExcep;
import com.ruimin.ifinflow.model.util.UUIDGenerator;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.Date;
import org.jbpm.pvm.internal.exception.ExceptionUtil;

public class ELogVO extends IFinFlowJProcessExcep {
	private static final long serialVersionUID = 1L;
	private IFinFlowException exception;

	public ELogVO(IFinFlowException exception) {
		this.exception = exception;
		setDbid(UUIDGenerator.generate(this));
		setCreatedDateTime(new Date());
		setStatus(0);
		setExpCode(String.valueOf(exception.getCode()));
		setExpMessage(ExceptionUtil.getDetailExcepion(exception));
	}

	public ELogVO(Exception exception) {
		setDbid(UUIDGenerator.generate(this));
		setCreatedDateTime(new Date());
		setStatus(0);
		setExpMessage(ExceptionUtil.getDetailExcepion(exception));
	}

	public IFinFlowException getException() {
		return this.exception;
	}

	public void setException(IFinFlowException exception) {
		this.exception = exception;
		setExpCode(String.valueOf(exception.getCode()));
		setExpMessage(exception.getMessage());
	}

	public IFinFlowJProcessExcep me() {
		IFinFlowJProcessExcep log = new IFinFlowJProcessExcep();
		log.setCreatedDateTime(getCreatedDateTime());
		log.setDbid(getDbid());
		log.setExpCode(getExpCode());
		log.setExpMessage(getExpMessage());
		log.setHacti(getHacti());
		log.setHmprocl(getHmprocl());
		log.setHproci(getHproci());
		log.setNodeId(getNodeId());
		log.setNodeKind(getNodeKind());
		log.setNodeName(getNodeName());
		log.setPackageId(getPackageId());
		log.setResolvedTime(getResolvedTime());
		log.setResolverId(getResolverId());
		log.setResolverName(getResolverName());
		log.setStatus(getStatus());
		log.setSubject(getSubject());
		log.setTemplateId(getTemplateId());
		log.setTemplateName(getTemplateName());
		log.setTemplateVersion(getTemplateVersion());
		log.setExceptionPos(getExceptionPos());
		log.setExecutionStatus(getExecutionStatus());
		log.setExecutionId(getExecutionId());
		log.setOperationName(getOperationName());
		return log;
	}
}
