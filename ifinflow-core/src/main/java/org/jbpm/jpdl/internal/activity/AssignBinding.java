/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.el.Expression;
/*    */ import org.jbpm.pvm.internal.el.UelValueExpression;
/*    */ import org.jbpm.pvm.internal.util.XmlUtil;
/*    */ import org.jbpm.pvm.internal.wire.Descriptor;
/*    */ import org.jbpm.pvm.internal.wire.xml.WireParser;
/*    */ import org.jbpm.pvm.internal.xml.Bindings;
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
/*    */ public class AssignBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   private static final String FROM_EXPR = "from-expr";
/*    */   private static final String LANG = "lang";
/*    */   private static final String FROM_VAR = "from-var";
/*    */   private static final String FROM_DESC = "from";
/*    */   private static final String TO_EXPR = "to-expr";
/*    */   private static final String TO_VAR = "to-var";
/*    */   
/*    */   public AssignBinding()
/*    */   {
/* 48 */     super("assign");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 52 */     AssignActivity assignActivity = new AssignActivity();
/*    */     
/*    */ 
/* 55 */     if (element.hasAttribute("from-expr")) {
/* 56 */       String lang = XmlUtil.attribute(element, "lang");
/* 57 */       Expression fromExpression = Expression.create(element.getAttribute("from-expr"), lang);
/* 58 */       assignActivity.setFromExpression(fromExpression);
/*    */ 
/*    */     }
/* 61 */     else if (element.hasAttribute("from-var")) {
/* 62 */       assignActivity.setFromVariable(element.getAttribute("from-var"));
/*    */     }
/*    */     else
/*    */     {
/* 66 */       Element fromElement = XmlUtil.element(element, "from");
/* 67 */       if (fromElement != null) {
/* 68 */         Set<String> descriptorTags = WireParser.getInstance().getBindings().getTagNames("descriptor");
/*    */         
/*    */ 
/* 71 */         Element descriptorElement = XmlUtil.element(fromElement);
/* 72 */         if ((descriptorElement != null) && (descriptorTags.contains(descriptorElement.getTagName())))
/*    */         {
/* 74 */           Descriptor descriptor = parser.parseDescriptor(descriptorElement, parse);
/* 75 */           assignActivity.setFromDescriptor(descriptor);
/*    */         }
/*    */         else {
/* 78 */           parse.addProblem("missing descriptor element", fromElement);
/*    */         }
/*    */       }
/*    */       else {
/* 82 */         parse.addProblem("missing from-expr attribute, from-var attribute or from element", element);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 87 */     if (element.hasAttribute("to-var")) {
/* 88 */       assignActivity.setToVariable(element.getAttribute("to-var"));
/*    */     }
/* 90 */     else if (element.hasAttribute("to-expr")) {
/* 91 */       Expression expression = Expression.create(element.getAttribute("to-expr"), "uel-value");
/* 92 */       assignActivity.setToExpression((UelValueExpression)expression);
/*    */     }
/*    */     
/* 95 */     return assignActivity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/AssignBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */