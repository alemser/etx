package com.alx.etx.test.coordination;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"},
		features = {"classpath:features/coordination-request.feature"},
		glue = {"com.alx.etx.test.coordination"})
public class CoordinationResourceTest {

}
