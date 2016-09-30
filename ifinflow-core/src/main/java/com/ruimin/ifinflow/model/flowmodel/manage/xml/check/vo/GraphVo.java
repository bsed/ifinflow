package com.ruimin.ifinflow.model.flowmodel.manage.xml.check.vo;

import java.util.ArrayList;
import java.util.List;

public class GraphVo {
	private String id;
	private String name;
	private List<String> priorNameList = new ArrayList();

	private List<String> afterNameList = new ArrayList();

	private List<TransitionVo> transitions;

	private boolean isContinue;

	private String matchId;

	public String getMatchId() {
		return this.matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPriorNameList() {
		return this.priorNameList;
	}

	public void setPriorNameList(List<String> priorNameList) {
		this.priorNameList = priorNameList;
	}

	public List<String> getAfterNameList() {
		return this.afterNameList;
	}

	public void setAfterNameList(List<String> afterNameList) {
		this.afterNameList = afterNameList;
	}

	public List<TransitionVo> getTransitions() {
		return this.transitions;
	}

	public void setTransitions(List<TransitionVo> transitions) {
		this.transitions = transitions;
	}

	public boolean isContinue() {
		return this.isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}
}
