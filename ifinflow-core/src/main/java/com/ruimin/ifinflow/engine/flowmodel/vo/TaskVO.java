package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;
import java.util.Date;

public class TaskVO implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String TASK_ID = "taskId";
	public static final String PROCESS_ID = "historyProcessInstance.dbid";
	public static final String CREATE_TIME = "createTime";
	public static final String END_TIME = "endTime";
	public static final String DURATION = "duration";
	public static final String TAKE_DATE = "takeDate";
	public static final String OVERDUE = "overdue";
	public static final String DEAD_LINE = "deadLine";
	public static final String PACKAGE_ID = "packageId";
	public static final String TEMPLATE_ID = "templateId";
	public static final String TEMPLATE_NAME = "templateName";
	public static final String TEMPLATE_VERSION = "templateVersion";
	public static final String SUBJECT = "subject";
	public static final String STATUS = "status";
	public static final String NODE_ID = "nodeId";
	public static final String NODE_NAME = "nodeName";
	public static final String PRIORITY = "priority";
	public static final String SOURCE_URL = "sourceUrl";
	public static final String ASSIGN_MODE = "assignMode";
	public static final String SKIP_AUTH = "skipAuth";
	public static final String REJECT_AUTH = "rejectAuth";
	public static final String OWNER_ID = "assignee";
	public static final String OWNER_NAME = "ownerName";
	public static final String OWNER_UNIT = "ownerUnitId";
	public static final String OWNER_ROLE = "ownerRoleId";
	public static final String EXECUTOR_ID = "executorId";
	public static final String EXECUTOR_NAME = "executorName";
	public static final String EXECUTOR_UNIT = "executorUnit";
	public static final String EXECUTOR_ROLE = "executorRole";
	public static final String USER_EXT_STRING_1 = "userExtString1";
	public static final String USER_EXT_STRING_2 = "userExtString2";
	public static final String USER_EXT_STRING_3 = "userExtString3";
	public static final String USER_EXT_STRING_4 = "userExtString4";
	public static final String USER_EXT_STRING_5 = "userExtString5";
	public static final String USER_EXT_STRING_6 = "userExtString6";
	public static final String USER_EXT_STRING_7 = "userExtString7";
	public static final String USER_EXT_STRING_8 = "userExtString8";
	protected String taskId;
	private String processId;
	protected Date createTime;
	protected Date endTime;
	protected long duration;
	protected Date takeDate;
	protected Integer overdue;
	protected Date deadLine;
	protected String packageId;
	protected String templateId;
	protected int templateVersion;
	protected String subject;
	protected Integer status;
	protected String nodeId;
	protected String nodeName;
	protected int priority;
	protected String sourceUrl;
	protected Integer assignMode;
	protected String skipAuth;
	protected String rejectAuth;
	protected String assignee;
	protected String ownerId;
	protected String ownerName;
	protected String ownerUnitId;
	protected String ownerRoleId;
	protected String executorId;
	protected String executorName;
	protected String executorUnit;
	protected String executorRole;
	protected String userExtString1;
	protected String userExtString2;
	protected String userExtString3;
	protected String userExtString4;
	protected String userExtString5;
	protected String userExtString6;
	protected String userExtString7;
	protected String userExtString8;
	protected ProcessVO process;
	protected String assignUnitId;
	protected String assignRoleId;

	public void setAssignRoleId(String assignRoleId) {
		this.assignRoleId = assignRoleId;
	}

	public void setAssignUnitId(String assignUnitId) {
		this.assignUnitId = assignUnitId;
	}

	public String getAssignRoleId() {
		return this.assignRoleId;
	}

	public String getAssignUnitId() {
		return this.assignUnitId;
	}

	public ProcessVO getProcess() {
		return this.process;
	}

	public void setProcess(ProcessVO process) {
		this.process = process;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getSourceUrl() {
		return this.sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getDuration() {
		return this.duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getPackageId() {
		return this.packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getTemplateVersion() {
		return this.templateVersion;
	}

	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}

	public String getAssignee() {
		return this.assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Integer getAssignMode() {
		return this.assignMode;
	}

	public void setAssignMode(Integer assignMode) {
		this.assignMode = assignMode;
	}

	public String getSkipAuth() {
		return this.skipAuth;
	}

	public void setSkipAuth(String skipAuth) {
		this.skipAuth = skipAuth;
	}

	public String getRejectAuth() {
		return this.rejectAuth;
	}

	public void setRejectAuth(String rejectAuth) {
		this.rejectAuth = rejectAuth;
	}

	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerUnitId() {
		return this.ownerUnitId;
	}

	public void setOwnerUnitId(String ownerUnitId) {
		this.ownerUnitId = ownerUnitId;
	}

	public String getOwnerRoleId() {
		return this.ownerRoleId;
	}

	public void setOwnerRoleId(String ownerRoleId) {
		this.ownerRoleId = ownerRoleId;
	}

	public String getExecutorName() {
		return this.executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public String getExecutorUnit() {
		return this.executorUnit;
	}

	public void setExecutorUnit(String executorUnit) {
		this.executorUnit = executorUnit;
	}

	public String getExecutorRole() {
		return this.executorRole;
	}

	public void setExecutorRole(String executorRole) {
		this.executorRole = executorRole;
	}

	public Date getTakeDate() {
		return this.takeDate;
	}

	public void setTakeDate(Date takeDate) {
		this.takeDate = takeDate;
	}

	public Integer getOverdue() {
		return this.overdue;
	}

	public void setOverdue(Integer overdue) {
		this.overdue = overdue;
	}

	public Date getDeadLine() {
		return this.deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public String getUserExtString1() {
		return this.userExtString1;
	}

	public void setUserExtString1(String userExtString1) {
		this.userExtString1 = userExtString1;
	}

	public String getUserExtString2() {
		return this.userExtString2;
	}

	public void setUserExtString2(String userExtString2) {
		this.userExtString2 = userExtString2;
	}

	public String getUserExtString3() {
		return this.userExtString3;
	}

	public void setUserExtString3(String userExtString3) {
		this.userExtString3 = userExtString3;
	}

	public String getUserExtString4() {
		return this.userExtString4;
	}

	public void setUserExtString4(String userExtString4) {
		this.userExtString4 = userExtString4;
	}

	public String getUserExtString5() {
		return this.userExtString5;
	}

	public void setUserExtString5(String userExtString5) {
		this.userExtString5 = userExtString5;
	}

	public String getUserExtString6() {
		return this.userExtString6;
	}

	public void setUserExtString6(String userExtString6) {
		this.userExtString6 = userExtString6;
	}

	public String getUserExtString7() {
		return this.userExtString7;
	}

	public void setUserExtString7(String userExtString7) {
		this.userExtString7 = userExtString7;
	}

	public String getUserExtString8() {
		return this.userExtString8;
	}

	public void setUserExtString8(String userExtString8) {
		this.userExtString8 = userExtString8;
	}

	public String getExecutorId() {
		return this.executorId;
	}

	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}
}
