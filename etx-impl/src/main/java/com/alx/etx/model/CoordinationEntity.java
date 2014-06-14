package com.alx.etx.model;

import java.util.ArrayList;
import java.util.Date;

import com.alx.etx.Coordination;

/**
 * An coordination.
 * 
 * @author alemser
 */
public class CoordinationEntity extends Coordination {

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
		setState(RUNNING);
		setStartTime(new Date());
		setParticipants(new ArrayList<ParticipantEntity>());
	}
	
}
