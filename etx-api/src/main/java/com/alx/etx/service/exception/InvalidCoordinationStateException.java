package com.alx.etx.service.exception;

import com.alx.etx.data.CoordinationState;

public class InvalidCoordinationStateException extends CoordinationException {

	private static final long serialVersionUID = 1L;

	public InvalidCoordinationStateException(CoordinationState state, CoordinationState desiredState) {
		super("Cannot change the coordination state from " + state + " to " + desiredState + ".");
	}
	
	public static void throwWith(CoordinationState state, CoordinationState desiredState) {
		throw new InvalidCoordinationStateException( state, desiredState);
	}
}
