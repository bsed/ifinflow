package org.jbpm.jpdl.internal.activity;

import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class ForkBinding extends JpdlBinding {
	public ForkBinding() {
		super("fork");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		return new ForkActivity();
	}
}
