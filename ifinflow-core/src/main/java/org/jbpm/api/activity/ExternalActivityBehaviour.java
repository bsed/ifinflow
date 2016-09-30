package org.jbpm.api.activity;

import java.util.Map;

public abstract interface ExternalActivityBehaviour extends ActivityBehaviour {
	public abstract void signal(ActivityExecution paramActivityExecution,
			String paramString, Map<String, ?> paramMap) throws Exception;
}
