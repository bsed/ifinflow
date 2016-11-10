/*     */ package org.jbpm.jpdl.internal.activity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*     */ import org.jbpm.pvm.internal.el.Expression;
/*     */ import org.jbpm.pvm.internal.util.XmlUtil;
/*     */ import org.jbpm.pvm.internal.wire.Descriptor;
/*     */ import org.jbpm.pvm.internal.wire.WireContext;
/*     */ import org.jbpm.pvm.internal.wire.xml.WireParser;
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
/*     */ public class SubProcessBinding
/*     */   extends JpdlBinding
/*     */ {
/*     */   public SubProcessBinding()
/*     */   {
/*  45 */     super("sub-process");
/*     */   }
/*     */   
/*     */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/*  49 */     SubProcessActivity subProcessActivity = new SubProcessActivity();
/*     */     
/*  51 */     String subProcessKey = XmlUtil.attribute(element, "sub-process-key");
/*  52 */     subProcessActivity.setSubProcessKey(subProcessKey);
/*     */     
/*  54 */     String subProcessId = XmlUtil.attribute(element, "sub-process-id");
/*  55 */     subProcessActivity.setSubProcessId(subProcessId);
/*     */     
/*  57 */     List<SubProcessInParameterImpl> inParameters = new ArrayList();
/*  58 */     for (Element inElement : XmlUtil.elements(element, "parameter-in")) {
/*  59 */       SubProcessInParameterImpl inParameter = new SubProcessInParameterImpl();
/*  60 */       parseParameter(inElement, inParameter);
/*  61 */       inParameters.add(inParameter);
/*     */       
/*  63 */       if (inParameter.getSubVariableName() == null) {
/*  64 */         parse.addProblem("no 'subvar' specified for parameter-in", element);
/*     */       }
/*  66 */       if ((inParameter.getExpression() == null) && (inParameter.getVariableName() == null))
/*     */       {
/*     */ 
/*  69 */         parse.addProblem("no 'expr' or 'variable' specified for parameter-in '" + inParameter.getSubVariableName() + "'", element);
/*     */       }
/*  71 */       if ((inParameter.getExpression() != null) && (inParameter.getVariableName() != null))
/*     */       {
/*     */ 
/*  74 */         parse.addProblem("attributes 'expr' and 'variable' are mutually exclusive on parameter-in", element);
/*     */       }
/*     */     }
/*  77 */     subProcessActivity.setInParameters(inParameters);
/*     */     
/*  79 */     List<SubProcessOutParameterImpl> outParameters = new ArrayList();
/*  80 */     for (Element outElement : XmlUtil.elements(element, "parameter-out")) {
/*  81 */       SubProcessOutParameterImpl outParameter = new SubProcessOutParameterImpl();
/*  82 */       parseParameter(outElement, outParameter);
/*  83 */       outParameters.add(outParameter);
/*     */       
/*  85 */       if (outParameter.getVariableName() == null) {
/*  86 */         parse.addProblem("no 'variable' specified for parameter-in", element);
/*     */       }
/*  88 */       if ((outParameter.getExpression() == null) && (outParameter.getSubVariableName() == null))
/*     */       {
/*     */ 
/*  91 */         parse.addProblem("no 'expr' or 'subvar' specified for parameter-out '" + outParameter.getVariableName() + "'", element);
/*     */       }
/*  93 */       if ((outParameter.getExpression() != null) && (outParameter.getSubVariableName() != null))
/*     */       {
/*     */ 
/*  96 */         parse.addProblem("attributes 'expr' and 'subvar' are mutually exclusive on parameter-out '" + outParameter.getVariableName() + "'", element);
/*     */       }
/*     */     }
/*  99 */     subProcessActivity.setOutParameters(outParameters);
/*     */     
/* 101 */     Map<String, String> swimlaneMappings = parseSwimlaneMappings(element, parse);
/* 102 */     subProcessActivity.setSwimlaneMappings(swimlaneMappings);
/*     */     
/* 104 */     Map<Object, String> outcomeVariableMappings = new HashMap();
/*     */     
/* 106 */     String outcomeExpressionText = XmlUtil.attribute(element, "outcome");
/* 107 */     String outcomeLanguage = XmlUtil.attribute(element, "outcome-lang");
/* 108 */     if (outcomeExpressionText != null) {
/* 109 */       Expression outcomeExpression = Expression.create(outcomeExpressionText, outcomeLanguage);
/* 110 */       subProcessActivity.setOutcomeExpression(outcomeExpression);
/*     */       
/* 112 */       for (Element transitionElement : XmlUtil.elements(element, "transition")) {
/* 113 */         Element outcomeValueElement = XmlUtil.element(transitionElement, "outcome-value");
/* 114 */         if (outcomeValueElement != null) {
/* 115 */           String transitionName = XmlUtil.attribute(transitionElement, "name");
/* 116 */           if (transitionName == null) {
/* 117 */             parse.addProblem("transitions with an outcome-value must have a name", transitionElement);
/*     */           }
/* 119 */           Element valueElement = XmlUtil.element(outcomeValueElement);
/* 120 */           if (valueElement != null) {
/* 121 */             Descriptor descriptor = (Descriptor)WireParser.getInstance().parseElement(valueElement, parse);
/* 122 */             Object value = WireContext.create(descriptor);
/* 123 */             outcomeVariableMappings.put(value, transitionName);
/* 124 */             subProcessActivity.setOutcomeVariableMappings(outcomeVariableMappings);
/*     */           } else {
/* 126 */             parse.addProblem("outcome-value must contain exactly one element", outcomeValueElement);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 133 */     return subProcessActivity;
/*     */   }
/*     */   
/*     */   void parseParameter(Element element, SubProcessParameterImpl parameter) {
/* 137 */     String name = XmlUtil.attribute(element, "subvar");
/* 138 */     parameter.setSubVariableName(name);
/*     */     
/* 140 */     String expressionText = XmlUtil.attribute(element, "expr");
/* 141 */     String language = XmlUtil.attribute(element, "expr-lang");
/* 142 */     if (expressionText != null) {
/* 143 */       Expression expression = Expression.create(expressionText, language);
/* 144 */       parameter.setExpression(expression);
/*     */     }
/*     */     
/* 147 */     String variable = XmlUtil.attribute(element, "var");
/* 148 */     if (variable != null) {
/* 149 */       parameter.setVariableName(variable);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Map<String, String> parseSwimlaneMappings(Element element, Parse parse) {
/* 154 */     Map<String, String> swimlaneMappings = new HashMap();
/*     */     
/* 156 */     for (Element inElement : XmlUtil.elements(element, "swimlane-mapping")) {
/* 157 */       String swimlane = XmlUtil.attribute(inElement, "swimlane", parse);
/* 158 */       String subSwimlane = XmlUtil.attribute(inElement, "sub-swimlane", parse);
/*     */       
/* 160 */       swimlaneMappings.put(swimlane, subSwimlane);
/*     */     }
/*     */     
/* 163 */     return swimlaneMappings;
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/SubProcessBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */