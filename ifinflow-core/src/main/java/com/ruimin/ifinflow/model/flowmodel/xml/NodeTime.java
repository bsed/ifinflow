package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;

public class NodeTime implements Serializable {
	private static final long serialVersionUID = 1L;
	private String handle;
	private int delayDay;
	private int delayHour;
	private int delayMinute;
	private int delaySecond;
	private String cycleType;
	private int cycleCount;
	private int intervalDay;
	private int intervalHour;
	private int intervalMinute;
	private int intervalSecond;
	private int continueDay;
	private int continueHour;
	private int continueMinute;
	private int continueSecond;

	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public int getDelayDay() {
		return this.delayDay;
	}

	public void setDelayDay(int delayDay) {
		this.delayDay = delayDay;
	}

	public int getDelayHour() {
		return this.delayHour;
	}

	public void setDelayHour(int delayHour) {
		this.delayHour = delayHour;
	}

	public int getDelayMinute() {
		return this.delayMinute;
	}

	public void setDelayMinute(int delayMinute) {
		this.delayMinute = delayMinute;
	}

	public int getDelaySecond() {
		return this.delaySecond;
	}

	public void setDelaySecond(int delaySecond) {
		this.delaySecond = delaySecond;
	}

	public String getCycleType() {
		return this.cycleType;
	}

	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}

	public int getCycleCount() {
		return this.cycleCount;
	}

	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
	}

	public int getIntervalDay() {
		return this.intervalDay;
	}

	public void setIntervalDay(int intervalDay) {
		this.intervalDay = intervalDay;
	}

	public int getIntervalHour() {
		return this.intervalHour;
	}

	public void setIntervalHour(int intervalHour) {
		this.intervalHour = intervalHour;
	}

	public int getIntervalMinute() {
		return this.intervalMinute;
	}

	public void setIntervalMinute(int intervalMinute) {
		this.intervalMinute = intervalMinute;
	}

	public int getIntervalSecond() {
		return this.intervalSecond;
	}

	public void setIntervalSecond(int intervalSecond) {
		this.intervalSecond = intervalSecond;
	}

	public int getContinueDay() {
		return this.continueDay;
	}

	public void setContinueDay(int continueDay) {
		this.continueDay = continueDay;
	}

	public int getContinueHour() {
		return this.continueHour;
	}

	public void setContinueHour(int continueHour) {
		this.continueHour = continueHour;
	}

	public int getContinueMinute() {
		return this.continueMinute;
	}

	public void setContinueMinute(int continueMinute) {
		this.continueMinute = continueMinute;
	}

	public int getContinueSecond() {
		return this.continueSecond;
	}

	public void setContinueSecond(int continueSecond) {
		this.continueSecond = continueSecond;
	}
}
