package org.jbpm.internal.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Jdk14LogFactory implements LogFactory {
	public Jdk14LogFactory() {
		initializeJdk14Logging();
	}

	public Log getLog(String name) {
		return new Jdk14Log(Logger.getLogger(name));
	}

	public static synchronized void redirectCommonsToJdk14() {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.Jdk14Logger");
	}

	public static synchronized void initializeJdk14Logging() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream inputStream = classLoader
				.getResourceAsStream("logging.properties");
		try {
			if (inputStream != null) {
				LogManager.getLogManager().readConfiguration(inputStream);

				String redirectCommons = LogManager.getLogManager()
						.getProperty("redirect.commons.logging");
				if ((redirectCommons != null)
						&& (!redirectCommons.equalsIgnoreCase("disabled"))
						&& (!redirectCommons.equalsIgnoreCase("off"))
						&& (!redirectCommons.equalsIgnoreCase("false"))) {

					redirectCommonsToJdk14();
				}
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("couldn't initialize logging properly",
					e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
