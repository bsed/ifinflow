package org.jbpm.jpdl.internal.activity;

import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class GroupBinding extends JpdlBinding {
	public GroupBinding() {
		super("group");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		GroupActivity groupActivity = new GroupActivity();

		ActivityImpl activity = (ActivityImpl) parse
				.contextStackFind(ActivityImpl.class);

		parser.parseActivities(element, parse, activity);

		return groupActivity;
	}
}
