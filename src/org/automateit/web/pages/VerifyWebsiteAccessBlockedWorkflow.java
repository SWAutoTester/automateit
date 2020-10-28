package org.automateit.web.pages;

import org.apache.log4j.Logger;

/**
 * This class verifies that the web browser on the mobile device can not access a website of web page
 */
public class VerifyWebsiteAccessBlockedWorkflow extends BaseVerifyWebsiteAccessWorkflow {
    
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(VerifyWebsiteAccessBlockedWorkflow.class);
    
    /**
     * Default Constructor
     */
    public VerifyWebsiteAccessBlockedWorkflow() { super(); }
    
    /**
     * This method will iterate through the list of web pages and validate them.
     * 
     * @throws Exception 
     */
    public void execute() throws Exception {
        
        
        logger.info("Validating base mobile web page access: " + list.size());
        info("Validating base mobile web page access: " + list.size());
        
        try {
            
            for(BaseMobileWebPage baseMobileWebPage: list) {
                
                baseMobileWebPage.goToURL();
                
                delay(5000);
            
                baseMobileWebPage.verifyBlockedAccess();
            
            }
            
        }
        catch(Exception e) { 
            
            if(e.getMessage().contains("ERR_NAME_NOT_RESOLVED")) return; // we dont count this type of error as test failure
            else throw e; 
        }
        finally {
            
            try { addScreenshotToReport(); }
            catch(Exception e) { }
            
            // delay 60 seconds
            // add this delay as a temporary attempt at fix for chrome crash after launch by webdriver
            // (allows previous session to expire and clear on the service)
            delay(30000);
            
        }
        
    }
    
}
