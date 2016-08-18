/*    */ package com.ruimin.ifinflow.engine.flowmodel.vo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntrustTaskHistoryVO
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String taskId;
/*    */   private Date operateTime;
/*    */   private String message;
/*    */   
/*    */   public String getTaskId()
/*    */   {
/* 20 */     return this.taskId;
/*    */   }
/*    */   
/*    */   public void setTaskId(String taskId) {
/* 24 */     this.taskId = taskId;
/*    */   }
/*    */   
/*    */   public Date getOperateTime() {
/* 28 */     return this.operateTime;
/*    */   }
/*    */   
/*    */   public void setOperateTime(Date operateTime) {
/* 32 */     this.operateTime = operateTime;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 36 */     return this.message;
/*    */   }
/*    */   
/*    */   public void setMessage(String message) {
/* 40 */     this.message = message;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/flowmodel/vo/EntrustTaskHistoryVO.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */