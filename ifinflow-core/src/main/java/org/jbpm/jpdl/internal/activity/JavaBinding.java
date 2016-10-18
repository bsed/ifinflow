package org.jbpm.jpdl.internal.activity;

import java.util.List;
import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.jbpm.pvm.internal.wire.descriptor.ArgDescriptor;
import org.jbpm.pvm.internal.wire.descriptor.ObjectDescriptor;
import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
import org.jbpm.pvm.internal.wire.xml.WireParser;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class JavaBinding extends JpdlBinding {
	public static final String TAG = "java";

	public JavaBinding() {
		super("java");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		JavaActivity javaActivity = new JavaActivity();
		if (XmlUtil.attribute(element, "method", parse) != null) {
			String jndiName = XmlUtil.attribute(element, "ejb-jndi-name");
			if (jndiName != null) {
				parseEjbInvocation(javaActivity, element, parse, parser);
			} else {
				parseJavaInvocation(javaActivity, element, parse, parser);
			}
		}
		String variableName = XmlUtil.attribute(element, "var");
		javaActivity.setVariableName(variableName);
		return javaActivity;
	}

	private void parseEjbInvocation(JavaActivity javaActivity, Element element,
			Parse parse, JpdlParser parser) {
		javaActivity.setJndiName(XmlUtil.attribute(element, "ejb-jndi-name"));
		javaActivity.setMethodName(XmlUtil.attribute(element, "method"));
		List<Element> argElements = XmlUtil.elements(element, "arg");
		List<ArgDescriptor> argDescriptors = new WireParser().parseArgs(
				argElements, parse);
		javaActivity.setArgDescriptors(argDescriptors);
	}

	private void parseJavaInvocation(JavaActivity javaActivity,
			Element element, Parse parse, JpdlParser parser) {
		UserCodeReference invocationReference = parser.parseUserCodeReference(
				element, parse);
		javaActivity.setInvocationReference(invocationReference);
		ObjectDescriptor objectDescriptor = (ObjectDescriptor) invocationReference
				.getDescriptor();
		javaActivity.setArgDescriptors(objectDescriptor.getArgDescriptors());
		objectDescriptor.setArgDescriptors(null);
		javaActivity.setMethodName(objectDescriptor.getMethodName());
		objectDescriptor.setMethodName(null);
	}
}
