package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.util.exception.IFinFlowException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*     */
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
/*     */
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.cmd.CommandService;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.jobexecutor.JobExecutor;
/*     */
import org.jbpm.pvm.internal.model.Condition;
import org.jbpm.pvm.internal.model.ExecutionImpl;
/*     */
import org.jbpm.pvm.internal.model.TmpExecution;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.session.DbSession;


public class MonitorProcessCmd
        implements Command<Void> {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger("ifinflowEngine");

    private TmpExecution tmpExec;

    public MonitorProcessCmd(TmpExecution tmpExec) {
        this.tmpExec = tmpExec;
    }

    public Void execute(Environment environment) throws Exception {
        Session session = (Session) environment.get(Session.class);


        String joinActivityName = null;
        String executionId = this.tmpExec.getExecutionId();
        ExecutionImpl exec = null;


        if (executionId.contains("|JOINED|")) {
            joinActivityName = executionId.substring(executionId.indexOf("|JOINED|") + "|JOINED|".length());
            executionId = executionId.substring(0, executionId.indexOf("|JOINED|"));

            exec = getExecution(executionId, session);

            if (exec != null) {
                log.info(JobExecutor.getWebApplicationId() + "引擎" + Thread.currentThread().getName() + "推进流程：" + executionId + "节点：" + joinActivityName);


                if (!"created".equals(exec.getState())) {
                    ExecutionImpl exet = exec.getSuperProcessExecution();
                    if ((exet != null) && ("created".equals(exet.getState()))) {
                        return null;
                    }
                } else {
                    return null;
                }

                joinSignal(exec, joinActivityName);

                Activity activity = exec.getProcessDefinition().getActivity(joinActivityName);
                Transition transition = activity.getDefaultOutgoingTransition();
                if (transition == null) {
                    throw new IFinFlowException(107009, new Object[]{exec.getActivity().getName()});
                }
                exec.take(transition);


                deleteExecution(exec, session);
            }

        } else if (executionId.contains("|FORK|")) {
            executionId = executionId.substring(0, executionId.indexOf("|FORK|"));

            exec = getExecution(executionId, session);

            if (exec != null) {
                if (StringUtils.equals(exec.getActivityName(), this.tmpExec.getActivityName())) {
                    log.info(JobExecutor.getWebApplicationId() + "引擎" + Thread.currentThread().getName() + "推进流程：" + executionId + "节点：" + exec.getActivityName());


                    Activity activity = exec.getActivity();


                    if ((!activity.getName().startsWith("ForkNode_")) || ("created".equals(exec.getState()))) {
                        return null;
                    }


                    List<Transition> forkingTransitions = new ArrayList();
                    for (Transition transition : activity.getOutgoingTransitions()) {
                        Condition condition = ((TransitionImpl) transition).getCondition();
                        if ((condition == null) || (condition.evaluate(exec))) {
                            forkingTransitions.add(transition);
                        }
                    }
                    ExecutionImpl concurrentRoot;
                    switch (forkingTransitions.size()) {
                        case 0:
                            exec.end();
                            break;

                        case 1:
                            exec.take((Transition) forkingTransitions.get(0));
                            break;


                        default:
                            if ("active-root".equals(exec.getState())) {
                                concurrentRoot = exec;
                                exec.setState("inactive-concurrent-root");
                                exec.setActivity(null);
                            } else if ("active-concurrent".equals(exec.getState())) {
                                concurrentRoot = exec.getParent();
                                exec.end();
                            } else {
                                throw new AssertionError(exec.getState());
                            }

                            Map<Transition, ExecutionImpl> concurrentExecutions = new HashMap();
                            for (Transition transition : forkingTransitions) {
                                ExecutionImpl concurrentExecution = concurrentRoot.createExecution(transition.getName());
                                concurrentExecution.setActivity(activity);
                                concurrentExecution.setState("active-concurrent");
                                concurrentExecutions.put(transition, concurrentExecution);
                            }


                            for (Map.Entry<Transition, ExecutionImpl> entry : concurrentExecutions.entrySet()) {
                                ((ExecutionImpl) entry.getValue()).take((Transition) entry.getKey());
                                if (concurrentRoot.isEnded()) {
                                    break;
                                }
                            }
                    }
                    deleteExecution(exec, session);
                }

            }
        } else {
            exec = getExecution(executionId, session);

            if ((exec != null) && (exec.getActivity() != null) &&
                    (StringUtils.equals(exec.getActivityName(), this.tmpExec.getActivityName()))) {
                log.info("引擎" + JobExecutor.getWebApplicationId() + "中" + Thread.currentThread().getName() + "推进流程实例" + executionId + "中对应活动是:" + exec.getHistoryActivityInstanceDbid() + ";节点id:" + exec.getActivity().getName());


                if (StringUtils.equals(exec.getActivity().getType(), "1")) {
                    exec.start();
                    DbSession dbSession = (DbSession) environment.get(DbSession.class);
                    dbSession.update(exec);
                } else {
                    HistoryActivityInstanceImpl haii = (HistoryActivityInstanceImpl) session.createQuery("FROM " + HistoryActivityInstanceImpl.class.getName() + " HAII where HAII.dbid = '" + exec.getHistoryActivityInstanceDbid() + "'").uniqueResult();


                    if ((StringUtils.isNotBlank(haii.getTransitionName())) && (!haii.getTransitionName().startsWith("to "))) {
                        exec.signal(haii.getTransitionName());
                    } else {
                        exec.signal("");
                    }
                }

                deleteExecution(exec, session);
            }
        }


        return null;
    }


    protected ExecutionImpl getExecution(String executionId, Session session) {
        return (ExecutionImpl) session.createQuery("from " + ExecutionImpl.class.getName() + " ei where ei.executionImplId = '" + executionId + "'").setFirstResult(0).setMaxResults(1).uniqueResult();
    }


    protected void deleteExecution(ExecutionImpl exec, Session session) {
        if ("ended".equals(exec.getState())) {
            session.delete(exec);
            if ((exec.getProcessInstance().isEnded()) && (exec.getParent() != null)) {
                session.delete(exec.getProcessInstance());
            }
        }
    }


    protected void deleteTmpExecution() {
        CommandService commandService = (CommandService) EnvironmentImpl.getCurrent().get("txRequiredCommandService");
        commandService.execute(new UpdateTmpExecution(this.tmpExec));
    }

    protected boolean joinSignal(ExecutionImpl execution, String activityName) {
        Activity activity = execution.getProcessDefinition().getActivity(activityName);
        List<ExecutionImpl> joinedExecutions = getJoinedExecutions(execution, activity);

        if (!StringUtils.equals(execution.getState(), "active-root")) {
            endExecutions(joinedExecutions);

            ExecutionImpl outgoingExecution = null;
            if (execution.getExecutions().isEmpty()) {
                outgoingExecution = execution;
                outgoingExecution.setState("active-root");
            } else {
                outgoingExecution = execution.createExecution();
                outgoingExecution.setState("active-concurrent");
            }

            execution.setActivity(activity, outgoingExecution);


            return true;
        }
        return false;
    }


    protected List<ExecutionImpl> getJoinedExecutions(ExecutionImpl concurrentRoot, Activity activity) {
        List<ExecutionImpl> joinedExecutions = new ArrayList();
        Collection<ExecutionImpl> concurrentExecutions = concurrentRoot.getExecutions();
        for (ExecutionImpl concurrentExecution : concurrentExecutions) {
            if ((null != concurrentExecution) && ("inactive-join".equals(concurrentExecution.getState())) && (concurrentExecution.getActivity() == activity)) {

                joinedExecutions.add(concurrentExecution);
            }
        }
        return joinedExecutions;
    }

    protected boolean isComplete(Activity activity, List<ExecutionImpl> joinedExecutions) {
        int executionsToJoin = activity.getIncomingTransitions().size();
        return joinedExecutions.size() == executionsToJoin;
    }

    protected void endExecutions(List<ExecutionImpl> executions) {
        for (ExecutionImpl execution : executions) {
            execution.end();
        }
    }

    class UpdateTmpExecution
            implements Command<Void> {
        private static final long serialVersionUID = 1L;
        private TmpExecution tmpe;

        public UpdateTmpExecution(TmpExecution tmpe) {
            this.tmpe = tmpe;
        }

        public Void execute(Environment environment) throws Exception {
            Session session = (Session) environment.get(Session.class);

            session.delete(this.tmpe);

            return null;
        }
    }
}