package org.jbpm.jpdl.internal.activity;

import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.el.Expression;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExpressionCondition;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.jbpm.pvm.internal.wire.usercode.UserCodeCondition;
import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class ForEachBinding extends JpdlBinding {
	private static final String VARIABLE = "var";
	private static final String COLLECTION = "in";

	public ForEachBinding() {
		super("foreach");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		ForEachActivity activity = new ForEachActivity();

		if (element.hasAttribute("var")) {
			activity.setVariable(element.getAttribute("var"));
		} else {
			parse.addProblem("var attribute missing", element);
		}

		if (element.hasAttribute("in")) {
			Expression collection = Expression.create(
					element.getAttribute("in"), "uel-value");
			activity.setCollection(collection);
		} else {
			parse.addProblem("in attribute missing", element);
		}

		Element transitionElement = XmlUtil.element(element, "transition");

		if (transitionElement == null) {
			parse.addProblem("outgoing transition expected", element);
		} else {
			ActivityImpl activityFromStack = (ActivityImpl) parse
					.contextStackFind(ActivityImpl.class);
			TransitionImpl transition = activityFromStack
					.getDefaultOutgoingTransition();

			Element conditionElement = XmlUtil.element(transitionElement,
					"condition");
			if (conditionElement != null) {
				if (conditionElement.hasAttribute("expr")) {
					ExpressionCondition condition = new ExpressionCondition();
					condition.setExpression(conditionElement
							.getAttribute("expr"));
					condition.setLanguage(XmlUtil.attribute(conditionElement,
							"lang"));
					transition.setCondition(condition);
				} else {
					Element handlerElement = XmlUtil.element(conditionElement,
							"handler");
					if (handlerElement != null) {
						UserCodeReference conditionReference = parser
								.parseUserCodeReference(handlerElement, parse);
						UserCodeCondition condition = new UserCodeCondition();
						condition.setConditionReference(conditionReference);
						transition.setCondition(condition);
					}
				}
			}
		}

		return activity;
	}
}
