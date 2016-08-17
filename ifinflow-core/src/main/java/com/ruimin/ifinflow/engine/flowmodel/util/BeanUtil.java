package com.ruimin.ifinflow.engine.flowmodel.util;

import com.ruimin.ifinflow.engine.flowmodel.vo.ActivityVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.DefinitionVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.EntrustTaskHistoryVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessTraceVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.ProcessVO;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskVO;
import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import com.ruimin.ifinflow.util.TemplateCacheUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryComment;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryCommentImpl;
import org.jbpm.pvm.internal.history.model.HistoryDetailImpl;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.task.TopTaskVO;

public class BeanUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	public static List<ProcessVO> createProcessVOs(
			List<HistoryProcessInstance> list) {

		List<ProcessVO> result = new ArrayList();

		if (list == null) {

			return result;

		}

		for (HistoryProcessInstance pro : list) {

			result.add(createProcessVO(pro));

		}

		return result;

	}

	public static ProcessVO createProcessVO(HistoryProcessInstance proc) {

		ProcessVO processvo = new ProcessVO();

		if (proc == null) {

			return null;

		}

		HistoryProcessInstanceImpl process = (HistoryProcessInstanceImpl) proc;

		processvo.setProcessId(process.getDbid());

		processvo.setExecutionId(process.getProcessInstanceId());

		processvo.setTemplateId(process.getTemplateId());

		processvo.setTempleteName(process.getTempleteName());

		processvo.setTempleteVersion(process.getTempleteVersion());

		processvo.setEndActivityName(process.getEndActivityName());

		processvo.setDeadLine(process.getDeadLine());

		processvo.setOverdue(process.getOverdue());

		processvo.setInitiatorId(process.getInitiatorId());

		processvo.setInitiatorName(process.getInitiatorName());

		processvo.setInitiatorRole(process.getInitiatorRole());

		processvo.setOwnerUnitId(process.getOwnerUnitId());

		processvo.setPackageId(process.getPackageId());

		processvo.setStartTime(process.getStartTime());

		processvo.setEndTime(process.getEndTime());

		processvo.setStatus(process.getStatus());

		processvo.setSubject(process.getSubject());

		processvo.setSuperProcessId(process.getSuperProcessExecution());

		return processvo;

	}

	public static List<TaskVO> createTaskVOs(List<?> list) {

		List<TaskVO> res = new ArrayList();

		if (list == null) {

			return res;

		}

		for (Object task : list) {

			if ((task instanceof HistoryTask)) {

				res.add(createTaskVO((HistoryTaskImpl) task));

			} else if ((task instanceof TopTaskVO)) {

				res.add(createTaskVO((TopTaskVO) task));

			}

		}

		return res;

	}

	@Deprecated
	public static TaskVO createTaskVO(HistoryTaskImpl task) {

		TaskVO taskvo = new TaskVO();

		if (task == null) {

			return null;

		}

		taskvo.setTaskId(task.getId());

		taskvo.setProcessId(task.getHistoryProcessInstanceId());

		taskvo.setProcess(createProcessVO(task.getHistoryProcessInstance()));

		taskvo.setAssignMode(task.getAssignMode());

		taskvo.setRejectAuth(task.getRejectAuth());

		taskvo.setSkipAuth(task.getSkipAuth());

		taskvo.setNodeId(task.getNodeId());

		taskvo.setNodeName(task.getNodeName());

		taskvo.setStatus(task.getStatus());

		taskvo.setPackageId(task.getPackageId());

		taskvo.setTemplateId(task.getTemplateId());

		taskvo.setTemplateVersion(task.getTemplateVersion());

		taskvo.setSubject(task.getSubject());

		taskvo.setPriority(task.getPriority());

		taskvo.setSourceUrl(task.getSourceUrl());

		taskvo.setCreateTime(task.getCreateTime());

		taskvo.setEndTime(task.getEndTime());

		taskvo.setTakeDate(task.getTakeDate());

		taskvo.setDuration(task.getDuration());

		taskvo.setOverdue(task.getOverdue());

		taskvo.setDeadLine(task.getDeadLine());

		taskvo.setAssignee(task.getAssignee());

		taskvo.setOwnerId(task.getOwnerId());

		taskvo.setOwnerName(task.getOwnerName());

		taskvo.setOwnerRoleId(task.getOwnerRoleId());

		taskvo.setOwnerUnitId(task.getOwnerUnitId());

		taskvo.setExecutorId(task.getExecutorId());

		taskvo.setExecutorName(task.getExecutorName());

		taskvo.setExecutorRole(task.getExecutorRole());

		taskvo.setExecutorUnit(task.getExecutorUnit());

		taskvo.setUserExtString1(task.getUserExtString1());

		taskvo.setUserExtString2(task.getUserExtString2());

		taskvo.setUserExtString3(task.getUserExtString3());

		taskvo.setUserExtString4(task.getUserExtString4());

		taskvo.setUserExtString5(task.getUserExtString5());

		taskvo.setUserExtString6(task.getUserExtString6());

		taskvo.setUserExtString7(task.getUserExtString7());

		taskvo.setUserExtString8(task.getUserExtString8());

		return taskvo;

	}

	public static TaskVO createTaskVO(HistoryTaskImpl task,
			HistoryProcessInstance hpi) {

		TaskVO taskvo = new TaskVO();

		if (task == null) {

			return null;

		}

		taskvo.setTaskId(task.getId());

		taskvo.setProcessId(task.getHistoryProcessInstanceId());

		taskvo.setProcess(createProcessVO(hpi));

		taskvo.setAssignMode(task.getAssignMode());

		taskvo.setRejectAuth(task.getRejectAuth());

		taskvo.setSkipAuth(task.getSkipAuth());

		taskvo.setNodeId(task.getNodeId());

		taskvo.setNodeName(task.getNodeName());

		taskvo.setStatus(task.getStatus());

		taskvo.setPackageId(task.getPackageId());

		taskvo.setTemplateId(task.getTemplateId());

		taskvo.setTemplateVersion(task.getTemplateVersion());

		taskvo.setSubject(task.getSubject());

		taskvo.setPriority(task.getPriority());

		taskvo.setSourceUrl(task.getSourceUrl());

		taskvo.setCreateTime(task.getCreateTime());

		taskvo.setEndTime(task.getEndTime());

		taskvo.setTakeDate(task.getTakeDate());

		taskvo.setDuration(task.getDuration());

		taskvo.setOverdue(task.getOverdue());

		taskvo.setDeadLine(task.getDeadLine());

		taskvo.setAssignee(task.getAssignee());

		taskvo.setOwnerId(task.getOwnerId());

		taskvo.setOwnerName(task.getOwnerName());

		taskvo.setOwnerRoleId(task.getOwnerRoleId());

		taskvo.setOwnerUnitId(task.getOwnerUnitId());

		taskvo.setExecutorId(task.getExecutorId());

		taskvo.setExecutorName(task.getExecutorName());

		taskvo.setExecutorRole(task.getExecutorRole());

		taskvo.setExecutorUnit(task.getExecutorUnit());

		taskvo.setUserExtString1(task.getUserExtString1());

		taskvo.setUserExtString2(task.getUserExtString2());

		taskvo.setUserExtString3(task.getUserExtString3());

		taskvo.setUserExtString4(task.getUserExtString4());

		taskvo.setUserExtString5(task.getUserExtString5());

		taskvo.setUserExtString6(task.getUserExtString6());

		taskvo.setUserExtString7(task.getUserExtString7());

		taskvo.setUserExtString8(task.getUserExtString8());

		return taskvo;

	}

	public static TaskVO createTaskVO(TopTaskVO task) {

		TaskVO taskvo = new TaskVO();

		if (task == null) {

			return null;

		}

		taskvo.setTaskId(task.getDBID_());

		taskvo.setProcessId(task.getPROCINST_());

		taskvo.setAssignMode(Integer.valueOf(task.getASSIGNMODE_()));

		NodeVo node = TemplateCacheUtil.getNodeVo(task.getPACKAGEID_(),
				task.getTEMPLATEID_(), task.getTEMPLATEVERSION_(),
				task.getACTIVITY_NAME_());

		taskvo.setRejectAuth(node.getRejectAuth());

		taskvo.setSkipAuth(node.getSkipAuth());

		taskvo.setNodeId(task.getACTIVITY_NAME_());

		taskvo.setNodeName(node.getName());

		taskvo.setStatus(Integer.valueOf(task.getSTATUS_()));

		taskvo.setPackageId(task.getPACKAGEID_());

		taskvo.setTemplateId(task.getTEMPLATEID_());

		taskvo.setTemplateVersion(task.getTEMPLATEVERSION_());

		taskvo.setPriority(task.getPRIORITY_());

		taskvo.setSourceUrl(task.getFORM_());

		taskvo.setCreateTime(task.getCREATE_());

		taskvo.setTakeDate(task.getTAKEDATE_());

		taskvo.setOverdue(Integer.valueOf((int) node.getOvertime()));

		taskvo.setDeadLine(new Date(taskvo.getOverdue().intValue()
				+ taskvo.getCreateTime().getTime()));

		taskvo.setAssignee(task.getASSIGNEE_());

		taskvo.setOwnerId(task.getOWNERID_());

		taskvo.setOwnerRoleId(task.getOWNERROLEID_());

		taskvo.setOwnerUnitId(task.getOWNERUNITID_());

		taskvo.setUserExtString1(task.getUSEREXTSTRING1_());

		taskvo.setUserExtString2(task.getUSEREXTSTRING2_());

		taskvo.setUserExtString3(task.getUSEREXTSTRING3_());

		taskvo.setUserExtString4(task.getUSEREXTSTRING4_());

		taskvo.setUserExtString5(task.getUSEREXTSTRING5_());

		taskvo.setUserExtString6(task.getUSEREXTSTRING6_());

		taskvo.setUserExtString7(task.getUSEREXTSTRING7_());

		taskvo.setUserExtString8(task.getUSEREXTSTRING8_());

		return taskvo;

	}

	public static ProcessTraceVO createProcessTraceVo(
			HistoryActivityInstanceImpl activity) {

		if (activity == null) {

			return null;

		}

		ProcessTraceVO vo = new ProcessTraceVO();

		vo.setStatus(activity.getStatus());

		vo.setType(activity.getType());

		vo.setStartTime(activity.getStartTime());

		vo.setEndTime(activity.getEndTime());

		vo.setDuration(activity.getDuration());

		vo.setActivityDbid(activity.getDbid());

		vo.setToActivityDbid(activity.getNextHistoryActivityDbid());

		vo.setNodeName(activity.getNodeName());

		if ("3".equals(vo.getType())) {

			HistoryTaskImpl hTask = ((HistoryTaskInstanceImpl) activity)
					.getHistoryTask();

			if (hTask != null) {

				vo.setTaskId(hTask.getId());

				vo.setOwnerName(hTask.getOwnerName());

				vo.setExecutorId(hTask.getExecutorId());

				vo.setExecutorName(hTask.getExecutorName());

				Set<HistoryDetailImpl> details = hTask.getDetails();

				if ((details != null) && (!details.isEmpty())) {

					HistoryCommentImpl hisc = (HistoryCommentImpl) details
							.iterator().next();

					vo.setMemo(hisc.getMessage());

				}

				vo.setTakeDate(hTask.getTakeDate());

			}

		}

		String tran = activity.getTransitionName();

		if ((!StringUtils.isEmpty(tran)) && (StringUtils.contains(tran, "to "))) {

			if ((StringUtils.equals(activity.getType(), "3"))
					&& ((4 == activity.getStatus().intValue()) || (6 == activity
							.getStatus().intValue()))
					&& (tran.startsWith("SubFlowNode"))) {

				vo.setToActivityName(tran.substring(0, tran.lastIndexOf(" to ")));

			} else {

				vo.setToActivityName(tran.substring(tran.lastIndexOf("to ") + 3));

			}

		}

		return vo;

	}

	public static ProcessVO createProcessByExecution(ExecutionImpl process) {

		if (process == null) {

			return null;

		}

		ProcessVO pro = new ProcessVO();

		pro.setProcessId(process.getDbid());

		pro.setExecutionId(process.getExecutionImplId());

		return pro;

	}

	public static List<ProcessVO> createProcessVOsByExecutions(
			List<ExecutionImpl> list) {

		List<ProcessVO> pros = new ArrayList();

		if (list == null) {

			return pros;

		}

		for (ExecutionImpl exe : list) {

			pros.add(createProcessByExecution(exe));

		}

		return pros;

	}

	public static List<DefinitionVO> createDefinitionVOs(List<Template> list) {

		List<DefinitionVO> res = new ArrayList();

		if (list == null) {

			return res;

		}

		for (Template template : list) {

			res.add(createDefinitionVO(template));

		}

		return res;

	}

	public static DefinitionVO createDefinitionVO(Template template) {

		DefinitionVO definitionVO = new DefinitionVO();

		if (template == null) {

			return null;

		}

		definitionVO.setPackageId(template.getTemplateId());

		definitionVO.setTempleteId(template.getTemplateId());

		definitionVO.setTempleteVersion(template.getVersion());

		definitionVO.setDeployId(template.getDeploymentId());

		definitionVO.setTempleteName(template.getName());

		definitionVO.setRemark(template.getRemark());

		definitionVO.setCreatedTime(template.getCreatedTime());

		definitionVO.setCreatorId(template.getCreatorId());

		definitionVO.setCreatorName(template.getCreatorName());

		return definitionVO;

	}

	public static List<ActivityVO> createActivityVOs(
			List<HistoryActivityInstance> activitys) {

		List<ActivityVO> res = new ArrayList();

		if (activitys == null) {

			return res;

		}

		for (HistoryActivityInstance act : activitys) {

			res.add(createActivityVO((HistoryActivityInstanceImpl) act));

		}

		return res;

	}

	public static ActivityVO createActivityVO(
			HistoryActivityInstanceImpl activity) {

		if (activity == null) {

			return null;

		}

		ActivityVO vo = new ActivityVO();

		vo.setId(activity.getDbid());

		vo.setProcessId(activity.getHistoryProcessInstance().getDbid());

		vo.setStatus(activity.getStatus());

		vo.setType(activity.getType());

		vo.setNodeId(activity.getActivityName());

		vo.setStartTime(activity.getStartTime());

		vo.setEndTime(activity.getEndTime());

		vo.setDuration(activity.getDuration());

		vo.setDeadLine(activity.getDeadLine());

		vo.setOverdue(activity.getOverdue());

		vo.setPackageId(activity.getPackageId());

		vo.setTemplateId(activity.getTemplateId());

		vo.setTemplateVersion(activity.getTemplateVersion());

		vo.setRejectAuth(activity.getRejectAuth());

		vo.setSkipAuth(activity.getSkipAuth());

		vo.setSouRejectName(activity.getSouRejectName());

		return vo;

	}

	public static List<EntrustTaskHistoryVO> createEntrustTaskHistoryVO(
			String taskId, List<HistoryComment> param) {

		List<EntrustTaskHistoryVO> results = new ArrayList();

		if ((param == null) || (param.isEmpty())) {

			return results;

		}

		EntrustTaskHistoryVO vo = null;

		for (HistoryComment comment : param) {

			vo = new EntrustTaskHistoryVO();

			createEntrustTaskHistoryVo(taskId, comment, vo);

			results.add(vo);

		}

		return results;

	}

	public static void createEntrustTaskHistoryVo(String taskId,
			HistoryComment param, EntrustTaskHistoryVO vo) {

		if (param == null) {

			return;

		}

		vo.setTaskId(taskId);

		vo.setOperateTime(param.getTime());

		vo.setMessage(param.getMessage());

	}

}
