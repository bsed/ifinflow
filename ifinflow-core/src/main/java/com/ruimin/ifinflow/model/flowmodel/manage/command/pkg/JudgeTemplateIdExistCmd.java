package com.ruimin.ifinflow.model.flowmodel.manage.command.pkg;

import com.ruimin.ifinflow.model.flowmodel.manage.command.AbstractCmdSupport;
import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;


public class JudgeTemplateIdExistCmd
        extends AbstractCmdSupport<Template>
        implements Command<Boolean> {
    private static final long serialVersionUID = 1L;
    private String packageHandle;
    private String templateId;

    public JudgeTemplateIdExistCmd(String packageHandle, String templateId) {
        this.packageHandle = packageHandle;
        this.templateId = templateId;
    }

    public Boolean execute(Environment environment) throws Exception {
        return ((Long) ((Session) environment.get(Session.class)).createQuery("select count(o) from " + this.entityClass.getName() + " AS o where o.packageHandle = :packageHandle and o.templateId = :id").setParameter("packageHandle", this.packageHandle).setParameter("id", this.templateId).uniqueResult()).longValue() > 0L;
    }
}