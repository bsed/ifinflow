package com.ruimin.ifinflow.engine.internal.config;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cfg.ConfigurationImpl;
import org.jbpm.pvm.internal.env.BasicEnvironment;

import com.ruimin.ifinflow.engine.assign.OrgDefaultImpl;
import com.ruimin.ifinflow.engine.external.adapter.IBusinessAdapter;
import com.ruimin.ifinflow.engine.external.adapter.IOvertimeAdapter;
import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

public class UserExtendsReference {
    public static IdentityAdapter getIdentityAdapter()
            throws IFinFlowException {

        String adapterName = Configuration.getValue("OrgAdapter");

        IdentityAdapter identityAdapter = null;

        if (StringUtils.isEmpty(adapterName)) {

            throw new IFinFlowException(106003, new Object[0]);
        }

        if (StringUtils.equals("com.ruimin.ifinflow.engine.assign.OrgDefaultImpl", adapterName)) {
            org.jbpm.api.Configuration.getProcessEngine().execute(new Command<IdentityAdapter>() {
                public IdentityAdapter execute(Environment arg0) throws Exception {
                    return new OrgDefaultImpl(BasicEnvironment.getFromCurrent(Session.class));
                }
            });
        }


        try {

            identityAdapter = (IdentityAdapter) Class.forName(adapterName).newInstance();
        } catch (Exception e) {

            throw new IFinFlowException(106004, e);
        }


        return identityAdapter;
    }


    public static IBusinessAdapter getBusinessAdapter(String className, String classType, String nodeId)
            throws IFinFlowException {

        Object obj = new Object();
        try {

            obj = getObjectByName(className, classType);
        } catch (Exception e) {

            throw new IFinFlowException(106006, e, new Object[]{nodeId, className});
        }


        if ((obj instanceof IBusinessAdapter)) {

            return (IBusinessAdapter) obj;
        }

        throw new IFinFlowException(106005, new Object[]{nodeId, className});
    }


    public static IOvertimeAdapter getOvertimeAdapter(String className, String classType, String nodeId)
            throws IFinFlowException {

        Object obj = new Object();
        try {

            obj = getObjectByName(className, classType);
        } catch (Exception e) {

            throw new IFinFlowException(106006, new Object[]{nodeId, className});
        }

        if ((obj instanceof IOvertimeAdapter)) {

            return (IOvertimeAdapter) obj;
        }

        throw new IFinFlowException(106010, new Object[]{nodeId, className});
    }


    public static Object getObjectByName(String className, String classType)
            throws IFinFlowException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Object refer = null;


        if ("1".equals(classType)) {

            refer = ConfigurationImpl.getProcessEngine().get(className);

        } else if ("2".equals(classType)) {

            Class<?> clazz = Class.forName(className);

            refer = clazz.newInstance();
        }
        return refer;
    }
}