package com.alx.etx.data;

import lombok.Data;

import static com.alx.etx.data.ParticipantState.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

public @Data class Participant implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String payload;
	private String callbackUrl;
	private String callbackToken;
	private OffsetDateTime joinTime;
	private OffsetDateTime executeTime;
	private OffsetDateTime confirmTime;
	private OffsetDateTime cancelTime;
	private ParticipantState state;

	public Participant() {
		this.state = UNDEFINED;
		this.joinTime = OffsetDateTime.now();
	}

	public Participant(String name) {
		this();
		setName(name);
	}
	
	public ParticipantState updateState(ParticipantState state) {
		this.state = state;
		return state;
	}
}
