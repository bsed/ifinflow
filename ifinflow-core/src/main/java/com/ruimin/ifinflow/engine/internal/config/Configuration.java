package com.ruimin.ifinflow.engine.internal.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.ruimin.ifinflow.engine.internal.entity.IFinFlowConfig;

public class Configuration {
	private static Map<String, String> _configs = new HashMap();

	private static boolean isLoaded = false;

	public static int INIT = 1;
	public static int RELOAD = 2;
	public static int CLEAR = 3;

	public static synchronized void init(Session session) {
		if (isLoaded) {
			return;
		}
		try {
			List<IFinFlowConfig> list = session.createCriteria(
					IFinFlowConfig.class).list();

			for (IFinFlowConfig config : list) {
				if (config.getConfigValue() == null) {
					_configs.put(config.getConfigKey(),
							config.getDefaultValue());
				} else {
					_configs.put(config.getConfigKey(), config.getConfigValue());
				}
			}
			isLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void reload(Session session) {
		clear();
		init(session);
	}

	public static synchronized void setKeyValue(String key, String value) {
		_configs.put(key, value);
	}

	public static synchronized void clear() {
		_configs.clear();
		isLoaded = false;
	}

	public static String getValue(String key) {
		if (_configs.containsKey(key)) {
			return (String) _configs.get(key);
		}
		return null;
	}
}
