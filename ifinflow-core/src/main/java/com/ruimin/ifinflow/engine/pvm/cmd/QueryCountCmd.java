package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;

public class QueryCountCmd implements Command<Long> {
    private static final long serialVersionUID = 1L;
    private String hql;

    public QueryCountCmd(String hql) {
        this.hql = hql;
    }

    public Long execute(Environment environment) throws Exception {
        return (Long) (environment.get(Session.class)).createQuery(this.hql).uniqueResult();
    }
}