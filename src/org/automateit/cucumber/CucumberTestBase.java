package org.automateit.cucumber;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;

import cucumber.api.testng.TestNGCucumberRunner;

import org.automateit.test.TestBase;

import org.automateit.core.CommonWebDriver;

public class CucumberTestBase extends TestBase { 
    
    protected TestNGCucumberRunner testNGCucumberRunner = null;
    
    @BeforeClass(alwaysRun = true)
    public void setUpClass() throws Exception { this.testNGCucumberRunner = new TestNGCucumberRunner(this.getClass()); }

    @DataProvider
    public Object[][] features() { return this.testNGCucumberRunner.provideFeatures(); }
    
    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception { 
        
        try { this.testNGCucumberRunner.finish();}
        catch(Exception e) { }
        finally { CommonWebDriver.getInstance().getWebDriver().quit(); }
    
    }
   
}