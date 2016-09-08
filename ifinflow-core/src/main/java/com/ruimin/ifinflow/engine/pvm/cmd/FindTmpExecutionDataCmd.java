package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.TmpExecution;

import java.util.List;

public class FindTmpExecutionDataCmd
        implements Command<List<TmpExecution>> {
    private static final long serialVersionUID = 1L;
    private int executionCount;
    private String webApplicationId;

    public FindTmpExecutionDataCmd(String webApplicationId, int executionCount) {
        this.webApplicationId = webApplicationId;
        this.executionCount = executionCount;
    }

    public List<TmpExecution> execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);
        List<TmpExecution> resultList = null;
        StringBuffer hql = new StringBuffer();
        hql.append("from ").append(TmpExecution.class.getName());
        if (this.webApplicationId == null) {
            hql.append(" t where t.status in(0,512) ");
            resultList = session.createQuery(hql.toString()).setFirstResult(0).setMaxResults(this.executionCount).list();
        } else {
            hql.append(" t where t.webApplicationId = '").append(this.webApplicationId).append("' and t.status in(0,512) ");

            resultList = session.createQuery(hql.toString()).setFirstResult(0).setMaxResults(this.executionCount).list();
        }
        return resultList;
    }
}