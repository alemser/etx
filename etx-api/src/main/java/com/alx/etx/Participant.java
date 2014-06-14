package com.alx.etx;

import java.io.Serializable;

public class Participant implements Serializable {

	public static final int CREATED = 0;
	public static final int EXECUTED = 1;
	public static final int CONFIRMED = 2;
	public static final int CANCELLED = 3;
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private int state;

	public Participant() {
		this.state = CREATED;
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


}
