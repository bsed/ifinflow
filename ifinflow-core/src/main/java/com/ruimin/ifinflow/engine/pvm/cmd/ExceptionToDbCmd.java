package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.internal.entity.IFinFlowJProcessExcep;
import com.ruimin.ifinflow.engine.internal.log.ELogVO;
import org.apache.commons.lang.StringUtils;
/*     */
import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;
/*     */
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.cmd.CommandService;
import org.jbpm.pvm.internal.env.Context;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.EventImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.model.op.AtomicOperation;
/*     */
import org.jbpm.pvm.internal.session.DbSession;

import java.util.Collection;

/*     */


public class ExceptionToDbCmd
        extends AbstractCommand<Object> {
    private static final long serialVersionUID = 1L;
    private ExecutionImpl execution;
    private Exception exception;
    private AtomicOperation operation;

    public ExceptionToDbCmd(ExecutionImpl execution, AtomicOperation operation, Exception e) {
        this.execution = execution;
        this.exception = e;
        this.operation = operation;
    }

    public Object execute(Environment environment) throws Exception {
        CommandService commandService = (CommandService) environment.get("newTxRequiredCommandService");

        return commandService.execute(new HandleCmd());
    }

    class HandleCmd extends AbstractCommand<Object> {
        private static final long serialVersionUID = 1L;

        HandleCmd() {
        }

        public Collection<String> execute(Environment environment) throws Exception {
            ExceptionToDbCmd.this.execution.waitForSignal();

            int exceptionPos = -1;

            EventImpl event = ExceptionToDbCmd.this.execution.getEvent();
            if (event != null) {
                String eventName = event.getName();
                if (StringUtils.equals("start", eventName)) {
                    exceptionPos = 1;
                } else if (StringUtils.equals("end", eventName)) {
                    exceptionPos = 2;
                }
            }

            Session session = (Session) EnvironmentImpl.getFromCurrent(Session.class);
            StringBuilder hql = new StringBuilder();
            hql.append("FROM ").append(IFinFlowJProcessExcep.class.getName()).append(" T WHERE ").append("T.operationName = '").append(ExceptionToDbCmd.this.operation.getClass().getSimpleName()).append("' AND").append(" T.nodeId = '").append(ExceptionToDbCmd.this.execution.getActivityName()).append("'").append(" AND").append(" T.executionId = '").append(ExceptionToDbCmd.this.execution.getExecutionImplId()).append("'");


            IFinFlowJProcessExcep tjpe = (IFinFlowJProcessExcep) session.createQuery(hql.toString()).uniqueResult();


            DbSession dbSession = (DbSession) EnvironmentImpl.getFromCurrent(DbSession.class);


            if (tjpe == null) {
                ELogVO log = new ELogVO(ExceptionToDbCmd.this.exception);
                log.setHacti(ExceptionToDbCmd.this.execution.getHistoryActivityInstanceDbid());
                log.setHmprocl(ExceptionToDbCmd.this.execution.getProcessInstance().getDbid());
                log.setHproci(ExceptionToDbCmd.this.execution.getExecution().getDbid());
                log.setExecutionId(ExceptionToDbCmd.this.execution.getExecutionImplId());
                String executionId = null;
                if (ExceptionToDbCmd.this.execution.getIsProcessInstance()) {
                    executionId = ExceptionToDbCmd.this.execution.getDbid();
                } else {
                    executionId = ExceptionToDbCmd.this.execution.getProcessInstance().getDbid();
                }
                HistoryProcessInstanceImpl hpii = (HistoryProcessInstanceImpl) dbSession.createHistoryProcessInstanceQuery().processId(executionId).uniqueResult();


                log.setPackageId(hpii.getPackageId());
                log.setTemplateId(hpii.getTemplateId());
                log.setTemplateName(hpii.getTempleteName());
                log.setTemplateVersion(hpii.getTempleteVersion());
                log.setSubject(hpii.getSubject());


                ActivityImpl act = ExceptionToDbCmd.this.execution.getActivity();
                if (act == null) {
                    TransitionImpl transition = ExceptionToDbCmd.this.execution.getTransition();
                    if (StringUtils.isBlank(transition.getName())) {
                        log.setNodeId(transition.toString());
                    } else {
                        log.setNodeId(transition.getName());
                    }
                    log.setNodeKind(transition.getSource().getType());
                } else {
                    log.setNodeId(ExceptionToDbCmd.this.execution.getActivity().getName());
                    log.setNodeKind(ExceptionToDbCmd.this.execution.getActivity().getType());
                }
                log.setExceptionPos(exceptionPos);
                log.setOperationName(ExceptionToDbCmd.this.operation.getClass().getSimpleName());
                log.setExecutionStatus(hpii.getStatus().intValue());


                hpii.setStatus(Integer.valueOf(512));
                dbSession.save(log.me());
                dbSession.update(hpii);
            } else {
                Context context = ((EnvironmentImpl) environment).getContext("process-engine");
                context.set("repeat", Boolean.TRUE);
            }
            return null;
        }
    }
}