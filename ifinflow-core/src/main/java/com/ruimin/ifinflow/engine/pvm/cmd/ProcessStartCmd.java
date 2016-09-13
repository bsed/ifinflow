package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.model.IWfRole;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.util.BeanUtil;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.WfStaffVO;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
import org.jbpm.internal.log.Log;
import org.jbpm.pvm.internal.cmd.VariablesCmd;
import org.jbpm.pvm.internal.exception.ExceptionUtil;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.session.RepositorySession;

import java.util.List;


public class ProcessStartCmd
        extends VariablesCmd<ProcessVO> {
    private static final long serialVersionUID = 1L;
    private static final Log log = Log.getLog(ProcessStartCmd.class.getName());

    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String packageId;
    protected String templateId;
    protected int version;
    protected String subject;
    protected WfStaffVO user;
    protected VariableSet variableSet;

    public ProcessStartCmd(String packageId, String templateId, int version, WfStaffVO user, VariableSet variables, String subject) {
        if (!StringUtils.isEmpty(packageId)) {
            packageId = packageId.replace(".", "_");
        }
        this.packageId = packageId;
        this.templateId = templateId;
        this.version = version;
        this.user = user;
        this.subject = subject;
        this.variableSet = variables;
        if (version <= 0) {
            this.processDefinitionKey = (packageId + "_" + templateId);
        } else {
            this.processDefinitionId = (packageId + "_" + templateId + "-" + version);
        }
    }


    public ProcessVO execute(Environment environment)
            throws Exception {
        if (this.user == null) {
            throw new IFinFlowException(105005, this.user == null ? "null" : this.user.getStaffId());
        }
        IWfStaff staff = UserExtendsReference.getIdentityAdapter().getStaffById(this.user.getStaffId());
        IWfUnit unit = UserExtendsReference.getIdentityAdapter().getUnitByStaffId(this.user.getStaffId());
        List<IWfRole> roles = UserExtendsReference.getIdentityAdapter().getRolesByStaffId(this.user.getStaffId());

        if ((staff == null) || (staff.getStaffId() == null)) {
            throw new IFinFlowException(105005, this.user == null ? "null" : this.user.getStaffId());
        }
        this.user.setStaffName(staff.getStaffName());
        if (unit != null) {
            this.user.setUnitId(unit.getUnitId());
        }

        if ((roles == null) || (roles.isEmpty())) {
            StringBuffer roleIds = new StringBuffer();
            for (IWfRole r : roles) {
                roleIds.append(r.getRoleId()).append(",");
            }
            if (roleIds.length() > 0) {
                this.user.setRoleId(roleIds.substring(0, roleIds.length() - 1));
            }
        }


        RepositorySession repositorySession = environment.get(RepositorySession.class);


        ProcessDefinitionImpl processDefinition = null;
        if (this.version <= 0) {
            processDefinition = repositorySession.findProcessDefinitionByKey(this.processDefinitionKey);
        } else {
            processDefinition = repositorySession.findProcessDefinitionById(this.processDefinitionId);
        }

        if (processDefinition == null) {
            throw new IFinFlowException(107001, this.packageId, this.templateId, Integer.valueOf(this.version));
        }

        if (processDefinition.isSuspended()) {
            throw new IFinFlowException(109006, this.packageId, this.templateId);
        }
        ExecutionImpl processInstance = new ExecutionImpl();
        HistoryProcessInstanceImpl hpii = processInstance.initializeProcessInstance(processDefinition, this.subject, this.user != null ? this.user.getWfUserInfo() : null);


        if (this.variableSet != null) {
            processInstance.setVariables(this.variableSet.getList());
        }


        if (!processInstance.isEnded()) {
            Session session = environment.get(Session.class);
            session.save(processInstance);
        }


        log.debug("ProcessStartCmd 182 发起流程待推进 processDefinition.getInitial().getName()=" + processDefinition.getInitial().getName());
        ExceptionUtil.saveTmpExecution(processInstance.getExecutionImplId(), processDefinition.getInitial().getName());

        return BeanUtil.createProcessVO(hpii);
    }

    public String getProcessDefinitionKey() {
        return this.processDefinitionId;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionId = processDefinitionKey;
    }
}