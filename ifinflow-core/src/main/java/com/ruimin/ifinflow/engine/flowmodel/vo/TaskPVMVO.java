package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;

public class TaskPVMVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String timeScope;
	private String todayAvg;
	private String fiveDaysAvg;
	private String thirtyDaysAvg;

	public TaskPVMVO(String timeScope) {
		this.timeScope = timeScope;
	}

	public TaskPVMVO(String timeScope, String todayAvg, String fiveDaysAvg,
			String thirtyDaysAvg) {
		this.timeScope = timeScope;
		this.todayAvg = todayAvg;
		this.fiveDaysAvg = fiveDaysAvg;
		this.thirtyDaysAvg = thirtyDaysAvg;
	}

	public String getTimeScope() {
		return this.timeScope;
	}

	public void setTimeScope(String timeScope) {
		this.timeScope = timeScope;
	}

	public String getTodayAvg() {
		return this.todayAvg;
	}

	public void setTodayAvg(String todayAvg) {
		this.todayAvg = todayAvg;
	}

	public String getFiveDaysAvg() {
		return this.fiveDaysAvg;
	}

	public void setFiveDaysAvg(String fiveDaysAvg) {
		this.fiveDaysAvg = fiveDaysAvg;
	}

	public String getThirtyDaysAvg() {
		return this.thirtyDaysAvg;
	}

	public void setThirtyDaysAvg(String thirtyDaysAvg) {
		this.thirtyDaysAvg = thirtyDaysAvg;
	}
}
