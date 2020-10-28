package org.automateit.web.pages;

import java.util.List;
import java.util.ArrayList;

import org.automateit.test.TestBase;

/**
 * This class verifies that the web browser on the mobile device can access a website of web page
 */
public abstract class BaseVerifyWebsiteAccessWorkflow extends TestBase {
    
    /**
     * This is the list of base mobile web pages to validate
     */
    protected List<BaseMobileWebPage> list = new ArrayList<BaseMobileWebPage>();
    
    /**
     * Default Constructor
     */
    public BaseVerifyWebsiteAccessWorkflow() { }
    
    /**
     * Add a page to validate.
     * 
     * @param baseMobileWebPage 
     */
    public void addPageToValidate(BaseMobileWebPage baseMobileWebPage) { list.add(baseMobileWebPage); }
    
    /**
     * This method will iterate through the list of web pages and validate them.
     * 
     * @throws Exception 
     */
    public abstract void execute() throws Exception;
    
}
