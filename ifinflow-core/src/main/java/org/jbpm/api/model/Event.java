package org.jbpm.api.model;

public abstract interface Event {
	public static final String TAKE = "take";
	public static final String START = "start";
	public static final String END = "end";
	public static final String ASSIGN = "assign";
	public static final String REMIND = "remind";
	public static final String CANCEL_PROCESS = "cancelprocess";
	public static final String START_PROCESS = "startprocess";
	public static final String END_PROCESS = "endprocess";
}
