package com.alx.etx.resource;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.Participant;
import com.alx.etx.model.ParticipantData;
import com.alx.etx.service.CoordinationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alx.etx.data.ParticipantState.JOINED;
import static com.alx.etx.resource.API.PARTICIPANTS_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ParticipantHandler {
    @Autowired
    private Logger logger;

    @Autowired
    private CoordinationService service;

    @Autowired
    private RequestResponseMapper mapper;

    public Mono<ServerResponse> get(ServerRequest request) {
        String cid = request.pathVariable("cid");
        return service.get(cid)
                .map(Coordination::getParticipants)
                .flatMap(mapper::getResponse);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String cid = request.pathVariable("cid");
        String pid = request.pathVariable("id");
        return service.get(cid)
                .map(c -> c.getParticipants().get(pid))
                .flatMap(mapper::getResponse);
    }

    public Mono<ServerResponse> join(ServerRequest request) {
        final String cid = request.pathVariable("cid");
        return mapper.bodyToParticipant(request)
                .flatMap( p -> service.join(cid, p))
                .doOnError( e -> logger.error("Exception while joining participant", e))
                .doOnSuccess( p -> logger.info("Successfully created participant with ID {}", p.getId()))
                .flatMap( p -> mapper.createResponse(cid, p.getId()));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        final String cid = request.pathVariable("cid");
        final String pid = request.pathVariable("id");
        return mapper.bodyToParticipant(request)
                .flatMap( p -> service.changeState(cid, pid, p.getState()))
                .doOnError( e -> logger.error("Exception while changing the participant state", e))
                .doOnSuccess( p -> logger.info("Successfully changed the participant to {}", p.name()))
                .flatMap( p -> mapper.createResponse(cid, pid));
    }
}
