package com.alx.etx.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static com.alx.etx.resource.API.COORDINATIONS_PATH;
import static com.alx.etx.resource.API.PARTICIPANTS_PATH;
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
                                .GET("", coordinationHandler::get)
                                .GET("/{id}", coordinationHandler::getById)
                                .POST("", coordinationHandler::create)
                                .PUT("/{id}", coordinationHandler::update))).build();
    }

    @Bean
    public RouterFunction<?> participantRoutes() {
        return route()
                .path(PARTICIPANTS_PATH, c -> c
                        .nest(accept(APPLICATION_JSON), c2 -> c2
                                .GET("", participantHandler::get)
                                .GET("/{id}", participantHandler::getById)
                                .POST("", participantHandler::join)
                                .PUT("/{id}", participantHandler::update))).build();
    }

}
