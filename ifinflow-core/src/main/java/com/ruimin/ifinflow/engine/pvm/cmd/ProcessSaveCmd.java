package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.model.Activity;
import org.jbpm.pvm.internal.model.ExecutionImpl;

public class ProcessSaveCmd
        implements Command<ExecutionImpl> {
    private static final long serialVersionUID = 1L;
    private ExecutionImpl execution;
    private Activity act;

    public ProcessSaveCmd(ExecutionImpl execution, Activity act) {
        this.execution = execution;
        this.act = act;
    }

    public ExecutionImpl execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        session.createQuery("update " + ExecutionImpl.class.getName() + " set state='" + "inactive-join" + "',activityName='" + this.act.getName() + "' where dbid='" + this.execution.getDbid() + "'").executeUpdate();
        return (ExecutionImpl) session.get(ExecutionImpl.class, this.execution.getDbid());
    }
}