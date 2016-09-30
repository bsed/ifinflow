/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.hibernate.LockMode;
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.el.Expression;
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
/*    */ public class JoinBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   private static final String MULTIPLICITY = "multiplicity";
/*    */   private static final String LOCKMODE = "lockmode";
/*    */   
/*    */   public JoinBinding()
/*    */   {
/* 41 */     super("join");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 45 */     JoinActivity joinActivity = new JoinActivity();
/*    */     
/* 47 */     if (element.hasAttribute("multiplicity")) {
/* 48 */       String multiplicityText = element.getAttribute("multiplicity");
/* 49 */       Expression expression = Expression.create(multiplicityText, "uel-value");
/* 50 */       joinActivity.setMultiplicity(expression);
/*    */     }
/*    */     
/* 53 */     if (element.hasAttribute("lockmode")) {
/* 54 */       String lockModeText = element.getAttribute("lockmode");
/* 55 */       LockMode lockMode = LockMode.parse(lockModeText.toUpperCase());
/* 56 */       if (lockMode == null) {
/* 57 */         parse.addProblem(lockModeText + " is not a valid lock mode", element);
/*    */       } else {
/* 59 */         joinActivity.setLockMode(lockMode);
/*    */       }
/*    */     }
/*    */     
/* 63 */     return joinActivity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/JoinBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */