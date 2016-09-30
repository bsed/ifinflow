package com.ruimin.ifinflow.model.flowmodel.xml;

import com.ruimin.ifinflow.framework.parser.ITreeNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class TemplatePackage implements ITreeNode, Serializable {
	private static final long serialVersionUID = 1L;
	private String handle;
	private String templatePackageId;
	private String currentId;
	private String name;
	private String superHandle;
	private String remark;
	private TemplatePackage parent;
	private Set<Object> children = new HashSet();

	public String getSuperHandle() {
		return this.superHandle;
	}

	public void setSuperHandle(String superHandle) {
		this.superHandle = superHandle;
	}

	public TemplatePackage getParent() {
		return this.parent;
	}

	public void setParent(TemplatePackage parent) {
		this.parent = parent;
	}

	public Set<Object> getChildren() {
		return this.children;
	}

	public void setChildren(Set<Object> children) {
		this.children = children;
	}

	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getTemplatePackageId() {
		return this.templatePackageId;
	}

	public void setTemplatePackageId(String templatePackageId) {
		this.templatePackageId = templatePackageId;
	}

	public String getCurrentId() {
		return this.currentId;
	}

	public void setCurrentId(String currentId) {
		this.currentId = currentId;
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

	public Collection<ITreeNode> children() {
		Collection<ITreeNode> c = new ArrayList();
		for (Object o : getChildren()) {
			c.add((ITreeNode) o);
		}
		return c;
	}

	public Element element() {
		Element e = DocumentHelper.createElement("package");
		e.addAttribute("ID", getHandle());
		e.addAttribute("handle", getHandle());
		e.addAttribute("id", getTemplatePackageId());
		String namePath = getName();
		TemplatePackage p = getParent();
		while (p != null) {
			namePath = p.getName() + "." + namePath;
			p = p.getParent();
		}
		e.addAttribute("namePath", namePath);
		e.addAttribute("currentid", getCurrentId());
		e.addAttribute("name", getName());
		e.addAttribute("superhandle", getParent() == null ? "" : getParent()
				.getHandle());
		e.addAttribute("remark", getRemark());
		e.addAttribute("icon", "packageIcon");
		return e;
	}
}
