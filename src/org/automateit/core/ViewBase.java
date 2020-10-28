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

package org.automateit.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.util.Properties;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;

import org.automateit.ocr.OCRProcessor;

import org.automateit.core.StringCapabilities;

import org.automateit.reports.ReportsManager;
import org.automateit.reports.ExtentReporter;

import org.automateit.util.CommandList;
import org.automateit.util.CommonProperties;
import org.automateit.util.CommonSelenium;
import org.automateit.util.Utils;

/**
 * This class contains common properties and methods used in all view mechanisms.
 * 
 * @author mburnside@Automate It!
 */
public class ViewBase {
     
    /**
     * If the web driver has been initialized
     */
    protected boolean hasBeenInitialized = false;
    
    /**
     * If the logging mechanism has been initialized
     */
    protected boolean loggingSetup = false;
   
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(ViewBase.class);
   
    /**
     * Utilities for convenience methods
     */
    protected Utils utils = new Utils();
    
    /**
     * Properties that can be used for any class extending this class
     */
    protected CommonProperties properties = CommonProperties.getInstance();
    
    /**
     * Command List
     */
    protected CommandList commandList = CommandList.getInstance();
    
    /**
     * Log a message at level INFO to the reporting framework.
     * 
     * @param message 
     */
    public void info(String message) {
        
        try { ReportsManager.getInstance().info(message); }
        catch(Exception e) { }
        
    }
    
    /**
     * Log a message at level DEBUG to the reporting framework.
     * 
     * @param message 
     */
    public void debug(String message) {
        
        try { ReportsManager.getInstance().debug(message); }
        catch(Exception e) { }
        
    }
    
    /**
     * Log a message at level WARN to the reporting framework.
     * 
     * @param message 
     */
    public void warn(String message) {
        
        try { ReportsManager.getInstance().warn(message); }
        catch(Exception e) { }
        
    }
    
    /**
     * Log a message at level ERROR to the reporting framework.
     * 
     * @param message 
     */
    public void error(String message) {
        
        try { ReportsManager.getInstance().error(message); }
        catch(Exception e) { }
        
    }
    
    /**
     * Log a message at level FATAL to the reporting framework.
     * 
     * @param message 
     */
    public void fatal(String message) {
        
        try { ReportsManager.getInstance().fatal(message); }
        catch(Exception e) { }
        
    }
    
    /**
     * Take a screenshot and add it to the test results report
     * 
     * @throws Exception 
     */
    public void addScreenshotToReport() throws Exception {
               
        try { utils.addScreenshotToReport(ExtentReporter.REPORTS_DIRECTORY); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get the text from a screenshot of the mobile app screen using OCR
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getCurrentTextOnScreenUsingOCR() throws Exception {
        
        String screenshotFilename = utils.getBaseScreenshotsDirectory() + "ocrimage.png";
                
        try { 
            
            FileUtils.copyFile(((TakesScreenshot)CommonSelenium.getInstance().getWebDriver()).getScreenshotAs(OutputType.FILE), new File(screenshotFilename));
            
            OCRProcessor ocrProcessor = new OCRProcessor();
            
            ocrProcessor.setDatapath("../framework/resources/tessdata");
            
            return ocrProcessor.getTextInImage(screenshotFilename); 
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Validate that the expected text appears on the screen using OCR.
     * 
     * @param expectedText
     * 
     * @throws Exception 
     */
    public void validateTextOnScreenUsingOCR(String expectedText) throws Exception {
        
        logger.info("Validating expected text on the Screen using OCR: " + expectedText);
        
        commandList.addToList("validateTextOnScreenUsingOCR|" + expectedText);
        
        try { 
            
            if(expectedText == null) throw new Exception("Unable to get any text from OCR because expected text to verify is null");
            
            String renderedText = getCurrentTextOnScreenUsingOCR();
            
            logger.info("Text read from OCR operation on the mobile screen:\n\n" + renderedText + "\n\n");
            
            if(renderedText == null) throw new Exception("Unable to get any text from OCR");
            
            if(!renderedText.contains(expectedText.trim())) throw new Exception("Expected text: " + expectedText + " does not appear on the screen");
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Validate that a set of expected text appears on the screen using OCR.
     * 
     * @param expectedText
     * 
     * @throws Exception 
     */
    public void validateTextOnScreenUsingOCR(String[] expectedText) throws Exception {
        
        logger.info("Validating expected text on the Screen using OCR");
        
        try { for(int i = 0; i < expectedText.length; i++) validateTextOnScreenUsingOCR(expectedText[i]); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Setup logging with log4j.
     * 
     * @throws Exception 
     */
    protected void setupLogging() throws Exception {
        
        try {
            
            Properties props = new Properties();
            props.load(new FileInputStream("./conf/log4j.properties"));
            PropertyConfigurator.configure(props);
            
            loggingSetup = true;
            
        }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Thread.sleep in milliseconds. Makes less code to type.
     * 
     * @param milliseconds 
     */
    protected void delay(long milliseconds) {
        
        try { Thread.sleep(milliseconds); } catch(Exception e) { }
        
    }
    
    /**
     * Log the exception.
     * 
     * @param e
     * 
     * @throws Exception 
     */
    protected void logException(Exception e) throws Exception {
        
        try {
            
            logger.info("----------------------------------------------------------");
            logger.info("");
            logger.error(e);
            logger.info("");
            logger.info("----------------------------------------------------------");
            
        }
        catch(Exception e2) { throw e2; }
        
    }
    
    /**
     * Get the platform type.
     * 
     * @return 
     */
    public String getPlatFormType() { return properties.get(StringCapabilities.PLATFORM_NAME.getCapability()); }
    
    /**
     * Convience method for all test needing to know what device is being used
     * 
     * @return 
     */
    public boolean isIOS() { return utils.isIOS(); }
    
    /**
     * Convience method for all test needing to know what device is being used
     * 
     * @return 
     */
    public boolean isAndroid() { return utils.isAndroid(); }
    
    /**
     * Set the properties up to prepare to load an app with
     * full reset and and re-install of the app
     * 
     * @param reset
     */
    public void setForAppReset(boolean reset) {
            
        properties.setAppNoReset(!reset);
            
        properties.setReInstallApp(reset);
        
    }
    
    /**
     * This method save a java serialized object to a file.
     * 
     * @param filename
     * @param obj
     * 
     * @throws Exception 
     */
    public void saveObjectToFile(String filename, Object obj) throws Exception {
 
        try {
 
            FileOutputStream fileOut = new FileOutputStream(filename);
            
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            
            objectOut.writeObject(obj);
            
            objectOut.close();
           
        } 
        catch (Exception e) { throw e; }
        
    }
    
    /**
     * Get an object from a file.
     * 
     * @param filename
     * @return
     * 
     * @throws Exception 
     */
    public Object getObjectFromFile(String filename) throws Exception {
        
        try {
    
            FileInputStream fis = new FileInputStream(filename);
    
            ObjectInputStream ois = new ObjectInputStream(fis);
    
            return ois.readObject();

        } 
        catch (Exception e) { throw e; } 

    }
            
}