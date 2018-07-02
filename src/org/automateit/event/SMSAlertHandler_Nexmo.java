/**
 * This file is part of Automate It!'s free and open source web and mobile 
 * application testing framework.
 * 
 * Automate It! is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Automate It! is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Automate It!.  If not, see <http://www.gnu.org/licenses/>.
 **/

package org.automateit.event;

import java.util.*; 
import javax.mail.*;  
import javax.mail.internet.*;  
//import javax.activation.*;  

import org.apache.log4j.Logger;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import org.automateit.util.Utils;

/**
 * The Alert Handler interface - SMS. 
 * 
 * This class im implemented as both a TestNG listener to make it easily added to the configuration
 * with needed to hard-code.
 * 
 * It registers itself during test startup.
 * 
 * @author mburnside
 */
public class SMSAlertHandler_Nexmo extends TestListenerAdapter implements AlertHandler {
    
    /**
     * The location of the email properties file
     */
    public static final String PROPERTIES_FILE = "./conf/sms.properties";
    
    /**
     * The framework utilities object that helps do stuff easy
     */
    private final Utils utils = new Utils();
    
    /**
     * The properties object
     */
    private Properties props = null;
    
    /**
     *  logging object
     */
    private static final Logger logger = Logger.getLogger(SMSAlertHandler_Nexmo.class);
    
    /**
     * Default Constructor
     */
    public SMSAlertHandler_Nexmo() { loadProperties(); }
    
    /**
     * Uniquely handle an alert event.
     * 
     * @param result
     * 
     * @throws Exception 
     */
    public void execute(ITestResult result) throws Exception { 
        
        try { 
            
            loadProperties();
            
            (new SendSMSThread_Nexmo(props, result)).start(); 
        
        }
        catch(Exception e) { logger.error(e); }  
    
    }
    
    /**
     * Uniquely handle an alert event.
     * 
     * @param message
     * 
     * @throws Exception 
     */
    public void execute(String message) throws Exception { 
        
        try { 
            
            loadProperties();
            
            (new SendSMSThread_Nexmo(props, message)).start(); 
        
        }
        catch(Exception e) { logger.error(e); }  
    
    }
    
    /**
     * Do actions after a test case execution failure.
     * 
     * @param result 
     */
    @Override
    public void onTestFailure(ITestResult result) { }

    /**
     * Do actions after a test case execution skipped.
     * 
     * @param result 
     */
    @Override
    public void onTestSkipped(ITestResult result) { }

    /**
     * Do actions after a test case execution success (no reported fail
     * during test case execution).
     * 
     * @param result 
     */
    @Override
    public void onTestSuccess(ITestResult result) { }
   
    /**
     * Do actions before a test case execution 
     * 
     * @param result 
     */
    @Override
    public void onTestStart(ITestResult result) { AlertEvent.getInstance().register(this);}
    
    /**
     * This method is invoked after all the tests have run and all their Configuration methods have been called.
     */
    @Override
    public void onFinish(ITestContext context) { }
    
    /**
     * Load the email properties
     */
    private void loadProperties() {
        
        if(props != null) return;
        
        try { props = utils.loadProperties(PROPERTIES_FILE); }
        catch(Exception e) { logger.error(e); }
        
    }
    
}

