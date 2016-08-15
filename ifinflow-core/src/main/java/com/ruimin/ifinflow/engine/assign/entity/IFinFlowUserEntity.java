package com.ruimin.ifinflow.engine.assign.entity;

import com.ruimin.ifinflow.engine.external.model.IWfStaff;
import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
































public class IFinFlowUserEntity
  implements IWfStaff
{
  private static final long serialVersionUID = -5926715762217440857L;
  public static final String DELETE_FLAG_DEL = "1";
  public static final String DELETE_FLAG_NORMAL = "0";
  private String staffId;
  private String staffName;
  private IWfUnit unit;
  private String deleteFlag;
  private String businessEmail;
  private String phone;
  private Set<IFinFlowUserRoleEntity> staffRoles;
  
  public String getStaffId()
  {
    return this.staffId;
  }
  
  public void setStaffId(String staffId) {
    this.staffId = staffId;
  }
  
  public String getStaffName() {
    return this.staffName;
  }
  
  public void setStaffName(String staffName) {
    this.staffName = staffName;
  }
  
  public IWfUnit getUnit() {
    return this.unit;
  }
  
  public void setUnit(IWfUnit unit) {
    this.unit = unit;
  }
  
  public String getDeleteFlag() {
    return this.deleteFlag;
  }
  
  public void setDeleteFlag(String deleteFlag) {
    this.deleteFlag = deleteFlag;
  }
  
  public String getBusinessEmail() {
    return this.businessEmail;
  }
  
  public void setBusinessEmail(String businessEmail) {
    this.businessEmail = businessEmail;
  }
  
  public String getPhone() {
    return this.phone;
  }
  
  public void setPhone(String phone) {
    this.phone = phone;
  }
  
  public Set<IFinFlowUserRoleEntity> getStaffRoles() {
    return this.staffRoles;
  }
  
  public void setStaffRoles(Set<IFinFlowUserRoleEntity> staffRoles) {
    this.staffRoles = staffRoles;
  }
  
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("IFinFlowUser{");
    buf.append("[staffId = " + this.staffId + "]");
    buf.append("[stafName = " + this.staffName + "]");
    if (this.unit != null) {
      buf.append("[unitId = " + this.unit.getUnitId() + "]");
    }
    buf.append("[deleteFlag = " + this.deleteFlag + "]");
    buf.append("[businessEmail = " + this.businessEmail + "]");
    buf.append("[phone = " + this.phone + "]");
    buf.append("}\n");
    return buf.toString();
  }
  

  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if ((obj instanceof IFinFlowUserEntity)) {
      IFinFlowUserEntity tue = (IFinFlowUserEntity)obj;
      if ((StringUtils.equals(tue.getStaffId(), this.staffId)) && (StringUtils.equals(tue.getStaffName(), this.staffName)))
      {
        return true;
      }
    } else {
      return false;
    }
    return false;
  }
}
