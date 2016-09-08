package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.external.model.IWfGroup;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;

import java.util.List;


public class GroupSaveCmd
        extends AbstractCommand<IWfGroup> {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private List<String> userIds;

    public GroupSaveCmd(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public GroupSaveCmd(String id, String name, List<String> userIds) {
        this.id = id;
        this.name = name;
        this.userIds = userIds;
    }


    public IWfGroup execute(Environment environment) {
        if (StringUtils.isEmpty(this.id)) {
            throw new IFinFlowException(105008, new Object[0]);
        }

        IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();

        IWfGroup group = adapter.getGroup(this.id);
        if (group != null) {
            throw new IFinFlowException(105007, new Object[]{this.id});
        }

        adapter.createGroup(this.id, this.name);

        if (this.userIds != null) {
            for (String staffId : this.userIds) {
                if (staffId != null) {
                    adapter.createUserGroup(staffId, this.id);
                }
            }
        }

        return group;
    }
}