/*     */ package org.jbpm.jpdl.internal.activity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.jbpm.api.JbpmException;
/*     */ import org.jbpm.api.activity.ActivityExecution;
/*     */ import org.jbpm.api.model.Activity;
/*     */ import org.jbpm.api.model.Transition;
/*     */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroupActivity
/*     */   extends JpdlExternalActivity
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public void execute(ActivityExecution execution)
/*     */   {
/*  43 */     execute((ExecutionImpl)execution);
/*     */   }
/*     */   
/*     */   public void execute(ExecutionImpl execution)
/*     */   {
/*  48 */     Activity activity = execution.getActivity();
/*  49 */     List<Activity> startActivities = findStartActivities(activity);
/*  50 */     ExecutionImpl concurrentRoot; if (startActivities.size() == 1) {
/*  51 */       execution.execute((Activity)startActivities.get(0));
/*     */     }
/*     */     else {
/*  54 */       concurrentRoot = null;
/*  55 */       if ("active-root".equals(execution.getState())) {
/*  56 */         concurrentRoot = execution;
/*  57 */       } else if ("active-concurrent".equals(execution.getState())) {
/*  58 */         concurrentRoot = execution.getParent();
/*     */       }
/*     */       else {
/*  61 */         throw new JbpmException("illegal state");
/*     */       }
/*     */       
/*  64 */       for (Activity startActivity : startActivities) {
/*  65 */         ExecutionImpl concurrentExecution = concurrentRoot.createExecution();
/*  66 */         concurrentExecution.setState("active-concurrent");
/*  67 */         concurrentExecution.execute(startActivity);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private List<Activity> findStartActivities(Activity activity) {
/*  73 */     List<Activity> startActivities = new ArrayList();
/*  74 */     List<? extends Activity> nestedActivities = activity.getActivities();
/*  75 */     for (Activity nestedActivity : nestedActivities) {
/*  76 */       if ((nestedActivity.getIncomingTransitions() == null) || (nestedActivity.getIncomingTransitions().isEmpty()))
/*     */       {
/*     */ 
/*  79 */         startActivities.add(nestedActivity);
/*     */       }
/*     */     }
/*  82 */     return startActivities;
/*     */   }
/*     */   
/*     */   public void signal(ActivityExecution execution, String signalName, Map<String, ?> parameters) throws Exception {
/*  86 */     signal((ExecutionImpl)execution, signalName, parameters);
/*     */   }
/*     */   
/*     */   public void signal(ExecutionImpl execution, String signalName, Map<String, ?> parameters) throws Exception {
/*  90 */     Transition transition = null;
/*  91 */     Activity activity = execution.getActivity();
/*  92 */     List<? extends Transition> outgoingTransitions = activity.getOutgoingTransitions();
/*     */     
/*  94 */     int nbrOfOutgoingTransitions = outgoingTransitions != null ? outgoingTransitions.size() : 0;
/*  95 */     if ((signalName == null) && (nbrOfOutgoingTransitions == 1))
/*     */     {
/*     */ 
/*  98 */       transition = (Transition)outgoingTransitions.get(0);
/*     */     } else {
/* 100 */       transition = activity.getOutgoingTransition(signalName);
/*     */     }
/*     */     
/* 103 */     execution.take(transition);
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/GroupActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */