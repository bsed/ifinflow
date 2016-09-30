/*     */ package org.jbpm.jpdl.internal.activity;
/*     */ 
/*     */ import org.hibernate.Query;
/*     */ import org.hibernate.Session;
/*     */ import org.jbpm.api.JbpmException;
/*     */ import org.jbpm.api.model.OpenExecution;
/*     */ import org.jbpm.internal.log.Log;
/*     */ import org.jbpm.pvm.internal.env.EnvironmentImpl;
/*     */ import org.jbpm.pvm.internal.model.ScopeInstanceImpl;
/*     */ import org.jbpm.pvm.internal.wire.Descriptor;
/*     */ import org.jbpm.pvm.internal.wire.WireContext;
/*     */ import org.jbpm.pvm.internal.wire.descriptor.ListDescriptor;
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
/*     */ public class HqlActivity
/*     */   extends JpdlAutomaticActivity
/*     */ {
/*  41 */   private static final Log log = Log.getLog(HqlActivity.class.getName());
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected String query;
/*     */   protected ListDescriptor parametersDescriptor;
/*     */   protected String resultVariableName;
/*     */   protected boolean isResultUnique;
/*     */   
/*     */   public void perform(OpenExecution execution)
/*     */   {
/*  51 */     EnvironmentImpl environment = EnvironmentImpl.getCurrent();
/*  52 */     if (environment == null) {
/*  53 */       throw new JbpmException("no environment for jpdl activity hql");
/*     */     }
/*  55 */     Session session = (Session)environment.get(Session.class);
/*     */     
/*  57 */     Query q = createQuery(session);
/*     */     
/*  59 */     if (this.parametersDescriptor != null) {
/*  60 */       for (Descriptor valueDescriptor : this.parametersDescriptor.getValueDescriptors()) {
/*  61 */         String parameterName = valueDescriptor.getName();
/*  62 */         Object value = WireContext.create(valueDescriptor, (ScopeInstanceImpl)execution);
/*  63 */         applyParameter(q, parameterName, value);
/*     */       }
/*     */     }
/*     */     
/*  67 */     Object result = null;
/*  68 */     if (this.isResultUnique) {
/*  69 */       result = q.uniqueResult();
/*     */     } else {
/*  71 */       result = q.list();
/*     */     }
/*     */     
/*  74 */     execution.setVariable(this.resultVariableName, result);
/*     */   }
/*     */   
/*     */   protected Query createQuery(Session session) {
/*  78 */     return session.createQuery(this.query);
/*     */   }
/*     */   
/*     */   public void applyParameter(Query q, String parameterName, Object value) {
/*  82 */     if ((value instanceof String)) {
/*  83 */       q.setString(parameterName, (String)value);
/*  84 */     } else if ((value instanceof Long)) {
/*  85 */       q.setLong(parameterName, ((Long)value).longValue());
/*     */     } else {
/*  87 */       log.error("unknown hql parameter type: " + value.getClass().getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public void setQuery(String query) {
/*  92 */     this.query = query;
/*     */   }
/*     */   
/*  95 */   public void setParametersDescriptor(ListDescriptor parametersDescriptor) { this.parametersDescriptor = parametersDescriptor; }
/*     */   
/*     */   public void setResultUnique(boolean isResultUnique) {
/*  98 */     this.isResultUnique = isResultUnique;
/*     */   }
/*     */   
/* 101 */   public void setResultVariableName(String resultVariableName) { this.resultVariableName = resultVariableName; }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/HqlActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */