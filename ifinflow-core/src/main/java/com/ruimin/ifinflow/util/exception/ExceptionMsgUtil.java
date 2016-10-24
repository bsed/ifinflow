package com.ruimin.ifinflow.util.exception;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public class ExceptionMsgUtil {
	public static String getStacktrace(Throwable thowable) {
		if (thowable == null) {
			return "";
		}

		IFinFlowException wfExcep = null;
		Throwable throwable = null;

		if ((thowable instanceof IFinFlowException)) {
			wfExcep = (IFinFlowException) thowable;
		} else {
			throwable = thowable;
		}
		CharArrayWriter cw = new CharArrayWriter(1024);
		PrintWriter pw = new PrintWriter(cw);
		Throwable rootException = null;
		if (wfExcep != null) {
			rootException = wfExcep.getRootException();
			rootException.printStackTrace(pw);
			pw.flush();
			pw.close();
			cw.close();
		} else {
			throwable.printStackTrace(pw);
			pw.flush();
			pw.close();
			cw.close();
		}
		StringBuffer sb = null;
		if (wfExcep != null) {
			sb = new StringBuffer(cw.size() + 512);
			sb.append("{{>>>Begin of IFinFlowException<<<\n");
			sb.append("[ExceptionCode: " + wfExcep.getCode() + "] ");
			sb.append("[ExcetpionMessage: ");
			sb.append(wfExcep.getMessage());
			sb.append("]\nCaused by ");
			sb.append(cw.toString());
			sb.append(">>>End of IFinFlowException<<<}}\n");
			return sb.toString();
		}
		return cw.toString();
	}
}
