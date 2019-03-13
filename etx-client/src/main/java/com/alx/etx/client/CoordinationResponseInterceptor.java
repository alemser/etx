package com.alx.etx.client;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static com.alx.etx.client.CoordinationContext.*;
import static com.alx.etx.client.ParticipantState.EXECUTED;
import static java.util.Objects.requireNonNull;

public class CoordinationResponseInterceptor extends CoordinationInterceptor implements HttpResponseInterceptor {

    private static Logger logger = LoggerFactory.getLogger(CoordinationResponseInterceptor.class);

    @Override
    public void process(HttpResponse httpResponse, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
        if (!initiated()) {
            logger.debug("Not in a coordination");
            return;
        }

        if (is2xx(httpResponse)) {
            Participant participant = new Participant();
            participant.setState(EXECUTED);

            HttpPost post = new HttpPost(getCoordinationUrl());
            post.setEntity(new StringEntity(participant.toJson()));
            post.setPath("/coordinations/".concat(getCurrentCoordinationId()).concat("/participants"));

            doPost(post, participantResponse -> {
                if (is2xx(participantResponse)) {
                    String participantId = getIdFromLocation(participantResponse);
                    initiate(getCurrentCoordinationId(), participantId);
                    logger.info("Participant {} joined coordination {}", participantId, getCurrentCoordinationId());
                } else {
                    throw new CoordinationClientException("Join of the participant to coordination "
                            + getCurrentCoordinationId() + " has failed with status "
                            + participantResponse.getCode()
                            + " - " + participantResponse.getReasonPhrase());
                }
            });
        } else {
            logger.debug("Participant not joined due to response status code {}", httpResponse.getCode());
        }
    }
}
