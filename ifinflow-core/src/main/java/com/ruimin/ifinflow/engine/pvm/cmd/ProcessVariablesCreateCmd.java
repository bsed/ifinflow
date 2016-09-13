package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.flowmodel.Variable;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.VariablesCmd;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import java.util.HashMap;
import java.util.Map;


public class ProcessVariablesCreateCmd
        extends VariablesCmd<Void> {
    private static final long serialVersionUID = 1L;
    protected String executionId;
    protected VariableSet vars;

    public ProcessVariablesCreateCmd(String executionId, VariableSet vars) {
        this.executionId = executionId;
        this.vars = vars;
    }

    public Void execute(Environment environment)
            throws Exception {
        ExecutionImpl execution = (ExecutionImpl) getExecution(environment, this.executionId);

        HashMap<String, Variable> map = this.vars.getVariableMap();
        Variable var = null;
        for (Map.Entry<String, Variable> entry : map.entrySet()) {
            var = (Variable) entry.getValue();
            execution.createVariable(var.getName(), var.getValue(), var.getBizName(), Integer.valueOf(var.getKind()));
        }

        return null;
    }
}
