package com.alx.etx.resource;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import com.alx.etx.data.Participant;
import com.alx.etx.model.CoordinationConfigData;
import com.alx.etx.model.CoordinationData;
import com.alx.etx.model.ParticipantData;
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
import static com.alx.etx.resource.API.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class RequestResponseMapper {

    public CoordinationConfiguration createConfiguration(CoordinationConfigData dto) {
        var config = new CoordinationConfiguration();
        config.setApplicationId(dto.getApplicationId());
        config.setBusinessKey(dto.getBusinessKey());
        var timeout = dto.getTimeout();
        var unit = dto.getTimeoutUnit();
        if (timeout != null && unit != null) {
            config.setTimeout((long)timeout);
            config.setTimeoutUnit(unit);
        }
        return config;
    }

    public Mono<ServerResponse> createdResponse(Coordination c) {
        var location = UriComponentsBuilder.fromPath(COORDINATIONS_PATH.concat(ID_PATH)).buildAndExpand(c.getId()).toUri();
        return created(location).contentType(APPLICATION_JSON).syncBody(toCoordinationData(c));
    }

    public Mono<ServerResponse> getResponse(Coordination c) {
        return ok().contentType(APPLICATION_JSON).syncBody(toCoordinationData(c));
    }

    public Mono<ServerResponse> getResponse(List<CoordinationData> c) {
        return ok().contentType(APPLICATION_JSON).syncBody(c);
    }

    public CoordinationData toCoordinationData(Coordination c) {
        var data = new CoordinationData();
        data.setId(c.getId());
        data.setApplicationId(c.getApplicationId());
        data.setBusinessKey(c.getBusinessKey());
        data.setCreateTime(c.getCreateTime());
        data.setEndTime(c.getEndTime());
        data.setState(c.getState());
        data.setTimeout(c.getTimeout());
        return data;
    }

    public Mono<ServerResponse> createResponse(String coordId, String pid) {
        URI location = UriComponentsBuilder.fromPath(PARTICIPANTS_PATH.concat("/{id}")).buildAndExpand(coordId, pid).toUri();
        return created(location).build();
    }

    public Mono<ServerResponse> getResponse(Participant p) {
        return ok().contentType(APPLICATION_JSON).syncBody(toParticipantData(p));
    }

    public Mono<ServerResponse> getResponse(Map<String, Participant> map) {
        List<ParticipantData> list = map.values().stream().map(this::toParticipantData).collect(Collectors.toList());
        return ok().contentType(APPLICATION_JSON).syncBody(list);
    }

    public Mono<Participant> bodyToParticipant(ServerRequest request) {
        return request.bodyToMono(ParticipantData.class)
                .map( data -> {
                    Participant p = new Participant(data.getName());
                    p.setCallbackToken(data.getCallbackToken());
                    p.setPayload(data.getPayload());
                    p.setState(data.getState() == null ? JOINED : data.getState());
                    return p;
                });
    }

    public ParticipantData toParticipantData(Participant p) {
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
