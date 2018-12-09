package com.alx.etx.model;

import com.alx.etx.data.Participant;
import com.alx.etx.data.ParticipantState;
import com.alx.etx.service.exception.InvalidParticipantStateException;

import java.time.OffsetDateTime;

import static com.alx.etx.data.ParticipantState.CONFIRMED;
import static com.alx.etx.data.ParticipantState.EXECUTED;
import static com.alx.etx.data.ParticipantState.JOINED;

/**
 * An coordination participant.
 * 
 * @author alemser
 */
public class ParticipantEntity extends Participant {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Update the participant state.
	 * @param state the new state of the participant.
	 */
	@Override
	public ParticipantState updateState(ParticipantState state) {
		if( isValidStateToGo(state) ) {
			super.updateState(state);
			
			switch (state) {
			case EXECUTED:
				setExecuteTime(OffsetDateTime.now());
				break;
			case CONFIRMED:
				setConfirmTime(OffsetDateTime.now());
				break;
			case CANCELLED:
				setCancelTime(OffsetDateTime.now());
				break;
			case JOINED:
				setJoinTime(OffsetDateTime.now());
				break;
			default:
				break;
			}
			return state;
		} else {
			throw new InvalidParticipantStateException( getState(), state);
		}
	}

	/**
	 * Checks if the desired state is applicable.
	 * @param state the desired state.
	 * @return true if it is a valid state to go.
	 */
	private boolean isValidStateToGo(ParticipantState state) {
		switch (state) {
			case JOINED:
				return JOINED == getState();
			case EXECUTED:
				return JOINED == getState();
			case CONFIRMED:
				return EXECUTED == getState();
			case CANCELLED:
			return EXECUTED == getState() || CONFIRMED == getState();
			case CANCELLED_BY_TIMEOUT: return true;
			default:
				return false;
		}		
	}

}
