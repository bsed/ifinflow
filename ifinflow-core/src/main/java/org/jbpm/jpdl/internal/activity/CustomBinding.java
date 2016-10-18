 package org.jbpm.jpdl.internal.activity;
 
 import org.jbpm.jpdl.internal.xml.JpdlParser;
 import org.jbpm.pvm.internal.wire.binding.ObjectBinding;
 import org.jbpm.pvm.internal.wire.usercode.UserCodeActivityBehaviour;
 import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
 import org.jbpm.pvm.internal.xml.Parse;
 import org.w3c.dom.Element;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class CustomBinding
   extends JpdlBinding
 {
   static ObjectBinding objectBinding = new ObjectBinding();
   
   public CustomBinding() {
     super("custom");
   }
   
   public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
     UserCodeActivityBehaviour userCodeActivityBehaviour = new UserCodeActivityBehaviour();
     UserCodeReference customActivityReference = parser.parseUserCodeReference(element, parse);
     userCodeActivityBehaviour.setCustomActivityReference(customActivityReference);
     return userCodeActivityBehaviour;
   }
 }
