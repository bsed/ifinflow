package com.ruimin.ifinflow.engine.assign.entity;

import java.io.Serializable;

public class IFinFlowWorkloadEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String staffId;
	private String staffName;
	private int workload;
	private long doneWorkCount;

	public String getStaffId() {
		return this.staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return this.staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public int getWorkload() {
		return this.workload;
	}

	public void setWorkload(int workload) {
		this.workload = workload;
	}

	public long getDoneWorkCount() {
		return this.doneWorkCount;
	}

	public void setDoneWorkCount(long doneWorkCount) {
		this.doneWorkCount = doneWorkCount;
	}
}