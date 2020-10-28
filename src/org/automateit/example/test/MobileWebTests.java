package org.automateit.example.test;

import org.testng.annotations.Test;

import org.automateit.test.TestBase;

import org.automateit.example.page.YahooHomeMobileWebPage;
import org.automateit.example.page.FacebookHomeMobileWebPage;
import org.automateit.example.page.TwitterHomeMobileWebPage;

import org.automateit.web.pages.VerifyWebsiteAccessWorkflow;

/**
 * This class shows an example of how to use the AutomateIt! framework - and various useful features
 * for testing.
 * 
 * @author mburnside
 */
public class MobileWebTests extends TestBase {
    
    /**
     * The page object to use to interact with Yahoo home page
     */
    protected YahooHomeMobileWebPage yahooHomePage = null;
    
    /**
     * The page object to use to interact with Facebook home page
     */
    protected FacebookHomeMobileWebPage facebookHomePage = null;
    
    /**
     * The page object to use to interact with Twitter home page
     */
    protected TwitterHomeMobileWebPage twitterHomePage = null;
    
    /**
     * Verify that we can display the Yahoo web page on the mobile device web browser
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the Yahoo web page on the mobile device web browser", groups = { "example_mobileweb" })
    public void test_A_Validate_Yahoo() throws Exception {
      
        try { 
            
            this.yahooHomePage = new YahooHomeMobileWebPage(); 
            
            this.yahooHomePage.goToURL();
            
            this.yahooHomePage.validate();
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that we can display the Facebook web page on the mobile device web browser
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the Facebook web page on the mobile device web browser", groups = { "example_mobileweb" })
    public void test_B_Validate_Facebook() throws Exception {
      
        try { 
            
            this.facebookHomePage = new FacebookHomeMobileWebPage(); 
            
            this.facebookHomePage.goToURL();
            
            this.facebookHomePage.validate();
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that we can display the Twitter web page on the mobile device web browser
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the Twitter web page on the mobile device web browser", groups = { "example_mobileweb" })
    public void test_C_Validate_Twitter() throws Exception {
      
        try { 
            
            this.twitterHomePage = new TwitterHomeMobileWebPage(); 
            
            this.twitterHomePage.goToURL();
            
            this.twitterHomePage.validate();
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that we can display the Twitter web page on the mobile device web browser
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the Twitter web page on the mobile device web browser", groups = { "example_mobileweb" })
    public void test_D_Validate_Group_Of_Mobile_Web_Page_Access() throws Exception {
      
        try { 
            
            VerifyWebsiteAccessWorkflow verifyWebsiteAccessWorkflow = new VerifyWebsiteAccessWorkflow();
            
            verifyWebsiteAccessWorkflow.addPageToValidate(new YahooHomeMobileWebPage());
            verifyWebsiteAccessWorkflow.addPageToValidate(new FacebookHomeMobileWebPage());
            verifyWebsiteAccessWorkflow.addPageToValidate(new TwitterHomeMobileWebPage()); 
            
            verifyWebsiteAccessWorkflow.execute();
            
        }
        catch(Exception e) { throw e; }
    
    }
    
	
}