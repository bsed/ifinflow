/*    */ package org.jbpm.jpdl.internal.activity;
/*    */ 
/*    */ import org.hibernate.Query;
/*    */ import org.hibernate.Session;
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
/*    */ public class SqlActivity
/*    */   extends HqlActivity
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   protected Query createQuery(Session session)
/*    */   {
/* 35 */     return session.createSQLQuery(this.query);
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/activity/SqlActivity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */