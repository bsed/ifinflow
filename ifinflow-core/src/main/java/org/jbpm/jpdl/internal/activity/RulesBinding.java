/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.el.Expression;
/*    */ import org.jbpm.pvm.internal.util.XmlUtil;
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
/*    */ public class RulesBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   public RulesBinding()
/*    */   {
/* 39 */     super("rules");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 43 */     RulesActivity rulesActivity = new RulesActivity();
/*    */     
/* 45 */     List<Element> factElements = XmlUtil.elements(element, "fact");
/* 46 */     for (Element factElement : factElements) {
/* 47 */       RulesFact rulesFact = new RulesFact();
/*    */       
/* 49 */       String factVar = XmlUtil.attribute(factElement, "var");
/* 50 */       if (factVar != null) {
/* 51 */         rulesFact.setVariableName(factVar);
/*    */       }
/*    */       else {
/* 54 */         String factExpr = XmlUtil.attribute(factElement, "expr");
/*    */         
/* 56 */         if (factExpr != null) {
/* 57 */           String factLang = XmlUtil.attribute(factElement, "lang");
/* 58 */           Expression expression = Expression.create(factExpr, factLang);
/* 59 */           rulesFact.setExpression(expression);
/*    */         }
/*    */         else {
/* 62 */           parse.addProblem("'fact' element inside 'rules' activity requires attribute 'var' or 'expr'", element);
/*    */         }
/*    */       }
/*    */       
/* 66 */       rulesActivity.addRulesFact(rulesFact);
/*    */     }
/*    */     
/* 69 */     return rulesActivity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/RulesBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */