/*     */ package com.ruimin.ifinflow.engine.assign;
/*     */ 
/*     */ import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
/*     */ import com.ruimin.ifinflow.engine.external.model.IWfRole;
/*     */ import com.ruimin.ifinflow.engine.external.model.IWfStaff;
/*     */ import com.ruimin.ifinflow.engine.external.model.IWfUnit;
/*     */ import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.hibernate.Session;
/*     */ import org.jbpm.api.identity.Group;
/*     */ import org.jbpm.api.identity.User;
/*     */ import org.jbpm.pvm.internal.identity.impl.GroupImpl;
/*     */ import org.jbpm.pvm.internal.identity.impl.UserImpl;
/*     */ import org.jbpm.pvm.internal.identity.spi.IdentitySession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TopIdentitySessionImpl
/*     */   implements IdentitySession
/*     */ {
/*     */   protected Session session;
/*     */   private IdentityAdapter identityAdapter;
/*     */   
/*     */   public TopIdentitySessionImpl()
/*     */   {
/*  36 */     this.identityAdapter = UserExtendsReference.getIdentityAdapter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public User findUserById(String userId)
/*     */   {
/*  43 */     IWfStaff staff = this.identityAdapter.getStaffById(userId);
/*     */     
/*     */ 
/*  46 */     return staff2User(new UserImpl(), staff);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<User> findUsersById(String... userIds)
/*     */   {
/*  53 */     List<IWfStaff> list = this.identityAdapter.getStaffsByids(userIds);
/*  54 */     return staffList2Users(list);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<User> findUsers()
/*     */   {
/*  62 */     return new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<User> findUsersByGroup(String groupId)
/*     */   {
/*  69 */     List<User> users = new ArrayList();
/*  70 */     if (groupId.startsWith("UNIT_")) {
/*  71 */       users = staffList2Users(this.identityAdapter.getStaffsByUintId(groupId.replace("UNIT_", "")));
/*     */ 
/*     */     }
/*  74 */     else if (groupId.startsWith("ROLE_")) {
/*  75 */       users = staffList2Users(this.identityAdapter.getStaffsByRole(groupId.replace("ROLE_", "")));
/*     */     }
/*     */     
/*     */ 
/*  79 */     return users;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Group findGroupById(String groupId)
/*     */   {
/*  86 */     if (groupId.startsWith("UNIT_")) {
/*  87 */       return unit2Group(new GroupImpl(), this.identityAdapter.getUnitById(groupId.replace("UNIT_", "")));
/*     */     }
/*     */     
/*  90 */     if (groupId.startsWith("ROLE_")) {
/*  91 */       return role2Group(new GroupImpl(), this.identityAdapter.getRoleById(groupId.replace("ROLE_", "")));
/*     */     }
/*     */     
/*     */ 
/*  95 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Group> findGroupsByUserAndGroupType(String userId, String groupType)
/*     */   {
/* 103 */     return findGroupsByUser(userId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<Group> findGroupsByUser(String userId)
/*     */   {
/* 110 */     List<Group> list = new ArrayList();
/*     */     
/* 112 */     list.add(unit2Group(new GroupImpl(), this.identityAdapter.getUnitByStaffId(userId)));
/*     */     
/*     */ 
/* 115 */     list.addAll(roleList2GroupList(this.identityAdapter.getRolesByStaffId(userId)));
/*     */     
/* 117 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<User> staffList2Users(List<IWfStaff> list)
/*     */   {
/* 127 */     List<User> userList = new ArrayList();
/* 128 */     UserImpl user = null;
/* 129 */     for (IWfStaff staff : list) {
/* 130 */       userList.add(staff2User(user, staff));
/*     */     }
/* 132 */     return userList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private User staff2User(UserImpl user, IWfStaff staff)
/*     */   {
/* 142 */     if (user == null) {
/* 143 */       user = new UserImpl();
/*     */     }
/* 145 */     if (staff != null) {
/* 146 */       user.setId(staff.getStaffId());
/* 147 */       user.setBusinessEmail(staff.getBusinessEmail());
/* 148 */       user.setGivenName(staff.getStaffName());
/*     */     }
/* 150 */     return user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Group unit2Group(GroupImpl group, IWfUnit iWfUnit)
/*     */   {
/* 173 */     if (group == null) {
/* 174 */       group = new GroupImpl();
/*     */     }
/* 176 */     if (iWfUnit != null) {
/* 177 */       group.setId(iWfUnit.getUnitId());
/* 178 */       group.setName(iWfUnit.getUnitName());
/* 179 */       group.setType(iWfUnit.getUnitKind());
/*     */     }
/* 181 */     return group;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<Group> roleList2GroupList(List<IWfRole> list2)
/*     */   {
/* 191 */     List<Group> list = new ArrayList();
/* 192 */     GroupImpl group = null;
/* 193 */     for (IWfRole role : list2) {
/* 194 */       list.add(role2Group(group, role));
/*     */     }
/* 196 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Group role2Group(GroupImpl group, IWfRole iWfRole)
/*     */   {
/* 207 */     if (group == null) {
/* 208 */       group = new GroupImpl();
/*     */     }
/* 210 */     if (iWfRole != null) {
/* 211 */       group.setId(iWfRole.getRoleId());
/* 212 */       group.setName(iWfRole.getRoleName());
/* 213 */       group.setType("ROLE");
/*     */     }
/* 215 */     return group;
/*     */   }
/*     */   
/*     */   public void setSession(Session session) {
/* 219 */     this.session = session;
/*     */   }
/*     */   
/*     */ 
/*     */   public String createUser(String userId, String givenName, String familyName, String businessEmail)
/*     */   {
/* 225 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void deleteUser(String userId) {}
/*     */   
/*     */ 
/*     */   public String createGroup(String groupName, String groupType, String parentGroupId)
/*     */   {
/* 234 */     return null;
/*     */   }
/*     */   
/*     */   public void deleteGroup(String groupId) {}
/*     */   
/*     */   public void createMembership(String userId, String groupId, String role) {}
/*     */   
/*     */   public void deleteMembership(String userId, String groupId, String role) {}
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/com/ruimin/ifinflow/engine/assign/TopIdentitySessionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */