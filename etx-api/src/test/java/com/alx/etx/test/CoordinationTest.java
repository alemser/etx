package com.alx.etx.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"},
		features = {"classpath:features/coordination.feature"},
		glue = {"com.alx.etx.test"})
public class CoordinationTest {

}
