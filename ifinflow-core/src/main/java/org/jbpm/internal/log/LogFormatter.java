 package org.jbpm.internal.log;
 
 import java.io.PrintWriter;
 import java.io.StringWriter;
 import java.text.DateFormat;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Formatter;
 import java.util.logging.Level;
 import java.util.logging.LogRecord;
 
 public class LogFormatter extends Formatter
 {
   static final String NEWLINE = System.getProperty("line.separator");
   static final DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss,SSS");
   static final Map<Level, String> levels = new HashMap();
   private static Map<Integer, Integer> indentations = new HashMap();
   
   static
   {
     levels.put(Level.ALL, "ALL");
     levels.put(Level.CONFIG, "CFG");
     levels.put(Level.FINE, "FIN");
     levels.put(Level.FINER, "FNR");
     levels.put(Level.FINEST, "FST");
     levels.put(Level.INFO, "INF");
     levels.put(Level.OFF, "OFF");
     levels.put(Level.SEVERE, "SEV");
     levels.put(Level.WARNING, "WRN");
   }
   
   public String format(LogRecord logRecord) {
     StringWriter msg = new StringWriter();
     if (logRecord.getThrown() != null) {
       msg.append("### EXCEPTION ###########################################");
       msg.append(NEWLINE);
     }
     msg.append(dateTimeFormat.format(new Date()));
     msg.append(" ");
     msg.append((CharSequence)levels.get(logRecord.getLevel()));
     msg.append(" ");
     
     int threadId = logRecord.getThreadID();
     for (int i = 0; i < getIndentation(threadId); i++) {
       msg.append("  ");
     }
     
     msg.append("| [");
     
     String loggerName = logRecord.getLoggerName();
     int dotIndex = loggerName.lastIndexOf('.');
     if (dotIndex != -1) {
       loggerName = loggerName.substring(dotIndex + 1);
     }
     msg.append(loggerName);
     
     msg.append("] ");
     
     msg.append(logRecord.getMessage());
     if (logRecord.getThrown() != null) {
       msg.append(NEWLINE);
       logRecord.getThrown().printStackTrace(new PrintWriter(msg));
       msg.append("### EXCEPTION ###########################################");
     }
     msg.append(NEWLINE);
     return msg.toString();
   }
   
   private int getIndentation(int threadId) {
     Integer indentation = (Integer)indentations.get(Integer.valueOf(threadId));
     if (indentation == null) {
       indentation = Integer.valueOf(indentations.size());
       indentations.put(Integer.valueOf(threadId), indentation);
     }
     return indentation.intValue();
   }
   
   public static void resetIndentation() {
     indentations = new HashMap();
   }
 }
