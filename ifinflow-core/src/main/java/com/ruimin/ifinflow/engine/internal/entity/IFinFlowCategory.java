package com.ruimin.ifinflow.engine.internal.entity;

import com.ruimin.ifinflow.framework.parser.ITreeNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class IFinFlowCategory implements ITreeNode, Serializable {
	private static final long serialVersionUID = 1L;
	private String poid;
	private IFinFlowCategory parent;
	private String code;
	private String name;
	private String remark;
	private String type;
	private Set<IFinFlowCategory> children = new HashSet(0);
	private Set<IFinFlowConfig> ifinflowConfigs = new HashSet(0);
	private Set<IFinFlowLog> ifinflowLogs = new HashSet(0);

	public String getPoid() {
		return this.poid;
	}

	public void setPoid(String poid) {
		this.poid = poid;
	}

	public IFinFlowCategory getParent() {
		return this.parent;
	}

	public void setParent(IFinFlowCategory parent) {
		this.parent = parent;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<IFinFlowCategory> getChildren() {
		return this.children;
	}

	public void setChildren(Set<IFinFlowCategory> children) {
		this.children = children;
	}

	public Set<IFinFlowConfig> getIfinflowConfigs() {
		return this.ifinflowConfigs;
	}

	public void setIfinflowConfigs(Set<IFinFlowConfig> ifinflowConfigs) {
		this.ifinflowConfigs = ifinflowConfigs;
	}

	public Set<IFinFlowLog> getIfinflowLogs() {
		return this.ifinflowLogs;
	}

	public void setIfinflowLogs(Set<IFinFlowLog> ifinflowLogs) {
		this.ifinflowLogs = ifinflowLogs;
	}

	public Collection<ITreeNode> children() {
		List<ITreeNode> list = new ArrayList();
		if ("config".equals(getType())) {
			list.addAll(getIfinflowConfigs());
		} else {
			list.addAll(getChildren());
		}
		return list;
	}

	public Element element() {
		Element e = DocumentHelper.createElement("category");
		e.addAttribute("ID", getPoid());
		e.addAttribute("poid", getPoid());
		e.addAttribute("name", getName());
		e.addAttribute("isBranch", String.valueOf(getParent() == null));
		return e;
	}
}
