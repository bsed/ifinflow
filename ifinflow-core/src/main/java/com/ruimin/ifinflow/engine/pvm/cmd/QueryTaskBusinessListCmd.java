/*     */ package com.ruimin.ifinflow.engine.pvm.cmd;
/*     */ 
/*     */ import com.ruimin.ifinflow.engine.flowmodel.vo.TaskBusinessVO;
/*     */ import com.ruimin.ifinflow.util.StringHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.hibernate.Criteria;
/*     */ import org.hibernate.Session;
/*     */ import org.hibernate.criterion.ProjectionList;
/*     */ import org.hibernate.criterion.Projections;
/*     */ import org.hibernate.criterion.Restrictions;
/*     */ import org.jbpm.api.cmd.Command;
/*     */ import org.jbpm.api.cmd.Environment;
/*     */ import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueryTaskBusinessListCmd
/*     */   implements Command<List<TaskBusinessVO>>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private Date startTime1;
/*     */   private Date startTime2;
/*     */   
/*     */   public QueryTaskBusinessListCmd(Date startTime1, Date startTime2)
/*     */   {
/*  29 */     this.startTime1 = startTime1;
/*  30 */     this.startTime2 = startTime2;
/*     */   }
/*     */   
/*     */   public List<TaskBusinessVO> execute(Environment environment)
/*     */     throws Exception
/*     */   {
/*  36 */     Session session = (Session)environment.get(Session.class);
/*     */     
/*  38 */     Criteria criteria = session.createCriteria(HistoryTaskImpl.class);
/*     */     
/*     */ 
/*     */ 
/*  42 */     if (null != this.startTime1) {
/*  43 */       criteria.add(Restrictions.ge("createTime", this.startTime1));
/*     */     }
/*     */     
/*  46 */     if (null != this.startTime2) {
/*  47 */       criteria.add(Restrictions.le("createTime", this.startTime2));
/*     */     }
/*     */     
/*  50 */     ProjectionList projectionList = Projections.projectionList();
/*  51 */     projectionList.add(Projections.groupProperty("packageId"));
/*  52 */     projectionList.add(Projections.groupProperty("templateId"));
/*  53 */     projectionList.add(Projections.groupProperty("templateVersion"));
/*  54 */     projectionList.add(Projections.groupProperty("status"));
/*  55 */     projectionList.add(Projections.rowCount());
/*     */     
/*  57 */     criteria.setProjection(projectionList);
/*     */     
/*  59 */     List<Object[]> histasks = criteria.list();
/*     */     
/*  61 */     List<TaskBusinessVO> results = new ArrayList();
/*  62 */     TaskBusinessVO tbvo = null;
/*  63 */     String tKey = null;
/*     */     
/*  65 */     for (Object[] tmps : histasks) {
/*  66 */       tKey = tmps[0] + "_" + tmps[1] + "-" + tmps[2];
/*     */       
/*  68 */       tbvo = new TaskBusinessVO(tKey, "0", "0", "0", "0", "0", "0");
/*     */       
/*  70 */       if (results.contains(tbvo)) {
/*  71 */         tbvo = (TaskBusinessVO)results.get(results.indexOf(tbvo));
/*     */       } else {
/*  73 */         results.add(tbvo);
/*     */       }
/*     */       
/*  76 */       int ti = 0;
/*  77 */       if (tmps[3] != null) {
/*  78 */         ti = ((Integer)tmps[3]).intValue();
/*  79 */         if ((1 == ti) || (2 == ti))
/*     */         {
/*  81 */           tbvo.setTodoNum(StringHelper.stringPlusN(tbvo.getTodoNum(), (Integer)tmps[4]));
/*     */         }
/*  83 */         else if ((16 == ti) || (6 == ti))
/*     */         {
/*  85 */           tbvo.setDoneNum(StringHelper.stringPlusN(tbvo.getDoneNum(), (Integer)tmps[4]));
/*     */         }
/*  87 */         else if (4 == ti) {
/*  88 */           tbvo.setBackNum(StringHelper.stringPlusN(tbvo.getBackNum(), (Integer)tmps[4]));
/*     */         }
/*  90 */         else if (512 == ti) {
/*  91 */           tbvo.setExceptionNum(StringHelper.stringPlusN(tbvo.getExceptionNum(), (Integer)tmps[4]));
/*     */         }
/*  93 */         else if (256 == ti) {
/*  94 */           tbvo.setOvertimeNum(StringHelper.stringPlusN(tbvo.getOvertimeNum(), (Integer)tmps[4]));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  99 */       tbvo.setTotal(StringHelper.stringPlusN(tbvo.getTotal(), (Integer)tmps[4]));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 104 */     return results;
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/pvm/cmd/QueryTaskBusinessListCmd.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */