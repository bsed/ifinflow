package com.ruimin.ifinflow.engine.internal.log.vo;

import com.ruimin.ifinflow.engine.internal.log.LogCategory;
import com.ruimin.ifinflow.engine.internal.log.LogVO;
import org.jbpm.api.model.WfUserInfo;

public class AppLog extends LogVO {
	public AppLog(String method, String[] argNames, Object[] args) {
		this(LogCategory.API, method, argNames, args);
	}

	public AppLog(LogCategory category, String method, String[] argNames,
			Object[] args) {
		super(category);
		switch (category) {
		case API:
			setLogTitle("API操作日志");
			break;
		case AUTONODE:
			setLogTitle("自动节点日志");
			break;
		case TIMERNODE:
			setLogTitle("定时节点日志");
			break;
		case OVERTIME:
			setLogTitle("超时日志");
		}

		setLogContent(formatArgs(method, argNames, args));
	}

	private String formatArgs(String method, String[] argNames, Object[] args) {
		StringBuffer sb = new StringBuffer("");
		sb.append("call ").append(method).append("(");
		String userid = null;
		for (int i = 0; i < argNames.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(argNames[i]).append("=").append(args[i]);
			if ((args[i] instanceof WfUserInfo)) {
				WfUserInfo user = (WfUserInfo) args[i];
				if ((user != null) && (userid == null)) {
					userid = user.getStaffId();
				}
			}
		}
		sb.append(")");

		return userid + " " + sb.toString();
	}
}
