package com.alx.etx;

/**
 * Possible states of a coordination.
 */
public interface CoordinationState {
	/**
	 * Coordination was created but was not started yet.
	 */
	public static final int CREATED = 0;
	/**
	 * Coordination was created and started.
	 */
	public static final int RUNNING = 1;
	/**
	 * Coordination ended successfully (confirmed).
	 */
	public static final int ENDED = 2;
	/**
	 * Coordination ended with timeout (error state)
	 */
	public static final int ENDED_TIMEOUT = 3;
	/**
	 * Coordination ended because participant cancellation (error state)
	 */
	public static final int ENDED_CANCELLED = 4;
	/**
	 * Coordination is in a inconsistent state (must be cancelled).
	 */
	public static final int INCONSISTENT = 5;
}
