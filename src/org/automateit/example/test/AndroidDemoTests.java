package org.automateit.example.test;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.automateit.example.screen.LoginScreen_Android;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing an Android demo app.
 * 
 * @author mburnside
 */
public class AndroidDemoTests extends MobileAppDemoTestBase {
   
    /**
     * Verify that the app loads correctly on the device and on the correct Activity.
     *
     * @throws Exception 
     */
    @BeforeTest(description = "Verify that we can display the correct starting page", groups = { "android" })
    public void setupInputDataBeforeTestExecution() throws Exception {
      
        try { setupInputData(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that the app loads correctly on the device and on the correct Activity.
     *
     * @throws Exception 
     */
    @Test(description = "Verify that we can display the correct starting page", groups = { "android" }, priority = 1)
    public void test_A_Validate_Login_Screen() throws Exception {
      
        try { 
            
            loginScreen = new LoginScreen_Android();
            
            //validateLoginScreen(loginScreen); 
            
            loginScreen.validateNotificationReceivedAndGoBack("⁨AutoBudd");
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that login fails when not providing a password
     *
     * @throws Exception 
     */
    //@Test(description = "Verify that login fails when not providing a password", groups = { "android" }, priority = 2)
    public void test_B_Validate_Failed_Login_Blank_Password() throws Exception {
      
        try { 
            
            executeLoginAttempt(VALID_USERNAME_DATA_SET_ID, null); 
            
            verifyRequiredFieldsMessageAppears();
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that the user can close the alert message popup window
     *
     * @throws Exception 
     */
    //@Test(description = "Verify that the user can close the alert message popup window", groups = { "android" }, priority = 3)
    public void test_C_Validate_Close_Alert_Message_Window() throws Exception {
      
        try { closeAlertMessage(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Verify that login fails when not providing a username
     *
     * @throws Exception 
     */
    //@Test(description = "Verify that login fails when not providing a username", groups = { "android" }, priority = 4)
    public void test_D_Validate_Failed_Login_Blank_Username() throws Exception {
      
        try { 
            
            executeLoginAttempt(null, VALID_PASSWORD_DATA_SET_ID); 
            
            verifyRequiredFieldsMessageAppears();
        
        }
        catch(Exception e) { throw e; }
        finally { closeAlertMessage(); }
    
    }
    
    /**
     * Verify that login fails when providing a valid username and incorrect password
     *
     * @throws Exception 
     */
    //@Test(description = "Verify that login fails when providing a valid username and incorrect password", groups = { "android" }, priority = 5)
    public void test_E_Validate_Failed_Login_Valid_Username_Invalid_Password() throws Exception {
      
        try { 
            
            executeLoginAttempt(VALID_USERNAME_DATA_SET_ID, INVALID_PASSWORD_DATA_SET_ID); 
            
            verifyFailedLoginMessageAppears();
        
        }
        catch(Exception e) { throw e; }
        finally { closeAlertMessage(); }
    
    }
    
    
    
    /**
     * Verify that login fails when providing a valid username and incorrect password
     *
     * @throws Exception 
     */
    //@Test(description = "Verify that login fails when providing a valid username and incorrect password", groups = { "android" }, priority = 6)
    public void test_F_Validate_Failed_Login_Invalid_Username_Valid_Password() throws Exception {
      
        try { 
            
            executeLoginAttempt(INVALID_USERNAME_DATA_SET_ID, VALID_PASSWORD_DATA_SET_ID); 
            
            verifyFailedLoginMessageAppears();
        
        }
        catch(Exception e) { throw e; }
        finally { closeAlertMessage(); }
    
    }
    
    /**
     * Verify that login succeeds when providing a valid username and valid password
     *
     * @throws Exception 
     */
    //@Test(description = "Verify that login succeeds when providing a valid username and valid password", groups = { "android" }, priority = 7)
    public void test_G_Validate_Successful_Login() throws Exception {
      
        try { 
            
            executeLoginAttempt(VALID_USERNAME_DATA_SET_ID, VALID_PASSWORD_DATA_SET_ID); 
            
            verifySuccessfulLogin();
        
        }
        catch(Exception e) { throw e; }
        finally { closeAlertMessage(); }
    
    }
    
    /**
     * Verify that the user can close the app in on the device
     *
     * @throws Exception 
     */
    //@Test(description = "Verify that the user can close the app in on the device", groups = { "android" }, priority = 100)
    public void test_H_Validate_Close_App() throws Exception {
      
        try { loginScreen.quit(); }
        catch(Exception e) { throw e; }
    
    }
	
}