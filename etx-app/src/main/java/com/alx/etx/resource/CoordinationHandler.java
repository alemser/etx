package com.alx.etx.resource;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import com.alx.etx.model.CoordinationConfigData;
import com.alx.etx.model.CoordinationData;
import com.alx.etx.service.CoordinationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.alx.etx.resource.API.COORDINATIONS_ID_PATH;
import static com.alx.etx.resource.API.ID_PATH_VAR;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class CoordinationHandler {

    @Autowired
    private Logger logger;

    @Autowired
    private CoordinationService service;

    @Autowired
    private ObjectMapper mapper;

    public Mono<ServerResponse> get(ServerRequest request) {
        return service.get(request.queryParams().toSingleValueMap())
                .map(this::toCoordinationData)
                .collectList()
                .flatMap(this::getResponse)
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return Mono.justOrEmpty(request.pathVariable(ID_PATH_VAR))
                .flatMap(service::get)
                .flatMap(this::getResponse)
                .doOnError( c -> logger.error("Exception while retrieving the coordination: {}", c.getMessage()))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CoordinationConfigData.class)
                .map(this::createConfiguration)
                .flatMap(service::start)
                .doOnError( e -> logger.error("Exception while creating coordination", e))
                .doOnSuccess( c -> logger.info("Successfully created coordination with ID_PATH {}", c.getId()))
                .flatMap(this::createdResponse);
    }

    private CoordinationConfiguration createConfiguration(CoordinationConfigData dto) {
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

    private Mono<ServerResponse> createdResponse(Coordination c) {
        var location = UriComponentsBuilder.fromPath(COORDINATIONS_ID_PATH).buildAndExpand(c.getId()).toUri();
        return created(location).eTag(c.getId()).contentType(APPLICATION_JSON).syncBody(toCoordinationData(c));
    }

    private Mono<ServerResponse> getResponse(Coordination c) {
        return ok().eTag(c.getId()).contentType(APPLICATION_JSON).syncBody(toCoordinationData(c));
    }

    private Mono<ServerResponse> getResponse(List<CoordinationData> c) {
        return ok().contentType(APPLICATION_JSON).syncBody(c);
    }

    private CoordinationData toCoordinationData(Coordination c) {
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
}
