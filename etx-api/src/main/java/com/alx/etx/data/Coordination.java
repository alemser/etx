package com.alx.etx.data;

import static com.alx.etx.data.CoordinationState.CREATED;
import static com.alx.etx.data.CoordinationState.ENDED;
import static com.alx.etx.data.CoordinationState.ENDED_CANCELLED;
import static com.alx.etx.data.CoordinationState.ENDED_TIMEOUT;
import static com.alx.etx.data.ParticipantState.CANCELLED;
import static com.alx.etx.data.ParticipantState.CONFIRMED;
import static com.alx.etx.data.ParticipantState.EXECUTED;
import static com.alx.etx.data.ParticipantState.JOINED;
import static java.time.OffsetDateTime.now;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * The client representation of the coordination.
 */
public class Coordination implements Serializable {
	
	private String id;
	private OffsetDateTime createTime;
	private OffsetDateTime startTime;
	private OffsetDateTime endTime;
	private OffsetDateTime inconsistenceStateTimeout;
	private CoordinationState state;
	private Map<String, Participant> participants = new HashMap<>();

	public Coordination() {
		this.createTime = now();
		this.state = CREATED;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
	}

	public CoordinationState getState() {
		return state;
	}

	public void setState(CoordinationState state) {
		this.state = state;
	}

	public Map<String, Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(Map<String, Participant> participants) {
		this.participants = participants;
	}
	
	public OffsetDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(OffsetDateTime createTime) {
		this.createTime = createTime;
	}

	public OffsetDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(OffsetDateTime endTime) {
		this.endTime = endTime;
	}
	
	public boolean allConfirmed() {
		return allInDesiredState(CONFIRMED);
	}
	
	public boolean allCancelled() {
		return allInDesiredState(CANCELLED);
	}

	public boolean isInEndState() {
		return getState() != ENDED && getState() != ENDED_CANCELLED && getState() != ENDED_TIMEOUT;
	}
	
	private boolean allInDesiredState(ParticipantState desiredState) {
		return getParticipants().isEmpty() ||
				getParticipants().values().stream().allMatch(p-> p.getState() == desiredState);
	}

	public OffsetDateTime getInconsistenceStateTimeout() {
		return inconsistenceStateTimeout;
	}

	public void setInconsistenceStateTimeout(OffsetDateTime inconsistenceStateTimeout) {
		this.inconsistenceStateTimeout = inconsistenceStateTimeout;
	}

	public boolean isTimedOut() {
		return now().isAfter(getInconsistenceStateTimeout());
	}
}
