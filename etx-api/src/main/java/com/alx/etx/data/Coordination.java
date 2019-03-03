package com.alx.etx.data;

import lombok.Data;

import static com.alx.etx.data.CoordinationState.CREATED;
import static com.alx.etx.data.ParticipantState.CANCELLED;
import static com.alx.etx.data.ParticipantState.CONFIRMED;
import static java.time.OffsetDateTime.now;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public @Data class Coordination implements Serializable {

	/**
	 * Auto generated id of the coordination.
	 */
	private String id;
	/**
	 * Key for the business context in which the coordination is running.
	 */
	private String businessKey;
	/**
	 * The id of the application which started the coordination.
	 */
	private String applicationId;

	private OffsetDateTime createTime;
	private OffsetDateTime startTime;
	private OffsetDateTime endTime;
	private OffsetDateTime timeout;

	private CoordinationState state;

	private Map<String, Participant> participants = new HashMap<>();

	public Coordination() {
		this.createTime = now();
		this.state = CREATED;
	}
	
	public boolean allConfirmed() {
		return allInDesiredState(CONFIRMED);
	}
	
	public boolean allCancelled() {
		return allInDesiredState(CANCELLED);
	}

	private boolean allInDesiredState(ParticipantState desiredState) {
		return getParticipants().isEmpty() ||
				getParticipants().values().stream().allMatch(p-> p.getState() == desiredState);
	}

	public boolean isTimedOut() {
		return now().isAfter(getTimeout());
	}
}
