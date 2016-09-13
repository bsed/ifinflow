 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import com.ruimin.ifinflow.engine.internal.entity.IFinFlowJProcessExcep;
 import com.ruimin.ifinflow.engine.internal.log.ELogVO;
 import org.apache.commons.lang.StringUtils;

 import org.hibernate.Session;
 import org.jbpm.api.cmd.Environment;

 import org.jbpm.pvm.internal.cmd.AbstractCommand;
 import org.jbpm.pvm.internal.cmd.CommandService;
 import org.jbpm.pvm.internal.env.Context;
 import org.jbpm.pvm.internal.env.EnvironmentImpl;
 import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
 import org.jbpm.pvm.internal.model.ActivityImpl;
 import org.jbpm.pvm.internal.model.EventImpl;
 import org.jbpm.pvm.internal.model.ExecutionImpl;

 import org.jbpm.pvm.internal.session.DbSession;

import java.util.Collection;


 
 
 
 public class HandleExceptionCmd
   extends AbstractCommand<Object>
 {
   private static final long serialVersionUID = 1L;
   private ExecutionImpl execution;
   private Exception exception;
   
   public HandleExceptionCmd(ExecutionImpl execution, Exception exception)
   {
     this.execution = execution;
     this.exception = exception;
   }
   
   public Object execute(Environment environment) throws Exception {
     CommandService commandService = (CommandService)environment.get("newTxRequiredCommandService");
     
     return commandService.execute(new HandleCmd());
   }
   
   class HandleCmd extends AbstractCommand<Object> {
     private static final long serialVersionUID = 1L;
     
     HandleCmd() {}
     
     public Collection<String> execute(Environment environment) throws Exception { HandleExceptionCmd.this.execution.waitForSignal();
       
       int exceptionPos = -1;
       
       EventImpl event = HandleExceptionCmd.this.execution.getEvent();
       if (event != null) {
         String eventName = event.getName();
         if (StringUtils.equals("start", eventName)) {
           exceptionPos = 1;
         }
         else if (StringUtils.equals("end", eventName)) {
           exceptionPos = 2;
         }
       }
       else {
         ActivityImpl ai = HandleExceptionCmd.this.execution.getActivity();
         if (StringUtils.equals("3", ai.getType())) {
           exceptionPos = 3;
         }
         else if (StringUtils.equals("4", ai.getType()))
         {
           exceptionPos = 4;
         }
         else if (StringUtils.equals("7", ai.getType()))
         {
           exceptionPos = 5;
         }
         else if (StringUtils.equals("10", ai.getType()))
         {
           exceptionPos = 6;
         }
       }
       
 
 
       Session session = (Session)EnvironmentImpl.getFromCurrent(Session.class);
       StringBuilder hql = new StringBuilder();
       hql.append("FROM ").append(IFinFlowJProcessExcep.class.getName()).append(" T WHERE ").append("T.exceptionPos = ").append(exceptionPos).append(" AND").append(" T.nodeId = '").append(HandleExceptionCmd.this.execution.getActivityName()).append("'").append(" AND").append(" T.executionId = '").append(HandleExceptionCmd.this.execution.getExecutionImplId()).append("'").append(" AND T.status=0");
       
 
 
 
 
 
 
 
 
       IFinFlowJProcessExcep tjpe = (IFinFlowJProcessExcep)session.createQuery(hql.toString()).uniqueResult();
       
 
       DbSession dbSession = (DbSession)EnvironmentImpl.getFromCurrent(DbSession.class);
       
 
       if (tjpe == null)
       {
         ELogVO log = new ELogVO(HandleExceptionCmd.this.exception);
         log.setHacti(HandleExceptionCmd.this.execution.getHistoryActivityInstanceDbid());
         log.setHmprocl(HandleExceptionCmd.this.execution.getProcessInstance().getDbid());
         log.setHproci(HandleExceptionCmd.this.execution.getExecution().getDbid());
         log.setExecutionId(HandleExceptionCmd.this.execution.getExecutionImplId());
         String executionId = null;
         if (HandleExceptionCmd.this.execution.getIsProcessInstance()) {
           executionId = HandleExceptionCmd.this.execution.getDbid();
         } else {
           executionId = HandleExceptionCmd.this.execution.getProcessInstance().getDbid();
         }
         HistoryProcessInstanceImpl hpii = (HistoryProcessInstanceImpl)dbSession.createHistoryProcessInstanceQuery().processId(executionId).uniqueResult();
         
 
         log.setPackageId(hpii.getPackageId());
         log.setTemplateId(hpii.getTemplateId());
         log.setTemplateName(hpii.getTempleteName());
         log.setTemplateVersion(hpii.getTempleteVersion());
         log.setNodeId(HandleExceptionCmd.this.execution.getActivityName());
         log.setNodeKind(HandleExceptionCmd.this.execution.getActivity().getType());
         log.setExceptionPos(exceptionPos);
         log.setExecutionStatus(hpii.getStatus().intValue());
         
 
         hpii.setStatus(Integer.valueOf(512));
         dbSession.save(log.me());
         dbSession.update(hpii);
       } else {
         Context context = ((EnvironmentImpl)environment).getContext("process-engine");
         context.set("repeat", Boolean.TRUE);
       }
       return null;
     }
   }
 }

