 package com.ruimin.ifinflow.engine.assign.entity;
 
 import com.ruimin.ifinflow.engine.external.model.IWfRole;
 import com.ruimin.ifinflow.engine.external.model.IWfStaff;
 import java.io.Serializable;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class IFinFlowUserRoleEntity
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private String relationId;
   private IWfStaff staff;
   private IWfRole role;
   
   public String getRelationId()
   {
     return this.relationId;
   }
   
   public void setRelationId(String relationId) {
     this.relationId = relationId;
   }
   
   public IWfStaff getStaff() {
     return this.staff;
   }
   
   public void setStaff(IWfStaff staff) {
     this.staff = staff;
   }
   
   public IWfRole getRole() {
     return this.role;
   }
   
   public void setRole(IWfRole role) {
     this.role = role;
   }
 }
