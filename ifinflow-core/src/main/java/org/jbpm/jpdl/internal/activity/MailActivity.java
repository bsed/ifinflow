/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import javax.mail.Message;
/*    */ import org.jbpm.api.model.OpenExecution;
/*    */ import org.jbpm.pvm.internal.email.spi.MailProducer;
/*    */ import org.jbpm.pvm.internal.email.spi.MailSession;
/*    */ import org.jbpm.pvm.internal.env.EnvironmentImpl;
/*    */ import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
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
/*    */ public class MailActivity
/*    */   extends JpdlAutomaticActivity
/*    */ {
/*    */   private UserCodeReference mailProducerReference;
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   void perform(OpenExecution execution)
/*    */     throws Exception
/*    */   {
/* 44 */     MailProducer mailProducer = (MailProducer)this.mailProducerReference.getObject(execution);
/* 45 */     Collection<Message> messages = mailProducer.produce(execution);
/* 46 */     ((MailSession)EnvironmentImpl.getFromCurrent(MailSession.class)).send(messages);
/*    */   }
/*    */   
/*    */   public UserCodeReference getMailProducerReference() {
/* 50 */     return this.mailProducerReference;
/*    */   }
/*    */   
/*    */   public void setMailProducerReference(UserCodeReference mailProducer) {
/* 54 */     this.mailProducerReference = mailProducer;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/MailActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */