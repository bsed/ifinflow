package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.flowmodel.vo.TaskBusinessVO;
import com.ruimin.ifinflow.util.StringHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;

public class QueryTaskBusinessListCmd implements Command<List<TaskBusinessVO>> {
	private static final long serialVersionUID = 1L;
	private Date startTime1;
	private Date startTime2;

	public QueryTaskBusinessListCmd(Date startTime1, Date startTime2) {
		this.startTime1 = startTime1;
		this.startTime2 = startTime2;
	}

	public List<TaskBusinessVO> execute(Environment environment)
			throws Exception {
		Session session = (Session) environment.get(Session.class);

		Criteria criteria = session.createCriteria(HistoryTaskImpl.class);

		if (null != this.startTime1) {
			criteria.add(Restrictions.ge("createTime", this.startTime1));
		}

		if (null != this.startTime2) {
			criteria.add(Restrictions.le("createTime", this.startTime2));
		}

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("packageId"));
		projectionList.add(Projections.groupProperty("templateId"));
		projectionList.add(Projections.groupProperty("templateVersion"));
		projectionList.add(Projections.groupProperty("status"));
		projectionList.add(Projections.rowCount());

		criteria.setProjection(projectionList);

		List<Object[]> histasks = criteria.list();

		List<TaskBusinessVO> results = new ArrayList();
		TaskBusinessVO tbvo = null;
		String tKey = null;

		for (Object[] tmps : histasks) {
			tKey = tmps[0] + "_" + tmps[1] + "-" + tmps[2];

			tbvo = new TaskBusinessVO(tKey, "0", "0", "0", "0", "0", "0");

			if (results.contains(tbvo)) {
				tbvo = (TaskBusinessVO) results.get(results.indexOf(tbvo));
			} else {
				results.add(tbvo);
			}

			int ti = 0;
			if (tmps[3] != null) {
				ti = ((Integer) tmps[3]).intValue();
				if ((1 == ti) || (2 == ti)) {
					tbvo.setTodoNum(StringHelper.stringPlusN(tbvo.getTodoNum(),
							(Integer) tmps[4]));
				} else if ((16 == ti) || (6 == ti)) {
					tbvo.setDoneNum(StringHelper.stringPlusN(tbvo.getDoneNum(),
							(Integer) tmps[4]));
				} else if (4 == ti) {
					tbvo.setBackNum(StringHelper.stringPlusN(tbvo.getBackNum(),
							(Integer) tmps[4]));
				} else if (512 == ti) {
					tbvo.setExceptionNum(StringHelper.stringPlusN(
							tbvo.getExceptionNum(), (Integer) tmps[4]));
				} else if (256 == ti) {
					tbvo.setOvertimeNum(StringHelper.stringPlusN(
							tbvo.getOvertimeNum(), (Integer) tmps[4]));
				}
			}

			tbvo.setTotal(StringHelper.stringPlusN(tbvo.getTotal(),
					(Integer) tmps[4]));
		}

		return results;
	}
}
