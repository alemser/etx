package com.alx.etx.client;

import org.springframework.web.reactive.function.server.ServerRequest;

public class IncomingHeaders {
    protected static final String COORD_ID_KEY = "alx-etx::coord-id";
    protected static final String PARTICIPANT_ID_KEY = "alx-etx::participant-id";
    private ServerRequest.Headers headers;

    private IncomingHeaders(ServerRequest.Headers headers) {
        this.headers = headers;
    }

    public static IncomingHeaders from(ServerRequest serverRequest) {
        return new IncomingHeaders(serverRequest.headers());
    }

    protected String getIncomingCoordinationId() {
        return headers.header(COORD_ID_KEY).isEmpty() ? null : headers.header(COORD_ID_KEY).get(0);
    }
}
