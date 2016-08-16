package com.ruimin.ifinflow.engine.external.model;

import java.io.Serializable;
import java.util.List;

public abstract interface IWfGroup extends Serializable {
	public abstract String getGroupId();

	public abstract String getGroupName();

	public abstract List<String> getStaffIds();
}
