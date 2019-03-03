package com.alx.etx.test.coordination;

import cucumber.api.java8.En;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static com.alx.etx.resource.API.COORDINATIONS_PATH;
import static com.alx.etx.resource.API.PARTICIPANTS_PATH;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class CoordinationResourceSteps implements En {

    private RequestSpecification requestSpecification;
    private Response response;

    public CoordinationResourceSteps() {
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
            response = given().contentType(JSON).body("{\"business_key\":\"myBK\"}").post(COORDINATIONS_PATH);

        });

        When("I {string} the participant {string}", (String action, String name) -> {
            String coordinationId = response.header("ETag").replaceAll("\"", "");
            if ("join".equals(action)) {
                String path = PARTICIPANTS_PATH.replace("{cid}", coordinationId);
                response = given().contentType(JSON).body("{\"name\":\"" + name + "\"}").post(path);
            }
        });

        When("I {string} the participant {string} with {string} state", (String action, String name, String state) -> {
            String coordinationId = response.header("ETag").replaceAll("\"", "");
            if ("join".equals(action)) {
                String path = PARTICIPANTS_PATH.replace("{cid}", coordinationId);
                String stateJsonPart = "";
                if (state != null && !"".equals(state.trim())) {
                    stateJsonPart = ", \"state\":\"" + state + "\"";
                }
                response = given().contentType(JSON).body("{\"name\":\"" + name + "\"" + stateJsonPart + "}").post(path);
            }

        });

        Then("the participant has the {string} state", (String state) -> {
            response.then().body("state", hasItems(state));
        });

    }
}
