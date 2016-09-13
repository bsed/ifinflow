package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import org.jbpm.api.JbpmException;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.VariablesCmd;
import org.jbpm.pvm.internal.model.ExecutionImpl;


public class ProcessVariableSetCmd
        extends VariablesCmd<Void> {
    private static final long serialVersionUID = 1L;
    protected String processId;
    protected VariableSet vs;

    public ProcessVariableSetCmd(String processId, VariableSet vs) {
        if (processId == null) {
            throw new JbpmException("processId is null");
        }
        this.processId = processId;
        this.vs = vs;
    }

    public Void execute(Environment environment) throws Exception {
        ExecutionImpl execution = (ExecutionImpl) getExecution(environment, this.processId);

        if (this.vs != null) {
            execution.setVariables(this.vs.getList());
        }
        return null;
    }
}