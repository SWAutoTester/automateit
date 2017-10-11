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
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_TITLE, "Login Attempt Failed");
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_MESSAGE, "Please enter a valid username/password credentials");
            
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
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_TITLE, "Login Attempt Succeeded");
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_MESSAGE, "Congratulations");
           
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
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_TITLE, "Login Attempt Failed");
            
            validateElementWithResourceIdContainingText(RESOURCE_ID_ALERT_MESSAGE, "Please enter data in both username/password text fields");
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}

