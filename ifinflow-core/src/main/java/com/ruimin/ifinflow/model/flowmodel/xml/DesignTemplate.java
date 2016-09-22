package com.ruimin.ifinflow.model.flowmodel.xml;

import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import org.hibernate.Hibernate;

public class DesignTemplate implements Serializable {
	private static final long serialVersionUID = 1L;
	private String handle;
	private String packageHandle;
	private String designTemplateId;
	private String name;
	private String remark;
	private int version;
	private String isDeployed;
	private String content;
	private Template template;
	private Blob templatePicture;

	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getPackageHandle() {
		return this.packageHandle;
	}

	public void setPackageHandle(String packageHandle) {
		this.packageHandle = packageHandle;
	}

	public String getDesignTemplateId() {
		return this.designTemplateId;
	}

	public void setDesignTemplateId(String designTemplateId) {
		this.designTemplateId = designTemplateId;
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

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getIsDeployed() {
		return this.isDeployed;
	}

	public void setIsDeployed(String isDeployed) {
		this.isDeployed = isDeployed;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Template getTemplate() {
		return this.template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Blob getTemplatePicture() {
		return this.templatePicture;
	}

	public void setTemplatePicture(Blob blob) {
		this.templatePicture = blob;
	}

	public void setPictureByte(byte[] bytes) {
		if (bytes != null)
			this.templatePicture = Hibernate.createBlob(bytes);
	}

	public byte[] getPictureByte() {
		Blob sqlBlob = this.templatePicture;
		if (sqlBlob != null) {
			try {
				return sqlBlob.getBytes(1L, (int) sqlBlob.length());
			} catch (SQLException e) {
				throw new IFinFlowException(107008, e);
			}
		}
		return null;
	}
}
