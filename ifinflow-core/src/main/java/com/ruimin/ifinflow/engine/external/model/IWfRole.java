package com.ruimin.ifinflow.engine.external.model;

import java.io.Serializable;

public abstract interface IWfRole extends Serializable {
	public abstract String getRoleId();

	public abstract String getRoleName();
}
