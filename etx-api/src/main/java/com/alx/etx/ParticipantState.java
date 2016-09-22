package com.alx.etx;

/**
 * Possible sates of a participant.
 */
public interface ParticipantState {

	/**
	 * Participant joined a coordination.
	 */
	public static final int JOINED = 0;
	
	/**
	 * Participant was already executed.
	 */
	public static final int EXECUTED = 1;
	
	/**
	 * Participant received a confirmation request and is confirmed.
	 */
	public static final int CONFIRMED = 2;
	
	/**
	 * Participant received a cancellation request and it cancelled.
	 */
	public static final int CANCELLED = 3;
	
}
