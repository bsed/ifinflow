 package com.ruimin.ifinflow.model.flowmodel.cache.vo;
 
 import java.io.Serializable;
 
 public class TemplateEventVo implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private int type;
   private String adapterType;
   private String adapterName;
   
   public int getType()
   {
     return this.type;
   }
   
   public void setType(int type) {
     this.type = type;
   }
   
   public String getAdapterType() {
     return this.adapterType;
   }
   
   public void setAdapterType(String adapterType) {
     this.adapterType = adapterType;
   }
   
   public String getAdapterName() {
     return this.adapterName;
   }
   
   public void setAdapterName(String adapterName) {
     this.adapterName = adapterName;
   }
 }

