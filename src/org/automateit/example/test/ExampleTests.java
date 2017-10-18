package org.automateit.example.test;

import org.testng.annotations.Test;

import org.automateit.test.TestBase;

import org.automateit.example.page.YahooHomePage;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing.
 * 
 * @author mburnside
 */
public class ExampleTests extends TestBase {
    
    protected YahooHomePage yahooHomePage = null;
    
    /**
     * Validate the the home page appears and is correct
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the correct starting page", groups = { "example" })
    public void test_A_Validate_Start_Page() throws Exception {
      
        try { this.yahooHomePage = new YahooHomePage(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Test that the user can enter a search term and that the expected result
     * appear.
     *
     * @throws Exception 
     */
    @Test(description = "Verify that search will execute", groups = { "example" })
    public void test_B_Validate_Search() throws Exception {
      
        try { 
            
            this.yahooHomePage.enterSearchText("Automate It");
            
            delay(2000); // wait 2 seconds
            
        }
        catch(Exception e) { this.yahooHomePage.printDOM(); throw e; }
    
    }
    
    /**
     * Test that the user can close the app in the browser
     *
     * @throws Exception 
     */
    @Test(description = "Verify that the user can close the app in the browser", groups = { "example" })
    public void test_C_Validate_Close_App() throws Exception {
      
        try { this.yahooHomePage.close(); }
        catch(Exception e) { this.yahooHomePage.printDOM(); throw e; }
    
    }
	
}