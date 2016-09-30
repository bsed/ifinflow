package org.jbpm.jpdl.internal.activity;

import org.jbpm.api.JbpmException;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.pvm.internal.el.Expression;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.env.ExecutionContext;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.Condition;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class ForEachActivity
        extends JpdlActivity {
    private String variable;
    private Expression collection;
    private static final long serialVersionUID = 1L;

    public void execute(ActivityExecution execution)
            throws Exception {
        execute((ExecutionImpl) execution);
    }

    public void execute(ExecutionImpl execution) {
        Collection<?> collection = evaluateCollection(execution);
        ActivityImpl activity = execution.getActivity();

        ExecutionImpl concurrentRoot;
        if ("active-root".equals(execution.getState())) {
            concurrentRoot = execution;
            execution.setState("inactive-concurrent-root");
            execution.setActivity(null);
        } else if ("active-concurrent".equals(execution.getState())) {
            concurrentRoot = execution.getParent();
            execution.end();
        } else {
            throw new AssertionError(execution.getState());
        }
        TransitionImpl transition = activity.getDefaultOutgoingTransition();
        List<ExecutionImpl> concurrentExecutions = new ArrayList();
        int index = 1;


        ExecutionContext originalContext = null;
        EnvironmentImpl environment = EnvironmentImpl.getCurrent();
        if (environment != null) {
            originalContext = (ExecutionContext) environment.removeContext("execution");
        }
        try {
            for (Object value : collection) {
                ExecutionImpl concurrentExecution = concurrentRoot.createExecution(Integer.toString(index++));

                concurrentExecution.setActivity(activity);
                concurrentExecution.setState("active-concurrent");
                concurrentExecution.createVariable(this.variable, value);


                if (environment != null) {
                    environment.setContext(new ExecutionContext(concurrentExecution));
                }

                Condition condition = transition.getCondition();
                if ((condition == null) || (condition.evaluate(concurrentExecution))) {
                    concurrentExecutions.add(concurrentExecution);
                } else {
                    concurrentExecution.end();
                }
            }
        } finally {
            if (environment != null) {
                environment.setContext(originalContext);
            }
        }

        if (concurrentExecutions.isEmpty()) {
            execution.end();
        } else
            for (ExecutionImpl concurrentExecution : concurrentExecutions) {
                concurrentExecution.take(transition);
                if (concurrentRoot.isEnded())
                    break;
            }
    }

    private Collection<?> evaluateCollection(ExecutionImpl execution) {
        Object value = this.collection.evaluate(execution);
        if ((value instanceof Collection)) {
            return (Collection) value;
        }
        if ((value instanceof Object[])) {
            return Arrays.asList((Object[]) value);
        }
        if ((value instanceof String)) {
            String csv = (String) value;
            return Arrays.asList(csv.split("[,\\s]+"));
        }
        throw new JbpmException("not a collection: " + value);
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public void setCollection(Expression collection) {
        this.collection = collection;
    }
}