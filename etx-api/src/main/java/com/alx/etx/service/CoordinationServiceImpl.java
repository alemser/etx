package com.alx.etx.service;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import com.alx.etx.data.Participant;
import com.alx.etx.data.ParticipantState;
import com.alx.etx.event.ParticipantEvent;
import com.alx.etx.model.CoordinationEntity;
import com.alx.etx.model.ParticipantEntity;
import com.alx.etx.service.exception.CoordinationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.alx.etx.data.CoordinationState.INCONSISTENT;
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
	private Logger logger;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public Mono<Coordination> start(CoordinationConfiguration configuration) {
	    return Mono.fromSupplier( () -> {
	        var coord = new CoordinationEntity().start();
	        var timeout = coord.getStartTime().plus(configuration.getTimeout(), configuration.getTimeoutUnit());
	        coord.setTimeout(timeout);
	        coord.setBusinessKey(configuration.getBusinessKey());
	        coord.setApplicationId(configuration.getApplicationId());
	        coordinations.put(coord.getId(), coord);
			logger.info("Coordination {} is {}. Timeout set to {}", coord.getId(), coord.getState(), coord.getTimeout());
	        return coord;
	    });
	}

	@Override
	public Mono<Participant> join(String coordinationId, Participant participant) {
		return get(coordinationId)
				.filter(coordination -> coordination.getState() == RUNNING)
                .switchIfEmpty(
                        Mono.error(new CoordinationException("Cannot join to not running coordination")))
				.map(
					coordination -> {
						Participant entity = new ParticipantEntity(participant);
						String id = UUID.randomUUID().toString();
						entity.setId(id);
						coordination.getParticipants().put(id, entity);
						logger.info("Participant ID {} joined coordination ID {}", entity.getId(), coordination.getId());
						return entity;
					}
				);
	}

	public Mono<Coordination> end(String coordinationId) {
	    return get(coordinationId)
				.map( c -> {
					((CoordinationEntity) c).end();
					logger.info("Coordination {} ended with state {}", c.getId(), c.getState());
					return c;
				});
	}

    @Override
    public Mono<ParticipantState> changeState(String coordinationId, String participantId, ParticipantState desiredState) {
        switch (desiredState) {
            case EXECUTED:
                return execute(coordinationId, participantId);
            case CONFIRMED:
                return confirm(coordinationId, participantId);
            case CANCELLED:
                return cancel(coordinationId, participantId);
        }
        return Mono.error(new CoordinationException("Cannot change participant state."));
    }

    @Override
	public Mono<ParticipantState> execute(String coordinationId, String participantId) {
		return changeParticipantState(coordinationId, participantId, EXECUTED);
	}

	@Override
	public Mono<ParticipantState> confirm(String coordinationId, String participantId) {
		return changeParticipantState(coordinationId, participantId, CONFIRMED);
	}

	@Override
	public Mono<ParticipantState> cancel(String coordinationId, String participantId) {
		return changeParticipantState(coordinationId, participantId, CANCELLED);
	}

	private Mono<ParticipantState> changeParticipantState(String coordinationId, String participantId, ParticipantState state) {
		return get(coordinationId).map( coord -> {
            var participants = coord.getParticipants();
            var participant = participants.get(participantId);
            var previousState = participant.getState();
			participant.updateState(state);
			applicationEventPublisher.publishEvent(new ParticipantEvent(participant, coord));
			logger.info("Participant {} state changed from {} to {}", participant.getId(), previousState, participant.getState());
			return participant.getState();
        });
	}

	public Mono<Coordination> get(String id) {
		return Mono.justOrEmpty(coordinations.get(id));
	}

	@Override
	public Flux<Coordination> getNonConsistents() {
		return Flux.fromIterable(coordinations.values()
				.stream()
				.filter( c -> c.getState() == INCONSISTENT)
				.collect(Collectors.toList()));
	}

	@Override
	public Flux<Coordination> get(Map<String, String> filters) {
	    if (filters == null || filters.isEmpty()) {
            return Flux.fromIterable(coordinations.values());
        }

        return Flux.fromIterable(coordinations.values()
                .stream()
                .filter(c -> c.getBusinessKey().equals(filters.get("business_key")))
                .collect(Collectors.toList()));
	}
}
