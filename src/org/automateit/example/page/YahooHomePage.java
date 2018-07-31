package org.automateit.example.page;

import org.automateit.web.pages.BasePage;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing a simple web page using the Page Object Creation Model
 * design pattern.
 * 
 * @author mburnside
 */
public class YahooHomePage extends BasePage {
    
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public YahooHomePage() throws Exception {
        
        try { init(); validate(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * This method has some validation checks for uniqueness on the page
     * that verifies that the user is seeing the correct content.
     * 
     * @throws Exception 
     */
    private void validate() throws Exception {
        
        info("Validating the Yahoo homepage");
        
        try { assertText("Yahoo"); }
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
        
        try { enterTextIntoWebElementUsingResourceId("uh-search-box", text); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Press on the Search button.
     * 
     * @throws Exception 
     */
    public void clickOnSearchButton() throws Exception {
        
        info("Clicking on the Search button");
        
        try { clickOnWebElementWithResourceId("uh-search-button"); }
        catch(Exception e) { throw e; }
        
    }
	
}