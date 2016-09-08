package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.internal.vo.SubProcessInstanceVo;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;

import java.util.ArrayList;
import java.util.List;

public class FindSubProcessInstanceCmd
        extends AbstractCommand<List<SubProcessInstanceVo>> {
    private static final long serialVersionUID = 1L;
    private String processInstanceId;
    private String subProcessNodeId;

    public FindSubProcessInstanceCmd(String processInstanceId, String subProcessNodeId) {
        this.processInstanceId = processInstanceId;
        this.subProcessNodeId = subProcessNodeId;
    }

    public List<SubProcessInstanceVo> execute(Environment environment) {
        List<SubProcessInstanceVo> result = null;
        Session session = (Session) environment.get(Session.class);
        StringBuilder hql = new StringBuilder();
        hql.append("select hpii.dbid,hpii.templeteVersion,hpii.packageId,hpii.templateId,hpii.templeteName from ").append(HistoryProcessInstanceImpl.class.getName()).append(" as hpii").append(" where hpii.superProcessExecution = :superProcessExecution").append("  and hpii.activityName like :activityName ");
        List<Object[]> processInstanceList = session.createQuery(hql.toString()).setString("superProcessExecution", this.processInstanceId).setString("activityName", this.subProcessNodeId + "%").list();
        if ((processInstanceList != null) && (!processInstanceList.isEmpty())) {
            if (result == null) {
                result = new ArrayList();
            }
            for (Object[] processInstance : processInstanceList) {
                SubProcessInstanceVo vo = new SubProcessInstanceVo();
                vo.setProcessInstanceId((String) processInstance[0]);
                vo.setVersion(String.valueOf((Integer) processInstance[1]));
                vo.setPackageId((String) processInstance[2]);
                vo.setTemplateId((String) processInstance[3]);
                result.add(vo);
            }
        }
        return result;
    }
}