package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;

public class VarTaskMapping implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String nodeHandle;
	private String variableName;
	private String userExtColume;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeHandle() {
		return this.nodeHandle;
	}

	public void setNodeHandle(String nodeHandle) {
		this.nodeHandle = nodeHandle;
	}

	public String getVariableName() {
		return this.variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getUserExtColume() {
		return this.userExtColume;
	}

	public void setUserExtColume(String userExtColume) {
		this.userExtColume = userExtColume;
	}
}
