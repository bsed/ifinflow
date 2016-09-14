package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.flowmodel.vo.TaskType;
import com.ruimin.ifinflow.engine.pvm.event.WorkloadUpdate;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.identity.User;
import org.jbpm.api.model.WfUserInfo;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.HistoryEvent;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.identity.spi.IdentitySession;
import org.jbpm.pvm.internal.query.TaskQueryUtil;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.jbpm.pvm.internal.task.TopTaskVO;
import org.jbpm.pvm.internal.util.Clock;
import org.jbpm.pvm.internal.util.CollectionUtil;

import java.util.List;


public class TasksAssigneeCmd
        extends AbstractCommand<Integer> {
    private static final long serialVersionUID = 1L;
    protected TaskType taskType;
    protected int count;
    protected WfUserInfo user;
    protected boolean take;

    public TasksAssigneeCmd(TaskType taskType, WfUserInfo user, int count) {
        this.taskType = taskType;
        this.user = user;
        this.count = count;
    }

    public Integer execute(Environment environment) throws Exception {
        if ((this.user != null) && (StringUtils.isEmpty(this.user.getStaffId()))) {
            throw new IFinFlowException(101001, new Object[0]);
        }
        DbSession dbSession = (DbSession) environment.get(DbSession.class);


        IdentitySession identitySession = (IdentitySession) EnvironmentImpl.getFromCurrent(IdentitySession.class);
        User staf = identitySession.findUserById(this.user.getStaffId());
        if (staf == null) {
            throw new IFinFlowException(105005, new Object[]{this.user.getStaffId()});
        }

        Session session = (Session) environment.get(Session.class);
        Query query = TaskQueryUtil.getTodoTaskQuery(session, false, this.user.getStaffId(), this.taskType, 1, this.count);
        List<TopTaskVO> topTasks = CollectionUtil.checkList(query.setResultTransformer(Transformers.aliasToBean(TopTaskVO.class)).list(), TopTaskVO.class);
        if (topTasks.isEmpty()) {
            throw new IFinFlowException(103051, new Object[]{this.user.getStaffId()});
        }

        for (TopTaskVO topTask : topTasks) {


            TaskImpl task = (TaskImpl) session.get(TaskImpl.class, topTask.getDBID_());
            HistoryTaskImpl histask = (HistoryTaskImpl) session.get(HistoryTaskImpl.class, topTask.getDBID_());
            task.setAssigneeOnly(staf.getId());
            task.setOwnerId(staf.getId());
            task.setStatus(2);
            task.setTakeDate(Clock.getTime());
            dbSession.update(task);

            if (topTask.getASSIGNMODE_() == 2) {
                HistoryEvent.fire(new WorkloadUpdate(staf.getId(), staf.getGivenName(), 1));
            }

            histask.setStatus(Integer.valueOf(2));

            histask.setAssignee(staf.getId());
            histask.setOwnerId(staf.getId());
            histask.setOwnerName(staf.getGivenName());
            histask.setTakeDate(Clock.getTime());

            dbSession.update(histask);
        }

        return Integer.valueOf(topTasks.size());
    }
}