package org.jbpm.jpdl.internal.activity;

import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.wire.usercode.UserCodeEventListener;
import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class EventListenerBinding extends JpdlBinding {
	public EventListenerBinding() {
		super("event-listener");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		UserCodeEventListener userCodeEventListener = new UserCodeEventListener();

		UserCodeReference eventListenerReference = parser
				.parseUserCodeReference(element, parse);
		userCodeEventListener.setEventListenerReference(eventListenerReference);

		return userCodeEventListener;
	}
}
