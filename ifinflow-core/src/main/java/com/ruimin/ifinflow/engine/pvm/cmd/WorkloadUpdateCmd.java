package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.assign.entity.IFinFlowWorkloadEntity;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.session.DbSession;

public class WorkloadUpdateCmd
        extends AbstractCommand<Void> {
    private static final long serialVersionUID = 1L;
    private String staffId;
    private String staffName;
    private int updateNum;

    public WorkloadUpdateCmd(String staffId, String staffName, int updateNum) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.updateNum = updateNum;
    }

    public Void execute(Environment environment) throws Exception {
        DbSession dbSession = (DbSession) environment.get(DbSession.class);
        IFinFlowWorkloadEntity work = (IFinFlowWorkloadEntity) dbSession.get(IFinFlowWorkloadEntity.class, this.staffId);

        if (work == null) {
            work = new IFinFlowWorkloadEntity();
            work.setStaffId(this.staffId);
            work.setStaffName(this.staffName);
            work.setWorkload(1);
            work.setDoneWorkCount(0L);
            dbSession.save(work);
            return null;
        }

        if (this.updateNum == 1) {
            work.setWorkload(work.getWorkload() + 1);
        } else if (this.updateNum == 0) {
            work.setWorkload(work.getWorkload() - 1);
        } else if (this.updateNum == -1) {
            work.setWorkload(work.getWorkload() - 1);
            work.setDoneWorkCount(work.getDoneWorkCount() + 1L);
        }
        dbSession.update(work);

        return null;
    }
}