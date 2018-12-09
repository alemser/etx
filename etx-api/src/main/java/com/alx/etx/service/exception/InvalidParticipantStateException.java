package com.alx.etx.service.exception;

import com.alx.etx.data.ParticipantState;

public class InvalidParticipantStateException extends CoordinationException {

	private static final long serialVersionUID = 1L;

	public InvalidParticipantStateException(ParticipantState state, ParticipantState desiredState) {
		super("Cannot change the participant state from " + state + " to " + desiredState + ".");
	}
}
