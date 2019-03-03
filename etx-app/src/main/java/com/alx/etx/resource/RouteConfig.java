package com.alx.etx.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;

import static com.alx.etx.resource.API.COORDINATIONS_PATH;
import static com.alx.etx.resource.API.ID_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteConfig {

    @Autowired
    private CoordinationHandler handler;

    @Bean
    public RouterFunction<?> coordinationRoutes() {
        return route()
                .path(COORDINATIONS_PATH, c -> c
                        .nest(accept(APPLICATION_JSON), c2 -> c2
                                .GET("", handler::get)
                                .GET(ID_PATH, handler::getById)
                                .POST("", handler::create))).build();
    }

}
