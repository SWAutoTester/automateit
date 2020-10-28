package org.automateit.example.test;

import org.automateit.web.pages.VerifyWebsiteAccessWorkflow;

import org.automateit.example.page.GoogleHomeMobileWebPage;
import org.automateit.example.page.YahooHomeMobileWebPage;

/**
 * This class verifies that the web browser on the mobile device can access a website of web page
 */
public class VerifySearchEngineWebsiteAccessWorkflow extends VerifyWebsiteAccessWorkflow {
  
    /**
     * Default Constructor
     */
    public VerifySearchEngineWebsiteAccessWorkflow() throws Exception { 
        
        try { 
            
            addPageToValidate(new GoogleHomeMobileWebPage());
            addPageToValidate(new YahooHomeMobileWebPage()); 
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}
