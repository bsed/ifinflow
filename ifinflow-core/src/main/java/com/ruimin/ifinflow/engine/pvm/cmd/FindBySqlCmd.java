package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;

import java.util.List;


public class FindBySqlCmd implements Command<List<?>> {
    private static final long serialVersionUID = 1L;
    private String sql;

    public FindBySqlCmd(String sql) {
        this.sql = sql;
    }

    public List<?> execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        Query query = session.createSQLQuery(this.sql);
        return query.list();
    }
}