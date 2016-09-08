package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.WebApplication;

import java.util.List;

public class FindWebApplicationDataCmd
        implements Command<List<WebApplication>> {
    private static final long serialVersionUID = 1L;

    public List<WebApplication> execute(Environment environment)
            throws Exception {
        StringBuffer hql = new StringBuffer();
        hql.append("from ").append(WebApplication.class.getName()).append(" order by lastUpdateTime desc");

        return (environment.get(Session.class)).createQuery(hql.toString()).list();
    }
}