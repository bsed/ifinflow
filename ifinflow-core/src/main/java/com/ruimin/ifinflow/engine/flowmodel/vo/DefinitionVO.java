package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;
import java.util.Date;

public class DefinitionVO implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String templeteId;
	protected String templeteName;
	protected int templeteVersion;
	protected String packageId;
	protected String deployId;
	private String remark;
	private String creatorId;
	private String creatorName;
	private Date createdTime;

	public String getTempleteName() {
		return this.templeteName;
	}

	public void setTempleteName(String templeteName) {
		this.templeteName = templeteName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return this.creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setTempleteVersion(int templeteVersion) {
		this.templeteVersion = templeteVersion;
	}

	public int getTempleteVersion() {
		return this.templeteVersion;
	}

	public DefinitionVO(String packageId, String templeteId,
			String templeteName, int templeteVersion, String deployId) {
		this.packageId = packageId;
		this.templeteId = templeteId;
		this.templeteVersion = templeteVersion;
		this.deployId = deployId;
		this.templeteName = templeteName;
	}

	public DefinitionVO(String packageId, String templeteId,
			int templeteVersion, String deployId) {
		this.packageId = packageId;
		this.templeteId = templeteId;
		this.templeteVersion = templeteVersion;
		this.deployId = deployId;
	}

	public DefinitionVO(String templeteId, int templeteVersion, String deployId) {
		this.templeteId = templeteId;
		this.templeteVersion = templeteVersion;
		this.deployId = deployId;
	}

	public DefinitionVO() {
	}

	public String getTempleteId() {
		return this.templeteId;
	}

	public void setTempleteId(String templeteId) {
		this.templeteId = templeteId;
	}

	public String getPackageId() {
		return this.packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getDeployId() {
		return this.deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}
}
