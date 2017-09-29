package org.automateit.example.test;

import org.testng.annotations.Test;

import org.automateit.test.TestBase;

import org.automateit.example.page.GoogleHomePage;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing.
 * 
 * @author mburnside
 */
public class ExampleTests extends TestBase {
    
    protected GoogleHomePage googleHomePage = null;
    
    /**
     * Test a failed login use case - using spaced.
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the correct starting page", groups = { "example" })
    public void test_A_Validate_Start_Page() throws Exception {
      
        try { this.googleHomePage = new GoogleHomePage(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Test that the user can enter a search term and that the expected result
     * appear.
     *
     * @throws Exception 
     */
    @Test(description = "Verify that search will execute", groups = { "example" })
    public void test_B_Validate_Search_Works() throws Exception {
      
        try { 
            
            this.googleHomePage.enterSearchText("Automate It");
            
            delay(2000); // wait 2 seconds
            
            this.googleHomePage.clickOnSearchButton();
             
            delay(5000); // wait seconds
            
        }
        catch(Exception e) { this.googleHomePage.printDOM(); throw e; }
    
    }
	
}