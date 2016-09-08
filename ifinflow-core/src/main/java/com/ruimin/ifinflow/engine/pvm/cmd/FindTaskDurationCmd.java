package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.internal.vo.TaskStatisticsVo;
import java.util.*;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;

public class FindTaskDurationCmd extends
		AbstractCommand<List<TaskStatisticsVo>> {
	private static final long serialVersionUID = 1L;
	private Date takeStartTime;
	private Date takeEndTime;

	public FindTaskDurationCmd(Date takeStartTime, Date takeEndTime) {
		this.takeStartTime = takeStartTime;
		this.takeEndTime = takeEndTime;
	}

	public List<TaskStatisticsVo> execute(Environment environment) {
		StringBuilder hql = new StringBuilder();
		hql.append("select hti.nodeId, hti.nodeName, hti.createTime from ")
				.append(HistoryTaskImpl.class.getName()).append(" hti")
				.append(" where 1=1 ").append(" and hti.status in (1,2,256)")
				.append(" and hti.createTime >= :takeStartTime")
				.append(" and hti.createTime <= :takeEndTime");

		Query query = ((Session) environment.get(Session.class))
				.createQuery(hql.toString());

		query.setTimestamp("takeStartTime", this.takeStartTime);
		query.setTimestamp("takeEndTime", this.takeEndTime);
		List<TaskStatisticsVo> taskList = null;
		for (Iterator<?> it = query.iterate(); it.hasNext();) {
			Object[] row = (Object[]) it.next();
			if ((row != null) && (row.length > 0)) {
				TaskStatisticsVo taskVo = new TaskStatisticsVo();
				Date takeDate = (Date) row[2];

				if (taskList == null) {
					taskList = new ArrayList();
				}
				taskVo.setNodeId((String) row[0]);
				taskVo.setNodeName((String) row[1]);
				Date current = new Date(System.currentTimeMillis());
				taskVo.setDuraton(String.valueOf((current.getTime() - takeDate
						.getTime()) / 60000L));

				taskList.add(taskVo);
			}
		}
		return taskList;
	}
}
