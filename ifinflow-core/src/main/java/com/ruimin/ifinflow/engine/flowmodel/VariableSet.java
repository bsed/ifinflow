package com.ruimin.ifinflow.engine.flowmodel;

import com.ruimin.ifinflow.engine.internal.log.WorkflowLog;
import com.ruimin.ifinflow.util.exception.ExceptionMsgUtil;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

import java.io.Serializable;
import java.util.*;

public class VariableSet
        implements Serializable {
    private static final long serialVersionUID = -218634150762228343L;
    private HashMap<String, Variable> mapVariables;
    protected HashMap<String, Object> valueMap;
    private String name;

    public VariableSet() {
        this.mapVariables = new HashMap();
        this.valueMap = new HashMap();
    }


    public void addVariable(Variable var)
            throws IFinFlowException {
        if (var == null)
            return;
        if (this.mapVariables.containsKey(var.getName())) {
            modifyVariable(var);
        } else {
            this.mapVariables.put(var.getName(), var);
        }
    }


    public Set<String> getNameSet() {
        return this.mapVariables.keySet();
    }


    public Set getValueSet() {
        return this.mapVariables.entrySet();
    }


    public void removeVariable(String varName) {
        this.mapVariables.remove(varName);
    }


    public void addAll(VariableSet varSet)
            throws IFinFlowException {
        if (varSet == null) {
            return;
        }
        Iterator vnItr = varSet.getNameSet().iterator();
        while (vnItr.hasNext()) {
            String varName = (String) vnItr.next();
            addVariable(varSet.getVariable(varName));
        }
    }


    public Variable getVariable(String varName) {
        Object var = this.mapVariables.get(varName);
        if (var == null) {
            return null;
        }
        return (Variable) var;
    }


    public Object getVariableValue(String varName) {
        Variable var = getVariable(varName);
        if (var != null) {
            return var.getValue();
        }
        return null;
    }


    public boolean modifyVariable(Variable var)
            throws IFinFlowException {
        if (var == null)
            return false;
        Variable thisVar = getVariable(var.getName());
        if (thisVar == null)
            return false;
        if (thisVar.getKind() != var.getKind())
            return false;
        thisVar.setValue(var.getValue());
        return true;
    }


    public VariableSet modifyVariables(VariableSet varSet)
            throws IFinFlowException {
        VariableSet notMod = new VariableSet();
        if (varSet == null)
            return notMod;
        Iterator varNameItr = varSet.getNameSet().iterator();
        while (varNameItr.hasNext()) {
            Variable v = varSet.getVariable((String) varNameItr.next());
            if (!modifyVariable(v))
                notMod.addVariable(v);
        }
        return notMod;
    }


    public void clear() {
        this.mapVariables.clear();
    }


    public HashMap<String, Variable> getVariableMap() {
        return this.mapVariables;
    }


    public List<Object[]> getList() {
        List<Object[]> result = null;
        if (this.mapVariables != null) {
            Iterator iter = this.mapVariables.entrySet().iterator();
            result = new ArrayList();
            while (iter.hasNext()) {
                Object[] obj = new Object[4];
                Map.Entry entry = (Map.Entry) iter.next();
                Variable variable = (Variable) entry.getValue();
                obj[0] = variable.getName();
                obj[1] = variable.getBizName();
                obj[2] = variable.getValue();
                obj[3] = Integer.valueOf(variable.getKind());
                result.add(obj);
            }
        }
        return result;
    }

    public HashMap<String, Object> getValueMap() {
        if (this.mapVariables != null) {
            Iterator iter = this.mapVariables.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Variable variable = (Variable) entry.getValue();
                this.valueMap.put(variable.getName(), variable.getValue());
            }
        }
        return this.valueMap;
    }


    public boolean contains(String varName) {
        return this.mapVariables.containsKey(varName);
    }


    public boolean isEmpty() {
        return this.mapVariables.isEmpty();
    }


    public String toString() {
        return this.mapVariables.toString();
    }


    public int size() {
        return this.mapVariables.size();
    }


    private List<Variable> variableList = new ArrayList();


    /**
     * @deprecated
     */
    public void setVariableList(List<Variable> variableList) {
        if (variableList == null) {
            return;
        }
        clear();
        for (Variable variable : variableList) {
            try {
                addVariable(variable);
            } catch (IFinFlowException e) {
                WorkflowLog.error(ExceptionMsgUtil.getStacktrace(e));
            }
        }
        this.variableList = variableList;
    }


    /**
     * @deprecated
     */
    public List<Variable> getVariableList() {
        this.variableList.clear();
        if (this.mapVariables != null) {
            Iterator iter = this.mapVariables.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Variable variable = (Variable) entry.getValue();
                this.variableList.add(variable);
            }
        }
        return this.variableList;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object vs) {
        if (vs == this) {
            return true;
        }

        if (vs == null) {
            return false;
        }

        if (!(vs instanceof VariableSet)) {
            return false;
        }

        VariableSet v = (VariableSet) vs;
        if (v.size() != size()) {
            return false;
        }

        if (this.mapVariables.equals(v.getVariableMap())) {
            return true;
        }
        return false;
    }
}