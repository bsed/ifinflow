/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import javax.mail.Message;
/*    */ import org.jbpm.api.listener.EventListener;
/*    */ import org.jbpm.api.listener.EventListenerExecution;
/*    */ import org.jbpm.pvm.internal.email.spi.MailProducer;
/*    */ import org.jbpm.pvm.internal.email.spi.MailSession;
/*    */ import org.jbpm.pvm.internal.env.EnvironmentImpl;
/*    */ import org.jbpm.pvm.internal.env.TaskContext;
/*    */ import org.jbpm.pvm.internal.session.DbSession;
/*    */ import org.jbpm.pvm.internal.task.TaskImpl;
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
/*    */ 
/*    */ public class MailListener
/*    */   implements EventListener
/*    */ {
/*    */   private UserCodeReference mailProducerReference;
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public void notify(EventListenerExecution execution)
/*    */     throws Exception
/*    */   {
/* 49 */     EnvironmentImpl environment = EnvironmentImpl.getCurrent();
/* 50 */     DbSession dbSession = (DbSession)environment.get(DbSession.class);
/* 51 */     TaskImpl task = dbSession.findTaskByExecution(execution);
/*    */     
/*    */ 
/* 54 */     TaskContext taskContext = new TaskContext(task);
/* 55 */     environment.setContext(taskContext);
/*    */     try {
/* 57 */       MailProducer mailProducer = (MailProducer)this.mailProducerReference.getObject(execution);
/* 58 */       Collection<Message> messages = mailProducer.produce(execution);
/* 59 */       ((MailSession)environment.get(MailSession.class)).send(messages);
/*    */     }
/*    */     finally {
/* 62 */       environment.removeContext(taskContext);
/*    */     }
/*    */   }
/*    */   
/*    */   public UserCodeReference getMailProducerReference() {
/* 67 */     return this.mailProducerReference;
/*    */   }
/*    */   
/*    */   public void setMailProducerReference(UserCodeReference mailProducer) {
/* 71 */     this.mailProducerReference = mailProducer;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/MailListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */