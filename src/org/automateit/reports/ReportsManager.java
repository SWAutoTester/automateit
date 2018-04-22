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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class manages reports for the Eyent Reporting framework
 * 
 * @author mburnside
 */
public class ReportsManager {
    
    public static final String TEXT_TEST_STARTED = "Test Started";
    
    public static final String TEXT_TEST_COMPLETED = "Test Completed";
    
    /**
     * ExtentReports object
     */
    private List<Reporter> reporters = new ArrayList();

    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    protected static Logger logger = Logger.getLogger(ReportsManager.class);
    
    /**
     * Reference to this object
     */
    private static ReportsManager instance = new ReportsManager();
    
    /**
     * Default Constructor
     */
    protected ReportsManager() { 
        
        try { for(Reporter reporter : reporters) reporter.startReport(); }
        catch(Exception e) { 
            
            logger.error("There was a problem starting the Reporter"); 
            logger.error(e);
        
        }
    
    }
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static ReportsManager getInstance() { return instance; }
    
    /**
     * SAdd a reporter to the list of reporters that will make reports
     * 
     * @param reporter
     * 
     * @throws Exception 
     */
    public void addReporter(Reporter reporter) throws Exception {
        
        try { if(!reporters.contains(reporter)) reporters.add(reporter); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * SAtart the report.
     * 
     * @throws Exception 
     */
    public void startReport() throws Exception {
        
        try { for(Reporter reporter : reporters) reporter.startReport(); }
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
        
        try { for(Reporter reporter : reporters) reporter.logFail(testName); }
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
        
        try { for(Reporter reporter : reporters) reporter.logFail(testName, throwable); }
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
        
        try { for(Reporter reporter : reporters) reporter.logPass(testName);  }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Log a Skipped test.
     * 
     * @param testName
     * @throws Exception 
     */
    public void logSkip(String testName) throws Exception {
 
        try { for(Reporter reporter : reporters) reporter.logSkip(testName); }
        catch(Exception e) { throw e; }
      
    }
    
    public void finishReport() throws Exception {
 
        try { for(Reporter reporter : reporters) reporter.finishReport(); }
        catch(Exception e) { throw e; }
      
    }
    
    /**
     * Log a message at level INFO.
     * 
     * @param message
     * 
     * @throws Exception 
     */
    public void info(String message) throws Exception {
        
        try { for(Reporter reporter : reporters) reporter.info(message); }
        catch(Exception e) { throw e; }
      
    }
    
    /**
     * Log a message at level DEBUG.
     * 
     * @param message
     * 
     * @throws Exception 
     */
    public void debug(String message) throws Exception {
 
        try { for(Reporter reporter : reporters) reporter.debug(message); }
        catch(Exception e) { throw e; }
      
    }
    
    /**
     * Log a message at level WARN.
     * 
     * @param message
     * 
     * @throws Exception 
     */
    public void warn(String message) throws Exception {
 
        try { for(Reporter reporter : reporters) reporter.warn(message); }
        catch(Exception e) { throw e; }
      
    }
    
    /**
     * Log a message at level ERROR.
     * 
     * @param message
     * 
     * @throws Exception 
     */
    public void error(String message) throws Exception {
 
        try { for(Reporter reporter : reporters) reporter.error(message); }
        catch(Exception e) { throw e; }
      
    }
    
    /**
     * Log a message at level FATAL.
     * 
     * @param message
     * 
     * @throws Exception 
     */
    public void fatal(String message) throws Exception {
 
        try { for(Reporter reporter : reporters) reporter.fatal(message); }
        catch(Exception e) { throw e; }
      
    }
    
    /**
     * Add an image to the report
     * 
     * @param imagePath
     */
    public void addImageToReport(String imagePath) {
 
        try { for(Reporter reporter : reporters) reporter.addImageToReport(imagePath); }
        catch(Exception e) { throw e; }
      
    }
    
    /**
     * Add an image to the report
     * 
     * @param imagePath
     * @param title
     */
    public void addImageToReport(String imagePath, String title) {
 
        try { for(Reporter reporter : reporters) reporter.addImageToReport(imagePath, title); }
        catch(Exception e) { throw e; }
      
    }
    
}



