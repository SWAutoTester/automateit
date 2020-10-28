package org.automateit.util;

import org.automateit.web.pages.VerifyWebsiteAccessWorkflow;

import org.automateit.mobile.MobileWebPage;

/**
 * This class verifies that the web browser on the mobile device can access a website of web page
 */
public class VerifyMobileBrowserWebsiteAccessWorkflow extends VerifyWebsiteAccessWorkflow {
  
    /**
     * Copy Constructor
     * 
     * @param mobileWebPage
     * 
     * @throws Exception 
     */
    public VerifyMobileBrowserWebsiteAccessWorkflow(MobileWebPage mobileWebPage) throws Exception { addPageToValidate(mobileWebPage); }
    
}
