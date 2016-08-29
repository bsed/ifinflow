 package com.ruimin.ifinflow.engine.internal.log;
 
 
 
 
 
 
 
 public enum LogCategory
 {
   SYSTEM("L1"),  LOGIN("L1.1"),  UNITMODEL("L1.2"),  RESOURCEMODEL("L1.3"),  AUTH("L1.4"), 
   APPLICATION("L2"),  API("L2.1"),  AUTONODE("L2.2"),  TIMERNODE("L2.3"), 
   RUNLOG("L3"),  EXCEPTION("L3.1"),  OVERTIME("L3.2"),  SYSTEMTIMER("L3.3");
   
   private String id;
   
   private LogCategory(String id)
   {
     this.id = id;
   }
   
   public String getId() {
     return this.id;
   }
 }
