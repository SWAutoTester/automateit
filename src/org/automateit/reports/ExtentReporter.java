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

package org.automateit.reports;

import java.io.File;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * This class manages reports obects for the Eyent Reporting framework
 * 
 * @author mburnside
 */
public class ExtentReporter implements Reporter {
    
    /**
     * The filename of the report
     */
    public final static String REPORTS_FILENAME = "results.html";
    
    /**
     * The directory of where to report will be located
     */
    public final static String REPORTS_DIRECTORY = "." + File.separator + "report" + File.separator;
    
    /**
     * ExtentReports object
     */
    private ExtentReports extentReports = null;
 
    /**
     * ExtentTest obect
     */
    private ExtentTest extentTest = null;
   
    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    private static Logger logger = Logger.getLogger(ExtentReporter.class);
   
    /**
     * Default Constructor
     */
    public ExtentReporter() { 
        
        try { startReport(); }
        catch(Exception e) { logger.error(e); }
    
    }
    
    /**
     * Start the report.
     * 
     * @throws Exception 
     */
    public void startReport() throws Exception {
        
        try {
            
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(REPORTS_DIRECTORY + REPORTS_FILENAME);
            
            extentReports = new ExtentReports();
 
            extentReports.attachReporter(htmlReporter);
 
            htmlReporter.config().setDocumentTitle("Test Result Report");
 
            htmlReporter.config().setReportName("Test Result Report");
 
            htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
 
            htmlReporter.config().setTheme(Theme.STANDARD);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Start a new test.
     * 
     * @param testName
     * 
     * @throws Exception 
     */
    public void startNewTest(String testName) throws Exception {
        
        try { 
            
            extentTest = extentReports.createTest(testName).createNode("Steps"); 
            
            info("Test Started");
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Log a failed test.
     * 
     * @param testName
     * 
     * @throws Exception 
     */
    public void logFail(String testName) throws Exception {
        
        try { extentTest.fail("Test Failed"); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Log a failed test.
     * 
     * @param testName
     * @param throwable
     * 
     * @throws Exception 
     */
    public void logFail(String testName, Throwable throwable) throws Exception {
        
        try { extentTest.fail("Test Failed"); extentTest.fail(throwable); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Log a Passed test.
     * 
     * @param testName
     * 
     * @throws Exception 
     */
    public void logPass(String testName) throws Exception {
        
        try { extentTest.pass("Test Passed"); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Log a Skipped test.
     * 
     * @param testName
     * @throws Exception 
     */
    public void logSkip(String testName) throws Exception {
 
        try { extentTest = extentReports.createTest(testName).skip("Test Skipped"); }
        catch(Exception e) { throw e; }
      
    }
    
    /**
     * Finish the report.
     * 
     * @throws Exception 
     */
    public void finishReport() throws Exception {
 
        try { 
            
            logger.info("Flushing");
            
            extentReports.flush(); 
        
        }
        catch(Exception e) { logger.error(e); throw e; }
      
    }
    
    /**
     * Log a message at level INFO.
     * 
     * @param message 
     */
    public void info(String message) {
 
        try { extentTest.info(message); }
        catch(Exception e) { }
      
    }
    
    /**
     * Log a message at level DEBUG.
     * 
     * @param message 
     */
    public void debug(String message) {
 
        try { extentTest.debug(message); }
        catch(Exception e) { }
      
    }
    
    /**
     * Log a message at level WARN.
     * 
     * @param message 
     */
    public void warn(String message) {
 
        try { extentTest.warning(message); }
        catch(Exception e) { }
      
    }
    
    /**
     * Log a message at level ERROR.
     * 
     * @param message 
     */
    public void error(String message) {
 
        try { extentTest.error(message); }
        catch(Exception e) { }
      
    }
    
    /**
     * Log a message at level FATAL.
     * 
     * @param message 
     */
    public void fatal(String message) {
 
        try { extentTest.fatal(message); }
        catch(Exception e) { }
      
    }
    
    /**
     * Add an image to the report
     * 
     * @param imagePath
     */
    public void addImageToReport(String imagePath) {
 
        try { extentTest.addScreenCaptureFromPath(imagePath); }
        catch(Exception e) { }
      
    }
    
    /**
     * Add an image to the report
     * 
     * @param imagePath
     * @param title
     */
    public void addImageToReport(String imagePath, String title) {
 
        try { extentTest.addScreenCaptureFromPath(imagePath, title); }
        catch(Exception e) { }
      
    }
    
}



