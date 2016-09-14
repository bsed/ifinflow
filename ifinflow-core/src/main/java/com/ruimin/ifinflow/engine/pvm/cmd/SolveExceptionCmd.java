package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.internal.entity.IFinFlowJProcessExcep;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.jbpm.api.cmd.Environment;

import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.env.Context;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.model.op.AtomicOperation;
import org.jbpm.pvm.internal.model.op.Signal;

import org.jbpm.pvm.internal.session.DbSession;


public class SolveExceptionCmd
        extends AbstractCommand<Boolean> {
    private static final long serialVersionUID = 1L;
    private static final String LOCK = "lock";
    private String executionId;

    public SolveExceptionCmd(String executionId) {
        this.executionId = executionId;
    }

    public Boolean execute(Environment environment) throws Exception {
        synchronized ("lock") {

            Context context = ((EnvironmentImpl) environment).getContext("process-engine");

            context.set("repeat", Boolean.FALSE);

            DbSession dbSession = (DbSession) environment.get(DbSession.class);
            Session session = (Session) environment.get(Session.class);
            IFinFlowJProcessExcep processException = getProcessException(this.executionId, (Session) environment.get(Session.class));

            if (processException == null) {
                return Boolean.valueOf(false);
            }

            ExecutionImpl execution = (ExecutionImpl) dbSession.findExecutionById(processException.getExecutionId());


            String operationName = processException.getOperationName();
            if (AtomicOperation.EXECUTE_EVENT_LISTENER.toString().equals(operationName)) {
                int exceptionPosition = processException.getExceptionPos();

                if (exceptionPosition == 1) {
                    TransitionImpl ti = (TransitionImpl) execution.getActivity().getIncomingTransitions().get(0);

                    execution.setTransition(ti);
                    execution.fire("start", execution.getActivity(), AtomicOperation.TRANSITION_START_ACTIVITY);
                    deleteOrUpdate(execution, dbSession);

                } else if (exceptionPosition == 2) {
                    execution.setTransition(execution.getActivity().findDefaultTransition());

                    execution.fire("end", execution.getActivity(), AtomicOperation.TRANSITION_END_ACTIVITY);
                    deleteOrUpdate(execution, dbSession);
                }


            } else {
                if (AtomicOperation.TRANSITION_START_ACTIVITY.toString().equals(operationName)) {
                    execution.setTransition((TransitionImpl) execution.getActivity().getIncomingTransitions().get(0));

                    execution.setActivity(null);
                } else if (AtomicOperation.TRANSITION_TAKE.toString().equals(operationName)) {
                    String transitionName = processException.getNodeId();
                    String activityName = transitionName.substring(1, transitionName.indexOf(")---->"));
                    ActivityImpl act = execution.getProcessDefinition().getActivity(activityName);
                    TransitionImpl transition = act.findOutgoingTransition(transitionName);
                    transition = transition == null ? act.findDefaultTransition() : transition;
                    execution.setTransition(transition);
                }

                AtomicOperation operation = null;

                if ("Signal".equals(operationName)) {
                    HistoryActivityInstanceImpl haii = (HistoryActivityInstanceImpl) session.createQuery("FROM " + HistoryActivityInstanceImpl.class.getName() + " HAII where HAII.dbid = '" + execution.getHistoryActivityInstanceDbid() + "'").uniqueResult();


                    operation = new Signal(haii.getTransitionName(), null);
                } else {
                    operation = AtomicOperation.parseAtomicOperation(operationName);
                }
                execution.performAtomicOperation(operation);
                deleteOrUpdate(execution, dbSession);
            }

            context = ((EnvironmentImpl) environment).getContext("process-engine");

            Boolean repeat = (Boolean) context.get("repeat");
            if (!repeat.booleanValue()) {
                String executionId = null;
                if (execution.getIsProcessInstance()) {
                    executionId = execution.getDbid();
                } else {
                    executionId = execution.getProcessInstance().getDbid();
                }

                HistoryProcessInstanceImpl hpii = (HistoryProcessInstanceImpl) dbSession.createHistoryProcessInstanceQuery().processId(executionId).uniqueResult();


                hpii.setStatus(Integer.valueOf(processException.getExecutionStatus()));
                dbSession.update(hpii);

                processException.setStatus(1);
                dbSession.update(processException);
            }
        }
        return Boolean.valueOf(true);
    }

    private void deleteOrUpdate(ExecutionImpl execution, DbSession dbSession) {
        HistoryProcessInstanceImpl hpi = (HistoryProcessInstanceImpl) dbSession.get(HistoryProcessInstanceImpl.class, execution.getProcessInstance().getDbid());
        if ((execution.getActivity().getType().equals("2")) && (hpi != null) && (hpi.getStatus().intValue() != 512)) {
            dbSession.delete(execution);
        } else {
            dbSession.update(execution);
        }
    }

    private IFinFlowJProcessExcep getProcessException(String executionId, Session session) {
        StringBuilder hql = new StringBuilder();
        hql.append("from ").append(IFinFlowJProcessExcep.class.getName()).append(" where executionId = '").append(executionId).append("'");


        List<IFinFlowJProcessExcep> resultList = session.createQuery(hql.toString()).list();
        return (resultList != null) && (resultList.size() > 0) ? (IFinFlowJProcessExcep) resultList.get(0) : null;
    }
}