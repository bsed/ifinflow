 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import java.util.ArrayList;
 import java.util.Collection;
  import java.util.List;
 import java.util.Set;
 import org.apache.commons.lang.StringUtils;

 import org.hibernate.Query;
 import org.hibernate.Session;
 import org.hibernate.criterion.Restrictions;
 import org.jbpm.api.cmd.Command;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.api.task.Task;
 import org.jbpm.pvm.internal.exception.ExceptionUtil;
 import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
 import org.jbpm.pvm.internal.session.DbSession;
 import org.jbpm.pvm.internal.task.MultiTaskExists;
 import org.jbpm.pvm.internal.task.TaskImpl;
 
 
 
 
 
 
 
 
 
 
 public class DeleteSubTasksCmd
   implements Command<Boolean>
 {
   private static final long serialVersionUID = -1154667280589986199L;
   private String processId;
   private String nodeId;
   private String taskId;
   private List<String> staffIds;
   private String exitType;
   private int exitCount;
   
   public DeleteSubTasksCmd(String taskId, List<String> staffIds, String exitType, int exitCount)
   {
     this.taskId = taskId;
     this.staffIds = staffIds;
     this.exitType = exitType;
     this.exitCount = exitCount;
   }
   
 
   public DeleteSubTasksCmd(String processId, String nodeId, List<String> staffIds, String exitType, int exitCount)
   {
     this.processId = processId;
     this.nodeId = nodeId;
     this.staffIds = staffIds;
     this.exitType = exitType;
     this.exitCount = exitCount;
   }
   
   public Boolean execute(Environment environment) throws Exception {
     Session session = (Session)environment.get(Session.class);
     TaskImpl task = null;
     if (null != this.taskId) {
       task = (TaskImpl)session.get(TaskImpl.class, this.taskId);
       if (null == task) {
         throw new IFinFlowException(103003, new Object[] { this.taskId });
       }
       if (null != task.getSuperTask()) {
         task = task.getSuperTask();
       }
     } else if (null != this.processId) {
       task = (TaskImpl)session.createCriteria(TaskImpl.class).add(Restrictions.eq("processId", this.processId)).add(Restrictions.eq("activityName", this.nodeId)).add(Restrictions.in("status", new Integer[] { Integer.valueOf(1), Integer.valueOf(2) })).add(Restrictions.isNull("superTask")).uniqueResult();
       
 
 
 
 
 
 
 
       if (null == task) {
         throw new IFinFlowException(103056, new Object[] { this.processId, this.nodeId });
       }
     }
     
 
     if (task.getAssignMode() != 3) {
       throw new IFinFlowException(103055, new Object[] { task.getDbid() });
     }
     
 
     int deleteTaskNum = 0;
     TaskImpl subTask = null;
     Set<Task> subTasks = task.getSubTasks();
     List<String> dbids = new ArrayList();
     for (Task st : subTasks) {
       subTask = (TaskImpl)st;
       if (this.staffIds.contains(st.getAssignee())) {
         dbids.add(subTask.getDbid());
         deleteTaskNum++;
       }
     }
     String hql = "delete from " + HistoryTaskImpl.class.getName() + " where dbid in (:dbids)";
     Query query = session.createQuery(hql);
     query.setParameterList("dbids", dbids);
     query.executeUpdate();
     
     hql = "delete from " + TaskImpl.class.getName() + " where dbid in (:dbids)";
     query = session.createQuery(hql);
     query.setParameterList("dbids", dbids);
     query.executeUpdate();
     
 
     MultiTaskExists te = null;
     if ((StringUtils.isNotBlank(this.exitType)) && (this.exitCount > 0)) {
       te = (MultiTaskExists)session.get(MultiTaskExists.class, task.getDbid());
       
       if (te == null) {
         te = new MultiTaskExists();
         te.setTaskId(task.getDbid());
       }
       te.setExitCount(this.exitCount);
       te.setExitType(this.exitType);
     }
     
     if (subTask.isCompletedMultiTask((DbSession)environment.get(DbSession.class), te, deleteTaskNum, false))
     {
       ExceptionUtil.saveTmpExecution(task.getExecutionId(), task.getActivityName());
       return Boolean.valueOf(true);
     }
     session.save(te);
     
     return Boolean.valueOf(false);
   }
 }

