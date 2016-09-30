/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.model.ActivityImpl;
/*    */ import org.jbpm.pvm.internal.util.TagBinding;
/*    */ import org.jbpm.pvm.internal.util.XmlUtil;
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
/*    */ public abstract class JpdlBinding
/*    */   extends TagBinding
/*    */ {
/*    */   public JpdlBinding(String tagName)
/*    */   {
/* 39 */     super(tagName, null, null);
/*    */   }
/*    */   
/*    */   public abstract Object parseJpdl(Element paramElement, Parse paramParse, JpdlParser paramJpdlParser);
/*    */   
/*    */   public final Object parse(Element element, Parse parse, Parser parser) {
/* 45 */     return parseJpdl(element, parse, (JpdlParser)parser);
/*    */   }
/*    */   
/*    */   public void parseName(Element element, ActivityImpl activity, Parse parse) {
/* 49 */     String name = XmlUtil.attribute(element, "name", isNameRequired() ? parse : null);
/*    */     
/* 51 */     if (name != null)
/*    */     {
/* 53 */       if (name.length() == 0) {
/* 54 */         parse.addProblem(XmlUtil.errorMessageAttribute(element, "name", name, "is empty"), element);
/*    */       }
/* 56 */       activity.setName(name);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isNameRequired() {
/* 61 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/JpdlBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */