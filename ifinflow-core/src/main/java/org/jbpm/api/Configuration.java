package org.jbpm.api;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import org.xml.sax.InputSource;

public class Configuration {
	private static ProcessEngine singleton;
	transient Configuration impl;

	public Configuration() {
		this.impl = instantiate("org.jbpm.pvm.internal.cfg.ConfigurationImpl");
	}

	protected Configuration(Object o) {
	}

	protected Configuration instantiate(String className) {
		Configuration implementation;
		try {
			Class<?> implementationClass = null;
			try {
				implementationClass = Class.forName(className, true,
						getClassLoader());
			} catch (ClassNotFoundException ex) {
				implementationClass = Class.forName(className);
			}
			implementation = (Configuration) implementationClass.newInstance();
		} catch (Exception e) {
			throw new JbpmException(
					"couldn't instantiate configuration of type " + className,
					e);
		}
		return implementation;
	}

	protected ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public Configuration setXmlString(String xmlString) {
		this.impl.setXmlString(xmlString);
		return this.impl;
	}

	public Configuration setResource(String resource) {
		this.impl.setResource(resource);
		return this.impl;
	}

	public Configuration setInputStream(InputStream inputStream) {
		this.impl.setInputStream(inputStream);
		return this.impl;
	}

	public Configuration setInputSource(InputSource inputSource) {
		this.impl.setInputSource(inputSource);
		return this.impl;
	}

	public Configuration setUrl(URL url) {
		this.impl.setUrl(url);
		return this.impl;
	}

	public Configuration setFile(File file) {
		this.impl.setFile(file);
		return this.impl;
	}

	public ProcessEngine buildProcessEngine() {
		return this.impl.buildProcessEngine();
	}

	public Configuration setHibernateSessionFactory(
			Object hibernateSessionFactory) {
		return this.impl.setHibernateSessionFactory(hibernateSessionFactory);
	}

	public static ProcessEngine getProcessEngine() {
		if (singleton == null) {
			synchronized (Configuration.class) {
				if (singleton == null) {
					singleton = new Configuration().setResource("jbpm.cfg.xml")
							.buildProcessEngine();
				}
			}
		}
		return singleton;
	}

	public void setCurrentProcessEngine(ProcessEngine pe) {
		singleton = pe;
	}
}
