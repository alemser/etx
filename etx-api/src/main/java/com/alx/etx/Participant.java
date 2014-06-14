package com.alx.etx;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

public class Participant implements Serializable {

	public static final int JOINED = 0;
	public static final int EXECUTED = 1;
	public static final int CONFIRMED = 2;
	public static final int CANCELLED = 3;
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private Date joinTime;
	private Date executeTime;
	private Date confirmTime;
	private Date cancelTime;
	private int state;

	public Participant() {
		this.state = JOINED;
		this.joinTime = new Date();
	}
	
	public Participant(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	
	@XmlTransient
	public String getStateDescription() {
		return getStateDescription(getState());
	}
	
	@XmlTransient
	public String getStateDescription(int state) {
		switch (state) {
		case JOINED:
			return "created";
		case EXECUTED:
			return "executed";
		case CONFIRMED:
			return "confirmed";
		case CANCELLED:
			return "cancelled";
		default:
			return "undefined";
		}
	}
}
