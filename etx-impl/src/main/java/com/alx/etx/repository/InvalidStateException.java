package com.alx.etx.repository;

import com.alx.etx.model.State;

public class InvalidStateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidStateException() {
		super();
	}

	public InvalidStateException(String state, String desiredState) {
		super("Cannot change the state from " + state + " to " + desiredState + ".");
	}
	
	public static void throwWith(State stateObj, int state, int desiredState) {
		throw new InvalidStateException( stateObj.getStateDescription(state), stateObj.getStateDescription(desiredState));
	}
}
