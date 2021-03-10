package org.automateit.example.cucumber;

import cucumber.api.PendingException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

import org.automateit.test.TestBase;

import org.automateit.example.page.YahooHomePage;

import cucumber.api.Scenario;
import cucumber.api.java.Before;

/**
 * This class shows an example of how to use the AutomateIt! framework - and various useful features
 * for testing.
 * 
 * @author mburnside
 */
public class Stepdefs extends TestBase {
    
    private Scenario scenario = null;

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }
    
    YahooHomePage yahooHomePage = null;
   
    @Given("^I open my browser to Yahoo homepage$")
    public void I_open_my_browser_to_Yahoo_homepage() throws Exception {
        
        info("I open my browser to Yahoo homepage");
        scenario.write( "I open my browser to Yahoo homepage" );
        
        try { this.yahooHomePage = new YahooHomePage(); }
        catch(Exception e) { throw e; }
        finally { 
            
            try { this.yahooHomePage.addScreenshotToReport(); }
            catch(Exception le) { }
        
        }
        
    }

    @When("^I enter Search text \"([^\"]*)\"$")
    public void I_enter_Search_text(String text) throws Exception {
        
        info("I enter Search text: " + text);
        
        try { 
            
            this.yahooHomePage.enterSearchText(text);
            
            delay(2000); // wait 2 seconds
            
        }
        catch(Exception e) { throw e; }
        finally { 
            
            try { this.yahooHomePage.addScreenshotToReport(); }
            catch(Exception le) { }
        
        }
        
    }

    @Then("^I will see the Search results$")
    public void I_will_see_the_Search_results() throws Exception {
        
        info("I will see the Search results and close the browser");
        
        try { this.yahooHomePage.close(); }
        catch(Exception e) { throw e; }
        finally { 
            
            try { this.yahooHomePage.addScreenshotToReport(); }
            catch(Exception le) { }
        
        }
        
    }
}