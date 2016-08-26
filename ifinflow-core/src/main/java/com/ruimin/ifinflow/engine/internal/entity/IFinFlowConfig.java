package com.ruimin.ifinflow.engine.internal.entity;

import com.ruimin.ifinflow.framework.parser.ITreeNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class IFinFlowConfig implements ITreeNode, Serializable {
	private String poid;
	private String categoryId;
	private String configName;
	private String configKey;
	private String configValue;
	private String remark;
	private String defaultValue;

	public String getPoid() {
		return this.poid;
	}

	public void setPoid(String poid) {
		this.poid = poid;
	}

	public String getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getConfigName() {
		return this.configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigKey() {
		return this.configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return this.configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Collection<ITreeNode> children() {
		List<ITreeNode> list = new ArrayList();
		return list;
	}

	public Element element() {
		Element e = DocumentHelper.createElement("config");
		e.addAttribute("ID", getPoid());
		e.addAttribute("poid", getPoid());
		e.addAttribute("name", getConfigName());
		return e;
	}
}
