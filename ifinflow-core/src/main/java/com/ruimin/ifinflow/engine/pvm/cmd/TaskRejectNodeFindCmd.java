 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
 import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;
 import com.ruimin.ifinflow.util.TemplateCacheUtil;
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import java.util.ArrayList;
 import java.util.List;
 import org.apache.commons.lang.StringUtils;
 import org.jbpm.api.HistoryService;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.api.history.HistoryActivityInstance;
 import org.jbpm.api.history.HistoryActivityInstanceQuery;
 import org.jbpm.api.model.Transition;
 import org.jbpm.pvm.internal.cmd.AbstractCommand;
 import org.jbpm.pvm.internal.model.ActivityImpl;
 import org.jbpm.pvm.internal.model.ExecutionImpl;
 import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
 import org.jbpm.pvm.internal.session.DbSession;
 import org.jbpm.pvm.internal.session.RepositorySession;
 import org.jbpm.pvm.internal.task.TaskImpl;
 
 
 
 
 
 
 
 
 
 public class TaskRejectNodeFindCmd
   extends AbstractCommand<List<String>>
 {
   private static final long serialVersionUID = 1L;
   private String taskId;
   private HistoryService historyService;
   
   public TaskRejectNodeFindCmd(String taskId, HistoryService historyService)
   {
     this.taskId = taskId;
     this.historyService = historyService;
   }
   
   public List<String> execute(Environment environment) throws Exception
   {
     DbSession dbSession = (DbSession)environment.get(DbSession.class);
     TaskImpl task = (TaskImpl)dbSession.get(TaskImpl.class, this.taskId);
     if (task == null) {
       throw new IFinFlowException(103003, new Object[] { this.taskId });
     }
     
     RepositorySession repositorySession = (RepositorySession)environment.get(RepositorySession.class);
     
     ProcessDefinitionImpl procDef = repositorySession.findProcessDefinitionById(task.getProcessInstance().getProcessDefinitionId());
     
 
 
     List<String> priorNodeListInPd = getPriorNodeListByProcDef(task.getActivityName(), procDef);
     
 
 
     List<HistoryActivityInstance> activityList = this.historyService.createHistoryActivityInstanceQuery().processId(task.getProcessInstance().getDbid()).list();
     if (activityList.isEmpty()) {
       throw new IFinFlowException(109002, new Object[0]);
     }
     List<String> rejectList = getPriorNodeList(priorNodeListInPd, activityList);
     
 
     List<HistoryActivityInstance> parentActivityList = null;
     ExecutionImpl parentExecution = task.getExecution().getSuperProcessExecution();
     if (parentExecution != null) {
       parentActivityList = this.historyService.createHistoryActivityInstanceQuery().processId(parentExecution.getDbid()).list();
     }
     List<String> parentExecRejectList = null;
     if ((parentActivityList != null) && (rejectList != null)) {
       for (HistoryActivityInstance hai : parentActivityList) {
         NodeVo nodeVo = TemplateCacheUtil.getNodeVo(parentExecution.getProcessDefinition(), hai.getActivityName());
         if (StringUtils.equals("12", String.valueOf(nodeVo.getType())))
         {
 
           rejectList.add(hai.getActivityName() + ";" + nodeVo.getName());
           
           parentExecRejectList = getPriorNodeList(getPriorNodeListByProcDef(hai.getActivityName(), parentExecution.getProcessDefinition()), parentActivityList);
           
           rejectList.addAll(parentExecRejectList);
           break;
         }
       }
     }
     
     return rejectList;
   }
   
 
 
 
 
 
 
 
   private List<String> getPriorNodeListByProcDef(String nodeId, ProcessDefinitionImpl procDef)
   {
     List<String> priorNodeListInPd = new ArrayList();
     
     getPriorNodeListInProcDef(procDef.getActivity(nodeId), procDef, priorNodeListInPd);
     if ((priorNodeListInPd.isEmpty()) && (priorNodeListInPd.size() <= 1)) {
       throw new IFinFlowException(109008, new Object[0]);
     }
     if (((String)priorNodeListInPd.get(0)).contains(nodeId))
     {
       priorNodeListInPd.remove(0);
     }
     
     return priorNodeListInPd;
   }
   
 
 
 
 
 
 
   private List<String> getPriorNodeList(List<String> priorNodesInPd, List<HistoryActivityInstance> activityList)
   {
     int size = priorNodesInPd.size();
     List<String> resultList = new ArrayList();
     resultList.addAll(priorNodesInPd);
     for (int i = 0; i < size; i++) {
       if (!isExistInAtivityList(((String)priorNodesInPd.get(i)).split(";", 2)[0], activityList)) {
         resultList.remove(priorNodesInPd.get(i));
       }
     }
     return resultList;
   }
   
 
   private boolean isExistInAtivityList(String activityName, List<HistoryActivityInstance> activityList)
   {
     for (HistoryActivityInstance hisactivity : activityList) {
       if (activityName.equals(hisactivity.getActivityName())) {
         return true;
       }
     }
     return false;
   }
   
 
 
 
 
 
 
 
 
 
 
 
   private void getPriorNodeListInProcDef(ActivityImpl activityImpl, ProcessDefinitionImpl procDef, List<String> result)
   {
     String nodeId = activityImpl.getName();
     NodeVo nodevo = TemplateCacheUtil.getTemplateVo(procDef).getNodeVo(nodeId);
     
 
 
     if ((!StringUtils.equals("6", activityImpl.getType())) && (!StringUtils.equals("1", activityImpl.getType())) && (isRejectable(nodevo)))
     {
 
 
       StringBuilder sb = new StringBuilder();
       sb.append(nodeId);
       sb.append(";");
       sb.append(nodevo.getName());
       
       if (!result.contains(sb.toString())) {
         result.add(sb.toString());
       } else {
         return;
       }
     }
     
     List<? extends Transition> transitionList = activityImpl.getIncomingTransitions();
     
 
     if ((!transitionList.isEmpty()) && (!StringUtils.equals("1", activityImpl.getType())))
     {
       for (Transition tran : transitionList) {
         if (!StringUtils.equals("1", ((ActivityImpl)tran.getSource()).getType())) {
           getPriorNodeListInProcDef((ActivityImpl)tran.getSource(), procDef, result);
         }
       }
     }
   }
   
 
 
 
 
 
 
 
 
   private boolean isRejectable(NodeVo nodevo)
   {
     String rejectAuth = nodevo.getRejectAuth();
     if ((StringUtils.isNotBlank(rejectAuth)) && 
       (rejectAuth.length() == 2) && (rejectAuth.endsWith("1"))) {
       return true;
     }
     
     return false;
   }
 }
