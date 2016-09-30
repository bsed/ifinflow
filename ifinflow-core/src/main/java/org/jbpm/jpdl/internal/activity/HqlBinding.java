/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.jbpm.jpdl.internal.xml.JpdlParser;
/*    */ import org.jbpm.pvm.internal.util.XmlUtil;
/*    */ import org.jbpm.pvm.internal.wire.Descriptor;
/*    */ import org.jbpm.pvm.internal.wire.descriptor.ListDescriptor;
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
/*    */ public class HqlBinding
/*    */   extends JpdlBinding
/*    */ {
/*    */   public static final String TAG = "hql";
/*    */   
/*    */   public HqlBinding()
/*    */   {
/* 44 */     super("hql");
/*    */   }
/*    */   
/*    */   protected HqlBinding(String tagName) {
/* 48 */     super(tagName);
/*    */   }
/*    */   
/*    */   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
/* 52 */     HqlActivity hqlActivity = createHqlActivity();
/*    */     
/* 54 */     Element queryElement = XmlUtil.element(element, "query", parse);
/* 55 */     if (queryElement != null) {
/* 56 */       String query = XmlUtil.getContentText(queryElement);
/* 57 */       hqlActivity.setQuery(query);
/*    */     }
/*    */     
/* 60 */     Boolean resultUnique = XmlUtil.attributeBoolean(element, "unique", parse);
/* 61 */     if (resultUnique != null) {
/* 62 */       hqlActivity.setResultUnique(resultUnique.booleanValue());
/*    */     }
/*    */     
/* 65 */     String variableName = XmlUtil.attribute(element, "var", parse);
/* 66 */     hqlActivity.setResultVariableName(variableName);
/*    */     
/* 68 */     Element parametersElement = XmlUtil.element(element, "parameters");
/* 69 */     List<Element> paramElements = XmlUtil.elements(parametersElement);
/* 70 */     if (!paramElements.isEmpty()) {
/* 71 */       List<Descriptor> parametersDescriptor = new ArrayList();
/* 72 */       for (Element paramElement : paramElements) {
/* 73 */         WireParser wireParser = WireParser.getInstance();
/* 74 */         Descriptor paramDescriptor = (Descriptor)wireParser.parseElement(paramElement, parse, "descriptor");
/* 75 */         parametersDescriptor.add(paramDescriptor);
/*    */       }
/*    */       
/* 78 */       ListDescriptor parametersListDescriptor = new ListDescriptor();
/* 79 */       parametersListDescriptor.setValueDescriptors(parametersDescriptor);
/* 80 */       hqlActivity.setParametersDescriptor(parametersListDescriptor);
/*    */     }
/*    */     
/* 83 */     return hqlActivity;
/*    */   }
/*    */   
/*    */   protected HqlActivity createHqlActivity() {
/* 87 */     return new HqlActivity();
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/HqlBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */