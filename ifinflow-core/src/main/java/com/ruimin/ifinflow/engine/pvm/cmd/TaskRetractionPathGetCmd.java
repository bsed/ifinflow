package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
import com.ruimin.ifinflow.util.TemplateCacheUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.HistoryService;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.session.DbSession;

import java.util.List;

public class TaskRetractionPathGetCmd implements Command<Object[]> {
    private static final long serialVersionUID = 1L;
    private String taskId;

    public TaskRetractionPathGetCmd(String sourceTaskId) {
        this.taskId = sourceTaskId;
    }

    public Object[] execute(Environment environment) throws Exception {
        if (this.taskId == null) {
            return null;
        }
        DbSession dbSession = (DbSession) environment.get(DbSession.class);
        HistoryTaskImpl task = (HistoryTaskImpl) dbSession.get(HistoryTaskImpl.class, this.taskId);
        ExecutionImpl processInstance = (ExecutionImpl) dbSession.createProcessInstanceQuery().dbid(task.getHistoryProcessInstanceId()).uniqueResult();
        if (processInstance == null) {
            return null;
        }
        HistoryService historyService = (HistoryService) environment.get(HistoryService.class);
        List l = historyService.createHistoryTaskQuery().processId(processInstance.getDbid()).nodeId(task.getNodeId()).statusCondetion(" in (16,4,6) ").startedAfter(task.getCreateTime()).list();
        if (!l.isEmpty()) {
            return null;
        }


        Session session = (Session) environment.get(Session.class);
        HistoryTaskInstanceImpl hti = (HistoryTaskInstanceImpl) session.createCriteria(HistoryTaskInstanceImpl.class).add(Restrictions.eq("historyTask.dbid", this.taskId)).uniqueResult();

        String nextactId = hti.getNextHistoryActivityDbid();

        ProcessDefinitionImpl pd = processInstance.getProcessDefinition();

        HistoryTaskImpl newtask = getNextDisplayAct(pd, nextactId, session);

        if ((newtask == null) || (!newtask.getStatus().equals(Integer.valueOf(1))) || (task.getAssignMode().equals(Integer.valueOf(3)))) {

            return null;
        }
        String path = newtask.getNodeId() + " to " + task.getNodeId();
        ActivityImpl newAct = pd.getActivity(newtask.getNodeId());
        if (newAct == null) {
            newAct = pd.createActivity(newtask.getNodeId());
        }
        ActivityImpl act = pd.getActivity(task.getNodeId());
        if (act == null) {
            act = pd.createActivity(task.getNodeId());
        }
        TransitionImpl toTran = newAct.getOutgoingTransition(path);

        if (toTran == null) {
            toTran = newAct.createOutgoingTransition();
            toTran.setName(path);
            toTran.setDestination(act);
            newAct.addOutgoingTransition(toTran);
        }
        Object[] obj = new Object[2];
        obj[0] = newtask;
        obj[1] = path;
        return obj;
    }

    private HistoryTaskImpl getNextDisplayAct(ProcessDefinitionImpl pd, String nextActivityId, Session session) {
        HistoryActivityInstanceImpl nextAct = (HistoryActivityInstanceImpl) session.get(HistoryActivityInstanceImpl.class, nextActivityId);

        NodeVo vo = TemplateCacheUtil.getNodeVo(pd, nextAct.getActivityName());
        if (vo.getDisplay() == 0)
            return getNextDisplayAct(pd, nextAct.getNextHistoryActivityDbid(), session);
        if (vo.getDisplay() == 1) {
            return ((HistoryTaskInstanceImpl) nextAct).getHistoryTask();
        }
        return null;
    }
}