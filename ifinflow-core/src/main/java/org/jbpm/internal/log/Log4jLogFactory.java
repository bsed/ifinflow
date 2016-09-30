package org.jbpm.internal.log;

import org.apache.log4j.LogManager;

public class Log4jLogFactory implements LogFactory {
	public Log getLog(String name) {
		return new Log4jLog(LogManager.getLogger(name));
	}
}
