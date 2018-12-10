package com.alx.etx.data;

import static com.alx.etx.data.ParticipantState.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

public class Participant implements Serializable {

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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ParticipantState getState() {
		return state;
	}

	public ParticipantState updateState(ParticipantState state) {
		this.state = state;
		return state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OffsetDateTime getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(OffsetDateTime joinTime) {
		this.joinTime = joinTime;
	}

	public OffsetDateTime getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(OffsetDateTime executeTime) {
		this.executeTime = executeTime;
	}

	public OffsetDateTime getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(OffsetDateTime confirmTime) {
		this.confirmTime = confirmTime;
	}

	public OffsetDateTime getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(OffsetDateTime cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getCallbackToken() {
		return callbackToken;
	}

	public void setCallbackToken(String callbackToken) {
		this.callbackToken = callbackToken;
	}

	public void setState(ParticipantState state) {
		this.state = state;
	}
}
