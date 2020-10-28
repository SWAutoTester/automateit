package org.automateit.mobile;

import org.apache.log4j.Logger;

import org.automateit.web.pages.BaseMobileWebPage;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing a simple web page using the Page Object Creation Model
 * design pattern.
 * 
 * This is a generic class that can be used to validate any web page
 * rendered on a mobile device.
 * 
 * @author mburnside
 */
public class MobileWebPage extends BaseMobileWebPage {
   
    private String validateExpectedVisibleText = null;
    
    /**
     * The logging class to use for useful information
     */
    protected Logger logger = Logger.getLogger(MobileWebPage.class);
   
    /**
     * Copy Constructor.
     * 
     * @param urla
     * @param validateExpectedVisibleText
     * 
     * @throws Exception 
     */
    public MobileWebPage(String urla, String validateExpectedVisibleText) throws Exception { 
        
        super(urla); 
        
        url = urla;
    
        this.validateExpectedVisibleText = validateExpectedVisibleText;
        
    }
    
    /**
     * Copy Constructor.
     * 
     * @param urla
     * @param validateExpectedVisibleText
     * @param udida
     * 
     * @throws Exception 
     */
    public MobileWebPage(String urla, String validateExpectedVisibleText, String udida) throws Exception { 
        
        super(urla); 
        
        url = urla;
        
        udid = udida;
    
        this.validateExpectedVisibleText = validateExpectedVisibleText;
        
    }
    
    /**
     * This method has some validation checks for uniqueness on the page
     * that verifies that the user is seeing the correct content.
     * 
     * @throws Exception 
     */
    public void validate() throws Exception {
        
        info("Validating visible text on web page: " + validateExpectedVisibleText);
        logger.info("Validating visible text on web page: " + validateExpectedVisibleText);
        
        try { 
            
            open(url);
            
            delay(10000); // allow the web page to fully load within the browser
            
            validateWebElementContainsText_XPATH(validateExpectedVisibleText); 
        
        }
        catch(Exception e) { printDOM(); throw e; }
        finally {
            
            try { addScreenshotToReport(); }
            catch(Exception e) { }
            
        }
        
    }
	
}