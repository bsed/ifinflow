package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;


public class UserGroupDeleteCmd
        extends AbstractCommand<Void> {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String groupId;

    public UserGroupDeleteCmd(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public Void execute(Environment environment) {
        IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();

        adapter.deleteUserGroup(this.userId, this.groupId);

        return null;
    }
}