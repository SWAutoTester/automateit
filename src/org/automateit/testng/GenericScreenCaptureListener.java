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

import org.automateit.util.ScreenshotCapture;
import org.automateit.util.Utils;

/**
 * This class is added to testng task to listen for events.
 * 
 * One of things it does it take a screenshot when a test fails.
 * 
 * It does not use the selenium screen capture feature, it uses a generic
 * screen capture that captures the whole screen, not just the browser.
 * 
 * Use this if you are using a Selenium implementation that does not
 * support browser screenshot feature.
 * 
 * It may be useful to also to turn off the default listeners.
 *
 * @author mburnside
 */
public class GenericScreenCaptureListener extends TestListenerAdapter {
    
    /**
     * Screenshot capture object. 
     * 
     * It is does not use selenium's screen capture feature.
     */
    ScreenshotCapture screenshotCapture = new ScreenshotCapture();
    
    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    protected static Logger log = Logger.getLogger(GenericScreenCaptureListener.class);

    /**
     * testng utils class
     */
    private TestNGUtils testNGUtils = new TestNGUtils();
    
    /**
     * utils class
     */
    private Utils utils = new Utils();
    
    /**
     * div id number. needed for selenium command list toggle.
     */
    private int divIdNumber = 0;
    
    /**
     * Do actions after a test case execution failure.
     * 
     * @param result 
     */
    @Override
    public void onTestFailure(ITestResult result) { if(utils.doReportingOnTestFail()) doReporting(result); }

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
    public void onTestSuccess(ITestResult result) { if(utils.doReportingOnTestSuccess()) doReporting(result); }
   
    
    /**
     * This method is invoked after all the tests have run and all their Configuration methods have been called.
     */
    @Override
    public void onFinish(ITestContext context) { 
    	
        if (testNGUtils.capturePageLoadPerformance()) {
	    	
            try { testNGUtils.savePageSummaryPerformance(utils.getBasePagePerformanceDirectory()); }	        
            catch(Exception e) { log.error(e); }
    	
        }
    
    }
    
    /**
     * This method does all reporting into TestNG/ReportsNG.
     * 
     * @param result 
     */
    private void doReporting(ITestResult result) {
          
        try {
       
            String path = utils.getBaseScreenshotsDirectory();
           
            String screenshotFilename = path + utils.getFirstToken(result.getName(), " ") + Long.toHexString(System.currentTimeMillis());
        
            createScreenshot(screenshotFilename);
            
            testNGUtils.appendToReport(result, screenshotFilename + "." + ScreenshotCapture.JPG, divIdNumber++);
       
        }
        catch(Exception e) { log.error(e); }
   
    }

   /**
    * Create the screenshot in JPG format.
    * 
    * @param screenshotFilename the name of the screenshot image file
    * 
    * @throws Exception 
    */
   private void createScreenshot(String screenshotFilename) throws Exception {
       
       try { screenshotCapture.doScreenshot(screenshotFilename, ScreenshotCapture.JPG); }
       catch(Exception e) { throw e; }
       
   }
   
}



