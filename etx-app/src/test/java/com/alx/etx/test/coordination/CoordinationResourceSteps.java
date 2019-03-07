package com.alx.etx.test.coordination;

import com.alx.etx.model.CoordinationConfigData;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java8.En;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static com.alx.etx.resource.API.COORDINATIONS_PATH;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class CoordinationResourceSteps implements En {

    @Autowired
    private ObjectMapper mapper;

    private RequestSpecification requestSpecification;
    private Response response;
    private String coordinationLocation;
    private CoordinationConfigData config;
    private Map<String, String> pNameAndLocation = new HashMap<>();

    public CoordinationResourceSteps() {
        Given("a configuration with {int} seconds timeout", (Integer timeout) -> {
            config = new CoordinationConfigData();
            config.setTimeout(timeout);
            config.setTimeoutUnit(ChronoUnit.SECONDS);
        });

        When("I wait for {int} seconds", (Integer timeout) -> {
            sleep(timeout * 1000);
        });

        Given("the following payload", (io.cucumber.datatable.DataTable dataTable) -> {
            String payload = dataTable.asList().get(0);
            requestSpecification = given()
                    .body(payload)
                    .contentType(JSON);
        });

        When("I POST to the coordination endpoint", () -> {
            response = requestSpecification.post(COORDINATIONS_PATH);
        });

        Then("the response status code is {int}", (Integer code) -> {
            response.then().statusCode(equalTo(code));
        });

        Then("the coordination info is present in the response", () -> {
            response.then().body("id", not(nullValue()));
        });

        Then("the coordination state is {string}", (String desiredState) -> {
            response.then().body("state", equalTo(desiredState));
        });

        Then("there are {int} participants", (Integer expectedNumParticip) -> {
            response.then().body("participants.size()", equalTo(expectedNumParticip));
        });

        When("I GET to the location of the previous POST", () -> {
            String location = response.header("location");
            response = given().contentType(JSON).get(location);
        });

        Then("response headers contains keys", (io.cucumber.datatable.DataTable dataTable) -> {
            dataTable.asList().forEach(h-> assertThat(response.header(h), not(nullValue())));
        });

        Given("the a payload with business key {string} is POST to coordination endpoint", (String businessKey) -> {
            given().contentType(JSON).body("{\"business_key\":\"" + businessKey + "\"}").post(COORDINATIONS_PATH);
        });

        When("I GET a coordination by {string}", (String businesskey) -> {
            response = given().contentType(JSON).get(COORDINATIONS_PATH.concat("?business_key=" + businesskey));
        });

        Then("the coordination has {string} as business key", (String businesskey) -> {
            response.then().body("size()", equalTo(1));
            response.then().body("business_key", hasItems(businesskey));
        });

        Given("a started coordination", () -> {
            if (config == null) {
                config = new CoordinationConfigData();
            }
            config.setBusinessKey("myBK");
            var json = mapper.writeValueAsString(config);
            coordinationLocation = given().contentType(JSON)
                    .body(json)
                    .post(COORDINATIONS_PATH)
                    .header("Location");
        });

        When("I {string} the participant {string} with {string} state", (String action, String name, String state) -> {
            if ("join".equals(action)) {
                String stateJsonPart = "";
                if (state != null && !"".equals(state.trim())) {
                    stateJsonPart = ", \"state\":\"" + state + "\"";
                }
                response = given()
                        .contentType(JSON)
                        .body("{\"name\":\"" + name + "\"" + stateJsonPart + "}")
                        .post(coordinationLocation.concat("/participants"));

                pNameAndLocation.put(name, response.header("Location"));

            } else if("update".equals(action)) {
                String location = pNameAndLocation.get(name);
                String stateJsonPart = "";
                if (state != null && !"".equals(state.trim())) {
                    stateJsonPart = ", \"state\":\"" + state + "\"";
                }

                response = given()
                        .contentType(JSON)
                        .body("{\"name\":\"" + name + "\"" + stateJsonPart + "}")
                        .put(location);

            }
        });

        Then("the participant has the {string} state", (String state) -> {
            response.then().body("state", equalTo(state));
        });

        Then("the coordination has {int} participants with the following states", (Integer size, io.cucumber.datatable.DataTable dataTable) -> {
            response = given().contentType(JSON).get(coordinationLocation + "/participants");
            response.then().body("size()", equalTo(size));
            response.then().body("state", hasItems(dataTable.asList().toArray()));
        });

        When("I end the coordination", () -> {
            response = given().contentType(JSON).put(coordinationLocation);
        });
    }
}
