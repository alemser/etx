package com.alx.etx.client.test;

import com.alx.etx.client.CoordinationRequestInterceptor;
import com.alx.etx.client.CoordinationResponseInterceptor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.junit.Test;

import java.io.IOException;

public class CoordinationTest {

    @Test
    public void test() throws IOException {

        CloseableHttpClient httpclient = HttpClients.custom()
                .addRequestInterceptorFirst(new CoordinationRequestInterceptor())
                .addResponseInterceptorLast(new CoordinationResponseInterceptor())
                .build();
        HttpGet httpGet = new HttpGet("http://google.ie");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {
            HttpEntity entity1 = response1.getEntity();
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
    }

}
