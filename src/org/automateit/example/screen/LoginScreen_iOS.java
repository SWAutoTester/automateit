package org.automateit.example.screen;

import org.apache.log4j.Logger;

/**
 * This class is the iOS app screen interaction class
 * 
 * @author michaelburnside
 */
public class LoginScreen_iOS extends LoginScreen {
   
    protected final static String RESOURCE_ID_TITLE = "Automate It!";
    
    protected final static String RESOURCE_ID_TEXTFIELD_USERNAME = "textfield_username";
    
    protected final static String RESOURCE_ID_TEXTFIELD_PASSWORD = "textfield_password";
    
    protected final static String RESOURCE_ID_BUTTON_LOGIN = "Sign In";
    
    protected final static String RESOURCE_ID_ALERT_TITLE = "//XCUIElementTypeStaticText[@name=\"Login Attempt Failed\"]";
    
    protected final static String RESOURCE_ID_ALERT_MESSAGE = "Please enter a valid username/password credentials!";
    
    /**
     * Logging class
     */
    private final Logger logger = Logger.getLogger(LoginScreen_iOS.class);
    
    /**
     * Default Constructor
     * 
     * @throws Exception 
     */
    public LoginScreen_iOS() throws Exception {
        
        super(RESOURCE_ID_TITLE, RESOURCE_ID_TEXTFIELD_USERNAME, RESOURCE_ID_TEXTFIELD_PASSWORD, RESOURCE_ID_BUTTON_LOGIN);
        
    }
	
    /**
     * Click/Tap on the "OK" button that appears on the modal popup window.
     * 
     * @throws Exception 
     */
    public void clickOKButtonOnPopupWindow() throws Exception {
        
        try { 
            
            logger.info("Clicking on the OK button on popup window");
            
            clickOnWebElementMatchingText("OK", "XCUIElementTypeButton");
        
        }
        catch(Exception e) { throw e; }
        
    }

    /**
     * Validate the user sees the correct components/text on
     * the modal popup window that appears after a failed login.
     *
     * @throws Exception
     */
    public void validateFailedLoginPopupWindow() throws Exception {
        
        try { 
            
            logger.info("Validating that the failed login popup window and correct content appears to the user");
            
            validateWebElementContainingText(TITLE_LOGIN_FAIL, "XCUIElementTypeStaticText");
            
            validateWebElementContainingText(MESSAGE_LOGIN_FAIL_INVALID_CREDENTIALS, "XCUIElementTypeStaticText");
           
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Validate the user sees the correct components/text on
     * the modal popup window that appears after a successful login.
     *
     * @throws Exception
     */
    public void validateSuccessfulLoginPopupWindow() throws Exception {
        
        try { 
            
            logger.info("Validating that the failed login popup window and correct content appears to the user");
            
            validateWebElementContainingText(TITLE_LOGIN_SUCCESS, "XCUIElementTypeStaticText");
            
            validateWebElementContainingText(MESSAGE_LOGIN_SUCCEEDED, "XCUIElementTypeStaticText");
           
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Validate the user sees the correct components/text on
     * the modal popup window that appears after not entering all of 
     * the required fields.
     *
     * @throws Exception
     */
    public void validateRequiredFieldsMissingPopupWindow() throws Exception {
        
        try { 
            
            logger.info("Validating that the failed login popup window and correct content appears to the user");
            
            validateWebElementContainingText(TITLE_LOGIN_FAIL, "XCUIElementTypeStaticText");
            
            validateWebElementContainingText(MESSAGE_LOGIN_FAIL_REQUIRED_INPUT, "XCUIElementTypeStaticText");
           
        }
        catch(Exception e) { throw e; }
        
    }
    
}

