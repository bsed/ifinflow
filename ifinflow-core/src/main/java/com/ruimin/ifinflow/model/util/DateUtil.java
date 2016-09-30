package com.ruimin.ifinflow.model.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private static Log log = LogFactory.getLog(DateUtil.class);

	public static synchronized String getDatePattern() {
		return "yyyy-MM-dd HH:mm:ss";
	}

	private static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '"
					+ aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			log.error("ParseException: " + pe);
		}

		return date;
	}

	public static Date convertStringToDate(String strDate) {
		Date aDate = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("converting date with pattern: " + getDatePattern());
			}

			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException e) {
			log.error("Could not convert '" + strDate
					+ "' to a date, throwing exception");

			e.printStackTrace();
		}

		return aDate;
	}

	public static Date convertString2Date(String regx, String date) {
		Date aDate = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("converting date with pattern: " + getDatePattern());
			}

			aDate = convertStringToDate(regx, date);
		} catch (ParseException e) {
			log.error("Could not convert '" + date
					+ "' to a date, throwing exception");

			e.printStackTrace();
		}

		return aDate;
	}
}
