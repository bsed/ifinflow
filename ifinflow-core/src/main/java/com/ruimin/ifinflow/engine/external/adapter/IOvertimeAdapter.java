package com.ruimin.ifinflow.engine.external.adapter;

import com.ruimin.ifinflow.engine.flowmodel.VariableSet;

public abstract interface IOvertimeAdapter {
	public abstract VariableSet invoke(VariableSet paramVariableSet)
			throws Exception;
}
