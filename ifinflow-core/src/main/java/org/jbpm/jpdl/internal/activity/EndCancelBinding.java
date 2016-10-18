package org.jbpm.jpdl.internal.activity;

import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class EndCancelBinding extends EndBinding {
	public EndCancelBinding() {
		super("end-cancel");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		EndActivity endActivity = (EndActivity) super.parseJpdl(element, parse,
				parser);
		endActivity.setState("cancel");
		return endActivity;
	}
}
