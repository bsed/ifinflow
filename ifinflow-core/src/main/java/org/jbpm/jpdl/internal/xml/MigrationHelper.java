/*     */ package org.jbpm.jpdl.internal.xml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.jbpm.api.JbpmException;
/*     */ import org.jbpm.api.ProcessDefinition;
/*     */ import org.jbpm.pvm.internal.migration.AbortMigrationHandler;
/*     */ import org.jbpm.pvm.internal.migration.DefaultMigrationHandler;
/*     */ import org.jbpm.pvm.internal.migration.MigrationDescriptor;
/*     */ import org.jbpm.pvm.internal.xml.Parse;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MigrationHelper
/*     */ {
/*     */   public static void parseMigrationDescriptor(Element migrationElement, Parse parse, ProcessDefinition processDefinition)
/*     */   {
/*  21 */     Map<ProcessDefinition, MigrationDescriptor> migrations = (Map)parse.contextMapGet("migrations");
/*  22 */     if (migrations == null) {
/*  23 */       migrations = new HashMap();
/*  24 */       parse.contextMapPut("migrations", migrations);
/*     */     }
/*  26 */     MigrationDescriptor migrationDescriptor = new MigrationDescriptor();
/*  27 */     String action = migrationElement.getAttribute("action");
/*  28 */     if ("end".equals(action)) {
/*  29 */       migrationDescriptor.addMigrationHandlerClassName(AbortMigrationHandler.class.getName());
/*     */     }
/*  31 */     parseMigrationHandlers(migrationElement, migrationDescriptor);
/*  32 */     if (!"end".equals(action)) {
/*  33 */       migrationDescriptor.addMigrationHandlerClassName(DefaultMigrationHandler.class.getName());
/*  34 */       parseActivityMappings(migrationElement, migrationDescriptor);
/*     */     }
/*  36 */     String versions = migrationElement.getAttribute("versions");
/*  37 */     if ((versions != null) && (!"".equals(versions))) {
/*  38 */       addVersionInformation(versions, migrationDescriptor);
/*     */     }
/*  40 */     migrations.put(processDefinition, migrationDescriptor);
/*     */   }
/*     */   
/*     */   private static void addVersionInformation(String versions, MigrationDescriptor migrationDescriptor) {
/*  44 */     boolean isStartInfoRelative = false;
/*  45 */     boolean isEndInfoRelative = false;
/*  46 */     int startValue = -1;
/*  47 */     int endValue = -1;
/*  48 */     if ("*".equals(versions)) {
/*  49 */       migrationDescriptor.setStartVersion(1);
/*  50 */       migrationDescriptor.setEndVersion(Integer.MAX_VALUE);
/*     */     } else {
/*  52 */       int separatorIndex = versions.indexOf("..");
/*  53 */       if (separatorIndex == -1)
/*  54 */         throw new JbpmException("Wrong version information in migrate-instances descriptor.");
/*  55 */       String start = versions.substring(0, separatorIndex).trim();
/*  56 */       int minusIndex = start.indexOf('-');
/*  57 */       if (minusIndex != -1) {
/*  58 */         if (!"x".equals(start.substring(0, minusIndex).trim()))
/*  59 */           throw new JbpmException("Relative version info should be of the form 'x - n'");
/*  60 */         isStartInfoRelative = true;
/*  61 */         start = start.substring(minusIndex + 1).trim();
/*     */       }
/*     */       try {
/*  64 */         startValue = new Integer(start).intValue();
/*  65 */         if (isStartInfoRelative) {
/*  66 */           migrationDescriptor.setStartOffset(startValue);
/*     */         } else {
/*  68 */           migrationDescriptor.setStartVersion(startValue);
/*     */         }
/*     */       } catch (NumberFormatException e) {
/*  71 */         throw new JbpmException("Version information should be numeric.");
/*     */       }
/*  73 */       String end = versions.substring(separatorIndex + 2).trim();
/*  74 */       if ("x".equals(end)) return;
/*  75 */       minusIndex = end.indexOf('-');
/*  76 */       if (minusIndex != -1) {
/*  77 */         if (!"x".equals(end.substring(0, minusIndex).trim()))
/*  78 */           throw new JbpmException("Relative version info should be of the form 'x - n'");
/*  79 */         isEndInfoRelative = true;
/*  80 */         end = end.substring(minusIndex + 1).trim();
/*     */       }
/*     */       try {
/*  83 */         endValue = new Integer(end).intValue();
/*  84 */         if (isEndInfoRelative) {
/*  85 */           migrationDescriptor.setEndOffset(endValue);
/*     */         } else {
/*  87 */           migrationDescriptor.setEndVersion(endValue);
/*     */         }
/*     */       } catch (NumberFormatException e) {
/*  90 */         throw new JbpmException("Version information should be numeric.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void parseActivityMappings(Element migrationElement, MigrationDescriptor migrationDescriptor) {
/*  96 */     NodeList activityMappings = migrationElement.getElementsByTagName("activity-mapping");
/*  97 */     for (int i = 0; i < activityMappings.getLength(); i++) {
/*  98 */       Node activityMapping = activityMappings.item(i);
/*  99 */       if ((activityMapping instanceof Element)) {
/* 100 */         String oldName = ((Element)activityMapping).getAttribute("old-name");
/* 101 */         String newName = ((Element)activityMapping).getAttribute("new-name");
/* 102 */         migrationDescriptor.addMigrationElement("org.jbpm.pvm.internal.migration.activity", oldName, newName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void parseMigrationHandlers(Element migrationElement, MigrationDescriptor migrationDescriptor) {
/* 108 */     NodeList migrationHandlers = migrationElement.getElementsByTagName("migration-handler");
/* 109 */     for (int i = 0; i < migrationHandlers.getLength(); i++) {
/* 110 */       Node migrationHandler = migrationHandlers.item(i);
/* 111 */       if ((migrationHandler instanceof Element)) {
/* 112 */         String className = ((Element)migrationHandler).getAttribute("class");
/* 113 */         if ((className != null) && (!"".equals(className))) {
/* 114 */           migrationDescriptor.addMigrationHandlerClassName(className);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/xml/MigrationHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */