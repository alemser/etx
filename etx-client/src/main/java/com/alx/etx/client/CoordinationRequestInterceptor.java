package com.alx.etx.client;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.alx.etx.client.CoordinationContext.*;

public class CoordinationRequestInterceptor extends CoordinationInterceptor implements HttpRequestInterceptor {

    private static Logger logger = LoggerFactory.getLogger(CoordinationRequestInterceptor.class);

    @Override
    public void process(HttpRequest httpRequest, EntityDetails entityDetails, HttpContext httpContext) throws IOException, ProtocolException {
        if (!initiated()) {
            HttpPost post = new HttpPost(getCoordinationUrl());
            post.setEntity(new StringEntity("{}"));
            post.setPath("/coordinations");
            doPost(post, response -> {
                if (response.getCode() == 201) {
                    String id = getIdFromLocation(response);
                    initiate(id, null);
                    logger.info("Coordination {} initiated", id);
                } else {
                    throw new CoordinationClientException("Create coordination failed. Response status "
                            + response.getCode()
                            + " - " + response.getReasonPhrase());
                }
            });
        } else {
            logger.info("Existing coordination {} will be used in the request", getCurrentCoordinationId());
        }
    }
}
