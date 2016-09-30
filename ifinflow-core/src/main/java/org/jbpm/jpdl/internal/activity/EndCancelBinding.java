/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
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
/*    */ public class EndCancelBinding
/*    */   extends EndBinding
/*    */ {
/*    */   public EndCancelBinding()
/*    */   {
/* 35 */     super("end-cancel");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 39 */     EndActivity endActivity = (EndActivity)super.parseJpdl(element, parse, parser);
/* 40 */     endActivity.setState("cancel");
/* 41 */     return endActivity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/EndCancelBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */