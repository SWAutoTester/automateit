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
 * 
 **/

package org.automateit.test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

import org.apache.log4j.Logger;

import org.testng.Assert;

import org.automateit.data.DataDrivenInput;
import org.automateit.data.DataArchiveFactory;
import org.automateit.data.DataArchive;

import org.automateit.util.Utils;

import org.automateit.testng.listener.TestNGUtils;

import org.automateit.util.CommonProperties;

/**
 * This is the base testing class.
 * 
 * @author mburnside@Automate It!
 */
public class TestBase {
      
    /**
     * The delay we wait for certain actions to be completed.
     */
    protected static final long WAITDELAY = 10000;
    
    /**
     * The utility class for convenience methods
     */
    protected Utils utils = new Utils();
    
    /**
     * If the logging mechanism has been initialized
     */
    protected boolean loggingSetup = false;
    
    /**
     * The TestNGUtils class (if we need it)
     */
    TestNGUtils testNGUtils = new TestNGUtils();
    
    /**
     * The data base directory
     */
    public static final String DATADIRECTORY = "." + File.separator + "data" + File.separator;
   
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(TestBase.class);

    /**
     * Stop the program execution for N number of milliseconds.
     * 
     * @param milliseconds 
     */
    protected void delay(long milliseconds) {
        
        try { Thread.sleep(milliseconds); } 
        catch(Exception e) { }
        
    }
    
    /**
     * A file filename filter that returns all files in a directory with 
     * a ".properties" extension.
     * 
     * @param f The directory
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected FilenameFilter getPropertiesFileFilter(File f) throws Exception {
       
        try { return utils.getPropertiesFileFilter(f); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get the list of files in the directory.
     * 
     * @param directory
     * 
     * @return The list files with .properties extension
     * 
     * @throws Exception 
     */
    protected File[] getListOfPropertiesFilesInDirectory(String directory) throws Exception {
        
        try { return utils.getListOfPropertiesFilesInDirectory(directory); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Setup the data driven testing object so we can drive our tests using a
     * configuration file.
     * 
     * The file is expected to be an EXCEL file (.xls, .xlsx)
     * 
     * @param dataFile The file of input parameter values
     * 
     * @return A DataDrivenInput instance
     * 
     * @throws Exception 
     */
    protected DataDrivenInput setupDataDrivenInput(String dataFile) throws Exception {
        
        try { return utils.setupDataDrivenInput(dataFile); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Setup the data driven testing object so we can drive our tests using a
     * configuration file.
     * 
     * @param dataFile The file of input parameter values
     * @param id The id of the implementation to use
     * 
     * @return A DataDrivenInput instance
     * 
     * @throws Exception 
     */
    protected DataDrivenInput setupDataDrivenInput(String dataFile, int id) throws Exception {
        
        try { return utils.setupDataDrivenInput(dataFile, id); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get a data archive object so we save whatever data needed.
     * 
     * @param id
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected DataArchive getDataArchive(int id) throws Exception {
        
        try {
            
            DataArchiveFactory dataArchiveFactory = new DataArchiveFactory();
            return dataArchiveFactory.getDataArchive(id);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Save the data archive to the specified file.
     * 
     * @param dataArchive
     * @param filename
     * 
     * @throws Exception 
     */
    protected void saveDataArchiveInFile(DataArchive dataArchive, String filename) throws Exception {
        
        try { dataArchive.saveData(filename); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Setup logging with log4j.
     * 
     * Default location id at <code>./conf/log4j.properties</code>
     * 
     * @throws Exception 
     */
    protected void setupLogging() throws Exception {
        
        try { setupLogging("." + File.separator + "conf" + File.separator + "log4j.properties"); }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Setup logging with log4j.
     * 
     * @param log4jPropertiesFile
     * 
     * @throws Exception 
     */
    protected void setupLogging(final String log4jPropertiesFile) throws Exception {
        
        try { CommonProperties.getInstance().setupLogging(log4jPropertiesFile); }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Allow a test class to invoke the creation of performance data.
     * 
     * @throws Exception 
     */
    protected void createPerformanceReports() throws Exception {
        
        try { testNGUtils.savePageSummaryPerformance(utils.getBasePagePerformanceDirectory()); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get a unique string value based on Date.getTime().
     * 
     * @return
     * @throws Exception 
     */
    protected String getUniqueStringValue() throws Exception {
        
        try { return String.valueOf((new Date()).getTime()); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Assert true condition.
     * 
     * @param condition
     * 
     * @throws Exception 
     */
    protected void assertTrue(boolean condition) throws Exception {
        
        try { Assert.assertTrue(condition); }
        catch(Exception e) { throw e; }
        
        
    }
    
    /**
     * Assert true condition.
     * 
     * @param condition
     * @param errorMessage
     * 
     * @throws Exception 
     */
    protected void assertTrue(boolean condition, String errorMessage) throws Exception {
        
        try { Assert.assertTrue(condition, errorMessage); }
        catch(Exception e) { throw e; }
        
        
    }
    
    /**
     * Assert equals condition.
     * 
     * @param actual
     * @param expected
     * 
     * @throws Exception 
     */
    protected void assertEquals(boolean actual, boolean expected) throws Exception {
        
        try { Assert.assertEquals(actual, expected); }
        catch(Exception e) { throw e; }
        
        
    }
    
    /**
     * Assert equals condition.
     * 
     * @param actual
     * @param expected
     * 
     * @throws Exception 
     */
    protected void assertEquals(String actual, String expected) throws Exception {
        
        try { Assert.assertEquals(actual, expected); }
        catch(Exception e) { throw e; }
        
        
    }
    
    /**
     * Assert equals condition.
     * 
     * @param actual
     * @param expected
     * @param errorMessage
     * 
     * @throws Exception 
     */
    protected void assertEquals(boolean actual, boolean expected, String errorMessage) throws Exception {
        
        try { Assert.assertEquals(actual, expected, errorMessage); }
        catch(Exception e) { throw e; }
        
        
    }
    
}

