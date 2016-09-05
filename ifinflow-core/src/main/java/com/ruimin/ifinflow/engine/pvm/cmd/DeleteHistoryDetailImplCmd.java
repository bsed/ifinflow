package com.ruimin.ifinflow.engine.pvm.cmd;


import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.history.model.HistoryDetailImpl;

public class DeleteHistoryDetailImplCmd
        implements Command<Void> {
    private static final long serialVersionUID = 1L;
    private String taskDbid;

    public DeleteHistoryDetailImplCmd(String taskDbid) {
        this.taskDbid = taskDbid;
    }

    public Void execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        StringBuilder hql = new StringBuilder();
        hql.append("delete from ").append(HistoryDetailImpl.class.getName()).append(" o where o.historyTask = '").append(this.taskDbid).append("'");


        session.createQuery(hql.toString()).executeUpdate();
        return null;
    }
}
