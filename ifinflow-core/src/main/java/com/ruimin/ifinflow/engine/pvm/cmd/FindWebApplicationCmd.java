package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.WebApplication;

public class FindWebApplicationCmd
        implements Command<WebApplication> {
    private static final long serialVersionUID = 1L;
    private String dbid_;

    public FindWebApplicationCmd(String dbid) {
        this.dbid_ = dbid;
    }

    public WebApplication execute(Environment environment) throws Exception {
        return (WebApplication) (environment.get(Session.class)).get(WebApplication.class, this.dbid_);
    }
}