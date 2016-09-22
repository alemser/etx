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
	
	String join(String coordinationId, String participantName);

	void end(String coordinationId);
	
	void partState(String coordinationId, String participantId, int state);
	
	void coordState(String coordinationId, int state);
	
	Coordination coordination(String id);
}
