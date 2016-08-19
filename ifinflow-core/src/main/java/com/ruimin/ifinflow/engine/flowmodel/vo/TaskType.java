package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;

public class TaskType implements Serializable {
	private static final long serialVersionUID = 1L;
	private String packageId;
	private String templateId;
	private int templateVersion;
	private String nodeId;
	private String userExtString1;
	private String userExtString2;
	private String userExtString3;
	private String userExtString4;
	private String userExtString5;
	private String userExtString6;
	private String userExtString7;
	private String userExtString8;

	public TaskType(String packageId, String templateId, int templateVersion,
			String nodeId) {
		this.packageId = packageId;
		this.templateId = templateId;
		this.templateVersion = templateVersion;
		this.nodeId = nodeId;
	}

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

	public int getTemplateVersion() {
		return this.templateVersion;
	}

	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getUserExtString1() {
		return this.userExtString1;
	}

	public void setUserExtString1(String userExtString1) {
		this.userExtString1 = userExtString1;
	}

	public String getUserExtString2() {
		return this.userExtString2;
	}

	public void setUserExtString2(String userExtString2) {
		this.userExtString2 = userExtString2;
	}

	public String getUserExtString3() {
		return this.userExtString3;
	}

	public void setUserExtString3(String userExtString3) {
		this.userExtString3 = userExtString3;
	}

	public String getUserExtString4() {
		return this.userExtString4;
	}

	public void setUserExtString4(String userExtString4) {
		this.userExtString4 = userExtString4;
	}

	public String getUserExtString5() {
		return this.userExtString5;
	}

	public void setUserExtString5(String userExtString5) {
		this.userExtString5 = userExtString5;
	}

	public String getUserExtString6() {
		return this.userExtString6;
	}

	public void setUserExtString6(String userExtString6) {
		this.userExtString6 = userExtString6;
	}

	public String getUserExtString7() {
		return this.userExtString7;
	}

	public void setUserExtString7(String userExtString7) {
		this.userExtString7 = userExtString7;
	}

	public String getUserExtString8() {
		return this.userExtString8;
	}

	public void setUserExtString8(String userExtString8) {
		this.userExtString8 = userExtString8;
	}
}
