package com.alx.etx;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

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
	/**
	 * Coordination is in a inconsistent state (must be cancelled).
	 */
	public static final int INCONSISTENT = 5;	
	
	private static final long serialVersionUID = 1L;
	private String id;
	private Date createTime;
	private Date startTime;
	private Date endTime;
	private int state;
	private List<Participant> participants;

	public Coordination() {	
		this.createTime = new Date();
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

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	
	/**
	 * Print the current coordination state and info.
	 * @param out the outputstream the print the coordination info.
	 */
	public void print(OutputStream out) {
		StringBuilder sb = new StringBuilder();
		sb.append("Coordination " + getId() + " is " + getStateDescription());
		if( getStartTime() != null ) {
			sb.append(" and was started at " + getStartTime() + ".");
		} else {
			sb.append(" and is not started yet.");
		}
		
		sb.append("\n");
		
		int psize = getParticipants().size();
		if( psize == 0 ) {
			sb.append("There is no registered participants.");
			
		} else {
			sb.append("There " + (psize > 1 ? " are " : " is ") + psize + " registered participant" + (psize>1?"s" : "") + ".\n");
			for (Participant p : getParticipants() ) {
				sb.append("=> Participant " + p.getId() + " state is " + p.getStateDescription()+  "\n");
			}
		}
		
		try {
			out.write(sb.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@XmlTransient
	public String getStateDescription() {
		return getStateDescription(getState());
	}

	@XmlTransient
	public String getStateDescription(int state) {
		switch (state) {
		case CREATED:
			return "created";
		case ENDED:
			return "ended";
		case ENDED_CANCELLED:
			return "cancelled";
		case ENDED_TIMEOUT:
			return "timeouted";
		case RUNNING:
			return "running";
		default:
			return "undefined";
		}
	}

	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public boolean allConfirmed() {
		return allDesiredState(Participant.CONFIRMED);
	}
	
	public boolean allExecuted() {
		return allDesiredState(Participant.EXECUTED);
	}
	
	public boolean allCancelled() {
		return allDesiredState(Participant.CANCELLED);
	}
	
	public boolean allJoined() {
		return allDesiredState(Participant.JOINED);
	}
	
	private boolean allDesiredState(int desiredState) {
		boolean allIn = true;
		for (Participant p : this.participants ) {
			if( p.getState() != desiredState ) {
				allIn = false;
				break;
			}
		}
		return allIn;
	}
}
