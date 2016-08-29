package com.ruimin.ifinflow.engine.internal.spring;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfGroup;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.external.spring.WfIFinFlowConsoleService;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskBusinessVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskMechanismVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskPVMVO;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.engine.internal.entity.IFinFlowJProcessExcep;
import com.ruimin.ifinflow.engine.internal.exception.DeployTemplateException;
import com.ruimin.ifinflow.engine.internal.exception.UndoDeployTemplateException;
import com.ruimin.ifinflow.engine.internal.log.WorkflowLog;
import com.ruimin.ifinflow.engine.internal.log.vo.AppLog;
import com.ruimin.ifinflow.engine.internal.vo.SubProcessInstanceVo;
import com.ruimin.ifinflow.engine.internal.vo.TaskStatisticsVo;
import com.ruimin.ifinflow.engine.pvm.cmd.FindByHqlCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.FindSubProcessInstanceCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.FindTaskDurationCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.FindTaskStatisticsCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.GroupDeleteCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.GroupQueryCountCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.GroupQueryIdAndNameCountCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.GroupQueryIdAndNamePageCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.GroupQueryPageCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.GroupSaveCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.GroupUpdateCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.QueryTaskBusinessListCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.QueryTaskMechanismList;
import com.ruimin.ifinflow.engine.pvm.cmd.QueryTaskPvmListCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.SaveTmpExecutionCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.TaskHistoryVariableFindCmd;
import com.ruimin.ifinflow.engine.pvm.cmd.UserGroupDeleteCmd;
import com.ruimin.ifinflow.model.flowmodel.cache.TemplateCacheManager;
import com.ruimin.ifinflow.model.flowmodel.external.IModelService;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.jbpm.api.JbpmException;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.processengine.SpringHelper;


public class WfIFinFlowConsoleServiceImpl
        implements WfIFinFlowConsoleService {
    private SpringHelper springHelper;
    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private IModelService modelService;

    public String deployDefinitionFromClasspath(String templeteId, String path)
            throws IFinFlowException {
        WorkflowLog.dbLog(Level.INFO, new AppLog("deployDefinitionFromClasspath", new String[]{"templeteId", "path"}, new Object[]{templeteId, path}));


        String deployId = null;
        try {
            deployId = this.repositoryService.createDeployment().addResourceFromClasspath(path).deploy();
        } catch (JbpmException e) {
            throw new DeployTemplateException(e, templeteId);
        }
        return deployId;
    }


    public String deployDefinitionFromInputStream(String templeteId, InputStream inputStream)
            throws IFinFlowException {
        WorkflowLog.dbLog(Level.INFO, new AppLog("deployDefinitionFromInputStream", new String[]{"templeteId", "inputStream"}, new Object[]{templeteId, inputStream}));


        templeteId = templeteId + ".jpdl.xml";
        String deployId = null;
        try {
            deployId = this.repositoryService.createDeployment().addResourceFromInputStream(templeteId, inputStream).deploy();

        } catch (JbpmException e) {
            throw new DeployTemplateException(e, templeteId);
        }
        return deployId;
    }


    public String deployDefinitionFromString(String templeteId, String resource)
            throws IFinFlowException {
        WorkflowLog.dbLog(Level.INFO, new AppLog("deployDefinitionFromString", new String[]{"templeteId", "resource"}, new Object[]{templeteId, resource}));


        templeteId = templeteId + ".jpdl.xml";
        String deployId = null;
        try {
            deployId = this.repositoryService.createDeployment().addResourceFromString(templeteId, resource).deploy();


            TemplateCacheManager.getInstance().clearOnly(deployId);
        } catch (JbpmException e) {
            throw new DeployTemplateException(e, templeteId);
        }
        return deployId;
    }


    public String deployDefinitionFromZipInputStream(String templeteId, ZipInputStream zipis)
            throws IFinFlowException {
        WorkflowLog.dbLog(Level.INFO, new AppLog("deployDefinitionFromZipInputStream", new String[]{"templeteId", "zipis"}, new Object[]{templeteId, zipis}));


        String deployId = null;
        try {
            deployId = this.repositoryService.createDeployment().addResourcesFromZipInputStream(zipis).deploy();
        } catch (JbpmException e) {
            throw new DeployTemplateException(e, templeteId);
        }
        return deployId;
    }


    public boolean deleteDefinitionById(String packageId, String templeteId, Integer version)
            throws IFinFlowException {
        WorkflowLog.dbLog(Level.INFO, new AppLog("deleteDefinitionById", new String[]{"packageId", "templeteId", "version"}, new Object[]{packageId, templeteId, version}));


        String deployId = this.modelService.findDeploymentId(packageId, templeteId, version.intValue());

        if (StringUtils.isEmpty(deployId)) {
            throw new IFinFlowException(107001, new Object[]{packageId, templeteId, version});
        }


        String pkgId = packageId == null ? null : packageId.replace(".", "_");
        String procDefId = pkgId + "_" + templeteId + "-" + version;

        long hisProCount = this.processEngine.getExecutionService().createProcessInstanceQuery().processDefinitionId(procDefId).count();


        if (hisProCount > 0L) {
            throw new UndoDeployTemplateException(packageId, templeteId, String.valueOf(version));
        }

        try {
            this.repositoryService.deleteDeployment(deployId);

            TemplateCacheManager.getInstance().clearOnly(packageId + "_" + templeteId + "-" + version);
        } catch (Exception e) {
            throw new UndoDeployTemplateException(e, packageId, templeteId, String.valueOf(version));
        }

        return true;
    }


    private ProcessDefinitionImpl getDefinition(String deployId) {
        return (ProcessDefinitionImpl) this.repositoryService.createProcessDefinitionQuery().deploymentId(deployId).uniqueResult();
    }


    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.repositoryService = processEngine.getRepositoryService();
        this.modelService = processEngine.getModelService();
    }


    public VariableSet getTaskVariableByTaskForTrace(String taskId, String processId)
            throws IFinFlowException {
        WorkflowLog.dbLog(Level.INFO, new AppLog("getTaskVariableByTaskForTrace", new String[]{"taskId", "processId"}, new Object[]{taskId, processId}));


        return (VariableSet) this.processEngine.execute(new TaskHistoryVariableFindCmd(taskId, processId));
    }

    public List<String> getAllUnitLevel() {
        return UserExtendsReference.getIdentityAdapter().getAllUnitLevel();
    }

    public IWfGroup saveGroup(String id, String name, List<String> userIds)
            throws IFinFlowException {
        return (IWfGroup) this.processEngine.execute(new GroupSaveCmd(id, name, userIds));
    }

    public void updateGroup(String id, String name, List<String> userIds) throws IFinFlowException {
        this.processEngine.execute(new GroupUpdateCmd(id, name, userIds));
    }

    public void deleteGroup(String id) throws IFinFlowException {
        this.processEngine.execute(new GroupDeleteCmd(id));
    }

    public List<IWfGroup> getGroupPage(String param, int startNum, int pageSize) throws IFinFlowException {
        return (List) this.processEngine.execute(new GroupQueryPageCmd(param, startNum, pageSize));
    }

    public long getGroupCount(String param) throws IFinFlowException {
        return ((Long) this.processEngine.execute(new GroupQueryCountCmd(param))).longValue();
    }

    public List<IWfStaff> getStaffsByGroupId(final String groupId) throws IFinFlowException {
        return (List) this.processEngine.execute(new Command<List<IWfStaff>>() {
            private static final long serialVersionUID = 1L;
            public List<IWfStaff> execute(Environment environment) throws Exception {
                IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();


                return adapter.getStaffsByGroupId(groupId);
            }
        });
    }

    public List<IWfGroup> getGroupIdAndNamePage(String id, String name, int startNum, int pageSize) throws IFinFlowException {
        return (List) this.processEngine.execute(new GroupQueryIdAndNamePageCmd(id, name, startNum, pageSize));
    }

    public long getGroupIdAndNameCount(String id, String name)
            throws IFinFlowException {
        return ((Long) this.processEngine.execute(new GroupQueryIdAndNameCountCmd(id, name))).longValue();
    }

    public void refeshPVM() {
        try {
            this.springHelper.destroy();
            synchronized (this.springHelper) {
                this.springHelper.setJbpmCfg("jbpm.cfg.xml");
                this.springHelper.createProcessEngine();
            }
        } catch (Exception e) {
            throw new IFinFlowException(100000, e);
        }
    }

    public void setSpringHelper(SpringHelper springHelper) {
        this.springHelper = springHelper;
    }


    public List<TaskStatisticsVo> getTaskStatisticsList(String packageId, String templateId, int version, Date startTime, Date endTime)
            throws IFinFlowException {
        return (List) this.processEngine.execute(new FindTaskStatisticsCmd(packageId, templateId, version, startTime, endTime));
    }

    public List<TaskStatisticsVo> getTaskDurationList(Date takeStartTime, Date takeEndTime)
            throws IFinFlowException {
        return (List) this.processEngine.execute(new FindTaskDurationCmd(takeStartTime, takeEndTime));
    }


    public List<SubProcessInstanceVo> getSubProcessInstanceList(String processInstanceId, String subProcessNodeId) {
        return (List) this.processEngine.execute(new FindSubProcessInstanceCmd(processInstanceId, subProcessNodeId));
    }

    public void solveException(String executionId) throws IFinFlowException {
        this.processEngine.execute(new SaveTmpExecutionCmd(executionId));
    }

    public IFinFlowJProcessExcep queryException(String executionId)
            throws IFinFlowException {
        StringBuilder hql = new StringBuilder();
        hql.append("from ").append(IFinFlowJProcessExcep.class.getName()).append(" where executionId = '").append(executionId).append("'");


        List<IFinFlowJProcessExcep> resultList = (List) this.processEngine.execute(new FindByHqlCmd(hql.toString()));

        return (resultList != null) && (resultList.size() > 0) ? (IFinFlowJProcessExcep) resultList.get(0) : null;
    }


    public List<TaskPVMVO> queryTaskPVMList(int frequency, ArrayList<?> ogetOrganization) {
        return (List) this.processEngine.execute(new QueryTaskPvmListCmd(ogetOrganization, frequency));
    }


    public List<TaskBusinessVO> queryTaskBusinessList(Date startTime1, Date startTime2) {
        return (List) this.processEngine.execute(new QueryTaskBusinessListCmd(startTime1, startTime2));
    }


    public List<TaskMechanismVO> queryTaskMechanismList(Date startTime1, Date startTime2) {
        return (List) this.processEngine.execute(new QueryTaskMechanismList(startTime1, startTime2));
    }

    public boolean checkTaskOwner(String taskId, String userId) {
        HistoryTaskImpl ht = (HistoryTaskImpl) this.processEngine.getHistoryService().createHistoryTaskQuery().taskId(taskId).ownerId(userId).uniqueResult();


        return ht != null;
    }

    public void deleteUserGroup(String userId, String groupId) throws IFinFlowException {
        this.processEngine.execute(new UserGroupDeleteCmd(userId, groupId));
    }
}