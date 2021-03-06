package org.automateit.example.screen;

import org.apache.log4j.Logger;

/**
 * This class is the Android app screen interaction class
 * 
 * @author michaelburnside
 */
public class LoginScreen_Android extends LoginScreen {
   
    protected final static String RESOURCE_ID_TITLE = "automateit.org.demoapp:id/label_title_1";
    
    protected final static String RESOURCE_ID_TEXTFIELD_USERNAME = "automateit.org.demoapp:id/textfield_username";
    
    protected final static String RESOURCE_ID_TEXTFIELD_PASSWORD = "automateit.org.demoapp:id/textfield_password";
    
    protected final static String RESOURCE_ID_BUTTON_LOGIN = "automateit.org.demoapp:id/button_login";
    
    protected final static String RESOURCE_ID_ALERT_TITLE = "android:id/alertTitle";
    
    protected final static String RESOURCE_ID_ALERT_MESSAGE = "android:id/message";
    
    
    
    /**
     * Logging class
     */
    private final Logger logger = Logger.getLogger(LoginScreen_Android.class);
    
    /**
     * Default Constructor
     * 
     * @throws Exception 
     */
    public LoginScreen_Android() throws Exception {
        
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
            
            clickOnWebElementMatchingTextValueAttribute("Ok", "android.widget.Button");
        
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
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_TITLE, TITLE_LOGIN_FAIL);
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_MESSAGE, MESSAGE_LOGIN_FAIL_INVALID_CREDENTIALS);
            
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
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_TITLE, TITLE_LOGIN_SUCCESS);
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_MESSAGE, MESSAGE_LOGIN_SUCCEEDED);
           
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
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_TITLE, TITLE_LOGIN_FAIL);
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_MESSAGE, MESSAGE_LOGIN_FAIL_REQUIRED_INPUT);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}

