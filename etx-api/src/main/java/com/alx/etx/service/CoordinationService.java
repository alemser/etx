package com.alx.etx.service;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import com.alx.etx.data.Participant;
import reactor.core.publisher.Flux;
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
	 * Add a executed participant to the coordination.
	 *
	 * @param coordinationId the coordination id.
	 * @param participant the participant.
	 *
	 * @return the participant.
	 */
	Mono<Participant> join(String coordinationId, Participant participant);


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

	/**
	 * Returns a coordination by id.
	 *
	 * @param id the coordination id.
	 *
	 * @return the coordination id.
	 */
	Flux<Coordination> getNonConsistents();
}
