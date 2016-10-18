package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.criterion.type.TypedValue;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;

public abstract interface Criterion
  extends Serializable
{
  public abstract String toSqlString(Criteria paramCriteria)
    throws IFinFlowException;
  
  public abstract String toStaticSqlString()
    throws IFinFlowException;
  
  public abstract TypedValue[] getTypedValues(Criteria paramCriteria)
    throws IFinFlowException;
}

