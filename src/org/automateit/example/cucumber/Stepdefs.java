package org.automateit.example.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
//import cucumber.api.java.Before;
//import cucumber.api.java.After;

//import cucumber.api.Scenario;

import org.automateit.cucumber.StepDefinitionsBase;

import org.automateit.example.page.YahooHomePage;

//import org.automateit.core.CommonWebDriver;

/**
 * This class shows an example of how to use the AutomateIt! framework - and various useful features
 * for testing.
 * 
 * @author mburnside
 */
public class Stepdefs extends StepDefinitionsBase {
   
    YahooHomePage yahooHomePage = null;
   
    @Given("^I open my browser to Yahoo homepage$")
    public void I_open_my_browser_to_Yahoo_homepage() throws Exception {
        
        info("I open my browser to Yahoo homepage");
        writeToScenario( "I open my browser to Yahoo homepage" );
        
        try { this.yahooHomePage = new YahooHomePage(); }
        catch(Exception e) { throw e; }
        
    }

    @When("^I enter Search text \"([^\"]*)\"$")
    public void I_enter_Search_text(String text) throws Exception {
        
        info("I enter Search text: " + text);
        
        try { 
            
            this.yahooHomePage.enterSearchText(text);
            
            delay(2000); // wait 2 seconds
            
        }
        catch(Exception e) { throw e; }
        
    }

    @Then("^I will see the Search results$")
    public void I_will_see_the_Search_results() throws Exception {
        
        info("I will see the Search results and close the browser");
        
        delay(2000); // pretend to see the search results, in reality would add validation
        // to verify certain text is visible (search returned expected results)
        
    }
    /*
    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }
    
    @After
    public void after(Scenario scenario) {
        
        try { addScreenshotToReport(); }
        catch(Exception le) { }
        
        scenario.write("Scenario finished - closing the mobile app / web browser");
        
        CommonWebDriver.getInstance().close();
        
    }
*/
    
}