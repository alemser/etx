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

    public Mono<ServerResponse> get(ServerRequest request) {
        String cid = request.pathVariable("cid");
        return service.get(cid)
                .map(Coordination::getParticipants)
                .flatMap(this::getResponse);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String cid = request.pathVariable("cid");
        String pid = request.pathVariable("id");
        return service.get(cid)
                .map(c -> c.getParticipants().get(pid))
                .flatMap(this::getResponse);
    }

    public Mono<ServerResponse> join(ServerRequest request) {
        final String cid = request.pathVariable("cid");
        return bodyToParticipant(request)
                .flatMap( p -> service.join(cid, p))
                .doOnError( e -> logger.error("Exception while joining participant", e))
                .doOnSuccess( p -> logger.info("Successfully created participant with ID {}", p.getId()))
                .flatMap( p -> createResponse(cid, p.getId()));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        final String cid = request.pathVariable("cid");
        final String pid = request.pathVariable("id");
        return bodyToParticipant(request)
                .flatMap( p -> service.changeState(cid, pid, p.getState()))
                .doOnError( e -> logger.error("Exception while changing the participant state", e))
                .doOnSuccess( p -> logger.info("Successfully changed the participant to {}", p.name()))
                .flatMap( p -> createResponse(cid, pid));

    }

    private Mono<ServerResponse> createResponse(String coordId, String pid) {
        URI location = UriComponentsBuilder.fromPath(PARTICIPANTS_PATH.concat("/{id}")).buildAndExpand(coordId, pid).toUri();
        return created(location).build();
    }

    private Mono<ServerResponse> getResponse(Participant p) {
        return ok().contentType(APPLICATION_JSON).syncBody(toParticipantData(p));
    }

    private Mono<ServerResponse> getResponse(Map<String, Participant> map) {
        List<ParticipantData> list = map.values().stream().map(this::toParticipantData).collect(Collectors.toList());
        return ok().contentType(APPLICATION_JSON).syncBody(list);
    }

    private Mono<Participant> bodyToParticipant(ServerRequest request) {
        return request.bodyToMono(ParticipantData.class)
                .map( data -> {
                    Participant p = new Participant(data.getName());
                    p.setCallbackToken(data.getCallbackToken());
                    p.setPayload(data.getPayload());
                    p.setState(data.getState() == null ? JOINED : data.getState());
                    return p;
                });
    }

    private ParticipantData toParticipantData(Participant p) {
        ParticipantData data = new ParticipantData();
        data.setCallbackToken(p.getCallbackToken());
        data.setCallbackUrl(p.getCallbackUrl());
        data.setCancelTime(p.getCancelTime());
        data.setConfirmTime(p.getConfirmTime());
        data.setExecuteTime(p.getConfirmTime());
        data.setJoinTime(p.getJoinTime());
        data.setName(p.getName());
        data.setState(p.getState());
        data.setId(p.getId());
        return data;
    }
}
