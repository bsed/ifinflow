/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.model.ScopeElementImpl;
/*    */ import org.jbpm.pvm.internal.task.TaskDefinitionImpl;
/*    */ import org.jbpm.pvm.internal.xml.Parse;
/*    */ import org.w3c.dom.Element;
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
/*    */ public class TaskBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   private static final String TAG = "task";
/*    */   
/*    */   public TaskBinding()
/*    */   {
/* 38 */     super("task");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 42 */     TaskActivity taskActivity = new TaskActivity();
/*    */     
/* 44 */     ScopeElementImpl scopeElement = (ScopeElementImpl)parse.contextStackFind(ScopeElementImpl.class);
/* 45 */     TaskDefinitionImpl taskDefinition = parser.parseTaskDefinition(element, parse, scopeElement);
/* 46 */     taskActivity.setTaskDefinition(taskDefinition);
/*    */     
/* 48 */     return taskActivity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/TaskBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */