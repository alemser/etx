package com.alx.etx.test.coordination;

import com.alx.etx.Application;
import com.alx.etx.EtxConfiguration;
import com.alx.etx.model.ParticipantStateListener;
import com.alx.etx.service.CoordinationCheckTask;
import com.alx.etx.service.CoordinationServiceImpl;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java8.En;
import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {CoordinationServiceImpl.class, EtxConfiguration.class,
        ParticipantStateListener.class, CoordinationCheckTask.class})
public class BoostrapSteps implements En {

    @LocalServerPort
    private int randomServerPort;

    @Before
    public void setUp(Scenario scenario) {
        RestAssured.port = randomServerPort;
        RestAssured.baseURI = "http://localhost";
    }
}
