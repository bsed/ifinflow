package com.ruimin.ifinflow.engine.pvm.event;

import com.ruimin.ifinflow.engine.assign.entity.IFinFlowWorkloadEntity;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.HistoryEvent;
import org.jbpm.pvm.internal.session.DbSession;

public class WorkloadUpdate extends HistoryEvent {
	private static final long serialVersionUID = 1L;
	private String staffId;
	private String staffName;
	private int updateNum;

	public WorkloadUpdate(String staffId, String staffName, int updateNum) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.updateNum = updateNum;
	}

	public void process() {
		DbSession dbSession = (DbSession) EnvironmentImpl
				.getFromCurrent(DbSession.class);
		IFinFlowWorkloadEntity work = (IFinFlowWorkloadEntity) dbSession.get(
				IFinFlowWorkloadEntity.class, this.staffId);

		if (work == null) {
			work = new IFinFlowWorkloadEntity();
			work.setStaffId(this.staffId);
			work.setStaffName(this.staffName);
			work.setWorkload(1);
			work.setDoneWorkCount(0L);
			dbSession.save(work);
		} else {
			if (2 == this.updateNum) {
				work.setWorkload(work.getWorkload() - 1);

			} else if (1 == this.updateNum) {
				work.setWorkload(work.getWorkload() + 1);
			} else if (0 == this.updateNum) {
				work.setWorkload(work.getWorkload() - 1);
			} else if (-1 == this.updateNum) {
				work.setWorkload(work.getWorkload() - 1);
				work.setDoneWorkCount(work.getDoneWorkCount() + 1L);
			}
			dbSession.update(work);
		}
	}
}
