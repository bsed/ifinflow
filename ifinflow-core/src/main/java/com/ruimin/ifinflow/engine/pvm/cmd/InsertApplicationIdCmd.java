package com.ruimin.ifinflow.engine.pvm.cmd;

import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.WebApplication;

import java.util.Collection;

public class InsertApplicationIdCmd
        implements Command<Void> {
    private static final long serialVersionUID = 1L;
    private WebApplication webApp;
    private String dialect;

    public InsertApplicationIdCmd(WebApplication webApp, String dialect) {
        this.webApp = webApp;
        this.dialect = dialect;
    }

    public Void execute(Environment environment) throws Exception {
        StringBuffer sql = new StringBuffer("insert into IFF_WEBAPPLICATION (DBID,LASTUPDATETIME,ENGINE_INDEX,IP_,HOST_NAME) ");
        if (this.dialect.contains("DB2")) {

            sql.append("(select '").append(this.webApp.getDbid()).append("',current timestamp,case when tw.cc is null then 0 else tw.cc end,'" + this.webApp.getIp() + "','" + this.webApp.getHostName() + "' from (select max(ENGINE_INDEX)+1 cc from IFF_WEBAPPLICATION ) tw)");
        } else if (this.dialect.contains("Oracle")) {
            sql.append("(select '").append(this.webApp.getDbid()).append("',sysdate,case when cc is null then 0 else cc end,'" + this.webApp.getIp() + "','" + this.webApp.getHostName() + "' from (select max(ENGINE_INDEX)+1 cc from IFF_WEBAPPLICATION)) ");

        } else if (this.dialect.contains("SQLServer")) {
            sql.append("(select '").append(this.webApp.getDbid()).append("',getdate(),case when tw.cc is null then 0 else tw.cc end,'" + this.webApp.getIp() + "','" + this.webApp.getHostName() + "' from (select max(ENGINE_INDEX)+1 cc from IFF_WEBAPPLICATION ) tw)");
        } else if (this.dialect.contains("MySQL")) {
            sql.append("(select '").append(this.webApp.getDbid()).append("',now(),case when tw.cc is null then 0 else tw.cc end,'" + this.webApp.getIp() + "','" + this.webApp.getHostName() + "' from (select max(ENGINE_INDEX)+1 cc from IFF_WEBAPPLICATION ) tw)");
        }

        ((Session) environment.get(Session.class)).createSQLQuery(sql.toString()).executeUpdate();
        return null;
    }
}