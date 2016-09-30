/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.model.ActivityImpl;
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
/*    */ 
/*    */ public class GroupBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   public GroupBinding()
/*    */   {
/* 36 */     super("group");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 40 */     GroupActivity groupActivity = new GroupActivity();
/*    */     
/* 42 */     ActivityImpl activity = (ActivityImpl)parse.contextStackFind(ActivityImpl.class);
/*    */     
/* 44 */     parser.parseActivities(element, parse, activity);
/*    */     
/* 46 */     return groupActivity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/GroupBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */