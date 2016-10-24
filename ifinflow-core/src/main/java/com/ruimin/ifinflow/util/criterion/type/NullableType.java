 package com.ruimin.ifinflow.util.criterion.type;
 
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import java.sql.PreparedStatement;
 import java.sql.SQLException;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public abstract class NullableType
   implements Type
 {
   public abstract void set(PreparedStatement paramPreparedStatement, Object paramObject, int paramInt)
     throws IFinFlowException, SQLException;
   
   public abstract int sqlType();
   
   public final void nullSafeSet(PreparedStatement st, Object value, int index)
     throws IFinFlowException, SQLException
   {
     try
     {
       if (value == null) {
         st.setNull(index, sqlType());
       }
       else {
         set(st, value, index);
       }
     }
     catch (RuntimeException re) {
       throw re;
     }
     catch (SQLException se) {
       throw se;
     }
   }
 }
