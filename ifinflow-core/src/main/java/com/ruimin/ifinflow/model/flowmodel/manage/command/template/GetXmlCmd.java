package com.ruimin.ifinflow.model.flowmodel.manage.command.template;

import com.ruimin.ifinflow.model.flowmodel.manage.command.AbstractCmdSupport;
import com.ruimin.ifinflow.model.flowmodel.xml.DesignTemplate;
import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;

import java.util.List;


public class GetXmlCmd<T>
        extends AbstractCmdSupport<DesignTemplate>
        implements Command<String> {
    private static final long serialVersionUID = 1L;
    private String templateHandle;
    private String packageId;
    private String templateId;
    private int version;

    public GetXmlCmd(String templateHandle) {
        this.templateHandle = templateHandle;
    }


    public GetXmlCmd(String packageId, String templateId, int version) {
        this.packageId = packageId;
        this.templateId = templateId;
        this.version = version;
    }


    public String execute(Environment environment)
            throws IFinFlowException {
        DesignTemplate designTemplate = null;

        List<DesignTemplate> templateList = null;
        StringBuilder hql = new StringBuilder();
        Session session = (Session) environment.get(Session.class);
        if (!StringUtils.isEmpty(this.templateHandle)) {
            hql.append("from ").append(this.entityClass.getName()).append(" as t where ");
            hql.append("t.template = :templateHandle");
            Template template = new Template();
            template.setHandle(this.templateHandle);
            templateList = session.createQuery(hql.toString()).setParameter("templateHandle", template).list();
            if ((templateList != null) && (templateList.size() > 0)) {
                designTemplate = (DesignTemplate) templateList.get(0);
            } else {
                return null;
            }
        } else if ((!StringUtils.isEmpty(this.packageId)) && (!StringUtils.isEmpty(this.templateId)) && (this.version > 0)) {
            hql.append("select dt from ").append(TemplatePackage.class.getName()).append(" tp,").append(DesignTemplate.class.getName()).append(" dt").append(" where tp.handle=dt.packageHandle ").append(" and dt.version=").append(this.version).append(" and tp.templatePackageId='").append(this.packageId).append("'").append(" and dt.designTemplateId='").append(this.templateId).append("'");


            designTemplate = (DesignTemplate) session.createQuery(hql.toString()).uniqueResult();
        }

        if (designTemplate != null) {
            return designTemplate.getContent();
        }
        return null;
    }
}