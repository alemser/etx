package com.alx.etx.data;

/**
 * Possible states of a coordination.
 */
public enum CoordinationState {
	/**
	 * Coordination was created but was not started yet.
	 */
	CREATED,
	/**
	 * Coordination was created and started.
	 */
	RUNNING,
	/**
	 * Coordination ended successfully (confirm).
	 */
	ENDED,
	/**
	 * Coordination ended with DEFAULT_TIMEOUT (error state)
	 */
	ENDED_TIMEOUT,
	/**
	 * Coordination ended because participant cancellation (error state)
	 */
	ENDED_CANCELLED,
	/**
	 * Coordination is in a inconsistent state (must be cancelled).
	 */
	INCONSISTENT,

	/**
	 * Means that one of the participants has cancelled.
	 */
	ROLLEDBACK
}
