package com.alx.etx.model;

import java.util.Date;

import com.alx.etx.Participant;

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
	public void updateState(int state) {
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
	}
	
}
