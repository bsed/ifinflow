package com.ruimin.ifinflow.model.flowmodel.manage.command.pkg;

import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;


public class SavePackageCmd
        extends AbstractCommand<String> {
    private static final long serialVersionUID = 1L;
    private TemplatePackage templatePackage = null;

    public SavePackageCmd(TemplatePackage templatePackage) {
        this.templatePackage = templatePackage;
    }

    public String execute(Environment environment) throws Exception {
        ((Session) environment.get(Session.class)).save(this.templatePackage);
        return this.templatePackage.getHandle();
    }
}