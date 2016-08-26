package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;

public class TemplateNodeVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nodeId;
	private String nodeName;

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}
