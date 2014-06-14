package com.alx.etx.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alx.etx.Coordination;
import com.alx.etx.Participant;
import com.alx.etx.model.CoordinationEntity;
import com.alx.etx.model.ParticipantEntity;

/**
 * Implementation of the repository for tests purposes.
 * 
 * @author alemser
 */
public class MemoryRepository implements Repository {

	private static Map<String, CoordinationEntity> coordinations = new HashMap<String, CoordinationEntity>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String initCoordination() {
		String id = UUID.randomUUID().toString();
		CoordinationEntity c = new CoordinationEntity(id);
		c.start();
		coordinations.put(id, c);
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String join(String coordinationId, String participantName) {
		Coordination coord = coordinations.get(coordinationId);
		
		Participant pentity = new ParticipantEntity();
		String id = UUID.randomUUID().toString();
		pentity.setId(id);
		pentity.setName(participantName);
		
		coord.getParticipants().add(pentity);
		
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParticipantState(String coordinationId, String participantId, int state) {
		Coordination coord = coordinations.get(coordinationId);
		List<Participant> participants = coord.getParticipants();
		for (Participant participant : participants) {
			if( participant.getId().equals(participantId) ) {
				((ParticipantEntity) participant).updateState(state);
				break;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Coordination getCoordination(String coordId) {
		return coordinations.get(coordId);
	}
}
