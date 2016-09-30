package org.jbpm.internal.log;

public abstract class Log {
	static LogFactory logFactory;

	public static synchronized Log getLog(String name) {
		if (logFactory == null) {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();

			if (classLoader.getResource("logging.properties") != null) {
				logFactory = new Jdk14LogFactory();

			} else if (isLog4jAvailable(classLoader)) {
				logFactory = new Log4jLogFactory();
			} else {
				logFactory = new Jdk14LogFactory();
			}
		}

		return logFactory.getLog(name);
	}

	static boolean isLog4jAvailable(ClassLoader classLoader) {
		try {
			Class.forName("org.apache.log4j.LogManager", false, classLoader);
			return true;
		} catch (ClassNotFoundException e) {
		}
		return false;
	}

	public abstract void error(String paramString);

	public abstract void error(String paramString, Throwable paramThrowable);

	public abstract boolean isInfoEnabled();

	public abstract void info(String paramString);

	public abstract void info(String paramString, Throwable paramThrowable);

	public abstract boolean isDebugEnabled();

	public abstract void debug(String paramString);

	public abstract void debug(String paramString, Throwable paramThrowable);

	public abstract boolean isTraceEnabled();

	public abstract void trace(String paramString);

	public abstract void trace(String paramString, Throwable paramThrowable);

	public abstract boolean isWarnEnabled();

	public abstract void warn(String paramString);

	public abstract void warn(String paramString, Throwable paramThrowable);
}
