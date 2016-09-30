package org.jbpm.api.model;

import java.util.List;
import java.util.Map;

public interface Activity
{
  String START_TYPE = "1";
  String END_TYPE = "2";
  String TASK_TYPE = "3";
  String AUTO_TYPE = "4";
  String FORK_TYPE = "5";
  String JOIN_TYPE = "6";
  String DECISION_TYPE = "7";
  String SJOIN_TYPE = "8";
  String WAIT_TYPE = "9";
  String TIMER_TYPE = "10";
  String RULE_TYPE = "11";
  String SUBFLOW_TYPE = "12";
  String VIRTUAL_TYPE = "13";
  
  String getName();
  
  List<? extends Transition> getOutgoingTransitions();
  
  Transition getDefaultOutgoingTransition();
  
  Transition getOutgoingTransition(String paramString);
  
  boolean hasOutgoingTransition(String paramString);
  
  boolean hasOutgoingTransitions();
  
  Map<String, ? extends Transition> getOutgoingTransitionsMap();
  
  Transition findOutgoingTransition(String paramString);
  
  List<? extends Transition> getIncomingTransitions();
  
  boolean hasIncomingTransitions();
  
  Activity getParentActivity();
  
  boolean hasActivities();
  
  List<? extends Activity> getActivities();
  
  boolean hasActivity(String paramString);
  
  Activity getActivity(String paramString);
  
  Activity findActivity(String paramString);
  
  Map<String, ? extends Activity> getActivitiesMap();
  
  String getType();
}