package com.ruimin.ifinflow.engine.external.spring;

import com.ruimin.ifinflow.engine.external.model.IWfGroup;
import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskBusinessVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskMechanismVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskPVMVO;
import com.ruimin.ifinflow.engine.internal.entity.IFinFlowJProcessExcep;
import com.ruimin.ifinflow.engine.internal.vo.SubProcessInstanceVo;
import com.ruimin.ifinflow.engine.internal.vo.TaskStatisticsVo;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

public abstract interface WfIFinFlowConsoleService {
	public abstract String deployDefinitionFromClasspath(String paramString1,
			String paramString2) throws IFinFlowException;

	public abstract String deployDefinitionFromInputStream(String paramString,
			InputStream paramInputStream) throws IFinFlowException;

	public abstract String deployDefinitionFromString(String paramString1,
			String paramString2) throws IFinFlowException;

	public abstract String deployDefinitionFromZipInputStream(
			String paramString, ZipInputStream paramZipInputStream)
			throws IFinFlowException;

	public abstract boolean deleteDefinitionById(String paramString1,
			String paramString2, Integer paramInteger) throws IFinFlowException;

	public abstract VariableSet getTaskVariableByTaskForTrace(
			String paramString1, String paramString2) throws IFinFlowException;

	public abstract List<String> getAllUnitLevel();

	public abstract IWfGroup saveGroup(String paramString1,
			String paramString2, List<String> paramList)
			throws IFinFlowException;

	public abstract void updateGroup(String paramString1, String paramString2,
			List<String> paramList) throws IFinFlowException;

	public abstract void deleteGroup(String paramString)
			throws IFinFlowException;

	public abstract void deleteUserGroup(String paramString1,
			String paramString2) throws IFinFlowException;

	public abstract List<IWfGroup> getGroupPage(String paramString,
			int paramInt1, int paramInt2) throws IFinFlowException;

	public abstract long getGroupCount(String paramString)
			throws IFinFlowException;

	public abstract List<IWfStaff> getStaffsByGroupId(String paramString)
			throws IFinFlowException;

	public abstract List<IWfGroup> getGroupIdAndNamePage(String paramString1,
			String paramString2, int paramInt1, int paramInt2)
			throws IFinFlowException;

	public abstract long getGroupIdAndNameCount(String paramString1,
			String paramString2) throws IFinFlowException;

	public abstract void refeshPVM();

	public abstract List<TaskStatisticsVo> getTaskStatisticsList(
			String paramString1, String paramString2, int paramInt,
			Date paramDate1, Date paramDate2) throws IFinFlowException;

	public abstract List<TaskStatisticsVo> getTaskDurationList(Date paramDate1,
			Date paramDate2) throws IFinFlowException;

	public abstract List<SubProcessInstanceVo> getSubProcessInstanceList(
			String paramString1, String paramString2);

	public abstract IFinFlowJProcessExcep queryException(String paramString)
			throws IFinFlowException;

	public abstract void solveException(String paramString)
			throws IFinFlowException;

	public abstract List<TaskPVMVO> queryTaskPVMList(int paramInt,
			ArrayList<?> paramArrayList);

	public abstract List<TaskBusinessVO> queryTaskBusinessList(Date paramDate1,
			Date paramDate2);

	public abstract List<TaskMechanismVO> queryTaskMechanismList(
			Date paramDate1, Date paramDate2);

	public abstract boolean checkTaskOwner(String paramString1,
			String paramString2);
}
