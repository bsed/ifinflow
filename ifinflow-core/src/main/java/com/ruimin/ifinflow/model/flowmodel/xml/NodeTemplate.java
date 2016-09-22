package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;

public class NodeTemplate implements Serializable {
	private static final long serialVersionUID = 1L;
	private String handle;
	private int templateHandle;
	private String nodeTemplateId;
	private String name;
	private String remark;
	private int kind;
	private String url;

	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public int getTemplateHandle() {
		return this.templateHandle;
	}

	public void setTemplateHandle(int templateHandle) {
		this.templateHandle = templateHandle;
	}

	public String getNodeTemplateId() {
		return this.nodeTemplateId;
	}

	public void setNodeTemplateId(String nodeTemplateId) {
		this.nodeTemplateId = nodeTemplateId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getKind() {
		return this.kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
