package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;

public class TemplateEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String templateHandle;
	private int type;
	private String adapterType;
	private String adapterName;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplateHandle() {
		return this.templateHandle;
	}

	public void setTemplateHandle(String templateHandle) {
		this.templateHandle = templateHandle;
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
