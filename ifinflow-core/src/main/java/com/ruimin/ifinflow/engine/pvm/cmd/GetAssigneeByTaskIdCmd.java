 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
 import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
 import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
 import com.ruimin.ifinflow.util.DefinitionUtil;
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;
 import org.apache.commons.lang.StringUtils;
 import org.hibernate.Session;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.jpdl.internal.assign.AssignTask;
 import org.jbpm.pvm.internal.cmd.AbstractCommand;
 import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
 import org.jbpm.pvm.internal.session.DbSession;
 import org.jbpm.pvm.internal.task.ParticipationImpl;
 import org.jbpm.pvm.internal.task.TaskImpl;
 
 
 
 
 
 
 
 
 
 
 
 public class GetAssigneeByTaskIdCmd
   extends AbstractCommand<List<String>>
 {
   private static final long serialVersionUID = 1L;
   protected String taskId;
   
   public GetAssigneeByTaskIdCmd(String taskId)
   {
     this.taskId = taskId;
   }
   
   public List<String> execute(Environment environment) throws Exception {
     DbSession dbSession = (DbSession)environment.get(DbSession.class);
     
     List<String> list = new ArrayList();
     TaskImpl task = (TaskImpl)dbSession.get(TaskImpl.class, this.taskId);
     HistoryTaskImpl hisTask = (HistoryTaskImpl)dbSession.get(HistoryTaskImpl.class, this.taskId);
     
     if (task == null) {
       if (hisTask == null) {
         throw new IFinFlowException(103003, new Object[] { this.taskId });
       }
       list.add(hisTask.getAssignee());
       return list;
     }
     NodeVo node = DefinitionUtil.getCacshByTask(task);
     
     if ((hisTask.getAssignMode().equals(Integer.valueOf(2))) && (1 == hisTask.getStatus().intValue()))
     {
       Session session = (Session)environment.get(Session.class);
       
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
       AssignTask assign = new AssignTask();
       assign.assignTask(task.getExecution(), task.getTaskDefinition(), task);
       
       IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();
       
       if (1 == node.getParticipantType()) {
         String staffs = assign.getOwnerId();
         staffs = staffs.replace("[", "").replace("]", "").replace(" ", "");
         String[] staffArray = staffs.split(",");
         
         return Arrays.asList(staffArray);
       }
       
       if (3 == node.getParticipantType()) {
         return identityAdapter.getStaffIdsByUnit(new String[] { assign.getOwnerUnitId() });
       }
       
       if (2 == node.getParticipantType()) {
         return identityAdapter.getStaffIdsByRole(new String[] { assign.getOwnerRoleId() });
       }
       
       if ((4 == node.getParticipantType()) || (6 == node.getParticipantType()))
       {
         return identityAdapter.getStaffIdsByRolesWithUnit(assign.getOwnerUnitId(), new String[] { assign.getOwnerRoleId() });
       }
       
       if (5 == node.getParticipantType()) {
         return identityAdapter.getStaffIdsByGroupId(assign.getOwnerGroupId());
       }
       
       if (3 == node.getParticipantAssign()) {
         String roleId = assign.getOwnerRoleId();
         String unitId = assign.getOwnerUnitId();
         String groupId = assign.getOwnerGroupId();
         String ownerId = assign.getOwnerId();
         List<String> staffs = null;
         
         if (!StringUtils.isEmpty(roleId)) {
           staffs = identityAdapter.getStaffIdsByRole(new String[] { roleId });
           list.addAll(staffs);
         }
         if (!StringUtils.isEmpty(unitId)) {
           staffs = identityAdapter.getStaffIdsByUnit(new String[] { unitId });
           list.addAll(staffs);
         }
         if (!StringUtils.isEmpty(groupId)) {
           staffs = identityAdapter.getStaffIdsByGroupId(groupId);
           list.addAll(staffs); }
         if (!StringUtils.isEmpty(ownerId)) {
           for (String tmp : ownerId.split(",")) {
             list.add(tmp);
           }
         }
       }
     }
     else
     {
       list.add(task.getAssignee());
     }
     
 
     return list;
   }
   
   private static String[] partAssignToIds(List<ParticipationImpl> set) { String[] s = new String[set.size()];
     int i = 0;
     for (ParticipationImpl part : set) {
       s[i] = part.getGroupId();
       i++;
     }
     return s;
   }
 }

