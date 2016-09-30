/*     */ package org.jbpm.jpdl.internal.activity;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.MapMessage;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.QueueSender;
/*     */ import javax.jms.QueueSession;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.TextMessage;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicPublisher;
/*     */ import javax.jms.TopicSession;
/*     */ import javax.jms.XAConnectionFactory;
/*     */ import javax.jms.XAQueueConnection;
/*     */ import javax.jms.XAQueueConnectionFactory;
/*     */ import javax.jms.XAQueueSession;
/*     */ import javax.jms.XATopicConnection;
/*     */ import javax.jms.XATopicConnectionFactory;
/*     */ import javax.jms.XATopicSession;
/*     */ import javax.naming.InitialContext;
/*     */ import org.jbpm.api.JbpmException;
/*     */ import org.jbpm.api.model.OpenExecution;
/*     */ import org.jbpm.internal.log.Log;
/*     */ import org.jbpm.pvm.internal.el.Expression;
/*     */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*     */ import org.jbpm.pvm.internal.wire.Descriptor;
/*     */ import org.jbpm.pvm.internal.wire.WireContext;
/*     */ import org.jbpm.pvm.internal.wire.descriptor.MapDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JmsActivity
/*     */   extends JpdlAutomaticActivity
/*     */ {
/*  68 */   private static final Log log = Log.getLog(JmsActivity.class.getName());
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*  72 */   protected String type = null;
/*  73 */   protected Expression textExpression = null;
/*  74 */   protected Expression objectExpression = null;
/*  75 */   protected MapDescriptor mapDescriptor = null;
/*  76 */   protected String connectionFactoryName = null;
/*  77 */   protected String destinationName = null;
/*  78 */   protected boolean transacted = true;
/*  79 */   protected int acknowledgeMode = 1;
/*     */   
/*     */   public void perform(OpenExecution execution) {
/*     */     try {
/*  83 */       InitialContext initialContext = new InitialContext();
/*     */       
/*  85 */       Destination destination = (Destination)initialContext.lookup(this.destinationName);
/*  86 */       Object connectionFactory = initialContext.lookup(this.connectionFactoryName);
/*     */       
/*  88 */       if ((connectionFactory instanceof XAConnectionFactory)) {
/*  89 */         log.debug("connection factory '" + this.connectionFactoryName + "' is a XAConnectionFactory: using xa jms apis");
/*  90 */         if ((destination instanceof Queue)) {
/*  91 */           log.debug("destination '" + this.destinationName + "' is a Queue: using xa queue jms apis");
/*  92 */           XAQueueConnectionFactory xaQueueConnectionFactory = (XAQueueConnectionFactory)connectionFactory;
/*  93 */           sendToQueueXA((Queue)destination, xaQueueConnectionFactory, (ExecutionImpl)execution);
/*     */         }
/*  95 */         else if ((destination instanceof Topic)) {
/*  96 */           log.debug("destination '" + this.destinationName + "' is a Topic: using xa topic jms apis");
/*  97 */           XATopicConnectionFactory xaTopicConnectionFactory = (XATopicConnectionFactory)connectionFactory;
/*  98 */           sendToTopicXA((Topic)destination, xaTopicConnectionFactory, (ExecutionImpl)execution);
/*     */         }
/*     */         else {
/* 101 */           throw new JbpmException("invalid destination type for '" + this.destinationName + "': " + destination.getClass().getName());
/*     */         }
/*     */       }
/*     */       else {
/* 105 */         log.debug("connection factory '" + this.connectionFactoryName + "' is a ConnectionFactory: using non-xa jms apis");
/* 106 */         if ((destination instanceof Queue)) {
/* 107 */           log.debug("destination '" + this.destinationName + "' is a Queue: using non-xa queue jms apis");
/* 108 */           QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)connectionFactory;
/* 109 */           sendToQueue((Queue)destination, queueConnectionFactory, (ExecutionImpl)execution);
/*     */         }
/* 111 */         else if ((destination instanceof Topic)) {
/* 112 */           log.debug("destination '" + this.destinationName + "' is a Topic: using non-xa topic jms apis");
/* 113 */           TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)connectionFactory;
/* 114 */           sendToTopic((Topic)destination, topicConnectionFactory, (ExecutionImpl)execution);
/*     */         }
/*     */         else {
/* 117 */           throw new JbpmException("invalid destination type for '" + this.destinationName + "': " + destination.getClass().getName());
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (RuntimeException e) {
/* 122 */       log.error("couldn't send jms message: " + e.getMessage(), e);
/* 123 */       throw e;
/*     */     }
/*     */     catch (Exception e) {
/* 126 */       log.error("couldn't send jms message: " + e.getMessage(), e);
/* 127 */       throw new JbpmException("couldn't send jms message to queue" + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void sendToQueueXA(Queue queue, XAQueueConnectionFactory xaQueueConnectionFactory, ExecutionImpl execution) throws Exception {
/* 132 */     XAQueueConnection xaQueueConnection = null;
/* 133 */     XAQueueSession xaQueueSession = null;
/* 134 */     MessageProducer messageProducer = null;
/*     */     try
/*     */     {
/* 137 */       xaQueueConnection = xaQueueConnectionFactory.createXAQueueConnection();
/* 138 */       xaQueueSession = xaQueueConnection.createXAQueueSession();
/* 139 */       messageProducer = xaQueueSession.createProducer(queue);
/* 140 */       Message message = createMessage(xaQueueSession, execution);
/* 141 */       messageProducer.send(message); return;
/*     */     }
/*     */     finally {
/*     */       try {
/* 145 */         messageProducer.close();
/*     */       } catch (Exception e) {
/* 147 */         e.printStackTrace();
/*     */       }
/*     */       try {
/* 150 */         xaQueueSession.close();
/*     */       } catch (Exception e) {
/* 152 */         e.printStackTrace();
/*     */       }
/*     */       try {
/* 155 */         xaQueueConnection.close();
/*     */       } catch (Exception e) {
/* 157 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void sendToTopicXA(Topic topic, XATopicConnectionFactory xaTopicConnectionFactory, ExecutionImpl execution) throws Exception {
/* 163 */     XATopicConnection xaTopicConnection = null;
/* 164 */     XATopicSession xaTopicSession = null;
/* 165 */     MessageProducer messageProducer = null;
/*     */     try
/*     */     {
/* 168 */       xaTopicConnection = xaTopicConnectionFactory.createXATopicConnection();
/* 169 */       xaTopicSession = xaTopicConnection.createXATopicSession();
/* 170 */       messageProducer = xaTopicSession.createProducer(topic);
/* 171 */       Message message = createMessage(xaTopicSession, execution);
/* 172 */       messageProducer.send(message); return;
/*     */     }
/*     */     finally {
/*     */       try {
/* 176 */         messageProducer.close();
/*     */       } catch (Exception e) {
/* 178 */         e.printStackTrace();
/*     */       }
/*     */       try {
/* 181 */         xaTopicSession.close();
/*     */       } catch (Exception e) {
/* 183 */         e.printStackTrace();
/*     */       }
/*     */       try {
/* 186 */         xaTopicConnection.close();
/*     */       } catch (Exception e) {
/* 188 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void sendToQueue(Queue queue, QueueConnectionFactory queueConnectionFactory, ExecutionImpl execution) throws Exception {
/* 194 */     QueueConnection queueConnection = null;
/* 195 */     QueueSession queueSession = null;
/* 196 */     QueueSender queueSender = null;
/*     */     try
/*     */     {
/* 199 */       queueConnection = queueConnectionFactory.createQueueConnection();
/* 200 */       queueSession = queueConnection.createQueueSession(this.transacted, this.acknowledgeMode);
/* 201 */       queueSender = queueSession.createSender(queue);
/* 202 */       Message message = createMessage(queueSession, execution);
/* 203 */       queueSender.send(message);
/* 204 */       if (this.transacted) {
/* 205 */         queueSession.commit();
/*     */       }
/*     */       return;
/*     */     } finally {
/*     */       try {
/* 210 */         queueSender.close();
/*     */       } catch (Exception e) {
/* 212 */         e.printStackTrace();
/*     */       }
/*     */       try {
/* 215 */         queueSession.close();
/*     */       } catch (Exception e) {
/* 217 */         e.printStackTrace();
/*     */       }
/*     */       try {
/* 220 */         queueConnection.close();
/*     */       } catch (Exception e) {
/* 222 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void sendToTopic(Topic topic, TopicConnectionFactory topicConnectionFactory, ExecutionImpl execution) throws Exception {
/* 228 */     TopicConnection topicConnection = null;
/* 229 */     TopicSession topicSession = null;
/* 230 */     TopicPublisher topicPublisher = null;
/*     */     try
/*     */     {
/* 233 */       topicConnection = topicConnectionFactory.createTopicConnection();
/* 234 */       topicSession = topicConnection.createTopicSession(this.transacted, this.acknowledgeMode);
/* 235 */       topicPublisher = topicSession.createPublisher(topic);
/* 236 */       Message message = createMessage(topicSession, execution);
/* 237 */       topicPublisher.send(message);
/* 238 */       if (this.transacted) {
/* 239 */         topicSession.commit();
/*     */       }
/*     */       return;
/*     */     } finally {
/*     */       try {
/* 244 */         topicPublisher.close();
/*     */       } catch (Exception e) {
/* 246 */         e.printStackTrace();
/*     */       }
/*     */       try {
/* 249 */         topicSession.close();
/*     */       } catch (Exception e) {
/* 251 */         e.printStackTrace();
/*     */       }
/*     */       try {
/* 254 */         topicConnection.close();
/*     */       } catch (Exception e) {
/* 256 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Message createMessage(Session session, ExecutionImpl execution) throws Exception {
/* 262 */     if ("text".equals(this.type))
/* 263 */       return createTextMessage(session, execution);
/* 264 */     if ("object".equals(this.type))
/* 265 */       return createObjectMessage(session, execution);
/* 266 */     if ("map".equals(this.type)) {
/* 267 */       return createMapMessage(session, execution);
/*     */     }
/* 269 */     throw new JbpmException("no type configured in jms activity");
/*     */   }
/*     */   
/*     */   private MapMessage createMapMessage(Session session, ExecutionImpl execution) throws Exception {
/* 273 */     MapMessage result = session.createMapMessage();
/* 274 */     if (this.mapDescriptor != null) {
/* 275 */       List<Descriptor> keyDescriptors = this.mapDescriptor.getKeyDescriptors();
/* 276 */       List<Descriptor> valueDescriptors = this.mapDescriptor.getValueDescriptors();
/* 277 */       WireContext wireContext = new WireContext();
/* 278 */       wireContext.setScopeInstance(execution);
/* 279 */       for (int i = 0; i < keyDescriptors.size(); i++) {
/* 280 */         String key = (String)wireContext.create((Descriptor)keyDescriptors.get(i), false);
/* 281 */         Object value = wireContext.create((Descriptor)valueDescriptors.get(i), false);
/* 282 */         result.setObject(key, value);
/*     */       }
/*     */     }
/* 285 */     return result;
/*     */   }
/*     */   
/*     */   private TextMessage createTextMessage(Session session, ExecutionImpl execution) throws Exception {
/* 289 */     Object value = this.textExpression.evaluate(execution);
/* 290 */     if (value != null) {
/* 291 */       return session.createTextMessage(value.toString());
/*     */     }
/* 293 */     throw new JbpmException("null value for expression '" + this.textExpression + "' in jms activity");
/*     */   }
/*     */   
/*     */   private ObjectMessage createObjectMessage(Session session, ExecutionImpl execution) throws Exception {
/* 297 */     Object object = this.objectExpression.evaluate(execution);
/* 298 */     if ((object != null) && (!(object instanceof Serializable)))
/*     */     {
/*     */ 
/* 301 */       throw new JbpmException("can't send jms message: creation of object message expression '" + this.objectExpression + "' must be done with serializable: " + object);
/*     */     }
/* 303 */     return session.createObjectMessage((Serializable)object);
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 307 */     this.type = type;
/*     */   }
/*     */   
/* 310 */   public void setTextExpression(Expression textExpression) { this.textExpression = textExpression; }
/*     */   
/*     */   public void setObjectExpression(Expression objectExpression) {
/* 313 */     this.objectExpression = objectExpression;
/*     */   }
/*     */   
/* 316 */   public void setMapDescriptor(MapDescriptor mapDescriptor) { this.mapDescriptor = mapDescriptor; }
/*     */   
/*     */   public void setConnectionFactoryName(String connectionFactoryName) {
/* 319 */     this.connectionFactoryName = connectionFactoryName;
/*     */   }
/*     */   
/* 322 */   public void setDestinationName(String destinationName) { this.destinationName = destinationName; }
/*     */   
/*     */   public boolean isTransacted() {
/* 325 */     return this.transacted;
/*     */   }
/*     */   
/* 328 */   public void setTransacted(boolean transacted) { this.transacted = transacted; }
/*     */   
/*     */   public int getAcknowledgeMode() {
/* 331 */     return this.acknowledgeMode;
/*     */   }
/*     */   
/* 334 */   public void setAcknowledgeMode(int acknowledgeMode) { this.acknowledgeMode = acknowledgeMode; }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/JmsActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */