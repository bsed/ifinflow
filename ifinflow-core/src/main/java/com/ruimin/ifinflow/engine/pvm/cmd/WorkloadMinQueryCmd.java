package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.assign.entity.IFinFlowWorkloadEntity;
import com.ruimin.ifinflow.engine.pvm.event.WorkloadUpdate;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.HistoryEvent;
import org.jbpm.pvm.internal.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkloadMinQueryCmd extends AbstractCommand<String> {
	private static final long serialVersionUID = 1L;
	private String[] staffs;

	public WorkloadMinQueryCmd(String... staffs) {
		this.staffs = staffs;
	}

	public String execute(Environment environment) throws Exception {
		Session session = (Session) environment.get(Session.class);

		List<String> staffIds = new ArrayList();
		staffIds.addAll(Arrays.asList(this.staffs));
		Query query = session
				.createQuery("select wl from "
						+ IFinFlowWorkloadEntity.class.getName()
						+ "  wl where wl.staffId in (:staffIds) order by wl.workload asc");

		query.setParameterList("staffIds", this.staffs);
		List<IFinFlowWorkloadEntity> list = CollectionUtil.checkList(
				query.list(), IFinFlowWorkloadEntity.class);

		for (IFinFlowWorkloadEntity obj : list)
			staffIds.remove(obj.getStaffId());
		String minWorkStaffId;
		try {
			if (staffIds.isEmpty()) {
				minWorkStaffId = (list.get(0)).getStaffId();
			} else {
				minWorkStaffId = staffIds.get(0);
			}
			HistoryEvent.fire(new WorkloadUpdate(minWorkStaffId,
					minWorkStaffId, 1));
		} catch (Exception e) {
			throw new IFinFlowException(103039, e,
					new Object[] { Arrays.toString(this.staffs) });
		}
		return minWorkStaffId;
	}
}