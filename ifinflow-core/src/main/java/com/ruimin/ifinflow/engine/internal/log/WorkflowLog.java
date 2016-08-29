package com.ruimin.ifinflow.engine.internal.log;

import com.ruimin.ifinflow.engine.pvm.cmd.DbLogCmd;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;


public class WorkflowLog {
    private static Logger infologger = null;
    private static Logger errorlogger = null;

    private static final String LOGGER_NAME = "wfis";
    private static final String ERROR_LOGGER_NAME = "errorwfis";
    private static final String LOG_RESOURCE = "log4j_config.xml";
    private static int logLevel = 4;


    static {
        String logPath = com.ruimin.ifinflow.engine.internal.config.Configuration.getValue("LogPath");
        String logLevelName = com.ruimin.ifinflow.engine.internal.config.Configuration.getValue("LogLevel");


        if ((logPath == null) || (logPath.trim().length() == 0)) {
            logPath = "C:";
        }
        if ((logLevelName == null) || (logLevelName.trim().length() == 0)) {
            logLevelName = "debug";
            logLevel = 4;
        } else if ("error".equalsIgnoreCase(logLevelName)) {
            logLevel = 1;
        } else if ("warn".equalsIgnoreCase(logLevelName)) {
            logLevel = 2;
        } else if ("info".equalsIgnoreCase(logLevelName)) {
            logLevel = 3;
        } else if ("debug".equalsIgnoreCase(logLevelName)) {
            logLevel = 4;
        }

        System.out.println("Log path:" + logPath + ", log level:" + logLevelName + ".");
        System.setProperty("ifinflow.log.dir", logPath);
        System.setProperty("ifinflow.log.level", logLevelName);
        new WorkflowLog("wfis", "errorwfis");
    }


    public static void resetWorkflowLog() {
        String logPath = com.ruimin.ifinflow.engine.internal.config.Configuration.getValue("LogPath");
        String logLevelName = com.ruimin.ifinflow.engine.internal.config.Configuration.getValue("LogLevel");


        if ((logPath == null) || (logPath.trim().length() == 0)) {
            logPath = "C:";
        }
        if ((logLevelName == null) || (logLevelName.trim().length() == 0)) {
            logLevelName = "debug";
        } else if ("error".equalsIgnoreCase(logLevelName)) {
            logLevel = 1;
        } else if ("warn".equalsIgnoreCase(logLevelName)) {
            logLevel = 2;
        } else if ("info".equalsIgnoreCase(logLevelName)) {
            logLevel = 3;
        } else if ("debug".equalsIgnoreCase(logLevelName)) {
            logLevel = 4;
        }
        if ((System.getProperty("ifinflow.log.dir") == null) || (System.getProperty("ifinflow.log.level") == null) || (!System.getProperty("ifinflow.log.dir").equalsIgnoreCase(logPath)) || (!System.getProperty("ifinflow.log.level").equalsIgnoreCase(logLevelName))) {

            System.setProperty("ifinflow.log.dir", logPath);
            System.setProperty("ifinflow.log.level", logLevelName);
            new WorkflowLog("wfis", "errorwfis");
        }
    }

    private WorkflowLog(String loggerName, String errorLoggerName) {
        try {
            String resource = "log4j_config.xml";
            URL configFileResource = WorkflowLog.class.getResource(resource);

            DOMConfigurator.configure(configFileResource);
            infologger = Logger.getLogger(loggerName);
            if (errorLoggerName != null) {
                errorlogger = Logger.getLogger(errorLoggerName);
            }
        } catch (Throwable thrw) {
            infologger = Logger.getLogger(loggerName);
            infologger = Logger.getLogger(errorLoggerName);
            error("请检查平台的日志配置log4j_config.xml," + thrw.toString() + "该异常不会影响系统运行！");
        }
    }


    public static void debug(Object message) {
        infologger.log(Priority.DEBUG, message);
    }


    public static void debug(Object message, Throwable th) {
        infologger.log(Priority.DEBUG, message, th);
    }


    public static void info(Object message) {
        infologger.log(Priority.INFO, message);
    }


    public static void warn(Object message) {
        infologger.log(Priority.WARN, message);
    }


    public static void error(Object message) {
        errorlogger.log(Priority.ERROR, message);
    }


    public static void error(Object message, Throwable th) {
        errorlogger.log(Priority.ERROR, message, th);
    }


    public static void fatal(Object message) {
        errorlogger.log(Priority.FATAL, message);
    }


    public static boolean isDebug() {
        if (logLevel >= 4) {
            return true;
        }
        return false;
    }


    public static boolean isInfo() {
        if (logLevel >= 3) {
            return true;
        }
        return false;
    }


    public static boolean isWarn() {
        if (logLevel >= 2) {
            return true;
        }
        return false;
    }


    public static boolean isError() {
        if (logLevel >= 1) {
            return true;
        }
        return false;
    }


    public static void dbLog(Level level, LogVO vo) {
        LogCategory category = vo.getCategory();
        if (category == null)
            return;
        if (((category.compareTo(LogCategory.APPLICATION) < 0) && ("1".equals(com.ruimin.ifinflow.engine.internal.config.Configuration.getValue("SystemAuditLog")))) || ((category.compareTo(LogCategory.APPLICATION) >= 0) && (category.compareTo(LogCategory.RUNLOG) < 0) && ("1".equals(com.ruimin.ifinflow.engine.internal.config.Configuration.getValue("ApplicationAuditLog")))) || (category.compareTo(LogCategory.RUNLOG) >= 0)) {
            try {
                org.jbpm.api.Configuration.getProcessEngine().execute(new DbLogCmd(vo.me()));
            } catch (Exception e1) {
                error("操作日志入库失败", e1);
            }
        }
        Throwable e = vo.getException();
        if (category.compareTo(LogCategory.RUNLOG) >= 0) {
            if (level.isGreaterOrEqual(Level.WARN)) {
                if (e == null) {
                    infologger.log(level, vo.getLogContent());
                } else {
                    infologger.log(level, vo.getLogContent(), e);
                }
            } else if (e == null) {
                errorlogger.log(level, vo.getLogContent());
            } else {
                errorlogger.log(level, vo.getLogContent(), e);
            }
        }
    }


    public static void dbExceptionLog(ELogVO vo) {
        try {
            org.jbpm.api.Configuration.getProcessEngine().execute(new DbLogCmd(vo.me()));
        } catch (Exception e1) {
            error("流程引擎异常信息入库操作失败", e1);
        }
    }
}