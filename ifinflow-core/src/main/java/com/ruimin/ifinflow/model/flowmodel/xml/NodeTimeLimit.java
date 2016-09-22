package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;

public class NodeTimeLimit implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String nodeKind;
	private String nodeHandle;
	private String timingScheme;
	private int consumeDay;
	private int consumeHour;
	private int consumeMinute;
	private int consumeSecond;
	private String adapterType;
	private String adapterName;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeKind() {
		return this.nodeKind;
	}

	public void setNodeKind(String nodeKind) {
		this.nodeKind = nodeKind;
	}

	public String getNodeHandle() {
		return this.nodeHandle;
	}

	public void setNodeHandle(String nodeHandle) {
		this.nodeHandle = nodeHandle;
	}

	public String getTimingScheme() {
		return this.timingScheme;
	}

	public void setTimingScheme(String timingScheme) {
		this.timingScheme = timingScheme;
	}

	public int getConsumeDay() {
		return this.consumeDay;
	}

	public void setConsumeDay(int consumeDay) {
		this.consumeDay = consumeDay;
	}

	public int getConsumeHour() {
		return this.consumeHour;
	}

	public void setConsumeHour(int consumeHour) {
		this.consumeHour = consumeHour;
	}

	public int getConsumeMinute() {
		return this.consumeMinute;
	}

	public void setConsumeMinute(int consumeMinute) {
		this.consumeMinute = consumeMinute;
	}

	public String getAdapterType() {
		return this.adapterType;
	}

	public void setAdapterType(String adapterType) {
		this.adapterType = adapterType;
	}

	public String getAdapterName() {
		return this.adapterName;
	}

	public void setAdapterName(String adapterName) {
		this.adapterName = adapterName;
	}

	public int getConsumeSecond() {
		return this.consumeSecond;
	}

	public void setConsumeSecond(int consumeSecond) {
		this.consumeSecond = consumeSecond;
	}
}
