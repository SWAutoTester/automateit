package org.automateit.example.cucumber;

import org.testng.annotations.Test;

import cucumber.api.testng.CucumberFeatureWrapper;

import cucumber.api.CucumberOptions;

import org.automateit.cucumber.CucumberTestBase;

@CucumberOptions(
        features = {"resources/cucumber/demo"}, 
        glue = { "org.automateit.example.cucumber" },
        plugin = { "pretty" },
        strict = true)
public class ExampleTest extends CucumberTestBase { 
  
    /**
     * Validate that the home page appears and is correct
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the correct starting page", groups = { "cucumber_demo" }, dataProvider = "features")	
    public void exampleOpenYahooHomepageAndEnterSearchText(CucumberFeatureWrapper cucumberFeatureWrapper) throws Exception {
      
        testNGCucumberRunner.runCucumber(cucumberFeatureWrapper.getCucumberFeature());
       
    }
   
}