package com.alx.etx.service;

import com.alx.etx.Coordination;


/**
 * Interface for the coordination services.
 */
public interface CoordinationService {

	/**
	 * Starts an coordination and returns its id.
	 * 
	 * @return the coordination id;
	 */
	String start();
	
	/**
	 * Makes a participant join the given coordination.
	 * 
	 * @param coordinationId the coordination id.
	 * @param participantName the participant name.
	 * 
	 * @return the participant id.
	 */
	String join(String coordinationId, String participantName);

	/**
	 * Ends the coordination confirming or canceling the transaction.
	 * 
	 * @param coordinationId the coordination id.
	 */
	void end(String coordinationId);
	
	/**
	 * Change the participant state in the coordination.
	 * 
	 * @param coordinationId the coordination id.
	 * @param participantId the participant id.
	 * @param state the desired state {@see ParticipantState}
	 */
	void changeParticipatState(String coordinationId, String participantId, int state);
	
	/**
	 * Change the coordination state.
	 * 
	 * @param coordinationId the coordination id.
	 * @param state the coordination state {@see CoordinationState}.
	 */
	void changeCoordinationState(String coordinationId, int state);
	
	/**
	 * Returns a coordination by id.
	 * 
	 * @param id the coordination id.
	 * 
	 * @return the coordination id.
	 */
	Coordination coordination(String id);
}
