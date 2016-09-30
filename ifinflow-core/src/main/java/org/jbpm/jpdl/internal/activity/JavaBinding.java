/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.util.XmlUtil;
/*    */ import org.jbpm.pvm.internal.wire.descriptor.ArgDescriptor;
/*    */ import org.jbpm.pvm.internal.wire.descriptor.ObjectDescriptor;
/*    */ import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
/*    */ import org.jbpm.pvm.internal.wire.xml.WireParser;
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
/*    */ 
/*    */ public class JavaBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   public static final String TAG = "java";
/*    */   
/*    */   public JavaBinding()
/*    */   {
/* 45 */     super("java");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 49 */     JavaActivity javaActivity = new JavaActivity();
/* 50 */     if (XmlUtil.attribute(element, "method", parse) != null) {
/* 51 */       String jndiName = XmlUtil.attribute(element, "ejb-jndi-name");
/* 52 */       if (jndiName != null) {
/* 53 */         parseEjbInvocation(javaActivity, element, parse, parser);
/*    */       } else {
/* 55 */         parseJavaInvocation(javaActivity, element, parse, parser);
/*    */       }
/*    */     }
/* 58 */     String variableName = XmlUtil.attribute(element, "var");
/* 59 */     javaActivity.setVariableName(variableName);
/* 60 */     return javaActivity;
/*    */   }
/*    */   
/*    */   private void parseEjbInvocation(JavaActivity javaActivity, Element element, Parse parse, JpdlParser parser) {
/* 64 */     javaActivity.setJndiName(XmlUtil.attribute(element, "ejb-jndi-name"));
/* 65 */     javaActivity.setMethodName(XmlUtil.attribute(element, "method"));
/* 66 */     List<Element> argElements = XmlUtil.elements(element, "arg");
/* 67 */     List<ArgDescriptor> argDescriptors = new WireParser().parseArgs(argElements, parse);
/* 68 */     javaActivity.setArgDescriptors(argDescriptors);
/*    */   }
/*    */   
/*    */   private void parseJavaInvocation(JavaActivity javaActivity, Element element, Parse parse, JpdlParser parser) {
/* 72 */     UserCodeReference invocationReference = parser.parseUserCodeReference(element, parse);
/* 73 */     javaActivity.setInvocationReference(invocationReference);
/* 74 */     ObjectDescriptor objectDescriptor = (ObjectDescriptor)invocationReference.getDescriptor();
/* 75 */     javaActivity.setArgDescriptors(objectDescriptor.getArgDescriptors());
/* 76 */     objectDescriptor.setArgDescriptors(null);
/* 77 */     javaActivity.setMethodName(objectDescriptor.getMethodName());
/* 78 */     objectDescriptor.setMethodName(null);
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/JavaBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */