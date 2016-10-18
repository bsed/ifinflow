package org.jbpm.jpdl.internal.activity;

import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class MailBinding extends JpdlBinding {
	public MailBinding() {
		super("mail");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		MailActivity activity = new MailActivity();
		activity.setMailProducerReference(parser.parseMailProducer(element,
				parse, null));
		return activity;
	}
}
