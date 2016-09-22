package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;

public class NodeEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String nodeHandle;
	private String nodeKind;
	private int type;
	private String adapterType;
	private String adapterName;

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

	public String getNodeKind() {
		return this.nodeKind;
	}

	public void setNodeKind(String nodeKind) {
		this.nodeKind = nodeKind;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
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
}
