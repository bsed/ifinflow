/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.el.Expression;
/*    */ import org.jbpm.pvm.internal.model.ActivityImpl;
/*    */ import org.jbpm.pvm.internal.model.ExpressionCondition;
/*    */ import org.jbpm.pvm.internal.model.TransitionImpl;
/*    */ import org.jbpm.pvm.internal.util.XmlUtil;
/*    */ import org.jbpm.pvm.internal.wire.usercode.UserCodeCondition;
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
/*    */ public class ForEachBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   private static final String VARIABLE = "var";
/*    */   private static final String COLLECTION = "in";
/*    */   
/*    */   public ForEachBinding()
/*    */   {
/* 45 */     super("foreach");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser)
/*    */   {
/* 50 */     ForEachActivity activity = new ForEachActivity();
/*    */     
/* 52 */     if (element.hasAttribute("var")) {
/* 53 */       activity.setVariable(element.getAttribute("var"));
/*    */     }
/*    */     else {
/* 56 */       parse.addProblem("var attribute missing", element);
/*    */     }
/*    */     
/* 59 */     if (element.hasAttribute("in")) {
/* 60 */       Expression collection = Expression.create(element.getAttribute("in"), "uel-value");
/* 61 */       activity.setCollection(collection);
/*    */     }
/*    */     else {
/* 64 */       parse.addProblem("in attribute missing", element);
/*    */     }
/*    */     
/*    */ 
/* 68 */     Element transitionElement = XmlUtil.element(element, "transition");
/*    */     
/* 70 */     if (transitionElement == null) {
/* 71 */       parse.addProblem("outgoing transition expected", element);
/*    */     }
/*    */     else {
/* 74 */       ActivityImpl activityFromStack = (ActivityImpl)parse.contextStackFind(ActivityImpl.class);
/* 75 */       TransitionImpl transition = activityFromStack.getDefaultOutgoingTransition();
/*    */       
/* 77 */       Element conditionElement = XmlUtil.element(transitionElement, "condition");
/* 78 */       if (conditionElement != null) {
/* 79 */         if (conditionElement.hasAttribute("expr")) {
/* 80 */           ExpressionCondition condition = new ExpressionCondition();
/* 81 */           condition.setExpression(conditionElement.getAttribute("expr"));
/* 82 */           condition.setLanguage(XmlUtil.attribute(conditionElement, "lang"));
/* 83 */           transition.setCondition(condition);
/*    */         }
/*    */         else {
/* 86 */           Element handlerElement = XmlUtil.element(conditionElement, "handler");
/* 87 */           if (handlerElement != null) {
/* 88 */             UserCodeReference conditionReference = parser.parseUserCodeReference(handlerElement, parse);
/* 89 */             UserCodeCondition condition = new UserCodeCondition();
/* 90 */             condition.setConditionReference(conditionReference);
/* 91 */             transition.setCondition(condition);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 97 */     return activity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/ForEachBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */