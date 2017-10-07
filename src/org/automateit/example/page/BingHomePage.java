package org.automateit.example.page;

import org.automateit.web.pages.BasePage;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing a simple web page using the Page Object Creation Model
 * design pattern.
 * 
 * @author mburnside
 */
public class BingHomePage extends BasePage {
    
    /**
     * Xpath element locator for the search box
     */
    public static final String ELEMENT_SEARCH_FIELD = "//*[@id = 'sb_form_q']";
    
    /**
     * Xpath element locator for the search button
     */
    public static final String ELEMENT_SEARCH_BUTTON = "//*[@class = 'b_searchboxSubmit')]";
    
    /**
     * Xpath element locator for the validation check
     */
    public static final String ELEMENT_VALIDATION = "//*[contains(text(), 'Google')]";
    
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public BingHomePage() throws Exception {
        
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
        
        try { assertText("Bing"); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Enter text into the search text field.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void enterSearchText(String text) throws Exception {
        
        try { type(ELEMENT_SEARCH_FIELD, text); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Press on the Search button.
     * 
     * @throws Exception 
     */
    public void clickOnSearchButton() throws Exception {
        
        try { click(ELEMENT_SEARCH_BUTTON); }
        catch(Exception e) { throw e; }
        
    }
	
}