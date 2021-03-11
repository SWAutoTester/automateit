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

import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.automateit.ocr.OCRProcessor;

import org.automateit.reports.ReportsManager;
import org.automateit.reports.ExtentReporter;

import org.automateit.util.CommandList;
import org.automateit.util.CommonProperties;
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
     * The web driver wait object
     */
    protected WebDriverWait wait = null;
    
    /**
     * Web Driver object.
     * 
     * It is designated as "protected" in case child classes need access to it.
     */
    protected WebDriver driver = null;
    
    /**
     * Copy Constructor
     * 
     * @param driver 
     */
    public ViewBase(WebDriver driver) { setWebDriver(driver); }
    
    /**
     * Default Constructor 
     */
    public ViewBase() { }
    
    /**
     * Sets the driver for all instances of page/screen classes,
     * and other utilities not directly related to UI interaction 
     * (reports, events, testng, etc)
     * 
     * @param driver 
     */
    public void setWebDriver(WebDriver driver) {  
    
        // this makes the webdriver instance available to all non-view relates objects
        // (reports, events, testng, etc)
        CommonWebDriver.getInstance().setWebDriver(driver); 
        
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 60);
        
    }
    
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
            
            FileUtils.copyFile(((TakesScreenshot)CommonWebDriver.getInstance().getWebDriver()).getScreenshotAs(OutputType.FILE), new File(screenshotFilename));
            
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
    
    /*****************************************************************************
     * 
     * This code block defines element finding methods
     * 
     *****************************************************************************/
    
    /**
     * Find a WebElement
     * 
     * Example usage by client:
     * 
     * by = By.xpath("//*[contains(text(),'Login')]");
     * by = By.id("loginButton");
     * by = By.name("loginButton");
     *
     * @param by
     * 
     * @return The web element
     */
    protected WebElement find(By by) { return wait.until(presenceOfElementLocated(by)); }

    /**
     * Find WebElements
     *
     * @param by
     * 
     * @return The web element
     */
    protected List<WebElement> findElements(By by) { return this.driver.findElements(by); } 
            
}