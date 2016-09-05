package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.ExecutionImpl;

public class DeleteSubExecutionCmd
        implements Command<Void> {
    private static final long serialVersionUID = 1L;
    private String executionId;

    public DeleteSubExecutionCmd(String executionId) {
        this.executionId = executionId;
    }

    public Void execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        StringBuilder hql = new StringBuilder();
        hql.append("delete from ").append(ExecutionImpl.class.getName()).append(" o where o.parent = '").append(this.executionId).append("'");


        session.createQuery(hql.toString()).executeUpdate();
        return null;
    }
}