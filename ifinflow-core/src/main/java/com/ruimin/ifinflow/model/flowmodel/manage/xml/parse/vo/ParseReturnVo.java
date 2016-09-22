 package com.ruimin.ifinflow.model.flowmodel.manage.xml.parse.vo;
 
 import java.io.Serializable;
 
 
 
 public class ParseReturnVo
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private String packageId;
   private String jpdl;
   
   public String getPackageId()
   {
     return this.packageId;
   }
   
   public void setPackageId(String packageId) {
     this.packageId = packageId;
   }
   
   public String getJpdl() {
     return this.jpdl;
   }
   
   public void setJpdl(String jpdl) {
     this.jpdl = jpdl;
   }
 }
