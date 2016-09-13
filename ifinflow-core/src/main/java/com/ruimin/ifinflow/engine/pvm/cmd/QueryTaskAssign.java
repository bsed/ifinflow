/*    */ package com.ruimin.ifinflow.engine.pvm.cmd;
/*    */ 
/*    */ import com.ruimin.ifinflow.model.flowmodel.xml.AssignPolicy;
/*    */ import com.ruimin.ifinflow.model.flowmodel.xml.Node;
/*    */ import com.ruimin.ifinflow.model.flowmodel.xml.Template;
/*    */ import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
/*    */
/*    */ import org.hibernate.Session;
/*    */ import org.jbpm.api.cmd.Command;
/*    */ import org.jbpm.api.cmd.Environment;

import java.util.Collection;

/*    */
/*    */ public class QueryTaskAssign
/*    */   implements Command<AssignPolicy>
/*    */ {
/*    */   protected String packageId;
/*    */   protected String templateId;
/*    */   protected int version;
/*    */   protected String nodeId;
/*    */   
/*    */   public QueryTaskAssign(String packageId, String templateId, int version, String nodeId)
/*    */   {
/* 22 */     this.packageId = packageId;
/* 23 */     this.templateId = templateId;
/* 24 */     this.version = version;
/* 25 */     this.nodeId = nodeId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public AssignPolicy execute(Environment environment)
/*    */     throws Exception
/*    */   {
/* 33 */     Session session = (Session)environment.get(Session.class);
/*    */     
/* 35 */     String hql = "select a from " + Node.class.getName() + " n," + AssignPolicy.class.getName() + " a," + Template.class.getName() + " t," + TemplatePackage.class.getName() + " tp " + "where n.handle=a.handle and n.templateHandle=t.handle and t.packageHandle=tp.handle " + " and n.nodeId='" + this.nodeId + "' and t.templateId='" + this.templateId + "' and tp.templatePackageId='" + this.packageId + "' and t.version=" + this.version;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 41 */     AssignPolicy res = (AssignPolicy)session.createQuery(hql).uniqueResult();
/*    */     
/* 43 */     return res;
/*    */   }
/*    */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/pvm/cmd/QueryTaskAssign.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */