/*     */ package org.jbpm.jpdl.internal.assign;
/*     */ 
/*     */ import com.ruimin.ifinflow.engine.external.adapter.AssignCandidate;
/*     */ import com.ruimin.ifinflow.engine.external.adapter.IAssignAdapter;
/*     */ import com.ruimin.ifinflow.engine.external.adapter.IdentityAdapter;
/*     */ import com.ruimin.ifinflow.engine.external.model.IWfGroup;
/*     */ import com.ruimin.ifinflow.engine.external.model.IWfRole;
/*     */ import com.ruimin.ifinflow.engine.external.model.IWfStaff;
/*     */ import com.ruimin.ifinflow.engine.external.model.IWfUnit;
/*     */ import com.ruimin.ifinflow.engine.flowmodel.VariableSet;
/*     */ import com.ruimin.ifinflow.engine.flowmodel.util.VariableUtil;
/*     */ import com.ruimin.ifinflow.engine.flowmodel.vo.TaskVO;
/*     */ import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
/*     */ import com.ruimin.ifinflow.engine.pvm.cmd.WorkloadMinQueryCmd;
/*     */ import com.ruimin.ifinflow.engine.pvm.event.WorkloadUpdate;
/*     */ import com.ruimin.ifinflow.model.flowmodel.cache.vo.NodeVo;
/*     */ import com.ruimin.ifinflow.util.TemplateCacheUtil;
/*     */ import com.ruimin.ifinflow.util.exception.IFinFlowException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.jbpm.api.Configuration;
/*     */ import org.jbpm.api.HistoryService;
/*     */ import org.jbpm.api.ProcessEngine;
/*     */ import org.jbpm.api.history.HistoryProcessInstanceQuery;
/*     */ import org.jbpm.api.history.HistoryTaskQuery;
/*     */ import org.jbpm.pvm.internal.env.EnvironmentImpl;
/*     */ import org.jbpm.pvm.internal.history.HistoryEvent;
/*     */ import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
/*     */ import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
/*     */ import org.jbpm.pvm.internal.model.ActivityImpl;
/*     */ import org.jbpm.pvm.internal.model.ExecutionImpl;
/*     */ import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.model.TransitionImpl;
/*     */ import org.jbpm.pvm.internal.query.HistoryProcessInstanceQueryImpl;
/*     */ import org.jbpm.pvm.internal.session.DbSession;
/*     */ import org.jbpm.pvm.internal.task.TaskDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.task.TaskImpl;
/*     */ import org.jbpm.pvm.internal.type.Variable;
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
/*     */ 
/*     */ 
/*     */ public class AssignTask
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private IdentityAdapter identityAdapter;
/*     */   private String ownerId;
/*     */   private String ownerUnitId;
/*     */   private String unitLevel;
/*     */   private String ownerRoleId;
/*     */   private String ownerGroupId;
/*     */   private String nodeId;
/*     */   private String exceptUserId;
/*     */   private int historyType;
/*     */   private int assignMode;
/*     */   private int participateType;
/*     */   private int paramType;
/*     */   private String paramValue;
/*     */   private String startProcessUserId;
/*     */   
/*     */   public void assignTask(ExecutionImpl execution, TaskDefinitionImpl taskDefinition, TaskImpl task)
/*     */   {
/*  85 */     this.identityAdapter = UserExtendsReference.getIdentityAdapter();
/*     */     
/*  87 */     parseParam(execution);
/*     */     
/*     */ 
/*  90 */     if (1 == this.historyType) {
/*  91 */       this.ownerId = this.startProcessUserId;
/*  92 */       HistoryEvent.fire(new WorkloadUpdate(this.ownerId, this.ownerId, 1));
/*  93 */       return;
/*     */     }
/*     */     
/*  96 */     if (3 == this.historyType) {
/*  97 */       String[] roleIds = getStartProcessRoleIds(execution);
/*  98 */       this.participateType = 2;
/*  99 */       assignByValue(null, execution, taskDefinition, task, roleIds);
/* 100 */       return;
/*     */     }
/* 102 */     if (4 == this.historyType) {
/* 103 */       String orgId = getStartProcessUnitId(execution);
/* 104 */       this.participateType = 3;
/* 105 */       assignByValue(null, execution, taskDefinition, task, new String[] { orgId });
/* 106 */       return;
/*     */     }
/* 108 */     if (2 == this.historyType) {
/* 109 */       this.exceptUserId = getLastTaskExecutor(execution);
/*     */     }
/* 111 */     assignByParamType(execution, taskDefinition, task);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void parseParam(ExecutionImpl execution)
/*     */     throws IFinFlowException
/*     */   {
/* 124 */     NodeVo nodeVO = TemplateCacheUtil.getNodeVo(execution);
/*     */     
/* 126 */     this.assignMode = nodeVO.getAssignMode();
/*     */     
/* 128 */     if (this.assignMode == 0) {
/* 129 */       this.assignMode = 1;
/*     */     }
/* 131 */     this.participateType = nodeVO.getParticipantType();
/* 132 */     this.paramType = nodeVO.getParticipantAssign();
/* 133 */     this.historyType = nodeVO.getParticipantHistory();
/* 134 */     this.paramValue = nodeVO.getResult();
/* 135 */     this.nodeId = execution.getActivityName();
/*     */     
/* 137 */     this.startProcessUserId = getStartProcessUserId(execution);
/* 138 */     if (((0 == this.historyType) || (2 == this.historyType)) && (StringUtils.isEmpty(this.paramValue)))
/*     */     {
/*     */ 
/* 141 */       throw new IFinFlowException(103045, new Object[] { this.nodeId });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void assignByParamType(ExecutionImpl execution, TaskDefinitionImpl taskDefinition, TaskImpl task)
/*     */   {
/* 152 */     String roleFlag = "(岗位)=";
/* 153 */     String orgFlag = "(机构)=";
/* 154 */     String orgLevelFlag = "(机构级别)=";
/*     */     
/*     */ 
/* 157 */     if (1 == this.paramType) {
/* 158 */       String orgId = null;
/* 159 */       String orgLevel = null;
/* 160 */       String roleId = null;
/*     */       
/*     */ 
/* 163 */       if (this.paramValue.indexOf("(机构)=") >= 0) {
/* 164 */         if (this.paramValue.indexOf("(机构级别)=") >= 0) {
/* 165 */           orgId = this.paramValue.substring(this.paramValue.indexOf("(机构)=") + "(机构)=".length(), this.paramValue.indexOf("(机构级别)="));
/*     */         }
/*     */         else {
/* 168 */           orgId = this.paramValue.substring(this.paramValue.indexOf("(机构)=") + "(机构)=".length(), this.paramValue.indexOf("(岗位)="));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */       if (StringUtils.isEmpty(orgId)) {
/* 179 */         orgId = getStartProcessUnitId(execution);
/*     */       }
/*     */       
/*     */ 
/* 183 */       if (this.paramValue.indexOf("(机构级别)=") >= 0) {
/* 184 */         orgLevel = this.paramValue.substring(this.paramValue.indexOf("(机构级别)=") + "(机构级别)=".length(), this.paramValue.indexOf("(岗位)="));
/*     */         
/*     */ 
/* 187 */         if (StringUtils.isEmpty(orgLevel)) {
/* 188 */           throw new IFinFlowException(105011, new Object[] { this.nodeId });
/*     */         }
/* 190 */         if (!StringUtils.isNumeric(orgLevel)) {
/* 191 */           throw new IFinFlowException(105012, new Object[] { this.nodeId });
/*     */         }
/*     */         
/* 194 */         orgId = this.identityAdapter.getUnit(orgId, orgLevel).getUnitId();
/*     */       }
/*     */       
/*     */ 
/* 198 */       if (this.paramValue.indexOf("(岗位)=") >= 0) {
/* 199 */         roleId = this.paramValue.substring(this.paramValue.indexOf("(岗位)=") + "(岗位)=".length());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 205 */       if (this.paramValue.indexOf(";") > 0) {
/* 206 */         String[] str = this.paramValue.split(";");
/* 207 */         for (String s : str) {
/* 208 */           task.addCandidateUser(s);
/*     */         }
/* 210 */         assignByValue(orgId, execution, taskDefinition, task, this.paramValue.split(";"));
/*     */ 
/*     */       }
/* 213 */       else if (StringUtils.isEmpty(roleId)) {
/* 214 */         assignByValue(orgId, execution, taskDefinition, task, new String[] { this.paramValue });
/*     */       } else {
/* 216 */         assignByValue(orgId, execution, taskDefinition, task, new String[] { roleId });
/*     */       }
/*     */       
/*     */     }
/* 220 */     else if (2 == this.paramType)
/*     */     {
/*     */ 
/* 223 */       if (1 == this.participateType) {
/* 224 */         String userId = getVariableValue(execution, this.paramValue);
/* 225 */         assignByValue(null, execution, taskDefinition, task, new String[] { userId });
/*     */       }
/* 227 */       else if (6 == this.participateType) {
/* 228 */         String orgId = getVariableValue(execution, this.paramValue.substring(this.paramValue.indexOf("(机构)=") + "(机构)=".length(), this.paramValue.indexOf("(机构级别)=")));
/*     */         
/* 230 */         String level = getVariableValue(execution, this.paramValue.substring(this.paramValue.indexOf("(机构级别)=") + "(机构级别)=".length(), this.paramValue.indexOf("(岗位)=")));
/*     */         
/*     */ 
/* 233 */         orgId = this.identityAdapter.getUnit(orgId, level).getUnitId();
/* 234 */         String roleId = getVariableValue(execution, this.paramValue.substring(this.paramValue.indexOf("(岗位)=") + "(岗位)=".length()));
/*     */         
/*     */ 
/* 237 */         assignByValue(orgId, execution, taskDefinition, task, new String[] { roleId });
/*     */ 
/*     */       }
/* 240 */       else if ((2 == this.participateType) || (3 == this.participateType)) {
/* 241 */         String orgId = getVariableValue(execution, this.paramValue);
/* 242 */         assignByValue(null, execution, taskDefinition, task, new String[] { orgId });
/*     */       }
/* 244 */       else if (4 == this.participateType) {
/* 245 */         String orgId = getVariableValue(execution, this.paramValue.substring(this.paramValue.indexOf("(机构)=") + "(机构)=".length(), this.paramValue.indexOf("(岗位)=")));
/*     */         
/* 247 */         String roleId = getVariableValue(execution, this.paramValue.substring(this.paramValue.indexOf("(岗位)=") + "(岗位)=".length()));
/*     */         
/*     */ 
/* 250 */         assignByValue(orgId, execution, taskDefinition, task, new String[] { roleId });
/*     */ 
/*     */       }
/* 253 */       else if (5 == this.participateType) {
/* 254 */         String groupId = getVariableValue(execution, this.paramValue);
/* 255 */         assignByValue(null, execution, taskDefinition, task, new String[] { groupId });
/*     */       }
/*     */     }
/* 258 */     else if ((3 == this.paramType) && 
/* 259 */       (!StringUtils.isEmpty(this.paramValue))) {
/* 260 */       IAssignAdapter iAssignAdapter = null;
/*     */       try {
/* 262 */         Class<?> clazz = Class.forName(this.paramValue);
/* 263 */         iAssignAdapter = (IAssignAdapter)clazz.newInstance();
/*     */       } catch (ClassNotFoundException e) {
/* 265 */         throw new IFinFlowException(106001, e, new Object[] { this.nodeId, this.paramValue });
/*     */       } catch (InstantiationException e) {
/* 267 */         throw new IFinFlowException(106002, e, new Object[] { this.nodeId, this.paramValue });
/*     */       } catch (IllegalAccessException e) {
/* 269 */         throw new IFinFlowException(106002, e, new Object[] { this.nodeId, this.paramValue });
/*     */       }
/*     */       
/*     */ 
/* 273 */       VariableSet vs = VariableUtil.getVariableSet(execution);
/*     */       
/*     */ 
/* 276 */       NodeVo nodeVO = TemplateCacheUtil.getNodeVo(execution);
/* 277 */       TaskVO taskvo = new TaskVO();
/* 278 */       taskvo.setNodeName(nodeVO.getName());
/* 279 */       taskvo.setSourceUrl(nodeVO.getUrl());
/* 280 */       taskvo.setTaskId(task.getDbid());
/*     */       
/* 282 */       AssignCandidate assignCandidate = null;
/*     */       try {
/* 284 */         assignCandidate = iAssignAdapter.assign(vs, taskvo);
/*     */       } catch (Exception e) {
/* 286 */         throw new IFinFlowException(106007, e, new Object[] { taskvo.getNodeId(), this.paramValue });
/*     */       }
/* 288 */       assignAdapter(assignCandidate, execution, task);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private String getVariableValue(ExecutionImpl execution, String variableName)
/*     */   {
/* 295 */     String sv = null;
/* 296 */     Variable var = execution.getVariablePrototype(variableName);
/* 297 */     if (var == null) {
/* 298 */       throw new IFinFlowException(104001, new Object[] { variableName, this.nodeId });
/*     */     }
/* 300 */     if (var.getKind().intValue() == 1) {
/* 301 */       sv = (String)var.getValue(execution);
/* 302 */       if ((sv == null) || ("".equals(sv.trim()))) {
/* 303 */         throw new IFinFlowException(104001, new Object[] { var.getKey(), this.nodeId });
/*     */       }
/*     */     } else {
/* 306 */       throw new IFinFlowException(104006, new Object[0]);
/*     */     }
/* 308 */     return sv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void assignByValue(String orgId, ExecutionImpl execution, TaskDefinitionImpl taskDefinition, TaskImpl task, String... value)
/*     */   {
/* 319 */     if (1 == this.participateType)
/*     */     {
/* 321 */       if (1 == this.assignMode) {
/* 322 */         if (value.length == 1) {
/* 323 */           this.ownerId = value[0];
/*     */           
/* 325 */           HistoryEvent.fire(new WorkloadUpdate(this.ownerId, this.ownerId, 1));
/*     */         } else {
/* 327 */           this.ownerId = getMinWorkStaffByIds(value);
/*     */         }
/*     */         
/* 330 */         validateUser(this.ownerId);
/*     */         
/* 332 */         return;
/*     */       }
/*     */       
/* 335 */       if (2 == this.assignMode) {
/* 336 */         if (value.length <= 0) {
/* 337 */           throw new IFinFlowException(105001, new Object[] { this.nodeId });
/*     */         }
/* 339 */         checkStaff(value);
/* 340 */         this.ownerId = Arrays.toString(value);
/* 341 */         if (value.length == 1) {
/* 342 */           this.ownerId = value[0];
/*     */         }
/* 344 */         return;
/*     */       }
/*     */       
/* 347 */       if (3 == this.assignMode) {
/* 348 */         multiAssign(task, execution, taskDefinition, value);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 353 */     else if (3 == this.participateType)
/*     */     {
/*     */ 
/* 356 */       if (1 == this.assignMode) {
/* 357 */         this.ownerId = getMinWorkStaffByOrg(value);
/* 358 */         if (StringUtils.isEmpty(this.ownerId)) {
/* 359 */           throw new IFinFlowException(105001, new Object[] { this.nodeId });
/*     */         }
/* 361 */         return;
/*     */       }
/*     */       
/* 364 */       if (2 == this.assignMode) {
/* 365 */         List<String> ids = this.identityAdapter.getStaffIdsByUnit(value);
/* 366 */         if ((ids != null) && (ids.size() > 0)) {
/* 367 */           this.ownerUnitId = value[0];
/*     */         }
/*     */         else {
/* 370 */           IWfUnit unit = this.identityAdapter.getUnitById(value[0]);
/* 371 */           throw new IFinFlowException(103043, new Object[] { Arrays.toString(value), this.nodeId, unit == null ? "" : unit.getUnitName() });
/*     */         }
/* 373 */         return;
/*     */       }
/*     */       
/*     */ 
/* 377 */       if (3 == this.assignMode) {
/* 378 */         List<String> staffIds = this.identityAdapter.getStaffIdsByUnit(value);
/* 379 */         multiAssign(task, execution, taskDefinition, (String[])staffIds.toArray(new String[staffIds.size()]));
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 385 */     else if ((6 == this.participateType) || (4 == this.participateType))
/*     */     {
/*     */ 
/*     */ 
/* 389 */       if (1 == this.assignMode) {
/* 390 */         this.ownerId = getMinWorkStaffByOrgRole(orgId, value);
/* 391 */         if (StringUtils.isEmpty(this.ownerId)) {
/* 392 */           throw new IFinFlowException(105001, new Object[] { this.nodeId });
/*     */         }
/* 394 */         return;
/*     */       }
/*     */       
/*     */ 
/* 398 */       if (2 == this.assignMode) {
/* 399 */         this.ownerUnitId = orgId;
/* 400 */         this.ownerRoleId = value[0];
/*     */         
/*     */ 
/* 403 */         if (this.identityAdapter.getStaffIdsByRolesWithUnit(this.ownerUnitId, value).isEmpty())
/*     */         {
/* 405 */           IWfUnit unit = this.identityAdapter.getUnitById(this.ownerUnitId);
/* 406 */           IWfRole role = this.identityAdapter.getRoleById(value[0]);
/* 407 */           throw new IFinFlowException(105002, new Object[] { orgId, value[0], this.nodeId, unit == null ? "" : unit.getUnitName(), role == null ? "" : role.getRoleName() });
/*     */         }
/*     */         
/* 410 */         return;
/*     */       }
/*     */       
/*     */ 
/* 414 */       if (3 == this.assignMode) {
/* 415 */         List<String> staffIds = this.identityAdapter.getStaffIdsByRolesWithUnit(orgId, value);
/*     */         
/* 417 */         if ((staffIds == null) || (staffIds.isEmpty())) {
/* 418 */           throw new IFinFlowException(105001, new Object[] { this.nodeId });
/*     */         }
/* 420 */         multiAssign(task, execution, taskDefinition, (String[])staffIds.toArray(new String[staffIds.size()]));
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 426 */     else if (5 == this.participateType)
/*     */     {
/*     */ 
/* 429 */       if (1 == this.assignMode) {
/* 430 */         this.ownerId = getMinWorkStaffByGroup(value[0]);
/* 431 */         if (StringUtils.isEmpty(this.ownerId))
/*     */         {
/* 433 */           IWfGroup group = this.identityAdapter.getGroup(value[0]);
/* 434 */           throw new IFinFlowException(103046, new Object[] { value[0], this.nodeId, group == null ? "" : group.getGroupName() });
/*     */         }
/* 436 */         return;
/*     */       }
/*     */       
/*     */ 
/* 440 */       if (2 == this.assignMode) {
/* 441 */         this.ownerGroupId = value[0];
/*     */         
/*     */ 
/* 444 */         if (this.identityAdapter.getStaffsByGroupId(this.ownerGroupId).isEmpty())
/*     */         {
/* 446 */           IWfGroup group = this.identityAdapter.getGroup(value[0]);
/* 447 */           throw new IFinFlowException(103046, new Object[] { value[0], this.nodeId, group == null ? "" : group.getGroupName() });
/*     */         }
/* 449 */         return;
/*     */       }
/*     */       
/*     */ 
/* 453 */       if (3 == this.assignMode) {
/* 454 */         List<String> staffIds = this.identityAdapter.getStaffIdsByGroupId(value[0]);
/* 455 */         multiAssign(task, execution, taskDefinition, (String[])staffIds.toArray(new String[staffIds.size()]));
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 460 */     else if (2 == this.participateType)
/*     */     {
/*     */ 
/* 463 */       if (1 == this.assignMode) {
/* 464 */         this.ownerId = getMinWorkStaffByRole(value);
/* 465 */         if (StringUtils.isEmpty(this.ownerId)) {
/* 466 */           throw new IFinFlowException(105001, new Object[] { this.nodeId });
/*     */         }
/* 468 */         return;
/*     */       }
/*     */       
/* 471 */       if (2 == this.assignMode) {
/* 472 */         List<String> ids = this.identityAdapter.getStaffIdsByRole(value);
/* 473 */         if ((ids != null) && (ids.size() > 0)) {
/* 474 */           this.ownerRoleId = value[0];
/*     */         }
/*     */         else {
/* 477 */           IWfRole role = this.identityAdapter.getRoleById(value[0]);
/* 478 */           throw new IFinFlowException(103042, new Object[] { Arrays.toString(value), this.nodeId, role == null ? "" : role.getRoleName() });
/*     */         }
/* 480 */         return;
/*     */       }
/*     */       
/* 483 */       if (3 == this.assignMode) {
/* 484 */         List<String> staffIds = this.identityAdapter.getStaffIdsByRole(value);
/* 485 */         multiAssign(task, execution, taskDefinition, (String[])staffIds.toArray(new String[staffIds.size()]));
/*     */         
/* 487 */         return;
/*     */       }
/*     */     }
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
/*     */   private void assignAdapter(AssignCandidate assignCandidate, ExecutionImpl execution, TaskImpl task)
/*     */   {
/* 502 */     Set<String> staffIds = assignCandidate.getStaffIds();
/* 503 */     String roleId = assignCandidate.getRoleId();
/* 504 */     String unitId = assignCandidate.getUnitId();
/* 505 */     String groupId = assignCandidate.getGroupId();
/*     */     
/* 507 */     if (2 == this.assignMode) {
/* 508 */       if ((staffIds != null) && (staffIds.size() > 0)) {
/* 509 */         this.ownerId = Arrays.toString(staffIds.toArray(new String[staffIds.size()]));
/*     */         
/* 511 */         this.ownerId = this.ownerId.replace("[", "").replace("]", "");
/*     */       }
/* 513 */       if (!StringUtils.isEmpty(roleId)) {
/* 514 */         this.ownerRoleId = roleId;
/*     */         
/* 516 */         if (UserExtendsReference.getIdentityAdapter().getStaffsByRoles(new String[] { roleId }).isEmpty())
/*     */         {
/* 518 */           this.ownerRoleId = null;
/*     */         }
/*     */       }
/* 521 */       if (!StringUtils.isEmpty(unitId)) {
/* 522 */         this.ownerUnitId = unitId;
/*     */         
/* 524 */         if (UserExtendsReference.getIdentityAdapter().getStaffsByUintId(unitId).isEmpty())
/*     */         {
/* 526 */           this.ownerUnitId = null;
/*     */         }
/*     */       }
/* 529 */       if (!StringUtils.isEmpty(groupId)) {
/* 530 */         this.ownerGroupId = groupId;
/*     */         
/* 532 */         if (UserExtendsReference.getIdentityAdapter().getStaffsByGroupId(groupId).isEmpty())
/*     */         {
/* 534 */           this.ownerGroupId = null;
/*     */         }
/*     */       }
/*     */       
/* 538 */       if ((this.ownerId == null) && (this.ownerGroupId == null) && (this.ownerUnitId == null) && (this.ownerRoleId == null))
/*     */       {
/* 540 */         throw new IFinFlowException(106009, new Object[0]);
/*     */       }
/* 542 */       return;
/*     */     }
/* 544 */     List<String> staffs = new ArrayList();
/* 545 */     if ((staffIds != null) && (!staffIds.isEmpty())) {
/* 546 */       staffs.addAll(staffIds);
/*     */     }
/* 548 */     IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();
/*     */     
/* 550 */     if (!StringUtils.isEmpty(roleId)) {
/* 551 */       List<String> list = identityAdapter.getStaffIdsByRole(new String[] { roleId });
/* 552 */       staffs.addAll(list);
/*     */     }
/* 554 */     if (!StringUtils.isEmpty(unitId)) {
/* 555 */       List<String> list = identityAdapter.getStaffIdsByUnit(new String[] { unitId });
/* 556 */       staffs.addAll(list);
/*     */     }
/* 558 */     if (!StringUtils.isEmpty(groupId)) {
/* 559 */       List<String> list = identityAdapter.getStaffIdsByGroupId(groupId);
/* 560 */       staffs.addAll(list);
/*     */     }
/*     */     
/* 563 */     if (1 == this.assignMode) {
/* 564 */       if (!staffs.isEmpty()) {
/* 565 */         this.ownerId = getMinWorkStaffByIds((String[])staffs.toArray(new String[staffs.size()]));
/*     */         
/*     */ 
/* 568 */         HistoryEvent.fire(new WorkloadUpdate(this.ownerId, this.ownerId, 1));
/*     */         
/* 570 */         if (StringUtils.isEmpty(this.ownerId)) {
/* 571 */           throw new IFinFlowException(105001, new Object[0]);
/*     */         }
/*     */       }
/* 574 */       return;
/*     */     }
/*     */     
/* 577 */     if (3 == this.assignMode) {
/* 578 */       String staffstr = staffs.toString();
/* 579 */       if (staffstr.length() > 2) {
/* 580 */         staffstr = staffstr.substring(1, staffstr.length() - 1);
/*     */       }
/* 582 */       multiAssign(task, execution, task.getTaskDefinition(), (String[])staffs.toArray(new String[staffs.size()]));
/*     */       
/* 584 */       return;
/*     */     }
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
/*     */   private String getMinWorkStaffByIds(String... ids)
/*     */   {
/* 600 */     List<String> list = Arrays.asList(ids);
/* 601 */     if (list.contains(this.exceptUserId)) {
/* 602 */       list.remove(this.exceptUserId);
/*     */     }
/*     */     
/*     */ 
/* 606 */     List<IWfStaff> staffs = this.identityAdapter.getStaffsByids(ids);
/* 607 */     if ((staffs == null) || (staffs.isEmpty())) {
/* 608 */       throw new IFinFlowException(105013, new Object[0]);
/*     */     }
/*     */     
/* 611 */     if (list.size() == 1) {
/* 612 */       return (String)list.get(0);
/*     */     }
/* 614 */     ids = (String[])list.toArray(new String[list.size()]);
/*     */     
/* 616 */     return (String)Configuration.getProcessEngine().execute(new WorkloadMinQueryCmd(ids));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getMinWorkStaffByRole(String... ids)
/*     */   {
/* 627 */     IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();
/*     */     
/* 629 */     List<IWfStaff> iWfStaffs = identityAdapter.getStaffsByRoles(ids);
/*     */     
/* 631 */     return getMinWorkloadStaffIds(iWfStaffs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getMinWorkStaffByOrg(String... ids)
/*     */   {
/* 641 */     IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();
/*     */     
/* 643 */     List<IWfStaff> iWfStaffs = identityAdapter.getStaffsByUnitIds(ids);
/*     */     
/* 645 */     return getMinWorkloadStaffIds(iWfStaffs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getMinWorkStaffByOrgRole(String orgId, String... roleId)
/*     */   {
/* 655 */     IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();
/*     */     
/* 657 */     List<String> staffs = identityAdapter.getStaffIdsByRolesWithUnit(orgId, roleId);
/*     */     
/*     */ 
/* 660 */     if (staffs.isEmpty()) {
/* 661 */       IWfUnit unit = identityAdapter.getUnitById(orgId);
/* 662 */       IWfRole role = identityAdapter.getRoleById(roleId[0]);
/* 663 */       throw new IFinFlowException(105002, new Object[] { orgId, roleId[0], this.nodeId, unit == null ? "" : unit.getUnitName(), role == null ? "" : role.getRoleName() });
/*     */     }
/*     */     
/*     */ 
/* 667 */     return getStaffByIds(staffs);
/*     */   }
/*     */   
/*     */   private String getMinWorkloadStaffIds(List<IWfStaff> iWfStaffs) {
/* 671 */     if (iWfStaffs.isEmpty()) {
/* 672 */       throw new IFinFlowException(105001, new Object[] { this.nodeId });
/*     */     }
/* 674 */     String[] staffIds = new String[iWfStaffs.size()];
/* 675 */     int i = 0;
/* 676 */     for (IWfStaff staff : iWfStaffs) {
/* 677 */       if (!staff.getStaffId().equals(this.exceptUserId)) {
/* 678 */         staffIds[i] = staff.getStaffId();
/* 679 */         i++;
/*     */       }
/*     */     }
/* 682 */     return (String)Configuration.getProcessEngine().execute(new WorkloadMinQueryCmd(staffIds));
/*     */   }
/*     */   
/*     */   private String getStaffByIds(List<String> staffs)
/*     */   {
/* 687 */     if (staffs.contains(this.exceptUserId)) {
/* 688 */       staffs.remove(this.exceptUserId);
/*     */     }
/*     */     
/* 691 */     return (String)Configuration.getProcessEngine().execute(new WorkloadMinQueryCmd((String[])staffs.toArray(new String[staffs.size()])));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getLastTaskExecutor(ExecutionImpl execution)
/*     */   {
/* 703 */     ProcessDefinitionImpl pd = execution.getProcessDefinition();
/* 704 */     ActivityImpl act = pd.getActivity(this.nodeId);
/* 705 */     TransitionImpl tran = (TransitionImpl)act.getIncomingTransitions().get(0);
/*     */     
/* 707 */     ActivityImpl source = tran.getSource();
/* 708 */     if ("3".equals(source.getType())) {
/* 709 */       HistoryTaskImpl task = (HistoryTaskImpl)((HistoryService)Configuration.getProcessEngine().get(HistoryService.class)).createHistoryTaskQuery().executionId(execution.getExecutionImplId()).nodeId(source.getName()).orderDesc("createTime").page(0, 1).uniqueResult();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 715 */       if (task != null) {
/* 716 */         return task.getExecutorId();
/*     */       }
/*     */     }
/* 719 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void multiAssign(TaskImpl task, ExecutionImpl execution, TaskDefinitionImpl taskDefinition, String... ids)
/*     */   {
/* 730 */     if ((ids != null) && (ids.length > 0)) {
/* 731 */       int i = 0;
/* 732 */       for (String participant : ids) {
/* 733 */         TaskImpl subTask = task.createSubTask();
/* 734 */         subTask.setName(task.getName() + " sub " + i++);
/* 735 */         subTask.setAssigneeOnly(participant);
/* 736 */         subTask.setTaskDefinition(taskDefinition);
/* 737 */         subTask.setExecution(execution);
/* 738 */         subTask.setProcessInstance(execution.getProcessInstance());
/* 739 */         subTask.setPackageId(task.getPackageId());
/* 740 */         subTask.setTemplateId(task.getTemplateId());
/* 741 */         subTask.setTemplateVersion(task.getTemplateVersion());
/* 742 */         subTask.setPriority(task.getPriority());
/* 743 */         subTask.setOwnerId(participant);
/* 744 */         subTask.setAssignMode(task.getAssignMode());
/* 745 */         subTask.setParticipantMode(task.getParticipantMode());
/* 746 */         subTask.setUserExtString1(task.getUserExtString1());
/* 747 */         subTask.setUserExtString2(task.getUserExtString2());
/* 748 */         subTask.setUserExtString3(task.getUserExtString3());
/* 749 */         subTask.setUserExtString4(task.getUserExtString4());
/* 750 */         subTask.setUserExtString5(task.getUserExtString5());
/* 751 */         subTask.setUserExtString6(task.getUserExtString6());
/* 752 */         subTask.setUserExtString7(task.getUserExtString7());
/* 753 */         subTask.setUserExtString8(task.getUserExtString8());
/* 754 */         subTask.setFormResourceName(task.getFormResourceName());
/*     */         
/*     */ 
/* 757 */         HistoryEvent.fire(new WorkloadUpdate(participant, participant, 1));
/*     */       }
/*     */     } else {
/* 760 */       throw new IFinFlowException(103048, new Object[] { task.getActivityName() });
/*     */     }
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
/*     */   private void checkStaff(String[] ids)
/*     */   {
/* 797 */     IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();
/* 798 */     List<IWfStaff> ls = identityAdapter.getStaffsByids(ids);
/* 799 */     int num = ids.length - ls.size();
/* 800 */     if (num != 0) {
/* 801 */       List<String> idList = Arrays.asList(ids);
/* 802 */       for (IWfStaff st : ls) {
/* 803 */         if (idList.contains(st.getStaffId())) {
/* 804 */           idList.remove(st.getStaffId());
/*     */         }
/*     */       }
/* 807 */       throw new IFinFlowException(103041, new Object[] { this.nodeId, Arrays.toString(idList.toArray(new String[num])) });
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getStartProcessUserId(ExecutionImpl execution) {
/* 812 */     return getHistoryProcess(execution).getInitiatorId();
/*     */   }
/*     */   
/*     */   private static String[] getStartProcessRoleIds(ExecutionImpl execution) {
/* 816 */     String user = getHistoryProcess(execution).getInitiatorId();
/* 817 */     List<IWfRole> roles = UserExtendsReference.getIdentityAdapter().getRolesByStaffId(user);
/* 818 */     if ((roles == null) || (roles.isEmpty())) {
/* 819 */       return null;
/*     */     }
/* 821 */     String[] roleIds = new String[roles.size()];
/* 822 */     for (int i = 0; i < roles.size(); i++) {
/* 823 */       roleIds[i] = ((IWfRole)roles.get(i)).getRoleId();
/*     */     }
/* 825 */     return roleIds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getStartProcessUnitId(ExecutionImpl execution)
/*     */   {
/* 837 */     String user = getHistoryProcess(execution).getInitiatorId();
/* 838 */     IWfUnit unit = UserExtendsReference.getIdentityAdapter().getUnitByStaffId(user);
/* 839 */     if (unit == null) {
/* 840 */       return null;
/*     */     }
/* 842 */     return unit.getUnitId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static HistoryProcessInstanceImpl getHistoryProcess(ExecutionImpl execution)
/*     */   {
/* 852 */     DbSession dbSession = (DbSession)EnvironmentImpl.getFromCurrent(DbSession.class);
/*     */     
/* 854 */     HistoryProcessInstanceImpl hisProcess = (HistoryProcessInstanceImpl)dbSession.createHistoryProcessInstanceQuery().processId(execution.getProcessInstance().getDbid()).uniqueResult();
/*     */     
/*     */ 
/*     */ 
/* 858 */     return hisProcess;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void validateUser(String userId)
/*     */   {
/* 865 */     if (StringUtils.isEmpty(userId)) {
/* 866 */       throw new IFinFlowException(105001, new Object[] { this.nodeId });
/*     */     }
/*     */     
/* 869 */     IdentityAdapter identityAdapter = UserExtendsReference.getIdentityAdapter();
/*     */     
/* 871 */     IWfStaff sstaff = identityAdapter.getStaffById(userId);
/* 872 */     if (sstaff == null) {
/* 873 */       throw new IFinFlowException(103041, new Object[] { this.nodeId, userId });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String getMinWorkStaffByGroup(String groupId)
/*     */   {
/* 881 */     List<IWfStaff> staffs = this.identityAdapter.getStaffsByGroupId(groupId);
/*     */     
/* 883 */     return getMinWorkloadStaffIds(staffs);
/*     */   }
/*     */   
/*     */   public String getUnitLevel()
/*     */   {
/* 888 */     return this.unitLevel;
/*     */   }
/*     */   
/*     */   public void setOwnerId(String ownerId)
/*     */   {
/* 893 */     this.ownerId = ownerId;
/*     */   }
/*     */   
/* 896 */   public String getOwnerId() { return this.ownerId; }
/*     */   
/*     */   public String getOwnerUnitId()
/*     */   {
/* 900 */     return this.ownerUnitId;
/*     */   }
/*     */   
/*     */   public String getOwnerRoleId() {
/* 904 */     return this.ownerRoleId;
/*     */   }
/*     */   
/*     */   public String getOwnerGroupId() {
/* 908 */     return this.ownerGroupId;
/*     */   }
/*     */   
/*     */   public int getAssignMode() {
/* 912 */     return this.assignMode;
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/assign/AssignTask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */