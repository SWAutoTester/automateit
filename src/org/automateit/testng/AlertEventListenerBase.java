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

package org.automateit.testng;

import org.apache.log4j.Logger;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import org.automateit.event.AlertEvent;
import org.automateit.util.Utils;

/**
 * This class is added to testng task to listen for events.
 * 
 * It will handle the sending of alerts if a test fails/skipped and is 
 * identified as a test needing an alert to be sent
 *
 * @author mburnside
 */
public class AlertEventListenerBase extends TestListenerAdapter {
    
    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    protected static Logger logger = Logger.getLogger(AlertEventListenerBase.class);

    /**
     * testng utils class
     */
    protected TestNGUtils testNGUtils = new TestNGUtils();
    
    /**
     * utils class
     */
    protected Utils utils = new Utils();
    
    /**
     * Do actions after a test case execution failure.
     * 
     * @param result 
     */
    @Override
    public void onTestFailure(ITestResult result) { sendAlert(result); }

    /**
     * Do actions after a test case execution skipped.
     * 
     * @param result 
     */
    @Override
    public void onTestSkipped(ITestResult result) { sendAlert(result); }

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
    public void onTestStart(ITestResult result) { }
    
    /**
     * This method is invoked after all the tests have run and all their Configuration methods have been called.
     */
    @Override
    public void onFinish(ITestContext context) { }
    
    /**
     * This method does all reporting into TestNG/ReportsNG.
     * 
     * @param result 
     */
    protected void sendAlert(ITestResult result) {
          
        try {
            
            logger.info("Sending event alert for test: " + result.getName());
            
            AlertEvent.getInstance().sendAlert(result);
            
        }
        catch(Exception e) { logger.error(e); }
   
    }
    
    /**
     * This method does all reporting into TestNG/ReportsNG.
     * 
     * @param result 
     * @param ignoreAlertKeywords
     */
    protected void sendAlert(ITestResult result, boolean ignoreAlertKeywords) {
          
        try {
            
            logger.info("Sending event alert for test: " + result.getName() + "|" + ignoreAlertKeywords);
            
            AlertEvent.getInstance().sendAlert(result, ignoreAlertKeywords);
            
        }
        catch(Exception e) { logger.error(e); }
   
    }
    
}



