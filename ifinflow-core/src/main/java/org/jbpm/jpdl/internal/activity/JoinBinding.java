package org.jbpm.jpdl.internal.activity;

import org.hibernate.LockMode;
import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.el.Expression;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class JoinBinding extends JpdlBinding {
	private static final String MULTIPLICITY = "multiplicity";
	private static final String LOCKMODE = "lockmode";

	public JoinBinding() {
		super("join");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		JoinActivity joinActivity = new JoinActivity();

		if (element.hasAttribute("multiplicity")) {
			String multiplicityText = element.getAttribute("multiplicity");
			Expression expression = Expression.create(multiplicityText,
					"uel-value");
			joinActivity.setMultiplicity(expression);
		}

		if (element.hasAttribute("lockmode")) {
			String lockModeText = element.getAttribute("lockmode");
			LockMode lockMode = LockMode.parse(lockModeText.toUpperCase());
			if (lockMode == null) {
				parse.addProblem(lockModeText + " is not a valid lock mode",
						element);
			} else {
				joinActivity.setLockMode(lockMode);
			}
		}

		return joinActivity;
	}
}
