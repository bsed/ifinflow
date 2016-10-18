package com.ruimin.ifinflow.util.criterion;

import com.ruimin.ifinflow.util.criterion.type.BooleanType;
import com.ruimin.ifinflow.util.criterion.type.CharacterType;
import com.ruimin.ifinflow.util.criterion.type.DateType;
import com.ruimin.ifinflow.util.criterion.type.DoubleType;
import com.ruimin.ifinflow.util.criterion.type.FloatType;
import com.ruimin.ifinflow.util.criterion.type.IntegerType;
import com.ruimin.ifinflow.util.criterion.type.LongType;
import com.ruimin.ifinflow.util.criterion.type.NullableType;
import com.ruimin.ifinflow.util.criterion.type.ShortType;
import com.ruimin.ifinflow.util.criterion.type.StringType;
import java.io.Serializable;

public abstract interface Criteria extends Serializable {
	public static final NullableType LONG = new LongType();

	public static final NullableType SHORT = new ShortType();

	public static final NullableType INTEGER = new IntegerType();

	public static final NullableType FLOAT = new FloatType();

	public static final NullableType DOUBLE = new DoubleType();

	public static final NullableType CHARACTER = new CharacterType();

	public static final NullableType STRING = new StringType();

	public static final NullableType BOOLEAN = new BooleanType();

	public static final NullableType DATE = new DateType();

	public abstract Criteria add(Criterion paramCriterion);

	public abstract Criteria addOrder(Order paramOrder);

	public abstract Criteria setMaxResult(int paramInt);

	public abstract Criteria setFirstResult(int paramInt);

	public abstract Criteria setTotalResult(int paramInt);
}
