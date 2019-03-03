package com.alx.etx.resource;

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
        return null;
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> join(ServerRequest request) {
        final String cid = request.pathVariable("cid");
        return bodyToParticipant(request)
                .flatMap( p -> {
                    Mono<Participant> mono = service.join(cid, p);
                    System.out.println("mono = " + mono);
                    return mono;
                })
                .doOnError( e -> logger.error("Exception while joining participant", e))
                .doOnSuccess( p -> logger.info("Successfully created participant with ID {}", p.getId()))
                .flatMap( p -> createResponse(cid, p));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> createResponse(String coordId, Participant p) {
        URI location = UriComponentsBuilder.fromPath(PARTICIPANTS_PATH.concat("/{id}")).buildAndExpand(coordId, p.getId()).toUri();
        return created(location).eTag(p.getId()).build();
    }


    private Mono<ServerResponse> getResponse(Participant p) {
        return ok().eTag(p.getId()).contentType(APPLICATION_JSON).syncBody(toParticipantData(p));
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
        data.setId(p.getId());
        return data;
    }
}
