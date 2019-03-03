package com.alx.etx.test.coordination;

import com.alx.etx.Application;
import com.alx.etx.EtxConfiguration;
import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationConfiguration;
import com.alx.etx.data.Participant;
import com.alx.etx.model.ParticipantStateListener;
import com.alx.etx.service.CoordinationCheckTask;
import com.alx.etx.service.CoordinationService;
import com.alx.etx.service.CoordinationServiceImpl;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.alx.etx.resource.API.COORDINATIONS_PATH;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {CoordinationServiceImpl.class, EtxConfiguration.class,
        ParticipantStateListener.class, CoordinationCheckTask.class})
public class CoordinationResourceSteps implements En {

    @LocalServerPort
    private int randomServerPort;

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
    }

    @Before
    public void setUp(Scenario scenario) {
        RestAssured.port = randomServerPort;
        RestAssured.baseURI = "http://localhost";

    }
}
