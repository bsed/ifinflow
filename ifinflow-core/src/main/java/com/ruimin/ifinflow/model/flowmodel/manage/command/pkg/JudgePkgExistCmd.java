package com.ruimin.ifinflow.model.flowmodel.manage.command.pkg;

import com.ruimin.ifinflow.model.flowmodel.manage.command.AbstractCmdSupport;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;


public class JudgePkgExistCmd
        extends AbstractCmdSupport<TemplatePackage>
        implements Command<Boolean> {
    private static final long serialVersionUID = 1L;
    private String packageId;

    public JudgePkgExistCmd(String packageId) {
        this.packageId = packageId;
    }

    public Boolean execute(Environment environment) throws Exception {
        return ((Long) (environment.get(Session.class)).createQuery("select count(o) from " + this.entityClass.getName() + " AS o where o.templatePackageId = :packageId").setParameter("packageId", this.packageId).uniqueResult()).longValue() > 0L;
    }
}