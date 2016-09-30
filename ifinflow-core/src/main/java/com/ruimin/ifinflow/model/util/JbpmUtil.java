package com.ruimin.ifinflow.model.util;

import org.jbpm.api.Configuration;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.TaskService;

public class JbpmUtil {
	private static JbpmUtil instance = null;

	public static JbpmUtil getInstance() {
		if (instance == null) {
			synchronized (JbpmUtil.class) {
				if (instance == null) {
					instance = new JbpmUtil();
				}
			}
		}
		return instance;
	}

	public static ProcessEngine buildProcessEngine() {
		return Configuration.getProcessEngine();
	}

	public static ExecutionService getExecutionService() {
		return buildProcessEngine().getExecutionService();
	}

	public static TaskService getTaskService() {
		return buildProcessEngine().getTaskService();
	}
}
