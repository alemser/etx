package com.alx.etx.service;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import com.alx.etx.data.Participant;
import com.alx.etx.data.ParticipantState;
import com.alx.etx.event.ParticipantEvent;
import com.alx.etx.model.CoordinationEntity;
import com.alx.etx.model.ParticipantEntity;
import com.alx.etx.service.exception.CoordinationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.alx.etx.data.CoordinationState.RUNNING;
import static com.alx.etx.data.ParticipantState.CANCELLED;
import static com.alx.etx.data.ParticipantState.CONFIRMED;
import static com.alx.etx.data.ParticipantState.EXECUTED;

/**
 * Demonstration implementation of a coordination service.
 */
@Service
public class CoordinationServiceImpl implements CoordinationService {

	private static Map<String, Coordination> coordinations = new ConcurrentHashMap<>();
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public Mono<Coordination> start(CoordinationConfiguration configuration) {
	    return Mono.fromSupplier( () -> {
	        Coordination coord = new CoordinationEntity().start();
	        OffsetDateTime timeout = coord.getStartTime().plus(configuration.getTimeoutInMillis(), ChronoUnit.MILLIS);
	        coord.setInconsistenceStateTimeout(timeout);
	        coordinations.put(coord.getId(), coord);
	        return coord;
	    });
	}
	
	public Mono<String> join(String coordinationId, String participantName) {
		return get(coordinationId)
				.filter( c -> c.getState().equals(RUNNING))
				.map(
					coord -> {
						Participant participant = new ParticipantEntity();
						String id = UUID.randomUUID().toString();
						participant.setId(id);
						participant.setName(participantName);
						coord.getParticipants().put(id, participant);
						return id;
					}
			).switchIfEmpty(Mono.error(new CoordinationException("Cannot join to not running coordination")));
	}

	public Mono<Void> end(String coordinationId) {
	    return get(coordinationId)
				.map( c -> {
					((CoordinationEntity) c).end();
					return c;
				})
				.filter( c -> !c.isInEndState())
				.map( c -> {
					//schedule end
					return Void.TYPE;
				})
				.then();
	}

	@Override
	public Mono<Void> execute(String coordinationId, String participantId) {
		return changeParticipantState(coordinationId, participantId, EXECUTED);
	}

	@Override
	public Mono<Void> confirm(String coordinationId, String participantId) {
		return changeParticipantState(coordinationId, participantId, CONFIRMED);
	}

	@Override
	public Mono<Void> cancel(String coordinationId, String participantId) {
		return changeParticipantState(coordinationId, participantId, CANCELLED);
	}

	private Mono<Void> changeParticipantState(String coordinationId, String participantId, ParticipantState state) {
		return get(coordinationId).map( coord -> {
            Map<String, Participant> participants = coord.getParticipants();
            Participant participant = participants.get(participantId);
            participant.updateState(state);
            applicationEventPublisher.publishEvent(new ParticipantEvent(participant, coord));
            return participant.getState();
        }).then();
	}

	public Mono<Coordination> get(String id) {
		return Mono.justOrEmpty(coordinations.get(id));
	}
}
