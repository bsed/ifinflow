/*    */ package org.jbpm.jpdl.internal.xml;
/*    */ 
/*    */ import org.jbpm.internal.log.Log;
/*    */ import org.jbpm.pvm.internal.util.ReflectUtil;
/*    */ import org.jbpm.pvm.internal.util.TagBinding;
/*    */ import org.jbpm.pvm.internal.util.XmlUtil;
/*    */ import org.jbpm.pvm.internal.xml.Binding;
/*    */ import org.jbpm.pvm.internal.xml.Bindings;
/*    */ import org.jbpm.pvm.internal.xml.Parse;
/*    */ import org.jbpm.pvm.internal.xml.Parser;
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
/*    */ public class JpdlBindingsParser
/*    */   extends Parser
/*    */ {
/* 39 */   private static final Log log = Log.getLog(JpdlBindingsParser.class.getName());
/*    */   
/*    */   public Object parseDocumentElement(Element documentElement, Parse parse) {
/* 42 */     Bindings bindings = (Bindings)parse.contextMapGet("bindings");
/* 43 */     parse.setDocumentObject(bindings);
/*    */     
/* 45 */     for (Element bindingElement : XmlUtil.elements(documentElement)) {
/* 46 */       Binding binding = instantiateBinding(bindingElement, parse);
/* 47 */       bindings.addBinding(binding);
/*    */     }
/*    */     
/* 50 */     return bindings;
/*    */   }
/*    */   
/*    */   protected Binding instantiateBinding(Element bindingElement, Parse parse) {
/* 54 */     String bindingClassName = XmlUtil.attribute(bindingElement, "binding", parse);
/*    */     
/* 56 */     log.trace("adding jpdl binding " + bindingClassName);
/*    */     
/* 58 */     if (bindingClassName != null) {
/*    */       try {
/* 60 */         Class<?> bindingClass = ReflectUtil.classForName(bindingClassName);
/* 61 */         TagBinding binding = (TagBinding)bindingClass.newInstance();
/*    */         
/* 63 */         String tagLocalName = bindingElement.getLocalName();
/* 64 */         if ("activity".equals(tagLocalName)) {
/* 65 */           binding.setCategory("activity");
/* 66 */         } else if ("eventlistener".equals(tagLocalName)) {
/* 67 */           binding.setCategory("eventlistener");
/*    */         } else {
/* 69 */           parse.addProblem("unrecognized binding tag: " + tagLocalName);
/*    */         }
/*    */         
/* 72 */         return binding;
/*    */       } catch (Exception e) {
/* 74 */         parse.addProblem("couldn't instantiate activity binding " + bindingClassName, e);
/*    */       }
/*    */     }
/* 77 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/xml/JpdlBindingsParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */