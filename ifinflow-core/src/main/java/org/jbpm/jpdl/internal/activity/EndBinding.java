package org.jbpm.jpdl.internal.activity;

import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class EndBinding extends JpdlBinding {
	public EndBinding() {
		super("end");
	}

	protected EndBinding(String tag) {
		super(tag);
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		boolean endProcessInstance = true;
		String ends = XmlUtil.attribute(element, "ends");
		if ("execution".equalsIgnoreCase(ends)) {
			endProcessInstance = false;
		}

		String state = XmlUtil.attribute(element, "state");

		EndActivity endActivity = new EndActivity();
		endActivity.setEndProcessInstance(endProcessInstance);
		endActivity.setState(state);

		return endActivity;
	}
}
