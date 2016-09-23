package com.alx.etx.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

	private static Map<String, CoordinationEntity> coordinations = new ConcurrentHashMap<String, CoordinationEntity>();
	
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
	
	@Override
	public void end(String coordinationId) {
		CoordinationEntity coordination = (CoordinationEntity) getCoordination(coordinationId);
		coordination.end();
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
		participants.stream().forEach(p-> ((ParticipantEntity)p).updateState(state));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCoordinationState(String coordinationId, int state) {
		CoordinationEntity coord = (CoordinationEntity) getCoordination(coordinationId);
		coord.updateState(state);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Coordination getCoordination(String coordId) {
		return coordinations.get(coordId);
	}
}
