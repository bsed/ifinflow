package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.flowmodel.Variable;
import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;
import org.jbpm.pvm.internal.history.model.HistoryVariableImpl;

public class ProcessVariableFindCmd extends AbstractCommand<VariableSet> {
	private static final long serialVersionUID = 1L;
	protected String processId;
	protected Set<String> variableNames;

	public ProcessVariableFindCmd(String processId) {
		this.processId = processId;
	}

	public ProcessVariableFindCmd(String processId, Set<String> variableNames) {
		this.processId = processId;
		this.variableNames = variableNames;
	}

	public VariableSet execute(Environment environment) throws Exception {
		if (StringUtils.isEmpty(this.processId)) {
			throw new IFinFlowException(102005, new Object[0]);
		}
		VariableSet vs = new VariableSet();

		Session session = (Session) environment.get(Session.class);

		List<HistoryVariableImpl> varSet = session
				.createCriteria(HistoryVariableImpl.class)
				.add(Restrictions.eq("processId", this.processId)).list();

		if ((varSet == null) || (varSet.isEmpty())) {
			return vs;
		}

		if ((this.variableNames == null) || (this.variableNames.isEmpty())) {
			for (HistoryVariableImpl hisvar : varSet) {
				vs.addVariable(new Variable(hisvar.getVariableName(), hisvar
						.getKind().intValue(), Variable.valueOf(hisvar
						.getKind().intValue(), hisvar.getValue()), hisvar
						.getBizName()));
			}

		} else {
			for (HistoryVariableImpl hisvar : varSet) {
				if (this.variableNames.contains(hisvar.getVariableName())) {
					vs.addVariable(new Variable(hisvar.getVariableName(),
							hisvar.getKind().intValue(), Variable.valueOf(
									hisvar.getKind().intValue(),
									hisvar.getValue()), hisvar.getBizName()));
				}
			}
		}

		return vs;
	}
}
