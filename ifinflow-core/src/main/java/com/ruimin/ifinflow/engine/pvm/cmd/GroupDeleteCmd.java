package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;


public class GroupDeleteCmd
        extends AbstractCommand<Void> {
    private static final long serialVersionUID = 1L;
    private String id;

    public GroupDeleteCmd(String id) {
        this.id = id;
    }

    public Void execute(Environment environment) {
        if (StringUtils.isEmpty(this.id)) {
            throw new IFinFlowException(105008, new Object[0]);
        }

        IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();

        adapter.deleteGroup(this.id);

        return null;
    }
}