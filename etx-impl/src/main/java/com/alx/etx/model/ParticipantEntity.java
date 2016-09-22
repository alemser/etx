package com.alx.etx.model;

import static com.alx.etx.ParticipantState.*;
import java.util.Date;

import com.alx.etx.Participant;
import com.alx.etx.repository.InvalidStateException;

/**
 * An coordination participant.
 * 
 * @author alemser
 */
public class ParticipantEntity extends Participant implements State {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Update the participant state.
	 * @param state the new state of the participant.
	 */
	public void updateState(int state) {
		if( isValidStateToGo(state) ) {
			setState(state);
			
			switch (state) {
			case EXECUTED:
				setExecuteTime(new Date());
				break;
			case CONFIRMED:
				setConfirmTime(new Date());
				break;
			case CANCELLED:
				setCancelTime(new Date());
				break;
			case JOINED:
				setJoinTime(new Date());
				break;
			default:
				break;
			}
		} else {
			InvalidStateException.throwWith(this, getState(), state);
		}
	}

	/**
	 * Checks if the desired state is applicable.
	 * @param state the desired state.
	 * @return true if it is a valid state to go.
	 */
	public boolean isValidStateToGo(int state) {
		switch (state) {
		case JOINED:
			return JOINED == getState();
		case EXECUTED:
			return JOINED == getState();
		case CONFIRMED:
			return EXECUTED == getState();
		case CANCELLED:
			return EXECUTED == getState() || CONFIRMED == getState();
		default:
			return false;
		}		
	}

}
