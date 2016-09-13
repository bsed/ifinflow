
package com.ruimin.ifinflow.engine.pvm.cmd;


import com.ruimin.ifinflow.engine.flowmodel.vo.TaskPVMVO;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.util.DatetimeUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.util.CollectionUtil;


public class QueryTaskPvmListCmd
        implements Command<List<TaskPVMVO>> {
    private static final long serialVersionUID = 1L;
    private List<?> ogetOrganization;
    private int frequency;
    private final int SCOPE_1 = 1;
    private final int SCOPE_5 = 5;
    private final int SCOPE_30 = 30;

    private final int FREQUENCY_15 = 15;
    private final int FREQUENCY_30 = 30;
    private final int FREQUENCY_45 = 45;
    private final int FREQUENCY_60 = 60;

    public QueryTaskPvmListCmd(List<?> ogetOrganization, int frequency) {
        this.ogetOrganization = ogetOrganization;
        this.frequency = frequency;
    }

    public List<TaskPVMVO> execute(Environment environment)
            throws Exception {
        if ((this.ogetOrganization == null) || (this.ogetOrganization.isEmpty())) {
            return null;
        }


        List<TaskPVMVO> tbvos = new ArrayList();

        if (this.frequency == 15) {

            for (String st : this.timeScope15) {

                tbvos.add(new TaskPVMVO(st, "0", "0", "0"));

            }

        } else if (this.frequency == 30) {

            for (String st : this.timeScope30) {

                tbvos.add(new TaskPVMVO(st, "0", "0", "0"));

            }

        } else if (this.frequency == 45) {

            for (String st : this.timeScope45) {

                tbvos.add(new TaskPVMVO(st, "0", "0", "0"));

            }

        } else if (this.frequency == 60) {

            for (String st : this.timeScope60) {

                tbvos.add(new TaskPVMVO(st, "0", "0", "0"));
            }
        }
        Session session = (Session) environment.get(Session.class);
        count(session, tbvos, 1);
        count(session, tbvos, 5);
        count(session, tbvos, 30);
        for (TaskPVMVO tv : tbvos) {
            tv.setFiveDaysAvg(new BigDecimal(tv.getFiveDaysAvg()).divide(new BigDecimal(5), 1, 4).toString());
            tv.setThirtyDaysAvg(new BigDecimal(tv.getThirtyDaysAvg()).divide(new BigDecimal(30), 1, 4).toString());
        }
        return tbvos;
    }

    private void count(Session session, List<TaskPVMVO> tbvos, int nday) {
        List<HistoryTaskImpl> histasks = CollectionUtil.checkList(buildCriteria(session, nday).list(), HistoryTaskImpl.class);
        String ts = null;

        int tmpIdx = -1;

        TaskPVMVO tbv = null;


        for (HistoryTaskImpl task : histasks) {

            try {

                ts = getTimeScope(task.getCreateTime());

            } catch (Exception e) {

                tbv = (TaskPVMVO) tbvos.get(tbvos.size() - 1);

            }

            tmpIdx = (int) (DatetimeUtil.getBetweenBaseTime(task.getCreateTime()) / this.frequency);


            if ((tmpIdx < 0) || (tmpIdx >= tbvos.size())) {

                tbv = (TaskPVMVO) tbvos.get(tbvos.size() - 1);

            } else {
                tbv = (TaskPVMVO) tbvos.get(tmpIdx);
            }
            if (nday == 1) {
                tbv.setTodayAvg(String.valueOf(Long.parseLong(tbv.getTodayAvg()) + 1L));
            } else if (nday == 5) {
                tbv.setFiveDaysAvg(String.valueOf(Long.parseLong(tbv.getFiveDaysAvg()) + 1L));
            } else if (nday == 30) {
                tbv.setThirtyDaysAvg(String.valueOf(Long.parseLong(tbv.getThirtyDaysAvg()) + 1L));
            }
        }
    }

    private String getTimeScope(Date time) {
        int a = (int) (DatetimeUtil.getBetweenBaseTime(time) / this.frequency);
        String res = null;
        if (this.frequency == 15) {
            res = this.timeScope15[a];
        } else if (this.frequency == 30) {
            res = this.timeScope30[a];
        } else if (this.frequency == 45) {
            res = this.timeScope45[a];
        } else if (this.frequency == 60) {
            res = this.timeScope60[a];
        }
        return res;
    }

    private final String[] timeScope15 = {"8:00-8:15", "8:15-8:30", "8:30-8:45", "8:45-9:00", "9:00-9:15", "9:15-9:30", "9:30-9:45", "9:45-10:00", "10:00-10:15", "10:15-10:30", "10:30-10:45", "10:45-11:00", "11:00-11:15", "11:15-11:30", "11:30-11:45", "11:45-12:00", "12:00-12:15", "12:15-12:30", "12:30-12:45", "12:45-13:00", "13:00-13:15", "13:15-13:30", "13:30-13:45", "13:45-14:00", "14:00-14:15", "14:15-14:30", "14:30-14:45", "14:45-15:00", "15:00-15:15", "15:15-15:30", "15:30-15:45", "15:45-16:00", "16:00-16:15", "16:15-16:30", "16:30-16:45", "16:45-17:00", "17:00-17:15", "17:15-17:30", "17:30-17:45", "17:45-18:00", "其他"};
    private final String[] timeScope30 = {"8:00-8:30", "8:30-9:00", "9:00-9:30", "9:30-10:00", "10:00-10:30", "10:30-11:00", "11:00-11:30", "11:30-12:00", "12:00-12:30", "12:30-13:00", "13:00-13:30", "13:30-14:00", "14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00", "16:00-16:30", "16:30-17:00", "17:00-17:30", "17:30-18:00", "其他"};
    private final String[] timeScope45 = {"8:00-8:45", "8:45-9:30", "9:30-10:15", "10:15-11:00", "11:00-11:45", "11:45-12:30", "12:30-13:15", "13:15-14:00", "14:00-14:45", "14:45-15:30", "15:30-16:15", "16:15-17:00", "17:00-17:45", "17:45-18:00", "其他"};
    private final String[] timeScope60 = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00", "其他"};

    private Criteria buildCriteria(Session session, int nDay) {
        List<String> staffIds = UserExtendsReference.getIdentityAdapter().getStaffIdsByUnit((String[]) this.ogetOrganization.toArray(new String[this.ogetOrganization.size()]));
        Criteria criteria = session.createCriteria(HistoryTaskImpl.class);
        if (!staffIds.isEmpty()) {
            criteria.add(Restrictions.or(Restrictions.in("ownerUnitId", this.ogetOrganization), Restrictions.in("ownerId", staffIds)));
        } else {
            criteria.add(Restrictions.in("ownerUnitId", this.ogetOrganization));
        }
        criteria.add(Restrictions.ge("createTime", DatetimeUtil.getDay(nDay)));
        return criteria;
    }
}