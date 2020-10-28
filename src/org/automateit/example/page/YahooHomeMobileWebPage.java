package org.automateit.example.page;

import org.automateit.web.pages.BaseMobileWebPage;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing a simple web page using the Page Object Creation Model
 * design pattern.
 * 
 * @author mburnside
 */
public class YahooHomeMobileWebPage extends BaseMobileWebPage {
    
    private final static String URL = "http://www.yahoo.com/";
    
    private final static String TEXTFIELD_SEARCH = "header-search-button";
    
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public YahooHomeMobileWebPage() throws Exception { super(URL); }
    
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public YahooHomeMobileWebPage(boolean goToURL) throws Exception {
        
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
        
        info("Validating the Yahoo homepage");
        
        try { validateWebElementContainsText_XPATH("Yahoo"); }
        catch(Exception e) { throw e; }
        finally {
            
            try { addScreenshotToReport(); }
            catch(Exception e) { }
            
        }
        
    }
    
    /**
     * Enter text into the search text field.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void enterSearchText(String text) throws Exception {
        
        info("Entering search text: " + text);
        
        try { 
            
            clickOnSearchButton();
            
            delay(5000);
                                        
            enterTextIntoWebElementUsingResourceId(TEXTFIELD_SEARCH, text); 
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Press on the Search button.
     * 
     * @throws Exception 
     */
    public void clickOnSearchButton() throws Exception {
        
        info("Clicking on the Search button");
        
        try { clickOnWebElementWithResourceId(TEXTFIELD_SEARCH); }
        catch(Exception e) { throw e; }
        
    }
	
}