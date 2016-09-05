package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;

import org.hibernate.impl.SessionImpl;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;

public class FindDialectTypeCmd
        implements Command<String> {
    private static final long serialVersionUID = 1L;

    public String execute(Environment environment)
            throws Exception {
        SessionImpl session = (SessionImpl) environment.get(Session.class);
        return session.getFactory().getDialect().getClass().getName();
    }
}