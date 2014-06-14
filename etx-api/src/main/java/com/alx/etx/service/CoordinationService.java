package com.alx.etx.service;

import javax.ws.rs.POST;

/**
 * Proxy interface for the coordination services.
 * 
 * @author alemser
 */
public interface CoordinationService {

	/**
	 * Starts an coordination and returns its id.
	 * 
	 * @return the coordination id;
	 */
	@POST
	String start();
	
}
