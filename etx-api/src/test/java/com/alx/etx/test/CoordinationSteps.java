package com.alx.etx.test;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = {CoordinationServiceImpl.class, EtxConfiguration.class,
        ParticipantStateListener.class, CoordinationCheckTask.class})
public class CoordinationSteps implements En {

    @Autowired
    private CoordinationService service;

    private CoordinationConfiguration coordinationConfiguration = CoordinationConfiguration.builder().timeoutSec(4L).build();
    private Coordination coordination;
    private Set<String> currentScenarioTags = new HashSet<>();
    private List<String> participantNamesToActOn = new ArrayList<>();
    private Throwable caughtException;

    public CoordinationSteps() {
        When("a coordination is started", () -> {
            coordination = service.start(coordinationConfiguration).block();
        });

        Then("an id is created", () -> {
            assertThat(coordination.getId(), notNullValue());
        });

        Then("the coordination state is {string}", (String desiredState) -> {
            assertThat(coordination.getState().name(), equalTo(desiredState));
        });

        Then("there are {int} participants", (Integer expectedNumParticip) -> {
            assertThat(coordination.getParticipants().size(), equalTo(expectedNumParticip));
        });

        When("I {string} the participants", (String action, DataTable dataTable) -> {
            participantNamesToActOn = dataTable.asList();
            handleException(() -> {
                if ("join".equals(action)) {
                    participantNamesToActOn.forEach(pname -> service.join(coordination.getId(), new Participant(pname)).block());
                } else if ("execute".equals(action)) {
                    coordination.getParticipants().values().stream()
                            .filter(p -> participantNamesToActOn.contains(p.getName()))
                            .forEach(p -> service.execute(coordination.getId(), p.getId()).block());
                } else if ("confirm".equals(action)) {
                    coordination.getParticipants().values().stream()
                            .filter(p -> participantNamesToActOn.contains(p.getName()))
                            .forEach(p -> service.confirm(coordination.getId(), p.getId()).block());
                } else if ("cancel".equals(action)) {
                    coordination.getParticipants().values().stream()
                            .filter(p -> participantNamesToActOn.contains(p.getName()))
                            .forEach(p -> service.cancel(coordination.getId(), p.getId()).block());
                }
            });
        });

        Then("participants are in the {string} state", (String desiredState, DataTable dataTable) -> {
            participantNamesToActOn = dataTable.asList();
            coordination = service.get(coordination.getId()).block();
            coordination.getParticipants().values().stream()
                    .filter(p -> participantNamesToActOn.contains(p.getName()))
                    .forEach(p -> assertThat(p.getState().name(), equalTo(desiredState)));
        });

        When("I end the coordination", () -> {
            handleException(() -> service.end(coordination.getId()).block());
        });

        When("I end the coordination in {int} {string} from now", (Integer n, String timeUnit) -> {
            handleException(() -> {
                try {
                    TimeUnit.valueOf(timeUnit).sleep(n);
                } catch (InterruptedException e) {e.printStackTrace();}
                service.end(coordination.getId()).block();
            });
        });

        Then("an exception with message {string} is throw", (String message) -> {
            assertThat(caughtException, notNullValue());
            assertThat(caughtException.getMessage(), equalTo(message));
        });

        When("I wait {int} {string} from now", (Integer n, String timeUnit) -> {
            try {
                TimeUnit.valueOf(timeUnit).sleep(n);
            } catch (InterruptedException e) {e.printStackTrace();}
        });
    }

    @Before
    public void setUp(Scenario scenario) {
        currentScenarioTags.addAll(scenario.getSourceTagNames());
    }

    private void handleException(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            if (currentScenarioTags.contains("@handleExceptions")) {
                caughtException = throwable;
            } else {
                throw throwable;
            }
        }
    }
}
