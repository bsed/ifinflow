/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.drools.KnowledgeBase;
/*    */ import org.drools.runtime.Globals;
/*    */ import org.drools.runtime.StatelessKnowledgeSession;
/*    */ import org.jbpm.api.activity.ActivityExecution;
/*    */ import org.jbpm.jpdl.internal.rules.ExecutionGlobals;
/*    */ import org.jbpm.jpdl.internal.rules.Outcome;
/*    */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*    */ import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
/*    */ import org.jbpm.pvm.internal.repository.RulesDeployer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RulesDecisionActivity
/*    */   extends JpdlActivity
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public void execute(ActivityExecution execution)
/*    */     throws Exception
/*    */   {
/* 40 */     String deploymentId = ((ExecutionImpl)execution).getProcessDefinition().getDeploymentId();
/* 41 */     KnowledgeBase knowledgeBase = RulesDeployer.getKnowledgeBase(deploymentId);
/*    */     
/* 43 */     StatelessKnowledgeSession knowledgeSession = knowledgeBase.newStatelessKnowledgeSession();
/*    */     
/*    */ 
/* 46 */     ExecutionGlobals executionGlobals = new ExecutionGlobals(execution);
/* 47 */     knowledgeSession.getGlobals().setDelegate(executionGlobals);
/* 48 */     knowledgeSession.execute(execution);
/* 49 */     if (!executionGlobals.getOutcome().isDefined()) {
/* 50 */       execution.takeDefaultTransition();
/*    */     } else {
/* 52 */       execution.take(executionGlobals.getOutcome().get());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/RulesDecisionActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */