package org.automateit.util;

import org.automateit.web.pages.VerifyWebsiteAccessBlockedWorkflow;

import org.automateit.mobile.MobileWebPage;

/**
 * This class verifies that the web browser on the mobile device can NOT access a website of web page
 */
public class VerifyMobileBrowserWebsiteBlockedWorkflow extends VerifyWebsiteAccessBlockedWorkflow {
  
    /**
     * Copy Constructor
     * 
     * @param mobileWebPage
     * 
     * @throws Exception 
     */
    public VerifyMobileBrowserWebsiteBlockedWorkflow(MobileWebPage mobileWebPage) throws Exception { addPageToValidate(mobileWebPage); }
    
}
