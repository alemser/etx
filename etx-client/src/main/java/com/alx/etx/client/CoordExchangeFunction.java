package com.alx.etx.client;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.alx.etx.client.IncomingHeaders.COORD_ID_KEY;
import static com.alx.etx.client.IncomingHeaders.PARTICIPANT_ID_KEY;
import static java.lang.String.join;

public class CoordExchangeFunction implements ExchangeFunction {
    private IncomingHeaders incomingHeaders;
    private CoordExchangeService service = new CoordExchangeServiceImpl();

    public Mono<ClientResponse> exchange(IncomingHeaders incomingHeaders, ClientRequest clientRequest) {
        this.incomingHeaders = incomingHeaders;
        return exchange(clientRequest);
    }

    @Override
    public Mono<ClientResponse> exchange(ClientRequest clientRequest) {
        String currentCoordId = incomingHeaders == null ?
                service.start() :
                incomingHeaders.getIncomingCoordinationId();

        clientRequest.headers().add(COORD_ID_KEY, join(",", currentCoordId));

        Participant participant = new Participant();
        participant.setName("Test" + currentCoordId.substring(0, 4));
        participant.setCallbackUrl(clientRequest.url().toString());
        participant.setPayload(clientRequest.body().toString());
        String participantId = service.execute(currentCoordId, participant);

        clientRequest.headers().add(PARTICIPANT_ID_KEY, participantId);

        //make the request
        //see what people do with this class
        return null;
    }

    class CoordExchangeServiceImpl implements CoordExchangeService {
        @Override
        public String start() {
            return UUID.randomUUID().toString();
        }

        @Override
        public String execute(String coordinationId, Participant participant) {
            return UUID.randomUUID().toString();
        }
    }
}
