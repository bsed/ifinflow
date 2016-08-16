package com.ruimin.ifinflow.engine.external.adapter;

import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;

public abstract interface IBusinessAdapter {
	public abstract VariableSet invoke(VariableSet paramVariableSet,
			ProcessVO paramProcessVO) throws Exception;
}
