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

/**
 * This class manages reports obects for the Eyent Reporting framework
 * 
 * @author mburnside
 */
public interface Reporter {
    
    /**
     * Start the report.
     * 
     * @throws Exception 
     */
    public void startReport() throws Exception;
    
    /**
     * Start a new test.
     * 
     * @param testName
     * 
     * @throws Exception 
     */
    public void startNewTest(String testName) throws Exception;
    
    /**
     * Log a failed test.
     * 
     * @param testName
     * 
     * @throws Exception 
     */
    public void logFail(String testName) throws Exception;
    
    /**
     * Log a failed test.
     * 
     * @param testName
     * @param throwable
     * 
     * @throws Exception 
     */
    public void logFail(String testName, Throwable throwable) throws Exception;
    
    /**
     * Log a Passed test.
     * 
     * @param testName
     * 
     * @throws Exception 
     */
    public void logPass(String testName) throws Exception;
    
    /**
     * Log a Skipped test.
     * 
     * @param testName
     * @throws Exception 
     */
    public void logSkip(String testName) throws Exception;
    
    /**
     * Finish the report.
     * 
     * @throws Exception 
     */
    public void finishReport() throws Exception;
    
    /**
     * Log a message at level INFO.
     * 
     * @param message
     */
    public void info(String message);
    
    /**
     * Log a message at level DEBUG.
     * 
     * @param message
     */
    public void debug(String message);
    
    /**
     * Log a message at level WARN.
     * 
     * @param message
     */
    public void warn(String message);
    
    /**
     * Log a message at level ERROR.
     * 
     * @param message
     */
    public void error(String message);
    
    /**
     * Log a message at level FATAL.
     * 
     * @param message
     */
    public void fatal(String message);
    
    /**
     * Add a screen
     * 
     * @param imagePath
     */
    public void addImageToReport(String imagePath);
    
    /**
     * Add a screen
     * 
     * @param imagePath
     * @param title
     */
    public void addImageToReport(String imagePath, String title);
    
}



