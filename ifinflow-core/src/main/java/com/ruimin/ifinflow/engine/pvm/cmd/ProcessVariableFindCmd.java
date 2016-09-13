/*    */ package com.ruimin.ifinflow.engine.pvm.cmd;
/*    */ 
/*    */ import com.ruimin.ifinflow.engine.flowmodel.Variable;
/*    */ import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
/*    */ import com.ruimin.ifinflow.util.exception.IFinFlowException;
/*    */ import java.util.Collection;
import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */
/*    */ import org.hibernate.Session;
/*    */ import org.hibernate.criterion.Restrictions;
/*    */ import org.jbpm.api.cmd.Environment;
/*    */ import org.jbpm.pvm.internal.cmd.AbstractCommand;
/*    */ import org.jbpm.pvm.internal.history.model.HistoryVariableImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProcessVariableFindCmd
/*    */   extends AbstractCommand<VariableSet>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected String processId;
/*    */   protected Set<String> variableNames;
/*    */   
/*    */   public ProcessVariableFindCmd(String processId)
/*    */   {
/* 38 */     this.processId = processId;
/*    */   }
/*    */   
/*    */   public ProcessVariableFindCmd(String processId, Set<String> variableNames) {
/* 42 */     this.processId = processId;
/* 43 */     this.variableNames = variableNames;
/*    */   }
/*    */   
/*    */ 
/*    */   public VariableSet execute(Environment environment)
/*    */     throws Exception
/*    */   {
/* 50 */     if (StringUtils.isEmpty(this.processId)) {
/* 51 */       throw new IFinFlowException(102005, new Object[0]);
/*    */     }
/* 53 */     VariableSet vs = new VariableSet();
/*    */     
/* 55 */     Session session = (Session)environment.get(Session.class);
/*    */     
/* 57 */     List<HistoryVariableImpl> varSet = session.createCriteria(HistoryVariableImpl.class).add(Restrictions.eq("processId", this.processId)).list();
/*    */     
/*    */ 
/*    */ 
/* 61 */     if ((varSet == null) || (varSet.isEmpty())) {
/* 62 */       return vs;
/*    */     }
/*    */     
/*    */ 
/* 66 */     if ((this.variableNames == null) || (this.variableNames.isEmpty())) {
/* 67 */       for (HistoryVariableImpl hisvar : varSet)
/*    */       {
/* 69 */         vs.addVariable(new Variable(hisvar.getVariableName(), hisvar.getKind().intValue(), Variable.valueOf(hisvar.getKind().intValue(), hisvar.getValue()), hisvar.getBizName()));
/*    */       }
/*    */       
/*    */     }
/*    */     else {
/* 74 */       for (HistoryVariableImpl hisvar : varSet) {
/* 75 */         if (this.variableNames.contains(hisvar.getVariableName())) {
/* 76 */           vs.addVariable(new Variable(hisvar.getVariableName(), hisvar.getKind().intValue(), Variable.valueOf(hisvar.getKind().intValue(), hisvar.getValue()), hisvar.getBizName()));
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 84 */     return vs;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/pvm/cmd/ProcessVariableFindCmd.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */