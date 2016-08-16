package com.ruimin.ifinflow.engine.external.model;

import java.io.Serializable;

public abstract interface IWfStaff extends Serializable {
	public abstract String getStaffId();

	public abstract String getStaffName();

	public abstract String getBusinessEmail();

	public abstract String getPhone();
}
