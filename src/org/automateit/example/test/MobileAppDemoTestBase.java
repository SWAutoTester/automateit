package org.automateit.example.test;

import org.automateit.data.DataDrivenInput;

import org.automateit.test.TestBase;

import org.automateit.example.screen.LoginScreen;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing an Android demo app.
 * 
 * @author mburnside
 */
public class MobileAppDemoTestBase extends TestBase {
    
    /**
     * The valid username data set id
     */
    public static final String VALID_USERNAME_DATA_SET_ID = "username_valid";
    
    /**
     * The valid password data set id
     */
    public static final String VALID_PASSWORD_DATA_SET_ID = "password_valid";
    
    /**
     * The invalid username data set id
     */
    public static final String INVALID_USERNAME_DATA_SET_ID = "username_invalid";
    
    /**
     * The invalid password data set id
     */
    public static final String INVALID_PASSWORD_DATA_SET_ID = "password_invalid";
    
    /**
     * The screen class instance that we use for all of the tests
     */
    protected LoginScreen loginScreen = null;
    
    /**
     * The file where the input data is in
     */
    protected final static String DATA_INPUT_FILE = "data/login.csv"; 
    
    /**
     * The test data object for retrieving values
     */
    protected DataDrivenInput testData = null;
    
    /**
     * Read in all of the data that we need for the tests.
     * 
     * @throws Exception 
     */
    protected void setupInputData() throws Exception {
        
        try { testData = setupDataDrivenInput(DATA_INPUT_FILE); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Verify that the app loads correctly on the device and on the correct Activity.
     * 
     * @param loginScreen
     *
     * @throws Exception 
     */
    public void validateLoginScreen(LoginScreen loginScreen) throws Exception {
      
        try { this.loginScreen = loginScreen; loginScreen.validate(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Try to login
     * 
     * @param usernameDataSetId
     * @param passwordDataSetId
     * 
     * @throws Exception 
     */
    public void executeLoginAttempt(String usernameDataSetId, String passwordDataSetId) throws Exception {
      
        try { 
            
            if(usernameDataSetId != null) loginScreen.enterUsername(testData.returnInputDataForDataIdAndColumnNumber(usernameDataSetId, 1));
            else loginScreen.enterUsername("");
            
            if(passwordDataSetId != null) loginScreen.enterPassword(testData.returnInputDataForDataIdAndColumnNumber(passwordDataSetId, 1));
            else loginScreen.enterPassword("");
            
            loginScreen.clickLoginButton();
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that the user is aware that the login failed
     *
     * @throws Exception 
     */
    public void verifyFailedLoginMessageAppears() throws Exception {
      
        try { loginScreen.validateFailedLoginPopupWindow(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that the user is aware that the login failed
     *
     * @throws Exception 
     */
    public void verifyRequiredFieldsMessageAppears() throws Exception {
      
        try { loginScreen.validateRequiredFieldsMissingPopupWindow(); }
        catch(Exception e) { throw e; } 
    
    }
    
    /**
     * Verify that the user can login successfully and see the success message alert popup window
     *
     * @throws Exception 
     */
    public void verifySuccessfulLogin() throws Exception {
      
        try { loginScreen.validateSuccessfulLoginPopupWindow(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that the user can close the alert message window/popup
     *
     * @throws Exception 
     */
    public void closeAlertMessage() throws Exception {
      
        try { loginScreen.clickOKButtonOnPopupWindow(); }
        catch(Exception e) { throw e; }
    
    }
	
}