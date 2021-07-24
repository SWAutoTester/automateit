package org.automateit.example.page;

import org.automateit.mobileweb.MobileWebPageBase;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing a simple web page using the Page Object Creation Model
 * design pattern.
 * 
 * @author mburnside
 */
public class TwitterHomeMobileWebPage extends MobileWebPageBase {
    
    private final static String URL = "http://www.twitter.com/";
   
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public TwitterHomeMobileWebPage() throws Exception { super(URL); }
    
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public TwitterHomeMobileWebPage(boolean goToURL) throws Exception {
        
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
        
        info("Validating the Twitter homepage");
        
        try { validateWebElementContainsText_XPATH("Join Twitter today."); }
        catch(Exception e) { throw e; }
        finally {
            
            try { addScreenshotToReport(); }
            catch(Exception e) { }
            
        }
        
    }
	
}