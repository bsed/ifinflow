package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;

public class AssignPolicy implements Serializable {
	private static final long serialVersionUID = 1L;
	private String handle;
	private int assignMode;
	private String exitType;
	private int exitCount;
	private int participantType;
	private int participantAssign;
	private int participantHistory;
	private String result;

	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public int getAssignMode() {
		return this.assignMode;
	}

	public void setAssignMode(int assignMode) {
		this.assignMode = assignMode;
	}

	public String getExitType() {
		return this.exitType;
	}

	public void setExitType(String exitType) {
		this.exitType = exitType;
	}

	public int getExitCount() {
		return this.exitCount;
	}

	public void setExitCount(int exitCount) {
		this.exitCount = exitCount;
	}

	public int getParticipantType() {
		return this.participantType;
	}

	public void setParticipantType(int participantType) {
		this.participantType = participantType;
	}

	public int getParticipantAssign() {
		return this.participantAssign;
	}

	public void setParticipantAssign(int participantAssign) {
		this.participantAssign = participantAssign;
	}

	public int getParticipantHistory() {
		return this.participantHistory;
	}

	public void setParticipantHistory(int participantHistory) {
		this.participantHistory = participantHistory;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
