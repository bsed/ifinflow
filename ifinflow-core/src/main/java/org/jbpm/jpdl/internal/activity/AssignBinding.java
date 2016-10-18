package org.jbpm.jpdl.internal.activity;

import java.util.Set;
import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.el.Expression;
import org.jbpm.pvm.internal.el.UelValueExpression;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.jbpm.pvm.internal.wire.Descriptor;
import org.jbpm.pvm.internal.wire.xml.WireParser;
import org.jbpm.pvm.internal.xml.Bindings;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class AssignBinding extends JpdlBinding {
	private static final String FROM_EXPR = "from-expr";
	private static final String LANG = "lang";
	private static final String FROM_VAR = "from-var";
	private static final String FROM_DESC = "from";
	private static final String TO_EXPR = "to-expr";
	private static final String TO_VAR = "to-var";

	public AssignBinding() {
		super("assign");
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		AssignActivity assignActivity = new AssignActivity();

		if (element.hasAttribute("from-expr")) {
			String lang = XmlUtil.attribute(element, "lang");
			Expression fromExpression = Expression.create(
					element.getAttribute("from-expr"), lang);
			assignActivity.setFromExpression(fromExpression);

		} else if (element.hasAttribute("from-var")) {
			assignActivity.setFromVariable(element.getAttribute("from-var"));
		} else {
			Element fromElement = XmlUtil.element(element, "from");
			if (fromElement != null) {
				Set<String> descriptorTags = WireParser.getInstance()
						.getBindings().getTagNames("descriptor");

				Element descriptorElement = XmlUtil.element(fromElement);
				if ((descriptorElement != null)
						&& (descriptorTags.contains(descriptorElement
								.getTagName()))) {
					Descriptor descriptor = parser.parseDescriptor(
							descriptorElement, parse);
					assignActivity.setFromDescriptor(descriptor);
				} else {
					parse.addProblem("missing descriptor element", fromElement);
				}
			} else {
				parse.addProblem(
						"missing from-expr attribute, from-var attribute or from element",
						element);
			}
		}

		if (element.hasAttribute("to-var")) {
			assignActivity.setToVariable(element.getAttribute("to-var"));
		} else if (element.hasAttribute("to-expr")) {
			Expression expression = Expression.create(
					element.getAttribute("to-expr"), "uel-value");
			assignActivity.setToExpression((UelValueExpression) expression);
		}

		return assignActivity;
	}
}
