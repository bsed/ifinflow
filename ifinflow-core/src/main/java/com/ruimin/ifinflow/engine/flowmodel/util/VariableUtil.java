
package com.ruimin.ifinflow.engine.flowmodel.util;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.pvm.internal.model.ExecutionImpl;

import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
import com.ruimin.ifinflow.util.StringHelper;


public class VariableUtil {
    /*     */
    public static List<Object> getListData(String var) {

        return StringHelper.toList(var);

    }

    /*     */


    public static Map<String, Object> getMapData(String var) {

        return StringHelper.toMap(var);

    }

    /*     */


    public static int getVariableKind(Object varValue) {

        if (varValue == null) {

            return 0;

        }

        if ((varValue instanceof List)) {

            return 9;

        }

        if ((varValue instanceof Map)) {

            return 10;

        }

        if ((varValue instanceof String)) {

            return 1;

        }

        if ((varValue instanceof Integer)) {

            return 2;

        }

        if ((varValue instanceof Long)) {

            return 4;

        }

        if ((varValue instanceof Float)) {

            return 3;

        }

        if ((varValue instanceof Double)) {

            return 5;

        }

        if ((varValue instanceof Date)) {

            return 7;

        }

        if ((varValue instanceof Boolean)) {

            return 8;

        }

        if ((varValue instanceof Character)) {

            return 6;

        }

        return 0;

    }

    /*     */


    private static boolean havingMethod(Method[] methods, String methodName) {

        if (methodName == null) return false;

        for (int i = 0; i < methods.length; i++) {

            Method m = methods[i];

            String name = m.getName();

            if (name.equals(methodName))
                return true;

        }

        return false;

    }

    /*     */


    private static boolean isBeanMethod(String methodName) {

        boolean flag;


        if (methodName.startsWith("get")) {

            flag = true;
        } else {

            if (methodName.startsWith("set")) {

                flag = true;
            } else {

                if (methodName.startsWith("is")) {

                    flag = true;

                } else
                    flag = false;

            }
        }

        return flag;

    }

    /*     */

    private static int getKindByFieldClassType(Class type) {

        int kind = 0;

        if (type == String.class) {

            kind = 1;

        } else if ((type == Integer.class) || (type == Integer.TYPE)) {

            kind = 2;

        } else if ((type == Long.class) || (type == Long.TYPE)) {

            kind = 4;

        } else if ((type == Float.class) || (type == Float.TYPE)) {

            kind = 3;

        } else if ((type == Double.class) || (type == Double.TYPE)) {

            kind = 5;

        } else if (type == Date.class) {

            kind = 7;

        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {

            kind = 8;

        } else if (type == ArrayList.class) {

            kind = 9;

        } else if (type == HashMap.class)
            kind = 10;

        return kind;

    }

    /*     */

    public static Object getDefaultValueByKind(int kind) {

        Object valueObj = null;

        switch (kind) {

            case 8:

                valueObj = new Boolean(false);

                break;


            case 7:

                valueObj = new Date();

                break;


            case 5:

                valueObj = new Double(0.0D);

                break;


            case 3:

                valueObj = new Float(0.0F);

                break;


            case 2:

                valueObj = new Integer(0);

                break;


            case 4:

                valueObj = new Long(0L);

                break;


            case 6:

                valueObj = new Character('\000');

                break;


            case 9:

                valueObj = new ArrayList();

                break;


            case 10:

                valueObj = new HashMap();

        }


        return valueObj;

    }

    /*     */

    public static VariableSet getVariableSet(ExecutionImpl execution) {

        VariableSet vs = new VariableSet();

        Map<String, org.jbpm.pvm.internal.type.Variable> map = execution.getIsProcessInstance() ? execution.getVariablesPrototype() : execution.getProcessInstance().getVariablesPrototype();


        Set<Map.Entry<String, org.jbpm.pvm.internal.type.Variable>> set = map.entrySet();


        org.jbpm.pvm.internal.type.Variable var = null;

        for (Map.Entry<String, org.jbpm.pvm.internal.type.Variable> entry : set) {

            var = (org.jbpm.pvm.internal.type.Variable) entry.getValue();

            vs.addVariable(new com.ruimin.ifinflow.engine.flowmodel.Variable(var.getKey(), var.getKind().intValue(), var.getValue(execution), var.getBizName()));

        }


        return vs;

    }

}