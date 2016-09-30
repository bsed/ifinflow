package org.jbpm.api;

import com.ruimin.ifinflow.model.flowmodel.external.IModelService;
import org.jbpm.api.cmd.Command;

import java.sql.Connection;

public interface ProcessEngine {
    RepositoryService getRepositoryService();

    ExecutionService getExecutionService();

    HistoryService getHistoryService();

    TaskService getTaskService();

    IdentityService getIdentityService();

    ManagementService getManagementService();

    <T> T get(Class<T> paramClass);

    Object get(String paramString);

    ProcessEngine setAuthenticatedUserId(String paramString);

    ProcessEngine setHibernateSession(Object paramObject);

    ProcessEngine setJdbcConnection(Connection paramConnection);

    <T> T execute(Command<T> paramCommand);

    void close();

    IModelService getModelService();
}