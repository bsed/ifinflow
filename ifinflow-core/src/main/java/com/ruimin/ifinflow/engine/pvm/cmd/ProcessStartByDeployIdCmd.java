 package com.ruimin.ifinflow.engine.pvm.cmd;
 


 import com.ruimin.ifinflow.engine.external.model.IWfStaff;
 import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
 import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;
 import com.ruimin.ifinflow.engine.flowmodel.vo.WfStaffVO;
 import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
 import com.ruimin.ifinflow.model.flowmodel.cache.vo.TemplateVo;
 import com.ruimin.ifinflow.util.TemplateCacheUtil;
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import org.hibernate.Session;

 import org.jbpm.api.cmd.Environment;
 import org.jbpm.internal.log.Log;
 import org.jbpm.pvm.internal.cmd.VariablesCmd;
 import org.jbpm.pvm.internal.exception.ExceptionUtil;
 import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;

 import org.jbpm.pvm.internal.model.ExecutionImpl;
 import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;

 import org.jbpm.pvm.internal.session.RepositorySession;

	import java.util.Collection;


 public class ProcessStartByDeployIdCmd extends VariablesCmd<ProcessVO>
 {
   private static final long serialVersionUID = 1L;
   private static final Log log = Log.getLog(ProcessStartByDeployIdCmd.class.getName());
   private String deployId;
   private String subject;
   private WfStaffVO user;
   private VariableSet variableSet;
   
   public ProcessStartByDeployIdCmd(String deployId, WfStaffVO user, VariableSet variables, String subject)
     throws IFinFlowException
   {
     this.deployId = deployId;
     this.user = user;
     this.subject = subject;
     this.variableSet = variables;
   }
   
   public ProcessVO execute(Environment environment) throws IFinFlowException
   {
     if (this.user == null) {
       throw new IFinFlowException(105005, new Object[] { this.user == null ? "null" : this.user.getStaffId() });
     }
     IWfStaff staff = UserExtendsReference.getIdentityAdapter().getStaffById(this.user.getStaffId());
     if ((staff == null) || (staff.getStaffId() == null)) {
       throw new IFinFlowException(105005, new Object[] { this.user == null ? "null" : this.user.getStaffId() });
     }
     
     ProcessDefinitionImpl processDefinition = null;
     RepositorySession repositorySession = (RepositorySession)environment.get(RepositorySession.class);
     
     processDefinition = (ProcessDefinitionImpl)repositorySession.createProcessDefinitionQuery().deploymentId(this.deployId).uniqueResult();
     
 
 
     if (processDefinition == null) {
       throw new IFinFlowException(107001, new Object[0]);
     }
     if (processDefinition.isSuspended()) {
       TemplateVo tem = TemplateCacheUtil.getTemplateVo(processDefinition);
       if (tem == null) {
         throw new IFinFlowException(109006, new Object[0]);
       }
       throw new IFinFlowException(109006, new Object[] { tem.getPackageId(), tem.getTemplateId() });
     }
     
 
     ExecutionImpl processInstance = new ExecutionImpl();
     HistoryProcessInstanceImpl hpii = processInstance.initializeProcessInstance(processDefinition, this.subject, this.user != null ? this.user.getWfUserInfo() : null);
     
 
     if (this.variableSet != null) {
       processInstance.setVariables(this.variableSet.getList());
     }
     
 
     if (!processInstance.isEnded()) {
       Session session = (Session)environment.get(Session.class);
       session.save(processInstance);
     }
     
 
     log.debug("ProcessStartByDeployIdCmd 87 发起流程时待推进 processDefinition.getInitial().getName()=" + processDefinition.getInitial().getName());
     ExceptionUtil.saveTmpExecution(processInstance.getExecutionImplId(), processDefinition.getInitial().getName());
     
     return com.ruimin.ifinflow.engine.flowmodel.util.BeanUtil.createProcessVO(hpii);
   }
 }

