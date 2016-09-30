/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.wire.usercode.UserCodeEventListener;
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
/*    */ public class EventListenerBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   public EventListenerBinding()
/*    */   {
/* 37 */     super("event-listener");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 41 */     UserCodeEventListener userCodeEventListener = new UserCodeEventListener();
/*    */     
/* 43 */     UserCodeReference eventListenerReference = parser.parseUserCodeReference(element, parse);
/* 44 */     userCodeEventListener.setEventListenerReference(eventListenerReference);
/*    */     
/* 46 */     return userCodeEventListener;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/EventListenerBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */