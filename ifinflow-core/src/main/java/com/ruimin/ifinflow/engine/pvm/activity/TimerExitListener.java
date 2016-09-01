 package com.ruimin.ifinflow.engine.pvm.activity;
 
 import com.ruimin.ifinflow.engine.external.adapter.IBusinessAdapter;
 import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
 import com.ruimin.ifinflow.engine.flowmodel.util.BeanUtil;
 import com.ruimin.ifinflow.engine.flowmodel.util.VariableUtil;
 import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;
 import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
 import com.ruimin.ifinflow.engine.internal.log.LogCategory;
 import com.ruimin.ifinflow.engine.internal.log.WorkflowLog;
 import com.ruimin.ifinflow.engine.internal.log.vo.AppLog;
 import com.ruimin.ifinflow.util.DatetimeUtil;
 import java.util.Date;
 import java.util.List;
 import org.apache.commons.lang.StringUtils;
 import org.apache.log4j.Level;
 import org.hibernate.Session;
 import org.jbpm.api.history.HistoryProcessInstance;
 import org.jbpm.api.history.HistoryProcessInstanceQuery;
 import org.jbpm.api.job.Job;
 import org.jbpm.api.job.Timer;
 import org.jbpm.api.listener.EventListener;
 import org.jbpm.api.listener.EventListenerExecution;
 import org.jbpm.internal.log.Log;
 import org.jbpm.pvm.internal.env.EnvironmentImpl;
 import org.jbpm.pvm.internal.exception.ExceptionUtil;
 import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
 import org.jbpm.pvm.internal.job.JobImpl;
 import org.jbpm.pvm.internal.model.ActivityImpl;
 import org.jbpm.pvm.internal.model.ExecutionImpl;
 import org.jbpm.pvm.internal.model.TimerActivityImpl;
 import org.jbpm.pvm.internal.query.HistoryProcessInstanceQueryImpl;
 import org.jbpm.pvm.internal.session.DbSession;
 import org.jbpm.pvm.internal.session.TimerSession;
 import org.jbpm.pvm.internal.util.Clock;
 
 
 
 
 
 
 
 
 
 public class TimerExitListener
   implements EventListener
 {
   private static final long serialVersionUID = 1L;
   private static final Log logg = Log.getLog(TimerExitListener.class.getName());
   
 
   private String duration;
   
 
   private String repeat;
   
 
   private String duedate;
   
 
   private int cycleCount;
   
   private String classType;
   
   private String extendsclass;
   
 
   public void notify(EventListenerExecution execution)
     throws Exception
   {
     notify((ExecutionImpl)execution);
   }
   
   public void notify(ExecutionImpl execution)
     throws Exception
   {
     IBusinessAdapter iba = UserExtendsReference.getBusinessAdapter(this.extendsclass, this.classType, execution.getActivityName());
     
     VariableSet vs = VariableUtil.getVariableSet(execution);
     
 
     AppLog log = new AppLog(LogCategory.TIMERNODE, this.extendsclass + ".invoke", new String[] { "invokeParams" }, new Object[] { vs });
     
 
     log.setProcessPoid(execution.getDbid());
     log.setActionType("10");
     log.setActionName(execution.getActivityName());
     log.setMpid(execution.getProcessInstance().getDbid());
     WorkflowLog.dbLog(Level.INFO, log);
     
     DbSession session = (DbSession)EnvironmentImpl.getFromCurrent(DbSession.class, false);
     
     HistoryProcessInstance hpii = session.createHistoryProcessInstanceQuery().processId(execution.getDbid()).uniqueResult();
     
 
     ProcessVO process = BeanUtil.createProcessVO(hpii);
     
     vs = iba.invoke(vs, process);
     
     if (vs != null) {
       execution.setVariables(vs.getList());
     }
     
     if (!isComplete(execution)) {
       return;
     }
     
 
 
 
 
 
 
 
     logg.debug("TimerExitListener 125 待推进 execution.getActivity().getName()=" + execution.getActivity().getName());
     ExceptionUtil.saveTmpExecution(execution.getExecutionImplId(), execution.getActivity().getName());
   }
   
 
 
   public void destroyTimers(ExecutionImpl execution)
   {
     TimerSession timerSession = (TimerSession)EnvironmentImpl.getFromCurrent(TimerSession.class, false);
     
 
     if (timerSession != null)
     {
       List<Timer> timers = timerSession.findTimersByExecution(execution);
       
       for (Timer timer : timers) {
         Job job = (Job)EnvironmentImpl.getFromCurrent(JobImpl.class, false);
         if (timer == job) {}
       }
     }
   }
   
 
   private boolean isComplete(ExecutionImpl execution)
   {
     Session session = (Session)EnvironmentImpl.getFromCurrent(Session.class);
     
     if (this.cycleCount > 1) {
       TimerActivityImpl ta = (TimerActivityImpl)session.get(TimerActivityImpl.class, execution.getHistoryActivityInstanceDbid());
       
 
       if (ta == null) {
         ta = new TimerActivityImpl();
         ta.setHisActIns(execution.getHistoryActivityInstanceDbid());
         ta.setCylteCount(1);
         session.save(ta);
         return false;
       }
       if (this.cycleCount == ta.getCylteCount() + 1) {
         session.delete(ta);
       } else if (this.cycleCount > ta.getCylteCount() + 1) {
         ta.setCylteCount(ta.getCylteCount() + 1);
         session.update(ta);
         return false;
       }
       
     }
     else if (!StringUtils.isEmpty(this.duration)) {
       HistoryActivityInstanceImpl act = (HistoryActivityInstanceImpl)session.get(HistoryActivityInstanceImpl.class, execution.getHistoryActivityInstanceDbid());
       
 
       Date realStartdate = DatetimeUtil.calculateDueDate(act.getStartTime(), this.duedate);
       
 
       Date realEnddate = DatetimeUtil.calculateDueDate(realStartdate, this.duration);
       
 
       Date nextJobDate = DatetimeUtil.calculateDueDate(Clock.getTime(), this.repeat);
       
       if (nextJobDate.getTime() < realEnddate.getTime()) {
         return false;
       }
     }
     return true;
   }
 }

