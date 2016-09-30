package com.ruimin.ifinflow.model.flowmodel.xml;

import java.io.Serializable;









































public class Route
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String handle;
  private String name;
  private String remark;
  private String templateHandle;
  private String fromHandle;
  private String toHandle;
  private String exrpress;
  private String isDefault;
  private transient String toName;
  
  public String getHandle()
  {
    return this.handle;
  }
  
  public void setHandle(String handle) {
    this.handle = handle;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getRemark() {
    return this.remark;
  }
  
  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  public String getTemplateHandle() {
    return this.templateHandle;
  }
  
  public void setTemplateHandle(String templateHandle) {
    this.templateHandle = templateHandle;
  }
  
  public String getFromHandle() {
    return this.fromHandle;
  }
  
  public void setFromHandle(String fromHandle) {
    this.fromHandle = fromHandle;
  }
  
  public String getToHandle() {
    return this.toHandle;
  }
  
  public void setToHandle(String toHandle) {
    this.toHandle = toHandle;
  }
  
  public String getExrpress() {
    return this.exrpress;
  }
  
  public void setExrpress(String exrpress) {
    this.exrpress = exrpress;
  }
  
  public String getIsDefault() {
    return this.isDefault;
  }
  
  public void setIsDefault(String isDefault) {
    this.isDefault = isDefault;
  }
  
  public String getToName() {
    return this.toName;
  }
  
  public void setToName(String toName) {
    this.toName = toName;
  }
}
