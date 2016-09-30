package org.jbpm.internal.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Jdk14Log extends Log {
	Logger log;

	public Jdk14Log(Logger logger) {
		this.log = logger;
	}

	public void error(String msg) {
		this.log.log(Level.SEVERE, msg);
	}

	public void error(String msg, Throwable exception) {
		this.log.log(Level.SEVERE, msg, exception);
	}

	public boolean isInfoEnabled() {
		return this.log.isLoggable(Level.INFO);
	}

	public void info(String msg) {
		this.log.log(Level.INFO, msg);
	}

	public void info(String msg, Throwable exception) {
		this.log.log(Level.INFO, msg, exception);
	}

	public boolean isDebugEnabled() {
		return this.log.isLoggable(Level.FINE);
	}

	public void debug(String msg) {
		this.log.log(Level.FINE, msg);
	}

	public void debug(String msg, Throwable exception) {
		this.log.log(Level.FINE, msg, exception);
	}

	public boolean isTraceEnabled() {
		return this.log.isLoggable(Level.FINEST);
	}

	public void trace(String msg) {
		this.log.log(Level.FINEST, msg);
	}

	public void trace(String msg, Throwable exception) {
		this.log.log(Level.FINEST, msg, exception);
	}

	public boolean isWarnEnabled() {
		return this.log.isLoggable(Level.WARNING);
	}

	public void warn(String msg) {
		this.log.warning(msg);
	}

	public void warn(String msg, Throwable exception) {
		this.log.log(Level.WARNING, msg, exception);
	}
}
