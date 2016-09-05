package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.pvm.event.WorkloadUpdate;

import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.history.HistoryEvent;
import org.jbpm.pvm.internal.task.TaskImpl;

public class DeleteTaskImplCmd
        implements Command<Void> {
    private static final long serialVersionUID = 1L;
    private Task task;

    public DeleteTaskImplCmd(Task task) {
        this.task = task;
    }

    public Void execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        StringBuilder hql = new StringBuilder();
        hql.append("delete from ").append(TaskImpl.class.getName()).append(" o where o.dbid = '").append(this.task.getId()).append("'");


        session.createQuery(hql.toString()).executeUpdate();


        TaskImpl tt = (TaskImpl) this.task;
        if ((2 == tt.getStatus()) || ((1 == tt.getStatus()) && (2 != tt.getAssignMode()))) {
            HistoryEvent.fire(new WorkloadUpdate(tt.getAssignee(), tt.getAssignee(), 2));
        }
        return null;
    }
}