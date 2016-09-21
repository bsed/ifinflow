package com.ruimin.ifinflow.model.flowmodel.cache.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TemplateVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String packageId;
	private String templateId;
	private String templateName;
	private long overtime;
	private List<TemplateEventVo> events;
	private Map<String, NodeVo> nodeMap;
	private int version;
	private String firstTaskCommit;

	public String getPackageId() {
		return this.packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public long getOvertime() {
		return this.overtime;
	}

	public void setOvertime(long overtime) {
		this.overtime = overtime;
	}

	public List<TemplateEventVo> getEvents() {
		return this.events;
	}

	public void setEvents(List<TemplateEventVo> events) {
		this.events = events;
	}

	public Map<String, NodeVo> getNodeMap() {
		return this.nodeMap;
	}

	public void setNodeMap(Map<String, NodeVo> nodeMap) {
		this.nodeMap = nodeMap;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public NodeVo getNodeVo(String key) {
		return (NodeVo) this.nodeMap.get(key);
	}

	public String getFirstTaskCommit() {
		return this.firstTaskCommit;
	}

	public void setFirstTaskCommit(String firstTaskCommit) {
		this.firstTaskCommit = firstTaskCommit;
	}
}
