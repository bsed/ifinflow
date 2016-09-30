package org.jbpm.internal.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jLog extends Log {
	Logger log;

	public Log4jLog(Logger log) {
		this.log = log;
	}

	public void error(String msg) {
		this.log.error(msg);
	}

	public void error(String msg, Throwable exception) {
		this.log.error(msg, exception);
	}

	public boolean isInfoEnabled() {
		return this.log.isInfoEnabled();
	}

	public void info(String msg) {
		this.log.info(msg);
	}

	public void info(String msg, Throwable exception) {
		this.log.info(msg, exception);
	}

	public boolean isDebugEnabled() {
		return this.log.isDebugEnabled();
	}

	public void debug(String msg) {
		this.log.debug(msg);
	}

	public void debug(String msg, Throwable exception) {
		this.log.debug(msg, exception);
	}

	public boolean isTraceEnabled() {
		return this.log.isTraceEnabled();
	}

	public void trace(String msg) {
		this.log.trace(msg);
	}

	public void trace(String msg, Throwable exception) {
		this.log.trace(msg, exception);
	}

	public boolean isWarnEnabled() {
		return this.log.isEnabledFor(Level.WARN);
	}

	public void warn(String msg) {
		this.log.warn(msg);
	}

	public void warn(String msg, Throwable exception) {
		this.log.warn(msg, exception);
	}
}
