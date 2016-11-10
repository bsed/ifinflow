/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.model.JpdlProcessDefinition;
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.model.ActivityImpl;
/*    */ import org.jbpm.pvm.internal.model.Continuation;
/*    */ import org.jbpm.pvm.internal.util.XmlUtil;
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
/*    */ public class StartBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   public StartBinding()
/*    */   {
/* 38 */     super("start");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 42 */     ActivityImpl startActivity = (ActivityImpl)parse.contextStackFind(ActivityImpl.class);
/* 43 */     JpdlProcessDefinition processDefinition = (JpdlProcessDefinition)parse.contextStackFind(JpdlProcessDefinition.class);
/*    */     
/* 45 */     if (processDefinition.getInitial() == null) {
/* 46 */       processDefinition.setInitial(startActivity);
/*    */     }
/* 48 */     else if (startActivity.getParentActivity() == null) {
/* 49 */       parse.addProblem("multiple start events not yet supported", element);
/*    */     }
/*    */     
/* 52 */     StartActivity startActivityBehaviour = new StartActivity();
/*    */     
/* 54 */     if ((startActivity.getContinuation() == Continuation.ASYNCHRONOUS) && (startActivity.getName() == null)) {
/* 55 */       parse.addProblem("Using continuation=\"async\" on a start node requires a name to be present", element);
/*    */     }
/*    */     
/* 58 */     startActivityBehaviour.setFormResourceName(XmlUtil.attribute(element, "form"));
/*    */     
/* 60 */     return startActivityBehaviour;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isNameRequired()
/*    */   {
/* 66 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/StartBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */