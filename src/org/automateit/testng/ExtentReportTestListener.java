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
        
import org.automateit.reports.ReportsManager;
import org.automateit.reports.ExtentReporter;

import org.automateit.util.Utils;

/**
 * This class is added to testng task to listen for events.
 * 
 * It will handle the tracking of individual test results
 *
 * @author mburnside
 */
public class ExtentReportTestListener extends TestListenerAdapter {
    
    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    protected static Logger logger = Logger.getLogger(ExtentReportTestListener.class);
    
    /**
     * The Extent Reporter to use
     */
    private final ExtentReporter extentReporter = new ExtentReporter();

    /**
     * The extent reports manager using for this test run
     */
    private final ReportsManager reportsManager = ReportsManager.getInstance();
    
    /**
     * Utilities for convenience methods
     */
    protected Utils utils = new Utils();
    
    /**
     * Default Constructor
     */
    public ExtentReportTestListener() { 
        
        super(); 
        
        addToReportsManager();
    
    }
   
    /**
     * Do actions after a test case execution failure.
     * 
     * @param result 
     */
    @Override
    public void onTestFailure(ITestResult result) { 
        
        try { 
            
            addScreenshotToReport(reportsManager.TEXT_TEST_COMPLETED);
            
            addToReportsManager(); extentReporter.logFail(result.getName(), result.getThrowable()); 
        
        }
        catch(Exception e) { }
    
    }

    /**
     * Do actions after a test case execution skipped.
     * 
     * @param result 
     */
    @Override
    public void onTestSkipped(ITestResult result) { 
        
        try { addToReportsManager(); extentReporter.logSkip(result.getName()); }
        catch(Exception e) { }
        
    }

    /**
     * Do actions after a test case execution success (no reported fail
     * during test case execution).
     * 
     * @param result 
     */
    @Override
    public void onTestSuccess(ITestResult result) { 
        
        try { 
            
            addScreenshotToReport(reportsManager.TEXT_TEST_COMPLETED);
            
            addToReportsManager(); extentReporter.logPass(result.getName()); 
        
        }
        catch(Exception e) { }
        
    }
   
    /**
     * Do actions before a test case execution 
     * 
     * @param result 
     */
    @Override
    public void onTestStart(ITestResult result) { 
        
        try { 
            
            extentReporter.startNewTest(result.getName()); 
            
            addScreenshotToReport(reportsManager.TEXT_TEST_STARTED);
        
        }
        catch(Exception e) { logger.error(e); }
    
    }
    
    /**
     * This method is invoked after all the tests have run and all their Configuration methods have been called.
     */
    @Override
    public void onFinish(ITestContext context) { 
        
        try { extentReporter.finishReport(); }
        catch(Exception e) { }
    
    }
    
    /**
     * Add the internal reports object the reports manager
     */
    private void addToReportsManager() {
        
        try { reportsManager.addReporter(extentReporter); }
        catch(Exception e) { }
        
    }
    
    /**
     * Add the screenshot to the report
     * 
     * @param title
     */
    private void addScreenshotToReport(String title) {
        
        try { utils.addScreenshotToReport(extentReporter.REPORTS_DIRECTORY, title); }    
        catch(Exception le) { }
        
    }
    
}


