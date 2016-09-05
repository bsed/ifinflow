package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;

import java.util.List;

public class CriteriaQueryCmd
        extends AbstractCommand<List> {
    private static final long serialVersionUID = 1L;
    private String hql;
    private int fromCursor;
    private int count;

    public CriteriaQueryCmd(String hql, int fromCursor, int count) {
        this.hql = hql;
        this.fromCursor = fromCursor;
        this.count = count;
    }

    public List execute(Environment environment)
            throws Exception {
        Query query = ((Session) environment.get(Session.class)).createQuery(this.hql);

        if ((this.fromCursor >= 0) && (this.count >= 1)) {
            query.setFirstResult(this.fromCursor).setMaxResults(this.count);
        }
        return query.list();
    }
}