package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.jbpm.api.Execution;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.session.DbSession;

public class ExecutionQueryCmd
        extends AbstractCommand<Execution> {
    private static final long serialVersionUID = 1L;
    private String processId;
    private String nodeId;

    public ExecutionQueryCmd(String processId, String nodeId) {
        this.processId = processId;
        this.nodeId = nodeId;
    }

    public Execution execute(Environment environment) {
        DbSession dbSession = environment.get(DbSession.class);

        ProcessInstance process = dbSession.get(ExecutionImpl.class, this.processId);
        if (process == null) {
            throw new IFinFlowException(102021, new Object[]{this.processId, this.nodeId});
        }

        Execution execution = process.findActiveExecutionIn(this.nodeId);
        if (execution == null) {
            throw new IFinFlowException(102022, new Object[]{this.processId, this.nodeId});
        }

        return execution;
    }
}