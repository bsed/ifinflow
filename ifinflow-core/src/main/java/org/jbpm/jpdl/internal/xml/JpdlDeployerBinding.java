/*    */ package org.jbpm.jpdl.internal.xml;
/*    */ 
/*    */ import org.jbpm.jpdl.internal.repository.JpdlDeployer;
/*    */ import org.jbpm.pvm.internal.wire.binding.WireDescriptorBinding;
/*    */ import org.jbpm.pvm.internal.wire.descriptor.ObjectDescriptor;
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
/*    */ public class JpdlDeployerBinding
/*    */   extends WireDescriptorBinding
/*    */ {
/*    */   public JpdlDeployerBinding()
/*    */   {
/* 37 */     super("jpdl-deployer");
/*    */   }
/*    */   
/*    */   public Object parse(Element element, Parse parse, Parser parser) {
/* 41 */     return new ObjectDescriptor(JpdlDeployer.class);
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/xml/JpdlDeployerBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */