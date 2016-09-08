package com.ruimin.ifinflow.engine.pvm.cmd;


import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.WebApplication;


public class FindMaxEngineIdxCmd
        implements Command<Integer> {
    private static final long serialVersionUID = 1L;

    public Integer execute(Environment environment)
            throws Exception {
        StringBuffer hql = new StringBuffer();
        hql.append("select max(engineIdx) from ").append(WebApplication.class.getName());
        return (Integer) ((Session) environment.get(Session.class)).createQuery(hql.toString()).uniqueResult();
    }
}