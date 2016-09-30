package com.ruimin.ifinflow.util;

public class Constant {
	public static final int ASSIGNMODE_SINGLE = 1;

	public static final int ASSIGNMODE_CANDIDATE = 2;

	public static final int ASSIGNMODE_MULTI = 3;

	public static final String GROUPTYPE_PREFIX_UNIT = "UNIT_";

	public static final String GROUPTYPE_PREFIX_ROLE = "ROLE_";

	public static final String GROUPTYPE_PREFIX_DEPT = "DEPT_";

	public static final String GROUPTYPE_PREFIX_GROUP = "GROUP_";

	public static final int ASSIGN_HISTORY_NAN = 0;

	public static final int ASSIGN_HISTORY_PROCESS_RUNNER = 1;

	public static final int ASSIGN_HISTORY_LAST_ACTIVITY_ASSIGNEE = 2;

	public static final int ASSIGN_HISTORY_PROCESS_RUNNER_ROLE = 3;

	public static final int ASSIGN_HISTORY_PROCESS_RUNNER_ORG = 4;

	public static final int PARTICIPATE_TYPE_STAFF = 1;

	public static final int PARTICIPATE_TYPE_ROLE = 2;

	public static final int PARTICIPATE_TYPE_ORG = 3;

	public static final int PARTICIPATE_TYPE_ORG_ROLE = 4;

	public static final int PARTICIPATE_TYPE_GROUP = 5;

	public static final int PARTICIPATE_TYPE_ROLE_LEVEL = 6;

	public static final int PARAM_TYPE_STAFF = 1;

	public static final int PARAM_TYPE_VARIABLE = 2;

	public static final int PARAM_TYPE_ADAPTER = 3;

	public static final String EXIT_TYPE_NUMBER = "1";

	public static final String EXIT_TYPE_PERCENT = "2";

	public static final String REJECT_ASSIGN_LAST = "1";

	public static final String REJECT_ASSIGN_REASSIGN = "0";

	public static final String NODE_TYPE_START = "1";

	public static final String NODE_TYPE_END = "2";

	public static final String NODE_TYPE_TASK = "3";

	public static final String NODE_TYPE_AUTO = "4";

	public static final String NODE_TYPE_FORK = "5";

	public static final String NODE_TYPE_JOIN = "6";

	public static final String NODE_TYPE_DECISION = "7";

	public static final String NODE_TYPE_DEC_JOIN = "8";

	public static final String NODE_TYPE_WAIT = "9";

	public static final String NODE_TYPE_TIMER = "10";

	public static final String NODE_TYPE_CC = "11";

	public static final String NODE_TYPE_SUBPROCESS = "12";

	public static final String NODE_TYPE_VIRTUAL = "13";

	public static final int VARIABLE_KIND_UNDEFINED = 0;

	public static final int VARIABLE_KIND_STRING = 1;

	public static final int VARIABLE_KIND_INTEGER = 2;

	public static final int VARIABLE_KIND_FLOAT = 3;

	public static final int VARIABLE_KIND_LONG = 4;

	public static final int VARIABLE_KIND_DOUBLE = 5;

	public static final int VARIABLE_KIND_CHAR = 6;

	public static final int VARIABLE_KIND_DATE = 7;

	public static final int VARIABLE_KIND_BOOLEAN = 8;

	public static final int VARIABLE_KIND_LIST = 9;

	public static final int VARIABLE_KIND_MAP = 10;

	public static final int PROCESS_STATUS_RUNNING = 1;

	public static final int PROCESS_STATUS_SUSPENDED = 3;

	public static final int PROCESS_STATUS_COMPLETED = 16;

	public static final int PROCESS_STATUS_CANCEL = 32;

	public static final int PROCESS_STATUS_TIME_OUT = 256;

	public static final int PROCESS_STATUS_EXCEPTION = 512;

	public static final int ACTIVITY_STATUS_RUNNING = 1;

	public static final int ACTIVITY_STATUS_ASSIGNED = 2;

	public static final int ACTIVITY_STATUS_SUSPENDED = 3;

	public static final int ACTIVITY_STATUS_REJECT = 4;

	public static final int ACTIVITY_STATUS_TAKE_BACK = 5;

	public static final int ACTIVITY_STATUS_SKIP = 6;

	public static final int ACTIVITY_STATUS_COMPLETED = 16;

	public static final int ACTIVITY_STATUS_CANCEL = 32;

	public static final int ACTIVITY_STATUS_ABANDON = 64;

	public static final int ACTIVITY_STATUS_TIME_OUT = 256;

	public static final int ACTIVITY_STATUS_EXCEPTION = 512;

	public static final String REJECT_CONTINUE_DIRECT_BACK = "1";

	public static final String REJECT_CONTINUE_HISTORY_BACK = "2";

	public static final String REJECT_FIRST_NODE = "1";

	public static final String REJECT_LAST_NODE = "2";

	public static final String REJECT_DEFAULT_NODE = "3";

	public static final String APPLICATION_AUDIT_LOG = "ApplicationAuditLog";

	public static final String CLEAN_LOG_TIME = "CleanLogTime";

	public static final String DATABASE = "Database";

	public static final String FLOW_MODEL_CACHE_SIZE = "FlowModelCacheSize";

	public static final String LEAVE_LOG_DAYS = "LeaveLogDays";

	public static final String LOG_LEVEL = "LogLevel";

	public static final String LOG_PATH = "LogPath";

	public static final String ORGADAPTER = "OrgAdapter";

	public static final String SYSTEM_AUDIT_LOG = "SystemAuditLog";

	public static final String FIRST_TASK_AUTO_COMMIT_YES = "1";

	public static final String FIRST_TASK_AUTO_COMMIT_NO = "0";

	public static final int START_EVENT_EXCEPTION = 1;

	public static final int END_EVENT_EXCEPTION = 2;

	public static final int ASSIGN_EXCEPTION = 3;

	public static final int BUSINESS_EXCEPTION = 4;

	public static final int DECISION_EXCEPTION = 5;

	public static final int TIMER_EXCEPTION = 6;

	public static final int START_EVENT_EXCEPTION_STATUS = 9999;

	public static final int END_EVENT_EXCEPTION_STATUS = 8888;

	public static final int ASSIGN_EXCEPTION_STATUS = 7777;

	public static final int BUSINESS_EXCEPTION_STATUS = 6666;

	public static final int EXCEPTION_UNRESOLVED = 0;

	public static final int EXCEPTION_RESOLVED = 1;

	public static final String REPEAT_EXCEPTION = "repeat";

	public static final int THRESHOLD = 4;

	public static String getStatus(int status) {
		if (16 == status)
			return "办理";
		if (4 == status)
			return "退回";
		if (6 == status)
			return "跳转";
		if (5 == status) {
			return "收回";
		}
		return "";
	}

	public static String PROP_CONFIG_PATH = "ifinflow-config";

	public static String PROCESSENGINE_OFF = "off";
	public static String PROCESSENGINE_ON = "on";
}
