package com.alx.etx.resource;

import com.alx.etx.model.CoordinationConfigData;
import com.alx.etx.service.CoordinationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

@Component
public class CoordinationHandler {

    @Autowired
    private Logger logger;

    @Autowired
    private CoordinationService service;

    @Autowired
    private RequestResponseMapper mapper;

    public Mono<ServerResponse> get(ServerRequest request) {
        return service.get(request.queryParams().toSingleValueMap())
                .map(mapper::toCoordinationData)
                .collectList()
                .flatMap(mapper::getResponse)
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(request.pathVariable("id"))
                .flatMap(mapper::getResponse)
                .doOnError( c -> logger.error("Exception while retrieving the coordination: {}", c.getMessage()))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return service.end(request.pathVariable("id"))
                .doOnError( e -> logger.error("Exception while ending coordination", e))
                .doOnSuccess( c -> logger.info("Successfully ended coordination ID {}", c.getId()))
                .flatMap(mapper::getResponse);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CoordinationConfigData.class)
                .map(mapper::createConfiguration)
                .flatMap(service::start)
                .doOnError( e -> logger.error("Exception while creating coordination", e))
                .doOnSuccess( c -> logger.info("Successfully created coordination with ID {}", c.getId()))
                .flatMap(mapper::createdResponse);
    }
}
