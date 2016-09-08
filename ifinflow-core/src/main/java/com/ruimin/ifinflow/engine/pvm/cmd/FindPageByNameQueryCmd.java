package com.ruimin.ifinflow.engine.pvm.cmd;

import java.util.List;
import java.util.Map;

import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.QueryCommand;

public class FindPageByNameQueryCmd
        extends QueryCommand<List<?>> {
    private static final long serialVersionUID = 1L;
    protected String hql;
    protected Map<String, Object> params;

    public FindPageByNameQueryCmd(String hql, Map<String, Object> params, int startNum, int pageSize) {
        super(startNum, pageSize);

        this.hql = hql;
        this.params = params;
    }

    public FindPageByNameQueryCmd(String hql, Map<String, Object> params) {
        super(-1, 0);

        this.hql = hql;
        this.params = params;
    }

    public List<?> execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        Query query = session.createQuery(this.hql);
        if (this.params != null) {
            Set<Map.Entry<String, Object>> en = this.params.entrySet();
            for (Map.Entry<String, Object> entry : en) {
                if ((entry.getValue() instanceof List)) {
                    query.setParameterList((String) entry.getKey(), (List) entry.getValue());
                } else {
                    query.setParameter((String) entry.getKey(), entry.getValue());
                }
            }
        }
        if ((this.firstResult >= 0) && (this.maxResults > 0)) {
            query.setFirstResult(this.firstResult).setMaxResults(this.maxResults);
        }


        return query.list();
    }
}
