package com.ruimin.ifinflow.engine.pvm.cmd;


import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.task.TaskImpl;


public class DeleteParentTaskCmd
        implements Command<Void> {
    private static final long serialVersionUID = 1L;
    private Task task;

    public DeleteParentTaskCmd(Task task) {
        this.task = task;
    }

    public Void execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        StringBuilder hql = new StringBuilder();
        hql.append("delete from ").append(TaskImpl.class.getName()).append(" o where o.subTasks in elements(").append(this.task.getId()).append(")");

        session.createQuery(hql.toString()).executeUpdate();
        return null;
    }
}