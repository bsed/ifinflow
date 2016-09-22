package com.ruimin.ifinflow.model.flowmodel.manage.command;

import com.ruimin.ifinflow.model.util.GenericUtils;

public abstract class AbstractCmdSupport<T> {
	protected Class<T> entityClass;

	public AbstractCmdSupport() {
		this.entityClass = (Class<T>) GenericUtils
				.getSuperClassGenericType(getClass());
	}
}
