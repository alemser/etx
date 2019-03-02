package com.alx.etx.resource;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import com.alx.etx.model.CoordinationDto;
import com.alx.etx.service.CoordinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;

import static java.time.OffsetDateTime.now;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class CoordinationHandler {

    @Autowired
    private CoordinationService service;

    public Mono<ServerResponse> get(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CoordinationDto.class)
                .map(this::createConfiguration)
                .flatMap(service::start)
                .flatMap(this::createdResponse);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return null;
    }


    private CoordinationConfiguration createConfiguration(CoordinationDto dto) {
        Integer timeout = dto.getTimeout();
        ChronoUnit unit = dto.getTimeoutUnit();
        if (timeout == null) {
            timeout = 60;
            unit = ChronoUnit.SECONDS;
        }
        CoordinationConfiguration config = new CoordinationConfiguration();
        config.setTimeout((long)timeout);
        config.setTimeoutUnit(unit);
        return config;
    }

    private Mono<ServerResponse> createdResponse(Coordination c) {
        try {
            return created(new URI("http://localhost/coordinations/" + c.getId())).build();
        } catch (URISyntaxException ex) {
            return Mono.error(ex);
        }
    }
}
