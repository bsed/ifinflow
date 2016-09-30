package org.jbpm.api.model;

public interface Transition
{
  String getName();
  
  Activity getSource();
  
  Activity getDestination();
}