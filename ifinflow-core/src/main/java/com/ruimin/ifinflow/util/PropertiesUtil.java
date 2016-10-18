package com.ruimin.ifinflow.util;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PropertiesUtil {
	private static Map<String, String> map = new HashMap();

	public static synchronized void init(String sPropFile) {
		ResourceBundle bundle = ResourceBundle.getBundle(sPropFile);
		Enumeration<String> enums = bundle.getKeys();
		String key = null;
		String value = null;
		while (enums.hasMoreElements()) {
			key = (String) enums.nextElement();
			value = bundle.getString(key);
			map.put(key, value);
		}
		bundle = null;
	}

	public static synchronized void reload(String sPropFile) {
		init(sPropFile);
	}

	public static String getValue(String key, Object... args) {
		if (map.containsKey(key)) {
			String value = (String) map.get(key);
			return MessageFormat.format(value, args);
		}
		return key;
	}

	public static void main(String[] args) {
		init("config.msg.basemessage");
		System.out.println((String) map.get("I_000001"));
	}
}
