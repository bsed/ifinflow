package com.ruimin.ifinflow.model.flowmodel.manage.command.pkg;

import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;

import java.util.Collection;


public class GetAllPackageCmd
        extends AbstractCommand<Object> {
    private static final long serialVersionUID = 1L;

    public Collection<String> execute(Environment environment)
            throws Exception {
        return environment.get(Session.class).createQuery("from TemplatePackage").list();
    }
}