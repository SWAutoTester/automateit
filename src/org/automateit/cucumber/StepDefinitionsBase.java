package org.automateit.cucumber;

import io.cucumber.java.AfterStep;
import io.cucumber.java.After;
import io.cucumber.java.BeforeStep;

import cucumber.api.Scenario;

import org.automateit.test.TestBase;

import org.automateit.core.CommonWebDriver;

/**
 * All specific Step Definition classes should EXTEND this class. It provides:
 * 
 * 1) Before scenario - create a Scenario object (to write output to console/logs)
 * 2) After Step - add a screenshot
 * 3) After scenario - close mobile app / web browser 
 */
public class StepDefinitionsBase extends TestBase { 
    
    protected Scenario scenario = null;
    
    @BeforeStep
    public void beforeStep(Scenario scenario) { this.scenario = scenario; }
    
    @AfterStep
    public void afterStep() {
        
        try { addScreenshotToReport(); }
        catch(Exception e) { }


    }
    
    @After
    public void afterScenario() {
        
        try { if(CommonWebDriver.getInstance().getWebDriver() != null) CommonWebDriver.getInstance().getWebDriver().quit(); }
        catch(Exception e) { }
       
    }
    
    /**
     * Write to scenario (console output/logs)
     * 
     * @param text 
     */
    protected void writeToScenario(String text) {
        
        try { scenario.write(text); }
        catch(Exception e) { }
        
    }
   
}