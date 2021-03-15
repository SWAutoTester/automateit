package org.automateit.example.cucumber;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.automateit.cucumber.StepDefinitionsBase;

public class StepDefinitions extends StepDefinitionsBase {

    @Given("^I have a configured cucumber jvm project$")
    public void i_have_a_configured_cucumber_jvm_project()/* throws Exception*/ {
        // Write code here that turns the phrase above into concrete actions
        try { System.out.println("Hello1"); }
        catch(Exception e) { /*throw new PendingException();*/ }
    }

    @When("^I run it within my IDE$")
    public void i_run_it_within_my_IDE() throws Exception {
        // Write code here that turns the phrase above into concrete actions
        try { System.out.println("Hello2"); }
        catch(Exception e) { throw new PendingException(); }
    }

    @Then("^I will be able to run connected step definitions$")
    public void i_will_be_able_to_run_connected_step_definitions() throws Exception {
        // Write code here that turns the phrase above into concrete actions
        try { System.out.println("Hello3"); }
        catch(Exception e) { throw new PendingException(); }
    }

}

