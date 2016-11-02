/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.env.EnvironmentImpl;
/*    */ import org.jbpm.pvm.internal.script.ScriptManager;
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
/*    */ public class ScriptBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   private static final String TAG = "script";
/*    */   
/*    */   public ScriptBinding()
/*    */   {
/* 40 */     super("script");
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 44 */     String language = null;
/*    */     
/* 46 */     String script = XmlUtil.attribute(element, "expr");
/* 47 */     Element textElement = XmlUtil.element(element, "text");
/* 48 */     if (script != null) {
/* 49 */       ScriptManager scriptManager = (ScriptManager)EnvironmentImpl.getFromCurrent(ScriptManager.class);
/* 50 */       language = scriptManager.getDefaultExpressionLanguage();
/* 51 */       if (textElement != null) {
/* 52 */         parse.addProblem("in <script ...> attribute expr can't be combined with a nested text element", element);
/*    */       }
/* 54 */     } else if (textElement != null) {
/* 55 */       language = XmlUtil.attribute(element, "lang");
/* 56 */       script = XmlUtil.getContentText(textElement);
/*    */     } else {
/* 58 */       parse.addProblem("<script...> element must have either expr attribute or nested text element", element);
/*    */     }
/*    */     
/* 61 */     String variableName = XmlUtil.attribute(element, "var");
/*    */     
/* 63 */     ScriptActivity scriptActivity = new ScriptActivity();
/* 64 */     scriptActivity.setScript(script);
/* 65 */     scriptActivity.setLanguage(language);
/* 66 */     scriptActivity.setVariableName(variableName);
/*    */     
/* 68 */     return scriptActivity;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/ScriptBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */