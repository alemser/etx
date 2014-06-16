package com.alx.etx.model;

import java.util.ArrayList;
import java.util.Date;

import com.alx.etx.Coordination;
import com.alx.etx.Participant;
import com.alx.etx.repository.InvalidStateException;

/**
 * An coordination.
 * 
 * @author alemser
 */
public class CoordinationEntity extends Coordination implements State {

	private static final long serialVersionUID = 1L;

	public CoordinationEntity() {
		super();
	}
	
	public CoordinationEntity(String id) {
		super(id);
	}

	/**
	 * Starts the coordination.
	 */
	public void start() {
		updateState(RUNNING);
		setParticipants(new ArrayList<Participant>());
	}

	/**
	 * Ends the coordination.
	 */
	public void end() {
		
		switch (getState()) {
		case RUNNING:	
			boolean allExecuted = true;
			for (Participant part : getParticipants() ) {
				ParticipantEntity pentity = (ParticipantEntity) part;
				if( pentity.getState() != Participant.CONFIRMED ) {
					allExecuted = false;
					break;
				}
			}
			
			if( allExecuted ) {
				updateState(ENDED);
			} else {
				updateState(INCONSISTENT);
				end();
			}
			break;
			
		case INCONSISTENT:
			for (Participant part : getParticipants() ) {
				ParticipantEntity pentity = (ParticipantEntity) part;
				pentity.updateState(Participant.CANCELLED);
			}
			updateState(ENDED_CANCELLED);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Update the coordination state.
	 * @param state the new state of the coordination.
	 */
	public void updateState(int state) {
		if( isValidStateToGo(state) ) {
			setState(state);
			
			switch (state) {
			case CREATED:
				setCreateTime(new Date());
			case RUNNING:
				setStartTime(new Date());
			case ENDED:
			case ENDED_CANCELLED:
			case ENDED_TIMEOUT:
				setEndTime(new Date());
			default:
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
		case CREATED:
			return CREATED == getState();
		case RUNNING:
			return CREATED == getState();
		case ENDED:
			return RUNNING == getState() || CREATED == getState();
		case ENDED_CANCELLED:
		case ENDED_TIMEOUT:
			return RUNNING == getState();
		default:
			return false;
		}		
	}
}
