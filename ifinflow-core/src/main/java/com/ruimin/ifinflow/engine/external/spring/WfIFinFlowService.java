package com.ruimin.ifinflow.engine.external.spring;

import com.ruimin.ifinflow.engine.flowmodel.Variable;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.vo.ActivityVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.DefinitionVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.EntrustTaskHistoryVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessExceptionVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessTraceVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.RejectActivityVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskType;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.TemplateNodeVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.WfStaffVO;
import com.ruimin.ifinflow.engine.internal.cal.vo.BusinessCalendarVo;
import com.ruimin.ifinflow.util.criterion.impl.CriteriaImpl;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract interface WfIFinFlowService {
	public abstract ProcessVO startProcess(String paramString1,
			String paramString2, int paramInt, WfStaffVO paramWfStaffVO,
			VariableSet paramVariableSet, String paramString3)
			throws IFinFlowException;

	public abstract ProcessVO startProcess(String paramString1,
			String paramString2, WfStaffVO paramWfStaffVO,
			VariableSet paramVariableSet, String paramString3)
			throws IFinFlowException;

	public abstract ProcessVO startProcess(String paramString1,
			WfStaffVO paramWfStaffVO, VariableSet paramVariableSet,
			String paramString2) throws IFinFlowException;

	public abstract void cancelProcess(String paramString,
			WfStaffVO paramWfStaffVO) throws IFinFlowException;

	public abstract void cancelProcessByTaskId(String paramString,
			WfStaffVO paramWfStaffVO) throws IFinFlowException;

	public abstract void suspendProcess(String paramString,
			WfStaffVO paramWfStaffVO) throws IFinFlowException;

	public abstract void suspendProcessByTaskId(String paramString,
			WfStaffVO paramWfStaffVO) throws IFinFlowException;

	public abstract void resumeProcess(String paramString,
			WfStaffVO paramWfStaffVO) throws IFinFlowException;

	public abstract void resumeProcessByTaskId(String paramString,
			WfStaffVO paramWfStaffVO) throws IFinFlowException;

	public abstract void updateProcessSubject(String paramString1,
			String paramString2) throws IFinFlowException;

	public abstract void takeTask(String paramString, WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract int takeTasks(TaskType paramTaskType,
			WfStaffVO paramWfStaffVO, int paramInt) throws IFinFlowException;

	public abstract void commitTask(String paramString,
			WfStaffVO paramWfStaffVO, VariableSet paramVariableSet)
			throws IFinFlowException;

	public abstract void commitTask(String paramString1,
			WfStaffVO paramWfStaffVO, VariableSet paramVariableSet,
			String paramString2) throws IFinFlowException;

	public abstract void rejectTask(String paramString1, String paramString2,
			WfStaffVO paramWfStaffVO, VariableSet paramVariableSet)
			throws IFinFlowException;

	public abstract void rejectTask(String paramString1, String paramString2,
			WfStaffVO paramWfStaffVO, VariableSet paramVariableSet,
			String paramString3) throws IFinFlowException;

	public abstract void retractTask(String paramString,
			WfStaffVO paramWfStaffVO, VariableSet paramVariableSet)
			throws IFinFlowException;

	public abstract void returnTask(String paramString, WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract void skipTask(String paramString1, String paramString2,
			WfStaffVO paramWfStaffVO, VariableSet paramVariableSet)
			throws IFinFlowException;

	public abstract void skipTask(String paramString1, String paramString2,
			WfStaffVO paramWfStaffVO, VariableSet paramVariableSet,
			String paramString3) throws IFinFlowException;

	public abstract void entrustTask(String paramString,
			WfStaffVO paramWfStaffVO1, WfStaffVO paramWfStaffVO2)
			throws IFinFlowException;

	public abstract void entrustTask(String paramString1,
			WfStaffVO paramWfStaffVO1, WfStaffVO paramWfStaffVO2,
			String paramString2) throws IFinFlowException;

	public abstract void setTaskPriority(String paramString,
			WfStaffVO paramWfStaffVO, int paramInt) throws IFinFlowException;

	public abstract void signal(String paramString1, String paramString2);

	public abstract List<ProcessExceptionVO> searchProcessException(
			CriteriaImpl paramCriteriaImpl) throws IFinFlowException;

	public abstract List<TaskVO> getTodoTask(WfStaffVO paramWfStaffVO,
			int paramInt1, int paramInt2) throws IFinFlowException;

	public abstract long getTodoTaskCount(WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract List<TaskVO> getTodoTask(WfStaffVO paramWfStaffVO,
			String paramString1, String paramString2, int paramInt1,
			int paramInt2, int paramInt3, String... paramVarArgs)
			throws IFinFlowException;

	public abstract long getTodoTaskCount(WfStaffVO paramWfStaffVO,
			String paramString1, String paramString2, int paramInt,
			String... paramVarArgs) throws IFinFlowException;

	public abstract List<TaskVO> getDoneTask(WfStaffVO paramWfStaffVO,
			int paramInt1, int paramInt2) throws IFinFlowException;

	public abstract long getDoneTaskCount(WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract TaskVO getTask(String paramString) throws IFinFlowException;

	public abstract List<TaskVO> getTodoTasksByProcessId(String paramString)
			throws IFinFlowException;

	public abstract List<TaskVO> getAllTasksByProcessId(String paramString)
			throws IFinFlowException;

	public abstract List<TaskVO> searchTask(WfStaffVO paramWfStaffVO,
			int paramInt1, int paramInt2) throws IFinFlowException;

	public abstract long searchTaskCount(WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract List<TaskVO> searchTask(String paramString1,
			String paramString2, Date paramDate1, Date paramDate2,
			int paramInt1, int paramInt2, int paramInt3)
			throws IFinFlowException;

	public abstract long searchTaskCount(String paramString1,
			String paramString2, Date paramDate1, Date paramDate2, int paramInt)
			throws IFinFlowException;

	public abstract List<TaskVO> searchTask(String paramString1,
			String paramString2, Date paramDate1, Date paramDate2,
			int paramInt1, int paramInt2, int paramInt3, String paramString3,
			String paramString4, String paramString5) throws IFinFlowException;

	public abstract long searchTaskCount(String paramString1,
			String paramString2, Date paramDate1, Date paramDate2,
			int paramInt, String paramString3, String paramString4,
			String paramString5) throws IFinFlowException;

	public abstract List<TaskVO> searchTask(CriteriaImpl paramCriteriaImpl)
			throws IFinFlowException;

	public abstract String searchTaskUrl(String paramString)
			throws IFinFlowException;

	public abstract ProcessVO getProcess(String paramString)
			throws IFinFlowException;

	public abstract List<ProcessVO> getProcess(String paramString1,
			String paramString2, int paramInt1, int paramInt2, int paramInt3)
			throws IFinFlowException;

	public abstract long getProcessCount(String paramString1,
			String paramString2, int paramInt) throws IFinFlowException;

	public abstract ProcessVO getProcessByTaskId(String paramString)
			throws IFinFlowException;

	public abstract List<ProcessVO> getSubProcess(String paramString)
			throws IFinFlowException;

	public abstract List<ProcessVO> searchProcess(CriteriaImpl paramCriteriaImpl)
			throws IFinFlowException;

	public abstract List<ActivityVO> searchActivity(
			CriteriaImpl paramCriteriaImpl) throws IFinFlowException;

	public abstract void setProcessVariable(String paramString,
			Variable paramVariable, WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract void setProcessVariables(String paramString,
			VariableSet paramVariableSet, WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract void setProcessVariableByTaskId(String paramString,
			Variable paramVariable, WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract void setProcessVariablesByTaskId(String paramString,
			VariableSet paramVariableSet, WfStaffVO paramWfStaffVO)
			throws IFinFlowException;

	public abstract VariableSet getProcessVariables(String paramString)
			throws IFinFlowException;

	public abstract VariableSet getProcessVariables(String paramString,
			Set<String> paramSet) throws IFinFlowException;

	public abstract VariableSet getProcessVariablesByTaskId(String paramString)
			throws IFinFlowException;

	public abstract VariableSet getProcessVariablesByTaskId(String paramString,
			Set<String> paramSet) throws IFinFlowException;

	public abstract List<String> getPackageList() throws IFinFlowException;

	public abstract List<DefinitionVO> searchDeployedTemplates(
			CriteriaImpl paramCriteriaImpl) throws IFinFlowException;

	public abstract Map<String, List<ProcessTraceVO>> getProcessTrace(
			String paramString) throws IFinFlowException;

	public abstract boolean checkTaskRetraction(String paramString)
			throws IFinFlowException;

	public abstract List<String> getRejectableTargets(String paramString)
			throws IFinFlowException;

	public abstract List<RejectActivityVO> getRejectableTargetList(
			String paramString) throws IFinFlowException;

	public abstract List<String> getSkipableTargets(String paramString)
			throws IFinFlowException;

	public abstract List<String> getActivityNames(String paramString);

	public abstract long searchTaskCount(CriteriaImpl paramCriteriaImpl)
			throws IFinFlowException;

	public abstract long searchProcessCount(CriteriaImpl paramCriteriaImpl)
			throws IFinFlowException;

	public abstract long searchActivityCount(CriteriaImpl paramCriteriaImpl)
			throws IFinFlowException;

	public abstract List<String> getAssigneeByTaskId(String paramString)
			throws IFinFlowException;

	public abstract List<TemplateNodeVO> getTemplateNodeList(
			String paramString1, String paramString2, int paramInt,
			String paramString3) throws IFinFlowException;

	public abstract String getNodeNameBeforeCurrNode(String paramString)
			throws IFinFlowException;

	public abstract String getFirstNodeNameBeforeCurrNode(String paramString)
			throws IFinFlowException;

	public abstract byte[] getTemplatePicture(String paramString1,
			String paramString2, int paramInt) throws IFinFlowException;

	public abstract void updateDefaultRejectValue(String paramString1,
			String paramString2) throws IFinFlowException;

	public abstract void updateBusinessCalendar(
			BusinessCalendarVo paramBusinessCalendarVo)
			throws IFinFlowException;

	public abstract boolean isHoliday(Date paramDate) throws IFinFlowException;

	public abstract BusinessCalendarVo getBusinessCalendar()
			throws IFinFlowException;

	public abstract void addTask(String paramString1, String paramString2,
			List<String> paramList, String paramString3, int paramInt)
			throws IFinFlowException;

	public abstract void addTask(String paramString1, List<String> paramList,
			String paramString2, int paramInt) throws IFinFlowException;

	public abstract boolean deleteTask(String paramString1,
			String paramString2, List<String> paramList, String paramString3,
			int paramInt) throws IFinFlowException;

	public abstract boolean deleteTask(String paramString1,
			List<String> paramList, String paramString2, int paramInt)
			throws IFinFlowException;

	public abstract void updateTaskExtendsField(String paramString,
			String... paramVarArgs) throws IFinFlowException;

	public abstract TaskVO getTaskvoWithAssign(String paramString)
			throws IFinFlowException;

	public abstract void updateTaskAssign(String paramString1,
			String paramString2) throws IFinFlowException;

	public abstract List<EntrustTaskHistoryVO> getTaskEntrustHistory(
			String paramString) throws IFinFlowException;
}
