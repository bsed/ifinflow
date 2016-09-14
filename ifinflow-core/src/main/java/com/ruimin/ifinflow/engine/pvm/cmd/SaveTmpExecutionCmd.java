package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.jbpm.api.cmd.Environment;
import org.jbpm.internal.log.Log;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.exception.ExceptionUtil;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.session.DbSession;

import java.util.Collection;

public class SaveTmpExecutionCmd extends AbstractCommand<Void> {
	private static final long serialVersionUID = 1L;
	private static final Log log = Log.getLog(SaveTmpExecutionCmd.class
			.getName());
	private String executionId;

	public SaveTmpExecutionCmd(String executionId) {
		this.executionId = executionId;
	}

	public Void execute(Environment environment) throws Exception {
		DbSession session = (DbSession) environment.get(DbSession.class);
		ExecutionImpl execution = (ExecutionImpl) session
				.findExecutionById(this.executionId);

		if (execution == null) {
			throw new IFinFlowException(102001,
					new Object[] { this.executionId });
		}

		if (execution.getActivityName() == null) {
			log.info("SaveTmpExecutionCmd 45 解决异常时待推进 execution.getActivityName()="
					+ execution.getActivityName());
		}
		ExceptionUtil.saveTmpExecution(this.executionId, 512,
				execution.getActivityName());
		return null;
	}
}
