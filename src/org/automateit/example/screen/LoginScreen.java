package org.automateit.example.screen;

import org.apache.log4j.Logger;

import org.automateit.mobile.BaseScreen;

/**
 * This class is an abstract class for test classes to interact with the login screen
 * components. The implementation class needs to extend this interface.
 * 
 * @author michaelburnside
 */
public abstract class LoginScreen extends BaseScreen {
    
    protected static final String SCREEN_NAME = "Login Screen";
    
    protected String resourceId_title = null;
    
    protected String resourceId_textfield_username = null;
    
    protected String resourceId_textfield_password = null;
    
    protected String resourceId_button_login = null;
    
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(LoginScreen.class);
    
    /**
     * Copy Constructor.
     * 
     * @param resourceId_title
     * @param resourceId_textfield_username
     * @param resourceId_textfield_password
     * @param resourceId_button_login
     * 
     * @throws Exception 
     */
    public LoginScreen(String resourceId_title, String resourceId_textfield_username, String resourceId_textfield_password, String resourceId_button_login) throws Exception {
        
        super();
        
        try {
            
            logger.info("Creating a new instance of: " + SCREEN_NAME);
            
            this.resourceId_title = resourceId_title;
            
            this.resourceId_textfield_username = resourceId_textfield_username;
            
            this.resourceId_textfield_password = resourceId_textfield_password;
            
            this.resourceId_button_login = resourceId_button_login;
            
            validate();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Validates the login screen and that all components are visible
     * to the user.
     * 
     * @throws Exception 
     */
    public void validate() throws Exception {
        
        try {
            
            // if the framework can find the element, that means that it is visible
            // and enabled on the UI
            
            getWebElementWithId(resourceId_title);
            
            getWebElementWithId(resourceId_textfield_username);
            
            getWebElementWithId(resourceId_textfield_password);
            
            getWebElementWithId(resourceId_button_login);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Enter text into the Username text field.
     * 
     * @param username The username
     * 
     * @throws Exception 
     */
    public void enterUsername(String username) throws Exception {
        
        try {
            
            clearWebElementByResourceId(resourceId_textfield_username);
            
            enterDataIntoWebElementByResourceId(resourceId_textfield_username, username);
           
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Enter text into the Password text field.
     * 
     * @param password The password
     * 
     * @throws Exception 
     */
    public void enterPassword(String password) throws Exception {
        
        try {
            
            clearWebElementByResourceId(resourceId_textfield_password);
            
            enterDataIntoWebElementByResourceId(resourceId_textfield_password, password);
            
        }
        catch(Exception e) { throw e; }
    }
    
    /**
     * Click/Tap on the "Sign In" button.
     * 
     * @throws Exception 
     */
    public void clickLoginButton() throws Exception {
        
        try { clickUsingResourceId(resourceId_button_login); }
        catch(Exception e) { throw e; }
        
    }
	
    /**
     * Click/Tap on the "OK" button that appears on the modal popup window.
     * 
     * @throws Exception 
     */
    public abstract void clickOKButtonOnPopupWindow() throws Exception;

    /**
     * Validate the user sees the correct components/text on
     * the modal popup window that appears after a failed login.
     *
     * @throws Exception
     */
    public abstract void validateFailedLoginPopupWindow() throws Exception;
    
    /**
     * Validate the user sees the correct components/text on
     * the modal popup window that appears after not entering all of 
     * the required fields.
     *
     * @throws Exception
     */
    public abstract void validateRequiredFieldsMissingPopupWindow() throws Exception;
    
    /**
     * Validate the user sees the correct components/text on
     * the modal popup window that appears after a successful login.
     *
     * @throws Exception
     */
    public abstract void validateSuccessfulLoginPopupWindow() throws Exception;
    
}

