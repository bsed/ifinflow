package com.ruimin.ifinflow.engine.pvm.cmd;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.QueryCommand;

public class FindPageByHqlCmd
        extends QueryCommand<List<?>> {
    private static final long serialVersionUID = 1L;
    String hql = null;

    public FindPageByHqlCmd(String hql, int startNum, int pageSize) {
        super(startNum, pageSize);
        this.hql = hql;
    }

    public List<?> execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        Query query = session.createQuery(this.hql).setFirstResult(this.firstResult).setMaxResults(this.maxResults);

        return query.list();
    }
}
