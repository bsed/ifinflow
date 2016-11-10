/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SqlBinding
/*    */   extends HqlBinding
/*    */ {
/*    */   public static final String TAG = "sql";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SqlBinding()
/*    */   {
/* 33 */     super("sql");
/*    */   }
/*    */   
/*    */   protected HqlActivity createHqlActivity() {
/* 37 */     return new SqlActivity();
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/SqlBinding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */