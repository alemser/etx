package com.alx.etx.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static com.alx.etx.resource.API.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteConfig {

    @Autowired
    private CoordinationHandler coordinationHandler;

    @Autowired
    private ParticipantHandler participantHandler;

    @Bean
    public RouterFunction<?> coordinationRoutes() {
        return route()
                .path(COORDINATIONS_PATH, c -> c
                        .nest(accept(APPLICATION_JSON), c2 -> c2
                                .GET(BLANK_PATH, coordinationHandler::get)
                                .GET(ID_PATH, coordinationHandler::getById)
                                .POST(BLANK_PATH, coordinationHandler::create)
                                .PUT(ID_PATH, coordinationHandler::update))).build();
    }

    @Bean
    public RouterFunction<?> participantRoutes() {
        return route()
                .path(PARTICIPANTS_PATH, c -> c
                        .nest(accept(APPLICATION_JSON), c2 -> c2
                                .GET(BLANK_PATH, participantHandler::get)
                                .GET(ID_PATH, participantHandler::getById)
                                .POST(BLANK_PATH, participantHandler::join)
                                .PUT(ID_PATH, participantHandler::update))).build();
    }
}
