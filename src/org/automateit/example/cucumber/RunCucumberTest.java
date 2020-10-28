package org.automateit.example.cucumber;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;

import cucumber.api.testng.TestNGCucumberRunner;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.CucumberOptions;

@CucumberOptions(
        features = {"resources/cucumber/poc"}, 
        glue = { "org.automateit.example.cucumber" },
        strict = true)
public class RunCucumberTest { 
    
    private TestNGCucumberRunner testNGCucumberRunner = null;
    
    @BeforeClass(alwaysRun = true)
    public void setUpClass() throws Exception { this.testNGCucumberRunner = new TestNGCucumberRunner(this.getClass()); }
    
    /**
     * Validate that the home page appears and is correct
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the correct starting page", groups = { "cucumber_poc" }, dataProvider = "features")	
    public void feature(CucumberFeatureWrapper cucumberFeatureWrapper) throws Exception {
      
        this.testNGCucumberRunner.runCucumber(cucumberFeatureWrapper.getCucumberFeature());
       
    }
    
    @DataProvider
    public Object[][] features() { return this.testNGCucumberRunner.provideFeatures(); }
    
    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception { this.testNGCucumberRunner.finish(); }

}
