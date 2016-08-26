package com.ruimin.ifinflow.engine.internal.cal.vo;

import java.io.Serializable;

public class BusinessCalendarVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sunday;
	private String monday;
	private String tuesday;
	private String wednesday;
	private String thursday;
	private String friday;
	private String saturday;
	private String holidayStr;
	private String[] holidays;
	private String nonworkingdays;

	public String getSunday() {
		return this.sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public String getMonday() {
		return this.monday;
	}

	public void setMonday(String monday) {
		this.monday = monday;
	}

	public String getTuesday() {
		return this.tuesday;
	}

	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	public String getWednesday() {
		return this.wednesday;
	}

	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	public String getThursday() {
		return this.thursday;
	}

	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	public String getFriday() {
		return this.friday;
	}

	public void setFriday(String friday) {
		this.friday = friday;
	}

	public String getSaturday() {
		return this.saturday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public String getHolidayStr() {
		return this.holidayStr;
	}

	public void setHolidayStr(String holidayStr) {
		this.holidayStr = holidayStr;
	}

	public String[] getHolidays() {
		return this.holidays;
	}

	public void setHolidays(String[] holidays) {
		this.holidays = holidays;
	}

	public String getNonworkingdays() {
		return this.nonworkingdays;
	}

	public void setNonworkingdays(String nonworkingdays) {
		this.nonworkingdays = nonworkingdays;
	}
}
