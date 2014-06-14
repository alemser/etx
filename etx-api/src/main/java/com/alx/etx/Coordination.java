package com.alx.etx;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The client representantion of the coordination.
 * 
 * @author alemser
 */
public class Coordination implements Serializable {
	
	/**
	 * Coordination was created but was not started yet.
	 */
	public static final int CREATED = 0;
	/**
	 * Coordination was created and started.
	 */
	public static final int RUNNING = 1;
	/**
	 * Coordination ended successfully (confirmed).
	 */
	public static final int ENDED = 2;
	/**
	 * Coordination ended with timeout (error state)
	 */
	public static final int ENDED_TIMEOUT = 3;
	/**
	 * Coordination ended because participant cancellation (error state)
	 */
	public static final int ENDED_CANCELLED = 4;
	
	private static final long serialVersionUID = 1L;
	private String id;
	private Date startTime;
	private int state;
	private List<? extends Participant> participants;

	public Coordination() {		
		this.state = CREATED;
	}
	
	public Coordination(String id) {
		this();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<? extends Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<? extends Participant> participants) {
		this.participants = participants;
	}

}
