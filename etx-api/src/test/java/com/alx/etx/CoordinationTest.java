package com.alx.etx;

import static com.alx.etx.ParticipantState.EXECUTED;
import static com.alx.etx.ParticipantState.CONFIRMED;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CoordinationTest {

	@Test
	public void shouldAllParticipantsHaveTheSameState() {
		List<Participant> participants = new ArrayList<>();
		participants.add(createParticipant("p1", EXECUTED));
		participants.add(createParticipant("p2", EXECUTED));
		participants.add(createParticipant("p3", EXECUTED));
		Coordination coordination = new Coordination();
		coordination.setParticipants(participants);
		Assert.assertTrue(coordination.allExecuted());
	}
	
	@Test
	public void shouldNotAllParticipantsHaveTheSameState() {
		List<Participant> participants = new ArrayList<>();
		participants.add(createParticipant("p1", EXECUTED));
		participants.add(createParticipant("p2", EXECUTED));
		participants.add(createParticipant("p3", CONFIRMED));
		Coordination coordination = new Coordination();
		coordination.setParticipants(participants);
		Assert.assertFalse(coordination.allExecuted());
	}	
	
	Participant createParticipant(String id, int state) {
		Participant p = new Participant(id);
		p.setState(state);
		return p;
	}
}
