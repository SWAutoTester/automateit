package org.automateit.example.test;

import org.testng.annotations.Test;

import org.automateit.test.TestBase;

import org.automateit.example.page.YahooHomePage;

/**
 * This class shows an example of how to use the AutomateIt! framework - and various useful features
 * for testing.
 * 
 * @author mburnside
 */
public class ExampleTests extends TestBase {
    
    /**
     * The page object top use to interact with Yahoo home page
     */
    protected YahooHomePage yahooHomePage = null;
    
    /**
     * The scroll interaction limit for example purposes
     */
    public static final int SCROLLINTERACTIONLIMIT = 5;
    
    /**
     * The scroll pixel value (scroll up or down this many pixels)
     */
    public static final int SCROLLPIXELS = 500;
    
    /**
     * Validate that the home page appears and is correct
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
        finally { this.yahooHomePage.addScreenshotToReport(); }
    
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
    
    /**
     * Validate that the framework can scroll down a web page
     *
     * @throws Exception 
     */
    @Test(description = "Validate that the framework can scroll down a web page", groups = { "example" })
    public void test_D_Validate_Scroll_Down() throws Exception {
      
        try { 
            
            this.yahooHomePage = new YahooHomePage(); 
            
            delay(2000);
            
            for(int i = 0; i < SCROLLINTERACTIONLIMIT; i++) { 
                
                this.yahooHomePage.scrollDown(SCROLLPIXELS); 
                
                delay(1000); 
                
                this.yahooHomePage.addScreenshotToReport(); 
            
            }
            
            delay(3000);
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Validate that the framework can scroll up a web page
     *
     * @throws Exception 
     */
    @Test(description = "Validate that the framework can scroll up a web page", groups = { "example" })
    public void test_E_Validate_Scroll_Up() throws Exception {
      
        try { 
           
            for(int i = 0; i < SCROLLINTERACTIONLIMIT; i++) { 
                
                this.yahooHomePage.scrollUp(SCROLLPIXELS); 
                
                delay(1000); 
                
                this.yahooHomePage.addScreenshotToReport(); 
            
            }
            
            delay(3000);
            
            this.yahooHomePage.close(); 
        
        }
        catch(Exception e) { throw e; }
    
    }
	
}