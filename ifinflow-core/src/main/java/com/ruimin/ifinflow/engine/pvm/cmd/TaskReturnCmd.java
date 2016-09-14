 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import com.ruimin.ifinflow.engine.flowmodel.vo.WfStaffVO;
 import com.ruimin.ifinflow.engine.pvm.event.HistoryTaskReturn;
 import com.ruimin.ifinflow.engine.pvm.event.WorkloadUpdate;
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import org.hibernate.Session;
 import org.jbpm.api.cmd.Command;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.jpdl.internal.assign.AssignTask;
 import org.jbpm.pvm.internal.history.HistoryEvent;
 import org.jbpm.pvm.internal.task.TaskImpl;

	import java.util.Collection;


 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class TaskReturnCmd
   implements Command<Void>
 {
   private TaskImpl task;
   private WfStaffVO user;
   private static final long serialVersionUID = 1L;
   
   public TaskReturnCmd(TaskImpl task, WfStaffVO user)
   {
     this.task = task;
     this.user = user;
   }
   
   public Void execute(Environment environment)
     throws Exception
   {
     if ((this.user != null) && (this.user.getStaffId() != null) && (!this.task.getAssignee().equals(this.user.getStaffId()))) {
       throw new IFinFlowException(103010, new Object[0]);
     }
     
     String staffId = this.task.getAssignee();
     
     AssignTask assignTask = new AssignTask();
     assignTask.assignTask(this.task.getExecution(), this.task.getTaskDefinition(), this.task);
     
     this.task.setAssignee(null);
     this.task.setStatus(1);
     this.task.setOwnerId(assignTask.getOwnerId());
     this.task.setOwnerRoleId(assignTask.getOwnerRoleId());
     this.task.setOwnerUnitId(assignTask.getOwnerUnitId());
     this.task.setOwnerGroupId(assignTask.getOwnerGroupId());
     this.task.setTakeDate(null);
     Session session = (Session)environment.get(Session.class);
     session.save(this.task);
     
 
     HistoryEvent.fire(new HistoryTaskReturn(assignTask), this.task.getExecution());
     
 
     HistoryEvent.fire(new WorkloadUpdate(staffId, staffId, 0));
     return null;
   }
 }
