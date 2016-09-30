/*     */ package org.jbpm.jpdl.internal.activity;
/*     */ 
/*     */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*     */ import org.jbpm.pvm.internal.el.Expression;
/*     */ import org.jbpm.pvm.internal.util.XmlUtil;
/*     */ import org.jbpm.pvm.internal.wire.Descriptor;
/*     */ import org.jbpm.pvm.internal.wire.descriptor.MapDescriptor;
/*     */ import org.jbpm.pvm.internal.xml.Parse;
/*     */ import org.w3c.dom.Element;
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
/*     */ public class JmsBinding
/*     */   extends JpdlBinding
/*     */ {
/*     */   public static final String TAG = "jms";
/*     */   
/*     */   public JmsBinding()
/*     */   {
/*  42 */     super("jms");
/*     */   }
/*     */   
/*     */   protected JmsBinding(String tagName) {
/*  46 */     super(tagName);
/*     */   }
/*     */   
/*     */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/*  50 */     JmsActivity jmsActivity = createJmsActivity();
/*     */     
/*     */ 
/*     */ 
/*  54 */     String connectionFactoryName = XmlUtil.attribute(element, "connection-factory", parse);
/*  55 */     jmsActivity.setConnectionFactoryName(connectionFactoryName);
/*     */     
/*  57 */     String destinationName = XmlUtil.attribute(element, "destination", parse);
/*  58 */     jmsActivity.setDestinationName(destinationName);
/*     */     
/*  60 */     Boolean transacted = XmlUtil.attributeBoolean(element, "transacted", parse);
/*  61 */     if (transacted != null) {
/*  62 */       jmsActivity.setTransacted(transacted.booleanValue());
/*     */     }
/*     */     
/*  65 */     String acknowledge = XmlUtil.attribute(element, "acknowledge");
/*  66 */     if (acknowledge != null) {
/*  67 */       if (acknowledge.equalsIgnoreCase("auto")) {
/*  68 */         jmsActivity.setAcknowledgeMode(1);
/*  69 */       } else if (acknowledge.equalsIgnoreCase("client")) {
/*  70 */         jmsActivity.setAcknowledgeMode(2);
/*  71 */       } else if (acknowledge.equalsIgnoreCase("dups-ok")) {
/*  72 */         jmsActivity.setAcknowledgeMode(3);
/*     */       } else {
/*  74 */         parse.addProblem("unknown jms acknowledge: '" + acknowledge + "'", element);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  80 */     Element textElement = XmlUtil.element(element, "text");
/*  81 */     if (textElement != null) {
/*  82 */       String expressionText = XmlUtil.getContentText(textElement);
/*  83 */       jmsActivity.setType("text");
/*  84 */       Expression expression = Expression.create(expressionText, "uel-value");
/*  85 */       jmsActivity.setTextExpression(expression);
/*     */     }
/*     */     
/*  88 */     Element objectElement = XmlUtil.element(element, "object");
/*  89 */     if (objectElement != null) {
/*  90 */       jmsActivity.setType("object");
/*  91 */       String expressionText = XmlUtil.attribute(objectElement, "expr");
/*  92 */       Expression expression = Expression.create(expressionText, "uel-value");
/*  93 */       jmsActivity.setObjectExpression(expression);
/*     */     }
/*     */     
/*  96 */     Element mapElement = XmlUtil.element(element, "map");
/*  97 */     if (mapElement != null) {
/*  98 */       jmsActivity.setType("map");
/*  99 */       Descriptor descriptor = parser.parseDescriptor(mapElement, parse);
/* 100 */       if ((descriptor instanceof MapDescriptor)) {
/* 101 */         jmsActivity.setMapDescriptor((MapDescriptor)descriptor);
/*     */       } else {
/* 103 */         parse.addProblem("the parser did not return a descriptor of type MapDescriptor");
/*     */       }
/*     */     }
/*     */     
/* 107 */     return jmsActivity;
/*     */   }
/*     */   
/*     */   protected JmsActivity createJmsActivity() {
/* 111 */     return new JmsActivity();
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/JmsBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */