package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;

public class TemplateVariable implements Serializable {
	private static final long serialVersionUID = 1L;
	private String handle;
	private String templateHandle;
	private String name;
	private String displayName;
	private String remark;
	private int type;

	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getTemplateHandle() {
		return this.templateHandle;
	}

	public void setTemplateHandle(String templateHandle) {
		this.templateHandle = templateHandle;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type.intValue();
	}
}
