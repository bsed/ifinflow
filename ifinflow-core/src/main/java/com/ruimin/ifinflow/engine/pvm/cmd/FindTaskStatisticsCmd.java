package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.internal.vo.TaskStatisticsVo;

import java.util.*;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;

public class FindTaskStatisticsCmd
        extends AbstractCommand<List<TaskStatisticsVo>> {
    private static final long serialVersionUID = 1L;
    private String packageId;
    private String templateId;
    private int version;
    private Date startTime;
    private Date endTime;

    public FindTaskStatisticsCmd(String packageId, String templateId, int version, Date startTime, Date endTime) {
        this.packageId = packageId;
        this.templateId = templateId;
        this.version = version;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public List<TaskStatisticsVo> execute(Environment environment) {
        StringBuilder hql = new StringBuilder();
        hql.append("select hti.nodeId, hti.nodeName, hti.status, count(hti.status) from ").append(HistoryTaskImpl.class.getName()).append(" hti").append(" where hti.packageId = '").append(this.packageId).append("'").append(" and hti.templateId = '").append(this.templateId).append("'").append(" and hti.templateVersion = ").append(this.version).append(" and hti.createTime >= :takeStartTime").append(" and hti.createTime <= :takeEndTime").append(" group by hti.nodeId, hti.nodeName, hti.status");
        Query query = ((Session) environment.get(Session.class)).createQuery(hql.toString());
        query.setTimestamp("takeStartTime", this.startTime);
        query.setTimestamp("takeEndTime", this.endTime);
        List<TaskStatisticsVo> taskList = null;
        Map<String, TaskStatisticsVo> taskMap = null;
        for (Iterator<?> it = query.iterate(); it.hasNext(); ) {
            Object[] row = (Object[]) it.next();
            if ((row != null) && (row.length > 0)) {
                TaskStatisticsVo taskVo = null;
                String nodeId = (String) row[0];
                int status = ((Integer) row[2]).intValue();

                if (taskList == null) {
                    taskList = new ArrayList();
                    taskMap = new HashMap();
                }

                if (!taskMap.containsKey(nodeId)) {
                    taskVo = new TaskStatisticsVo();
                    taskVo.setNodeId(nodeId);
                    taskVo.setNodeName((String) row[1]);
                    taskMap.put(nodeId, taskVo);
                } else {
                    taskVo = (TaskStatisticsVo) taskMap.get(nodeId);
                }
                switch (status) {
                    case 1:
                        taskVo.setTodoCount(String.valueOf(Long.parseLong(taskVo.getTodoCount()) + ((Long) row[3]).longValue()));
                        break;
                    case 2:
                        taskVo.setTodoCount(String.valueOf(Long.parseLong(taskVo.getTodoCount()) + ((Long) row[3]).longValue()));
                        break;
                    case 3:
                        taskVo.setSuspendCount(String.valueOf(row[3]));
                        break;
                    case 4:
                        taskVo.setRejectCount(String.valueOf(row[3]));
                        break;
                    case 6:
                        taskVo.setDoneCount(String.valueOf(Long.parseLong(taskVo.getDoneCount()) + ((Long) row[3]).longValue()));
                        break;
                    case 16:
                        taskVo.setDoneCount(String.valueOf(Long.parseLong(taskVo.getDoneCount()) + ((Long) row[3]).longValue()));
                        break;
                    case 256:
                        taskVo.setTodoCount(String.valueOf(Long.parseLong(taskVo.getTodoCount()) + ((Long) row[3]).longValue()));
                        break;
                    case 512:
                        taskVo.setExceptionStatusCount(String.valueOf(row[3]));
                }

            }
        }
        if ((taskMap != null) && (!taskMap.isEmpty())) {
            for (Map.Entry<String, TaskStatisticsVo> entry : taskMap.entrySet()) {
                taskList.add(entry.getValue());
            }
        }
        return taskList;
    }
}