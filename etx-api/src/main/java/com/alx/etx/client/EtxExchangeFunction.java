package com.alx.etx.client;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class EtxExchangeFunction implements ExchangeFunction {

    @Override
    public Mono<ClientResponse> exchange(ClientRequest clientRequest) {
        return null;
    }
}
