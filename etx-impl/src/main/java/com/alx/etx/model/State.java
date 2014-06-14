package com.alx.etx.model;

/**
 * Facility to abstract states from coordination and participant.
 * @author alemser
 */
public interface State {

	/**
	 * Updates the state.
	 * @param state the state.
	 */
	void updateState(int state);
	
	/**
	 * Check if the state is valid.
	 * @param state the state.
	 * @return true if its is valid. 
	 */
	boolean isValidStateToGo(int state);
	
	/**
	 * Return the state description.
	 * @return the state description.
	 */
	String getStateDescription();
	
	/**
	 * Return the state description.
	 * @param state the state.
	 * @return the state description.
	 */
	String getStateDescription(int state);
}
