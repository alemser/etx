package com.alx.etx.client;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;

import java.io.IOException;
import java.util.function.Consumer;

public abstract class CoordinationInterceptor {

    private String coordinationUrl = "http://localhost:8080";
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    protected String getCoordinationUrl() {
        return coordinationUrl;
    }

    protected CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    protected String getIdFromLocation(CloseableHttpResponse response) {
        try {
            String location = response.getHeader("Location").getValue();
            return location.substring(location.lastIndexOf("/") + 1);
        } catch (Exception ex) {
            throw new CoordinationClientException("Cannot get id from location header.", ex);
        }
    }

    protected boolean is2xx(HttpResponse httpResponse) {
        return httpResponse.getCode() >= 200 && httpResponse.getCode() < 300;
    }


    protected void doPost(HttpPost post, Consumer<CloseableHttpResponse> consumer) throws IOException {
        post.addHeader("Content-Type","application/json");
        try(CloseableHttpResponse response = httpClient.execute(post)) {
            consumer.accept(response);
        }
    }
}
