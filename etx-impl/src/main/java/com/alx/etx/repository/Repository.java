package com.alx.etx.repository;

import com.alx.etx.Coordination;

/**
 * Repository for keep track of the coordinations.
 * 
 * @author alemser
 */
public interface Repository {

	/**
	 * Initializes an coordination and return its id.
	 * 
	 * @return the coordination id.
	 */
	String initCoordination();
	
	/**
	 * Finaliza a coordenação.
	 * @param coordinationId o id da coordenação.
	 */
	void end(String coordinationId);
	
	/**
	 * Join an participant into an coordination.
	 * @param coordinationId the coordination id.
	 * @param participantName the participant name.
	 * @return the created participant id.
	 */
	String join(String coordinationId, String participantName);

	/**
	 * Return an existing coordination.
	 * @param coordId the coordination id.
	 * @return the coordination.
	 */
	Coordination getCoordination(String coordId);

	/**
	 * Set the participant state.
	 * 
	 * @param coordinationId the coordination id.
	 * @param participantId the participant id.
	 */
	void setParticipantState(String coordinationId, String participantId, int state);

	/**
	 * Defines the coordination state.
	 * @param coordinationId the coordination id.
	 * @param state the disired state.
	 */
	void setCoordinationState(String coordinationId, int state);
}
