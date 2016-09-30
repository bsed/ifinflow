/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.wire.binding.ObjectBinding;
/*    */ import org.jbpm.pvm.internal.wire.usercode.UserCodeActivityBehaviour;
/*    */ import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
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
/*    */ 
/*    */ public class CustomBinding
/*    */   extends JpdlBinding
/*    */ {
/* 37 */   static ObjectBinding objectBinding = new ObjectBinding();
/*    */   
/*    */   public CustomBinding() {
/* 40 */     super("custom");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 44 */     UserCodeActivityBehaviour userCodeActivityBehaviour = new UserCodeActivityBehaviour();
/* 45 */     UserCodeReference customActivityReference = parser.parseUserCodeReference(element, parse);
/* 46 */     userCodeActivityBehaviour.setCustomActivityReference(customActivityReference);
/* 47 */     return userCodeActivityBehaviour;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/CustomBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */