package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.query.TaskQueryUtil;

public class TodoTaskQueryCountCmd
        extends AbstractCommand<Long> {
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String nodeName;
    protected String templateId;
    protected int status;
    protected String[] extendValue;
    protected String taskId;

    public TodoTaskQueryCountCmd(String userId) {
        this.userId = userId;
    }

    public TodoTaskQueryCountCmd(String userId, String nodeName, String templateId, int status, String... extendValue) {
        this.userId = userId;
        this.nodeName = nodeName;
        this.templateId = templateId;
        this.status = status;
        this.extendValue = extendValue;
    }

    public TodoTaskQueryCountCmd(String userId, String nodeName, String templateId, String taskId, int status, String[] extendValue) {
        this.userId = userId;
        this.nodeName = nodeName;
        this.templateId = templateId;
        this.status = status;
        this.extendValue = extendValue;
        this.taskId = taskId;
    }


    public Long execute(Environment environment)
            throws Exception {
        Session session = (Session) environment.get(Session.class);

        Query query = TaskQueryUtil.getTodoTaskQuery(session, true, this.userId, this.nodeName, this.templateId, null, this.status, -1, 0, this.extendValue);


        return (Long) query.uniqueResult();
    }
}