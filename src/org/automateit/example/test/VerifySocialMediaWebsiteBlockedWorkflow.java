package org.automateit.example.test;

import org.automateit.web.pages.VerifyWebsiteAccessBlockedWorkflow;

import org.automateit.example.page.FacebookHomeMobileWebPage;
import org.automateit.example.page.TwitterHomeMobileWebPage;

/**
 * This class verifies that the web browser on the mobile device can access a website of web page
 */
public class VerifySocialMediaWebsiteBlockedWorkflow extends VerifyWebsiteAccessBlockedWorkflow {
  
    /**
     * Default Constructor
     */
    public VerifySocialMediaWebsiteBlockedWorkflow() throws Exception { 
        
        try { 
            
            addPageToValidate(new FacebookHomeMobileWebPage());
            addPageToValidate(new TwitterHomeMobileWebPage()); 
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}
