 package com.ruimin.ifinflow.util;
 
 import java.io.Serializable;
 import java.util.List;
 
 public class HqlUtil implements Serializable
 {
   private static final long serialVersionUID = 1L;
   
   public static String inToOrString(List<?> params, String columnName, boolean isAnd)
   {
     if (params == null) {
       return "";
     }
     List tempList = new java.util.ArrayList();
     for (Object obj : params) {
       if (obj != null) {
         tempList.add(obj);
       }
     }
     if (tempList.size() == 0) {
       return "";
     }
     StringBuffer str = new StringBuffer();
     if (isAnd) {
       str.append(" and (");
     } else
       str.append(" or (");
     int i;
     if (tempList.size() == 1) {
       Object obj = tempList.get(0);
       str.append(columnName);
       str.append("=");
       if ((obj instanceof String)) {
         str.append("'" + obj.toString() + "'");
       } else {
         str.append(obj);
       }
     } else {
       i = 0;
       for (Object obj : tempList) {
         str.append(columnName);
         str.append("=");
         if ((obj instanceof String)) {
           str.append("'" + obj.toString() + "'");
         } else {
           str.append(obj);
         }
         if (i != tempList.size() - 1) {
           str.append(" or ");
         }
         i++;
       }
     }
     str.append(") ");
     return str.toString();
   }
 }
