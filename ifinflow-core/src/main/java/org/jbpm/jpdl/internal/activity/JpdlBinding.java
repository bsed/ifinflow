package org.jbpm.jpdl.internal.activity;

import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.util.TagBinding;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.jbpm.pvm.internal.xml.Parse;
import org.jbpm.pvm.internal.xml.Parser;
import org.w3c.dom.Element;

public abstract class JpdlBinding extends TagBinding {
	public JpdlBinding(String tagName) {
		super(tagName, null, null);
	}

	public abstract Object parseJpdl(Element paramElement, Parse paramParse,
			JpdlParser paramJpdlParser);

	public final Object parse(Element element, Parse parse, Parser parser) {
		return parseJpdl(element, parse, (JpdlParser) parser);
	}

	public void parseName(Element element, ActivityImpl activity, Parse parse) {
		String name = XmlUtil.attribute(element, "name",
				isNameRequired() ? parse : null);

		if (name != null) {
			if (name.length() == 0) {
				parse.addProblem(XmlUtil.errorMessageAttribute(element, "name",
						name, "is empty"), element);
			}
			activity.setName(name);
		}
	}

	public boolean isNameRequired() {
		return true;
	}
}
