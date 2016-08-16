package com.ruimin.ifinflow.engine.external.adapter;

import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskVO;

public abstract interface IAssignAdapter {
	public abstract AssignCandidate assign(VariableSet paramVariableSet,
			TaskVO paramTaskVO) throws Exception;
}
