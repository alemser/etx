package com.alx.etx.service;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import reactor.core.publisher.Mono;


/**
 * Interface for the coordination services.
 */
public interface CoordinationService {

	/**
	 * Starts an coordination and returns its id.
	 * 
	 * @return the coordination id;
	 */
	Mono<Coordination> start(CoordinationConfiguration configuration);
	
	/**
	 * Makes a participant join the given coordination.
	 *
	 * @param coordinationId the coordination id.
	 * @param participantName the participant name.
	 *
	 * @return the participant id.
	 */
	Mono<String> join(String coordinationId, String participantName);

	/**
	 * Ends the coordination confirming or canceling the transaction.
	 * 
	 * @param coordinationId the coordination id.
	 */
	Mono<Void> end(String coordinationId);

	/**
	 * Change the participant state in the coordination to execute.
	 *
	 * @param coordinationId the coordination id.
	 * @param participantId the participant id.
	 */
	Mono<Void> execute(String coordinationId, String participantId);

	/**
	 * Change the participant state in the coordination to confirm.
	 *
	 * @param coordinationId the coordination id.
	 * @param participantId the participant id.
	 */
	Mono<Void> confirm(String coordinationId, String participantId);

	/**
	 * Change the participant state in the coordination to cancelled.
	 *
	 * @param coordinationId the coordination id.
	 * @param participantId the participant id.
	 */
	Mono<Void> cancel(String coordinationId, String participantId);

	/**
	 * Returns a coordination by id.
	 * 
	 * @param id the coordination id.
	 * 
	 * @return the coordination id.
	 */
	Mono<Coordination> get(String id);
}
