package com.alx.etx.client;

/**
 * Possible sates of a participant.
 */
public enum ParticipantState {

	/**
	 * Participant joined a coordination.
	 */
	JOINED,

	/**
	 * Participant was already execute.
	 */
	EXECUTED,

	/**
	 * Participant received a confirmation request and is confirm.
	 */
	CONFIRMED,

	/**
	 * Participant received a cancellation request and it cancelled.
	 */
	CANCELLED
}
