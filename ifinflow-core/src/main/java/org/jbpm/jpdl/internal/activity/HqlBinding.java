package org.jbpm.jpdl.internal.activity;

import java.util.ArrayList;
import java.util.List;
import org.jbpm.jpdl.internal.xml.JpdlParser;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.jbpm.pvm.internal.wire.Descriptor;
import org.jbpm.pvm.internal.wire.descriptor.ListDescriptor;
import org.jbpm.pvm.internal.wire.xml.WireParser;
import org.jbpm.pvm.internal.xml.Parse;
import org.w3c.dom.Element;

public class HqlBinding extends JpdlBinding {
	public static final String TAG = "hql";

	public HqlBinding() {
		super("hql");
	}

	protected HqlBinding(String tagName) {
		super(tagName);
	}

	public Object parseJpdl(Element element, Parse parse, JpdlParser parser) {
		HqlActivity hqlActivity = createHqlActivity();

		Element queryElement = XmlUtil.element(element, "query", parse);
		if (queryElement != null) {
			String query = XmlUtil.getContentText(queryElement);
			hqlActivity.setQuery(query);
		}

		Boolean resultUnique = XmlUtil.attributeBoolean(element, "unique",
				parse);
		if (resultUnique != null) {
			hqlActivity.setResultUnique(resultUnique.booleanValue());
		}

		String variableName = XmlUtil.attribute(element, "var", parse);
		hqlActivity.setResultVariableName(variableName);

		Element parametersElement = XmlUtil.element(element, "parameters");
		List<Element> paramElements = XmlUtil.elements(parametersElement);
		if (!paramElements.isEmpty()) {
			List<Descriptor> parametersDescriptor = new ArrayList();
			for (Element paramElement : paramElements) {
				WireParser wireParser = WireParser.getInstance();
				Descriptor paramDescriptor = (Descriptor) wireParser
						.parseElement(paramElement, parse, "descriptor");
				parametersDescriptor.add(paramDescriptor);
			}

			ListDescriptor parametersListDescriptor = new ListDescriptor();
			parametersListDescriptor.setValueDescriptors(parametersDescriptor);
			hqlActivity.setParametersDescriptor(parametersListDescriptor);
		}

		return hqlActivity;
	}

	protected HqlActivity createHqlActivity() {
		return new HqlActivity();
	}
}
