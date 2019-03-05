package com.alx.etx.service;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import com.alx.etx.data.Participant;
import com.alx.etx.data.ParticipantState;
import com.sun.org.apache.xpath.internal.operations.Bool;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


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
	Mono<Coordination> end(String coordinationId);

	/**
	 * Change the state of a participant of a given coordination to a desired state.
	 * @param coordinationId The id of the coordination.
	 * @param participantId The id of the participant.
	 * @param desiredState The desired state.
	 *
	 * @return true if the state change is successful.
	 */
	Mono<ParticipantState> changeState(String coordinationId, String participantId, ParticipantState desiredState);

	/**
	 * Change the participant state in the coordination to execute.
	 *
	 * @param coordinationId the coordination id.
	 * @param participantId the participant id.
	 */
	Mono<ParticipantState> execute(String coordinationId, String participantId);

	/**
	 * Change the participant state in the coordination to confirm.
	 *
	 * @param coordinationId the coordination id.
	 * @param participantId the participant id.
	 */
	Mono<ParticipantState> confirm(String coordinationId, String participantId);

	/**
	 * Change the participant state in the coordination to cancelled.
	 *
	 * @param coordinationId the coordination id.
	 * @param participantId the participant id.
	 */
	Mono<ParticipantState> cancel(String coordinationId, String participantId);

	/**
	 * Returns a coordination by id.
	 * 
	 * @param id the coordination id.
	 * 
	 * @return the coordination id.
	 */
	Mono<Coordination> get(String id);

	/**
	 * Returns all coordinations.
	 */
	Flux<Coordination> get(Map<String, String> filters);

	/**
	 * Returns coordinations in non consistent state.
	 */
	Flux<Coordination> getNonConsistents();
}
