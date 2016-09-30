/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.drools.KnowledgeBase;
/*    */ import org.drools.runtime.Globals;
/*    */ import org.drools.runtime.StatefulKnowledgeSession;
/*    */ import org.jbpm.api.activity.ActivityExecution;
/*    */ import org.jbpm.jpdl.internal.rules.ExecutionGlobals;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RulesActivity
/*    */   extends DecisionConditionActivity
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 42 */   List<RulesFact> rulesFacts = new ArrayList();
/*    */   
/*    */   public void execute(ActivityExecution execution) throws Exception {
/* 45 */     ExecutionImpl executionImpl = (ExecutionImpl)execution;
/* 46 */     String deploymentId = executionImpl.getProcessDefinition().getDeploymentId();
/* 47 */     KnowledgeBase knowledgeBase = RulesDeployer.getKnowledgeBase(deploymentId);
/*    */     
/* 49 */     StatefulKnowledgeSession knowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
/*    */     
/*    */ 
/* 52 */     ExecutionGlobals executionGlobals = new ExecutionGlobals(execution);
/* 53 */     knowledgeSession.getGlobals().setDelegate(executionGlobals);
/*    */     
/* 55 */     for (RulesFact rulesFact : this.rulesFacts) {
/* 56 */       Object fact = rulesFact.getObject(execution);
/* 57 */       knowledgeSession.insert(fact);
/*    */     }
/*    */     
/* 60 */     knowledgeSession.fireAllRules();
/*    */     
/* 62 */     super.execute(executionImpl);
/*    */   }
/*    */   
/*    */   public void addRulesFact(RulesFact rulesFact) {
/* 66 */     this.rulesFacts.add(rulesFact);
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/RulesActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */