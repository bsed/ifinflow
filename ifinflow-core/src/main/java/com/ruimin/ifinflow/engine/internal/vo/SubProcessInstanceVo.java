package com.ruimin.ifinflow.engine.internal.vo;

import java.io.Serializable;














public class SubProcessInstanceVo
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String handle;
  private String processInstanceId;
  private String version;
  private String packageId;
  private String templateId;
  
  public String getHandle()
  {
    return this.handle;
  }
  
  public void setHandle(String handle) {
    this.handle = handle;
  }
  
  public String getProcessInstanceId() {
    return this.processInstanceId;
  }
  
  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }
  
  public String getVersion() {
    return this.version;
  }
  
  public void setVersion(String version) {
    this.version = version;
  }
  
  public String getTemplateId() { return this.templateId; }
  
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
  
  public String getPackageId() {
    return this.packageId;
  }
  
  public void setPackageId(String packageId) {
    this.packageId = packageId;
  }
}

