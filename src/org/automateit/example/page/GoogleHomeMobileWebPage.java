package org.automateit.example.page;

import org.automateit.mobileweb.MobileWebPageBase;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing a simple web page using the Page Object Creation Model
 * design pattern.
 * 
 * @author mburnside
 */
public class GoogleHomeMobileWebPage extends MobileWebPageBase {
    
    private final static String URL = "http://www.google.com/";
    
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public GoogleHomeMobileWebPage() throws Exception { super(URL); }
    
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public GoogleHomeMobileWebPage(boolean goToURL) throws Exception {
        
        super(URL);
        
        try { 
            
            if(goToURL) {
                
                open(URL);
                
                validate(); 
                
            } 
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * This method has some validation checks for uniqueness on the page
     * that verifies that the user is seeing the correct content.
     * 
     * @throws Exception 
     */
    public void validate() throws Exception {
        
        info("Validating the Google homepage");
        
        try { validateWebElementContainsText_XPATH("Sign in"); }
        catch(Exception e) { throw e; }
        finally {
            
            try { addScreenshotToReport(); }
            catch(Exception e) { }
            
        }
        
    }
	
}