package com.ruimin.ifinflow.engine.assign.entity;

import com.ruimin.ifinflow.engine.external.model.IWfUnit;

public class IFinFlowUnitEntity implements IWfUnit {
	private static final long serialVersionUID = 3140754439863908268L;
	public static final String UNIT_KIND_ORG = "ORG";
	public static final String UNIT_KIND_DEPT = "DEPT";
	public static final String UNIT_KIND_TEAM = "TEAM";
	private String unitId;
	private String unitName;
	private String brno;
	private String unitLevel;
	private String unitKind;
	private String parentUnitId;

	public String getUnitLevel() {
		return this.unitLevel;
	}

	public void setUnitLevel(String unitLevel) {
		this.unitLevel = unitLevel;
	}

	public String getUnitKind() {
		return this.unitKind;
	}

	public void setUnitKind(String unitKind) {
		this.unitKind = unitKind;
	}

	public String getUnitId() {
		return this.unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getBrno() {
		return this.brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("IFinFlowUnit{");
		buf.append("[unitId = " + this.unitId + "]");
		buf.append("[unitName = " + this.unitName + "]");
		buf.append("[brno = " + this.brno + "]");
		buf.append("[unitKind = " + this.unitKind + "]");
		buf.append("[unitLevel = " + this.unitLevel + "]");
		buf.append("}\n");
		return buf.toString();
	}

	public String getParentUnitId() {
		return this.parentUnitId;
	}

	public void setParentUnitId(String parentUnitId) {
		this.parentUnitId = parentUnitId;
	}
}
