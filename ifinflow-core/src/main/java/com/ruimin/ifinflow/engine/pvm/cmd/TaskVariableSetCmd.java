package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.VariablesCmd;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.task.TaskImpl;


public class TaskVariableSetCmd
        extends VariablesCmd<Void> {
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected VariableSet vs;

    public TaskVariableSetCmd(String taskId, VariableSet vs) {
        if (taskId == null) {
            throw new IFinFlowException(103018, new Object[0]);
        }
        this.taskId = taskId;
        this.vs = vs;
    }

    public Void execute(Environment environment) throws Exception {
        DbSession dbSession = (DbSession) environment.get(DbSession.class);
        TaskImpl task = (TaskImpl) dbSession.get(TaskImpl.class, this.taskId);
        if (this.vs != null) {
            task.setVariables(this.vs.getList());
        }
        return null;
    }
}