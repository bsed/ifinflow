 package com.ruimin.ifinflow.engine.pvm.cmd;
 
 import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
 import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
 import com.ruimin.ifinflow.util.exception.IFinFlowException;
 import java.util.Collection;
	import java.util.List;
 import org.apache.commons.lang.StringUtils;
 import org.jbpm.api.cmd.Environment;
 import org.jbpm.pvm.internal.cmd.AbstractCommand;
 
 
 
 
 
 
 
 
 
 
 public class GroupUpdateCmd
   extends AbstractCommand<Void>
 {
   private static final long serialVersionUID = 1L;
   private String id;
   private String name;
   private List<String> userIds;
   
   public GroupUpdateCmd(String id, String name)
   {
     this.id = id;
     this.name = name;
   }
   
   public GroupUpdateCmd(String id, String name, List<String> userIds)
   {
     this.id = id;
     this.name = name;
     this.userIds = userIds;
   }
   
 
   public Void execute(Environment environment)
   {
     if (StringUtils.isEmpty(this.id)) {
       throw new IFinFlowException(105008, new Object[0]);
     }
     
     IdentityAdapter adapter = UserExtendsReference.getIdentityAdapter();
     
     adapter.deleteUserGroupByGroupId(this.id);
     
     if (this.userIds != null) {
       for (String staffId : this.userIds) {
         if (staffId != null) {
           adapter.createUserGroup(staffId, this.id);
         }
       }
     }
     
     adapter.updateGroup(this.id, this.name);
     
     return null;
   }
 }

