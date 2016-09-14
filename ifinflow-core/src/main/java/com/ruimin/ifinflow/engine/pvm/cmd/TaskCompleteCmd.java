package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.util.ActivityUtil;
import com.ruimin.ifinflow.util.Constant;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.JbpmException;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.model.WfUserInfo;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.task.TaskImpl;

public class TaskCompleteCmd
        extends AbstractCommand<Void> {
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String outcome;
    protected Integer status;
    protected boolean outcomeSpecified;
    protected WfUserInfo user;

    public TaskCompleteCmd(String taskId, String outcome, WfUserInfo user) {
        this.taskId = taskId;
        this.outcome = outcome;
        this.outcomeSpecified = true;
        this.user = user;
        this.status = Integer.valueOf(16);
    }

    public TaskCompleteCmd(String taskId, WfUserInfo user) {
        this.taskId = taskId;
        this.outcomeSpecified = false;
        this.status = Integer.valueOf(16);
        this.user = user;
    }

    public TaskCompleteCmd(String taskId, String outcome, Integer status, WfUserInfo user) {
        this.taskId = taskId;
        this.outcome = outcome;
        this.status = Integer.valueOf(status == null ? 16 : status.intValue());
        this.outcomeSpecified = true;
        this.user = user;
    }

    public Void execute(Environment environment) throws Exception {
        if (StringUtils.isEmpty(this.taskId)) {
            throw new JbpmException("Cannot complete a task with a null or empty taskId");
        }


        DbSession dbSession = (DbSession) environment.get(DbSession.class);
        TaskImpl task = (TaskImpl) dbSession.get(TaskImpl.class, this.taskId);
        if (task == null) {
            throw new IFinFlowException(103003, new Object[]{this.taskId});
        }

        HistoryTaskImpl hisTask = (HistoryTaskImpl) dbSession.get(HistoryTaskImpl.class, this.taskId);

        if (hisTask == null) {
            throw new IFinFlowException(103003, new Object[]{this.taskId});
        }

        if (this.user == null) {
            throw new IFinFlowException(103040, null, new Object[]{this.taskId, hisTask.getOwnerId(), Constant.getStatus(this.status.intValue())});
        }

        IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();
        IWfStaff staff = adapter.getStaffById(this.user.getStaffId());
        if (staff == null) {
            throw new IFinFlowException(105005, new Object[]{this.user.getStaffId()});
        }

        if ((!this.user.getStaffId().equals(hisTask.getOwnerId())) && (5 != this.status.intValue())) {
            throw new IFinFlowException(103040, new Object[]{this.user.getStaffId(), this.taskId, hisTask.getOwnerId(), Constant.getStatus(this.status.intValue())});
        }

        this.user.setStaffName(staff.getStaffName());


        if ((hisTask.getStatus().intValue() == 2) || ((hisTask.getStatus().intValue() == 1) && (this.status.intValue() == 5))) {


            if (this.status.intValue() == 16) {
                ExecutionImpl exe = task.getExecution();
                HistoryActivityInstanceImpl hai = ActivityUtil.getHistoryActIns(exe.getHistoryActivityInstanceDbid());


                String souRejectName = hai.getSouRejectName();


                if ((!StringUtils.isEmpty(souRejectName)) && (3 != task.getAssignMode())) {

                    this.outcome = (task.getName() + " to " + souRejectName);

                    this.status = Integer.valueOf(6);

                    this.outcomeSpecified = true;
                }
            }
            if (this.outcome == null) {
                this.outcome = "";
            }

            if (this.outcomeSpecified) {
                if ((this.status != null) && (this.status.intValue() > 3)) {
                    task.complete(this.outcome, this.status, this.user);
                } else {
                    task.complete(this.outcome, this.user);
                }
            } else {
                task.complete(this.user);
            }
        } else {
            throw new IFinFlowException(103007, new Object[]{this.taskId});
        }
        return null;
    }
}