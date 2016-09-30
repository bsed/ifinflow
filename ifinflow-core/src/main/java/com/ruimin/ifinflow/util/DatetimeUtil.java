package com.ruimin.ifinflow.util;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.jbpm.api.JbpmException;
import org.jbpm.pvm.internal.cal.BusinessCalendar;
import org.jbpm.pvm.internal.env.EnvironmentImpl;

public class DatetimeUtil {
	public static String toDatetimeString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static Date string2Date(String date) throws IFinFlowException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date result = null;
		try {
			result = format.parse(date);
		} catch (ParseException e) {
			throw new IFinFlowException(104005, e, new Object[] { date });
		}
		return result;
	}

	public static Date getDay(int n) {
		Calendar day = Calendar.getInstance();
		day.add(5, -n + 1);
		day.set(11, 0);
		day.set(12, 0);
		day.set(13, 0);
		day.set(14, 0);
		return day.getTime();
	}

	public static long getBetweenBaseTime(Date date) {
		Calendar targetDay = Calendar.getInstance();
		targetDay.setTime(date);
		targetDay.set(11, 8);
		targetDay.set(12, 0);
		targetDay.set(13, 0);
		targetDay.set(14, 0);

		return (date.getTime() - targetDay.getTimeInMillis()) / 60000L;
	}

	public static Date calculateDueDate(Date baseDate, String durationExpression) {
		Date duedate = null;
		if (durationExpression != null) {
			String durationString = null;
			char durationSeparator = '+';

			durationString = durationExpression;

			if ((durationString == null) || (durationString.length() == 0)) {
				duedate = baseDate;
			} else {
				if ((durationString.contains("business"))
						&& (durationSeparator == '-')) {
					throw new JbpmException(
							"Invalid duedate, subtraction ('-') not supported if duedate contains 'business'");
				}
				BusinessCalendar businessCalendar = (BusinessCalendar) EnvironmentImpl
						.getFromCurrent(BusinessCalendar.class);
				if (durationSeparator == '+') {
					duedate = businessCalendar.add(baseDate, durationString);
				} else {
					duedate = businessCalendar.subtract(baseDate,
							durationString);
				}
			}
		}
		return duedate;
	}

	public static void main(String[] args) {
		System.out.println(getDay(1));

		Calendar targetDay = Calendar.getInstance();
		targetDay.setTime(new Date());
		targetDay.set(targetDay.get(1), targetDay.get(2), targetDay.get(5), 8,
				0, 0);
		targetDay.set(14, 0);

		System.out.println(targetDay.getTime());
		System.out.println(getBetweenBaseTime(new Date()));
	}
}
