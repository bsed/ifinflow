package com.ruimin.ifinflow.engine.flowmodel.vo;

import org.apache.commons.lang.StringUtils;

public class TaskMechanismVO extends BaseStatisticVO {
	private static final long serialVersionUID = 1L;
	private String orgId;
	private String orgName;

	public TaskMechanismVO(String orgId, String orgName, String total,
			String doneNum, String todoNum, String overtimeNum, String backNum,
			String exceptionNum) {
		super(total, doneNum, todoNum, overtimeNum, backNum, exceptionNum);
		this.orgId = orgId;
		this.orgName = orgName;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (((obj instanceof TaskMechanismVO))
				&& (StringUtils.equals(((TaskMechanismVO) obj).getOrgId(),
						this.orgId))) {
			return true;
		}

		return false;
	}
}
