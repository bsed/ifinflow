package com.ruimin.ifinflow.model.flowmodel.manage.command.template;

import com.ruimin.ifinflow.model.flowmodel.manage.command.AbstractCmdSupport;
import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetTemplatesCmd
        extends AbstractCmdSupport<Template>
        implements Command<List> {
    private static final long serialVersionUID = 1L;
    private String packageHandle;
    private boolean cascade;

    public GetTemplatesCmd(String packageHandle, boolean cascade) {
        this.cascade = cascade;
        this.packageHandle = packageHandle;
    }

    public List execute(Environment environment) throws Exception {
        String hql = "";


        if (!this.cascade) {
            hql = "FROM " + this.entityClass.getName() + " AS T where T.packageHandle = ?";
        } else {
            hql = "FROM TemplatePackage AS T where T.handle = ?";
            TemplatePackage pkg = (TemplatePackage) environment.get(Session.class).createQuery(hql).setParameter(0, this.packageHandle).uniqueResult();
            List ids = new ArrayList();
            ids(pkg, ids);
            hql = "FROM " + this.entityClass.getName() + " AS T where T.packageHandle in (:ids)";
            return (environment.get(Session.class)).createQuery(hql).setParameterList("ids", ids).list();
        }
        return (environment.get(Session.class)).createQuery(hql).setParameter(0, this.packageHandle).list();
    }


    private void ids(TemplatePackage pkg, List ids) {
        ids.add(pkg.getHandle());
        for (Iterator it = pkg.getChildren().iterator(); it.hasNext(); ) {
            ids((TemplatePackage) it.next(), ids);
        }
    }
}