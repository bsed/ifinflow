package com.ruimin.ifinflow.engine.pvm.cmd;

import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.AbstractCommand;

public class GroupQueryIdAndNameCountCmd
        extends AbstractCommand<Long> {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;

    public GroupQueryIdAndNameCountCmd(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long execute(Environment environment) {
        IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();
        long count = adapter.queryGroupCount(this.id, this.name);
        return Long.valueOf(count);
    }
}