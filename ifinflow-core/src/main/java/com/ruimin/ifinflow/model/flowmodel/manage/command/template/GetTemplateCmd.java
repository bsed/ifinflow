package com.ruimin.ifinflow.model.flowmodel.manage.command.template;

import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;


public class GetTemplateCmd
        implements Command<Template> {
    private static final long serialVersionUID = 1L;
    private String packageId;
    private String templateId;
    private int version;

    public GetTemplateCmd(String packageId, String templateId, int version) {
        this.packageId = packageId;
        this.templateId = templateId;
        this.version = version;
    }

    public Template execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        StringBuilder hql = new StringBuilder();
        hql.append("select t from ").append(TemplatePackage.class.getName()).append(" tp,").append(Template.class.getName()).append(" t").append(" where tp.handle=t.packageHandle ");


        if (!StringUtils.isEmpty(this.packageId)) {
            hql.append(" and tp.templatePackageId=:packageId");
        }
        if (!StringUtils.isEmpty(this.templateId)) {
            hql.append(" and t.templateId=:templateId");
        }
        if (this.version != 0) {
            hql.append(" and t.version=:version");
        }
        Query query = session.createQuery(hql.toString());

        if (!StringUtils.isEmpty(this.packageId)) {
            query.setString("packageId", this.packageId);
        }
        if (!StringUtils.isEmpty(this.templateId)) {
            query.setString("templateId", this.templateId);
        }
        if (this.version != 0) {
            query.setParameter("version", Integer.valueOf(this.version));
        }
        return (Template) query.uniqueResult();
    }
}