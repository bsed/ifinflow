package com.ruimin.ifinflow.engine.external.model;

import java.io.Serializable;

public abstract interface IWfUnit extends Serializable {
	public abstract String getUnitId();

	public abstract String getUnitName();

	public abstract String getBrno();

	public abstract String getUnitLevel();

	public abstract String getUnitKind();
}
