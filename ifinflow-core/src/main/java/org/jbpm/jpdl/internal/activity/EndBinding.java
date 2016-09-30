/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
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
/*    */ 
/*    */ public class EndBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   public EndBinding()
/*    */   {
/* 36 */     super("end");
/*    */   }
/*    */   
/*    */   protected EndBinding(String tag) {
/* 40 */     super(tag);
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser)
/*    */   {
/* 45 */     boolean endProcessInstance = true;
/* 46 */     String ends = XmlUtil.attribute(element, "ends");
/* 47 */     if ("execution".equalsIgnoreCase(ends)) {
/* 48 */       endProcessInstance = false;
/*    */     }
/*    */     
/* 51 */     String state = XmlUtil.attribute(element, "state");
/*    */     
/* 53 */     EndActivity endActivity = new EndActivity();
/* 54 */     endActivity.setEndProcessInstance(endProcessInstance);
/* 55 */     endActivity.setState(state);
/*    */     
/* 57 */     return endActivity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/EndBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */