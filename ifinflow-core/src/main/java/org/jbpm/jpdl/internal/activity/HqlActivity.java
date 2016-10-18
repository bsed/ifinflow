package org.jbpm.jpdl.internal.activity;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.JbpmException;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.internal.log.Log;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.model.ScopeInstanceImpl;
import org.jbpm.pvm.internal.wire.Descriptor;
import org.jbpm.pvm.internal.wire.WireContext;
import org.jbpm.pvm.internal.wire.descriptor.ListDescriptor;

public class HqlActivity extends JpdlAutomaticActivity {
	private static final Log log = Log.getLog(HqlActivity.class.getName());

	private static final long serialVersionUID = 1L;
	protected String query;
	protected ListDescriptor parametersDescriptor;
	protected String resultVariableName;
	protected boolean isResultUnique;

	public void perform(OpenExecution execution) {
		EnvironmentImpl environment = EnvironmentImpl.getCurrent();
		if (environment == null) {
			throw new JbpmException("no environment for jpdl activity hql");
		}
		Session session = (Session) environment.get(Session.class);

		Query q = createQuery(session);

		if (this.parametersDescriptor != null) {
			for (Descriptor valueDescriptor : this.parametersDescriptor
					.getValueDescriptors()) {
				String parameterName = valueDescriptor.getName();
				Object value = WireContext.create(valueDescriptor,
						(ScopeInstanceImpl) execution);
				applyParameter(q, parameterName, value);
			}
		}

		Object result = null;
		if (this.isResultUnique) {
			result = q.uniqueResult();
		} else {
			result = q.list();
		}

		execution.setVariable(this.resultVariableName, result);
	}

	protected Query createQuery(Session session) {
		return session.createQuery(this.query);
	}

	public void applyParameter(Query q, String parameterName, Object value) {
		if ((value instanceof String)) {
			q.setString(parameterName, (String) value);
		} else if ((value instanceof Long)) {
			q.setLong(parameterName, ((Long) value).longValue());
		} else {
			log.error("unknown hql parameter type: "
					+ value.getClass().getName());
		}
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setParametersDescriptor(ListDescriptor parametersDescriptor) {
		this.parametersDescriptor = parametersDescriptor;
	}

	public void setResultUnique(boolean isResultUnique) {
		this.isResultUnique = isResultUnique;
	}

	public void setResultVariableName(String resultVariableName) {
		this.resultVariableName = resultVariableName;
	}
}
