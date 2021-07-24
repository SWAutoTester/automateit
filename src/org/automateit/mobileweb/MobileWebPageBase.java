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

package org.automateit.mobileweb;
 
import java.net.URL;

import org.testng.Assert;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.remote.MobileCapabilityType;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import org.apache.log4j.Logger;

import org.automateit.core.ViewBase;

import org.automateit.util.CommandList;
import org.automateit.util.CommonProperties;
import org.automateit.util.PerformanceCapture;
import org.automateit.util.ScreenshotCapture;
import org.automateit.util.Utils;

import org.automateit.core.StringCapabilities;
import org.automateit.core.BooleanCapabilities;
      
/**
 * This class is the base class for all other screen classes to use.
 * 
 * It abstracts away all of the webdriver-specific programming and provides 
 * easy to use convenience methods for rapid development of mobile apps using
 * webdriver and the screen design pattern.
 * 
 * @author mburnside
 */
public class MobileWebPageBase extends ViewBase {
    
    /**
     * The url where this mobile web page is accessed
     */
    protected String url = null;
    
    /**
     * This is the UDID of the device to use
     */
    protected String udid = null;
    
    /**
     * Indicates if this class has been initialized
     */
    protected boolean hasBeenInit = false;
    
    /**
     * Maximize Browser Window. Default is <code>true</code>.
     */
    protected boolean maximizeBrowserWindow = true;
    
    /**
     * The utilities class to help with convenience methods
     */
    protected Utils utils = new Utils();
    
    /**
     * Add a start URL to make sure that one specified cannot be overritten accidentally or by reload of common properties
     */
    protected String startURL =  null;
    
    /**
     * JavaScript library that the application is using. This is required to query the active ajax connection
     */
    protected String jsLibrary = null;
    
    /**
     * Perform screen shot. default is <code>false</code>.
     */
    protected boolean doScreenshot = false;
    
    /**
     * The default timeout (in seconds) - 25
     */
    public final static String TIMEOUT_DEFAULT = "25";
    
    /**
     * Default timeout for page loading in seconds (s). Default is 25 seconds. 
     */
    protected String timeout = TIMEOUT_DEFAULT;
    
    /**
     * Add extra wait time for page to load after ajax calls are finished.
     */
    protected boolean addExtraWaitTimeAfterAjaxComplete = false;

    /**
     * Wait time for page to load after ajax calls are finished.
     */
    protected long extraWaitTimeAfterAjaxComplete = 2000; 
    
    /**
     * Properties that can be used for any class extending this class
     */
    protected CommonProperties properties = CommonProperties.getInstance();
  
    /**
     * Non-selenium-based screenshot capture object.
     * 
     * It is does not use selenium's screen capture feature.
     * 
     * it captures the entire screen, not just the browser window.
     */
    protected ScreenshotCapture screenshotCapture = new ScreenshotCapture();
    
    /**
     * Command List
     */
    protected CommandList commandList = CommandList.getInstance();
    
    /**
     * Use this is property if wanting to bypass selenium/webdriver/jquery
     * techniques to allow for complete page loading. This is protection against
     * library bugs
     */
    protected boolean useForcePageLoadWaitTime = false;
    
    /**
     * Set the forced page load wait time.
     */
    protected long forcePageLoadWaitTime = 10000;
    
    /**
     * Capture the selenium/webdriver commands. Default is <code>true</code>.
     */
    protected boolean captureSeleniumCommands = true;
    
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(MobileWebPageBase.class);
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @throws Exception 
     */
    public MobileWebPageBase() throws Exception { }
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @param url
     * 
     * @throws Exception 
     */
    public MobileWebPageBase(String url) throws Exception {
    
        try { this.url = url; }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @param url
     * @param udid
     * 
     * @throws Exception 
     */
    public MobileWebPageBase(String url, String udid) throws Exception {
    
        try { this.url = url; this.udid = udid; }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @param init
     * 
     * @throws Exception 
     */
    public MobileWebPageBase(boolean init) throws Exception {
    
        try { if(init) initSetup(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Copy Constructor. Each screen class must use this constructor.
     * 
     * @param basePage
     * 
     * @throws Exception 
     */
    public MobileWebPageBase(MobileWebPageBase basePage) throws Exception {
    
        try { inheritSession(basePage); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Initialize the CommonProperties object
     * 
     * @throws Exception 
     */
    protected void initializeCommonProperties() throws Exception {
        
        try { this.properties.load(this.properties.PROPERTIESFILEPATH); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Initialize this class to prepare for testing. Loads the properties file 
     * located at <code>conf/seleniumconfiguration.properties</code>.
     * <p>
     * This method also calls "start" method.
     * 
     * This method uses the start page indicated by the baseURL property
     * 
     * @throws Exception
     */
    protected void initSetup() throws Exception { 
        
        logger.debug("Initializing a new BasePage -> selenium/webdriver and other settings using baseURL property (default initialization)");
        
        try {
            
            initializeCommonProperties();
        
            logger.info("Starting at baseURL: " + properties.get(StringCapabilities.URL.getCapability()));
            
            initSetup(properties.get(StringCapabilities.URL.getCapability()));
        
        }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Initialize this class to prepare for testing. Loads the properties file 
     * located at <code>conf/seleniumconfiguration.properties</code>.
     * <p>
     * This method also calls "start" method.
     * 
     * @param startURL The starting URL to open after the webdriver has been initialized and new session is created.
     * 
     * @throws Exception
     */
    protected void initSetup(String startURL) throws Exception {
        
        try { initSetup(startURL, false); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Initialize this class to prepare for testing. Loads the properties file 
     * located at <code>conf/seleniumconfiguration.properties</code>.
     * <p>
     * This method also calls "start" method.
     * 
     * @param startURL The starting URL to open after the webdriver has been initialized and new session is created.
     * @param goToURL Tells this if needing to render the4 web page immediately
     * 
     * @throws Exception
     */
    protected void initSetup(String startURL, boolean goToURL) throws Exception { 
      
        try {
            
            initializeCommonProperties();
            
            this.url = startURL;
            
            logger.info("Initializing a new BaseMobilewebPage -> WebDriver and other settings");
            
            logger.info("startURL: " + startURL);
            logger.info("goToURL: " + goToURL);
           
            logger.info("Properties loaded at: " + properties.PROPERTIESFILEPATH + ", size: " + properties.size());
         
            logger.info("Creating Selenium 3.0 instance: " + properties.get(StringCapabilities.ID_SELENIUM_DRIVER.getCapability()));

            // screenshot properties
            if(properties.get(BooleanCapabilities.CAPTURE_SCREENSHOTS.getCapability()) != null) { doScreenshot = Boolean.valueOf(properties.get(BooleanCapabilities.CAPTURE_SCREENSHOTS.getCapability())); }
            
            logger.info("Do screenshots on each page load: " + doScreenshot);
            
            // set the captureSeleniumCommands
            if(properties.get(BooleanCapabilities.CAPTURE_SELENIUM_COMMANDS.getCapability()) != null) { captureSeleniumCommands = Boolean.valueOf(properties.get(BooleanCapabilities.CAPTURE_SELENIUM_COMMANDS.getCapability())); }
            
            logger.info("Capture selenium commands: " + captureSeleniumCommands);
           
            logger.info("Opening the start url: " + this.url);
           
        }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Create the web driver
     * 
     * @param startURL
     * @param goToURL
     * 
     * @throws Exception 
     */
    protected void createWebDriver(String startURL, boolean goToURL) throws Exception { 
      
        try {
            
            logger.info("Creating Selenium/WebDriver: startURL: " + startURL + " goToURL: " + goToURL);
            
            DesiredCapabilities cap = new DesiredCapabilities();
		
            cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
		
            cap.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
            
            if(udid != null) cap.setCapability(MobileCapabilityType.UDID, udid);
            
            AndroidDriver<AndroidElement> driver = new AndroidDriver<AndroidElement>(new URL(properties.get(StringCapabilities.URL.getCapability())), cap);
           
            if(goToURL) driver.get(startURL);
            
            setWebDriver(driver);
           
            hasBeenInit = true;
            
            logger.info("Finished initializing Selenium/WebDriver: " + driver);
           
        }
        catch(Exception e) { logger.error("Error creating webdriver"); logger.error(e); throw e; }

    }
   
    /**
     * Return the webdriver interface.
     * 
     * @return The webdriver interface
     */
    public WebDriver getWebDriver() { return driver; }
    
    /**
     * Specifies the amount of time that Selenium will wait for actions to complete.
     * <p>
     * Actions that require waiting include "open" and the "waitFor*" actions.
     * <p>
     * The default timeout is 30 seconds. 
     * 
     * @param timeout The timeout in milliseconds, after which the action will return with an error 
     */
    protected void setTimeout(String timeout) { this.timeout = timeout; }
    
    /**
     * Return the default timeout. This can be set by assigning the value for
     * <code>timeout</code> in the <code>seleniumconfiguration</code> file.
     * 
     * @return The default timeout 
     */
    protected String getTimeout() { return this.timeout; }
    
    /**
     * Go to the URL
     * 
     * @throws Exception 
     */
    public void goToURL() throws Exception {
        
        info("Go To URL in Mobile Browser: " + this.url);
        logger.info("Go To URL in Mobile Browser: " + this.url);
        
        try { addScreenshotToReport(); }
        catch(Exception e) { }
        
        try { 
            
            if(!hasBeenInit) initSetup(this.url); 
            
            driver.get(this.url); 
            
            info("Go To URL successfully loaded in Mobile Browser: " + this.url);
            logger.info("Go To URL successfully loaded in Mobile Browser: " + this.url);
            
            try { addScreenshotToReport(); }
            catch(Exception e) { }
           
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Verify that the user is not able access a website (URL) from the mobile web browser - blocked.
     * 
     * @throws Exception 
     */
    public void verifyBlockedAccess() throws Exception {
        
        info("Verify Blocked access: " + this.url);
        logger.info("Verify Blocked access: " + this.url);
        
        try { 
            
            validateWebElementContainsText_XPATH("This site"); 
            validateWebElementContainsText_XPATH("be reached");
            validateWebElementContainsText_XPATH("server IP address could not be found");
        
        }
        catch(Exception e) { 
            
            printDOM();
            
            throw e; 
            
        }
        finally {
            
            try { addScreenshotToReport(); }
            catch(Exception e) { }
            
        }
   
    }
    
    /**
     * Inherits the session from the previous page class.
     * 
     * @param basePage 
     */
    protected void inheritSession(MobileWebPageBase basePage) { 
        
        logger.debug("Inheriting session from previous page: " + basePage);
       
        driver = basePage.getWebDriver();
       
        timeout = basePage.getTimeout();
      
        stopPerformanceCapturePageLoaded();
       
        try { if(doScreenshot) doScreenshot(); }
        catch(Exception e) { logger.error(e); }
                
    }

    /**
     * Return the html source.
     * 
     * @return The html source
     * 
     * @throws Exception
     */
    protected String getHtmlSource() throws Exception { 
        
        try { return driver.getPageSource(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Assert text <code>s</code> in the HTML source
     * 
     * @param s The text string that should be in the HTML source 
     * 
     * @throws Exception
     */
    public void assertTextInHtmlSource(String s) throws Exception { 
        
        logger.info("Asserting that the expected text is present in the html source: " + s);
        
        commandList.addToList("assertTextInHtmlSource: " + s);
        
        try { Assert.assertTrue(getHtmlSource().contains(s), "Expect text '"  + s + "' in html source but not found."); }
        catch(Exception e) { throw e; }
    
    }

    /**
     * Type text <code>value</code> into an element indicated by <code>locator</code>.
     * 
     * @param locator
     * @param value 
     * 
     * @throws Exception
     */
    public void type(String locator, String value) throws Exception { 
        
        logger.debug("Entering data: " + value + " into element at locator: " + locator);
        
        commandList.addToList("type: " + locator + "|" + value);
        
        try { 
            
            if(locator == null) throw new Exception("Unable to enter data because xpath locator value is null");
            if(value != null) {
                
                this.driver.findElement(By.xpath(locator)).sendKeys(value);
                this.driver.findElement(By.xpath(locator)).sendKeys(Keys.RETURN);
                
            } 
        
        }
        catch(Exception e) { throw new Exception(e); }
    
    }

    /**
     * Clicks on a link, button, checkbox or radio button using mouse event
     *
     * @param locator
     *
     * @throws Exception
     */
    public void mouseEventClick(String locator) throws Exception {
        
        commandList.addToList("mouseEventClick: " + locator);

        try {
            
            WebElement element = find(By.xpath(locator));

            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("var evt = document.createEvent('MouseEvents');" + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);" + "arguments[0].dispatchEvent(evt);", element);
        
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }

    /**
     * Type text <code>value</code> into an element indicated by <code>locator</code>.
     * 
     * @param locator
     * @param value 
     * * @param checkAjax check after waiting for ajax to complete
     * 
     * @throws Exception
     */
    public void type(String locator, String value, boolean checkAjax) throws Exception { 
        
        logger.debug("Entering data: " + value + " into element at locator: " + locator);
        
        commandList.addToList("type: " + locator + "|" + value+ "|ajaxCheck:" + checkAjax);
        
        try {
        	
            PerformanceCapture.getInstance().start(getPageName());
          
            waitForConditionElementXPathPresent(locator, timeout);
                
            this.driver.findElement(By.xpath(locator)).sendKeys(value);
           
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
   
    /**
     * Type text <code>value</code> into a file chooser.
     * 
     * @param locator
     * @param value
     * 
     * @throws Exception
     */
    public void enterTextIntoFileChooser(String locator, String value) throws Exception { 
        
        logger.info("Entering data into the active element: " + value);
        
        commandList.addToList("enterTextIntoFileChooser: " + value);
        
        try {
        	
            PerformanceCapture.getInstance().start(getPageName());
            
            WebElement element = getWebElementWithLocator(locator);
        	  
            element.sendKeys(value);
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Type text <code>value</code> into a file chooser.
     * 
     * @param value
     * 
     * @throws Exception
     */
    public void enterTextIntoFileChooser2(String value) throws Exception { 
        
        logger.info("Entering data into a file chooser: " + value);
        
        commandList.addToList("enterTextIntoFileChooser2: " + value);
        
        try { driver.findElement(By.id("inputFile")).sendKeys(value); }
        catch(Exception e) { 
            
            try { driver.findElement(By.xpath("//input[@type='file']")); }
            catch(Exception le) { printDOM(); throw new Exception(le); }
            
        }
    
    }

    /**
     * Clicks on a link, button, checkbox or radio button.
     * <p>
     * If the click action causes a new page to load (like a link usually does), call waitForPageToLoad.
     *
     * @param locator An element locator
     * @param checkAjax check after waiting for ajax to complete
     *
     * @throws Exception
     */
    public void click(String locator, boolean checkAjax) throws Exception { 

        logger.debug("Clicking on element with locator: " + locator + " turning off ajax completion checking");

        commandList.addToList("click: " + locator + "|ajaxCheck:" + checkAjax);
        
        PerformanceCapture.getInstance().start(getPageName());

        waitForConditionElementXPathPresent(locator, timeout);
              
        find(By.xpath(locator)).click();

    }

    /**
     * Clicks on a link, button, checkbox or radio button. 
     * <p>
     * If the click action causes a new page to load (like a link usually does), call waitForPageToLoad. 
     * 
     * @param locator An element locator
     * 
     * @throws Exception
     */
    public void click(String locator) throws Exception { 
        
        logger.debug("Clicking on element with locator: " + locator);
    
        commandList.addToList("click: " + locator);
        
        try { click(locator, true); }
        catch(Exception e) { printDOM(); throw e; }
    
    }
    
    /**
     * Ends the test session, killing the browser.
     * 
     * @throws Exception
     */
    public void stop() throws Exception { 
        
        logger.info("Stopping the selenium session");
    
        commandList.addToList("stop");
        
        try { this.driver.quit(); }
        catch(Exception e) { throw new Exception(e); }
    
    }
     
    /**
     * Use this method to wait for ajax completion with web driver because implicit wait does not
     * work consistently using webdriver selenium wrapper class.
     * 
     * @param timeout 
     */
    public void waitForAjaxCompletion(long timeout) throws Exception {
        
        logger.debug("Wait for Ajax and javascript execution completion with timeout: " + timeout);
        
        try { waitForJSandAjaxToLoad(timeout); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Wait for the condition of element id present in the rendered html. This is an
     * alternative method to use for waiting for page elements to load when 
     * ajax call to find 0 connections does leave enough wait time.
     * 
     * @param elementId
     * @param timeout
     * 
     * @throws Exception 
     */
    public void waitForConditionElementIDPresent(String elementId, String timeout) throws Exception {
        
        logger.debug("Wait For Condition - element Id present: " + timeout + "|" + elementId);
        
        commandList.addToList("waitForConditionElementIDPresent: " + timeout + "|" + elementId);
        
        try {
            
            if(timeout == null) timeout = this.timeout; // protect aginst null conditions
            
            waitForAjaxCompletion(Long.valueOf(timeout));
            
            WebElement myDynamicElement = (new WebDriverWait(driver, Long.valueOf(timeout))).until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));
            
        }
        catch(Exception e) { printDOM(); throw e; }
         
    }
    
    /**
     * Wait for the condition of xpath element locator present in the rendered html. This is an
     * alternative method to use for waiting for page elements to load when 
     * ajax call to find 0 connections does leave enough wait time.
     * 
     * @param xpath
     * @param timeout timeout in seconds
     * 
     * @throws Exception
     */
    public void waitForConditionElementXPathPresent(String xpath, String timeout) throws Exception {
        
        logger.debug("Wait For Condition - xpath present: " + timeout + "|" + xpath);
        
        commandList.addToList("waitForConditionElementXPathPresent: " + timeout + "|" + xpath);
        
        try {
            
            waitForAjaxCompletion(Long.valueOf(timeout));
            
            WebElement myDynamicElement = (new WebDriverWait(driver, Long.valueOf(timeout))).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            
        }
        catch(Exception e) { printDOM(); throw e; }
         
    }
    
    /**
     * Wait for the condition of classname element locator present in the rendered html. This is an
     * alternative method to use for waiting for page elements to load when 
     * ajax call to find 0 connections does leave enough wait time.
     * 
     * @param classname
     * @param timeout timeout in seconds
     * 
     * @throws Exception
     */
    public void waitForConditionElementClassnamePresent(String classname, String timeout) throws Exception {
        
        logger.debug("Wait For Condition - xpath present: " + timeout + "|" + classname);
        
        commandList.addToList("waitForConditionElementXPathPresent: " + timeout + "|" + classname);
        
        try {
            
            waitForAjaxCompletion(Long.valueOf(timeout));
            
            WebElement myDynamicElement = (new WebDriverWait(driver, Long.valueOf(timeout))).until(ExpectedConditions.presenceOfElementLocated(By.className(classname)));
            
        }
        catch(Exception e) { printDOM(); throw e; }
         
    }
    
    /**
     * Verifies that the specified text pattern appears somewhere on the rendered page shown to the user. 
     * 
     * Excludes text in tags.
     * 
     * @param s the pattern to match with the text of the page 
     * 
     * @return <code>true</code> if the pattern matches the text, <code>false</code> otherwise
     * 
     * @throws Exception
     */
    public boolean isTextPresent(String s) throws Exception { 
        
        logger.info("Asserting that text is present in the rendered page: " + s);
    
        commandList.addToList("isTextPresent: " + s); 
        
        try { return getHtmlSource().contains(s); }
        catch(Exception e) { printDOM(); throw e; }
    
    }
    
    /**
     * Gets the text of an element. This works for any element that contains text. 
     * This command uses either the textContent (Mozilla-like browsers) or the 
     * innerText (IE-like browsers) of the element, which is the rendered text 
     * shown to the user. 
     * 
     * @param locator An element locator
     * 
     * @return The text of the element
     */
    public String getText(String locator) throws Exception { 
        
        logger.info("Get text from locator: " + locator);
    
        commandList.addToList("getText: " + locator);
        
        try { return find(By.xpath(locator)).getText(); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Gets the text of an element. This works for any element that contains text. 
     * This command uses either the textContent (Mozilla-like browsers) or the 
     * innerText (IE-like browsers) of the element, which is the rendered text 
     * shown to the user. 
     * 
     * @param locator An element locator
     * 
     * @return The text of the element
     */
    public String getValue(String locator) throws Exception { 
        
        logger.info("Get value from locator: " + locator);
    
        commandList.addToList("getValue: " + locator);
        
        try { return find(By.xpath(locator)).getAttribute("value"); }
        catch(Exception e) { printDOM(); throw e; }
    
    }
    
    /**
     * Simulates the user clicking the "Refresh" button on their browser. 
     * 
     * @throws Exception
     */
    public void refresh() throws Exception { 
        
        logger.info("Simulate Refresh browser button click");
    
        commandList.addToList("refresh");
        
        try { this.driver.navigate().refresh(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Gets whether a toggle-button (checkbox/radio) is checked. 
     * <p>
     * Fails if the specified element doesn't exist or isn't a toggle-button.
     * 
     * @param locator An element locator pointing to a checkbox or radio button 
     * 
     * @return <code>true</code> if the checkbox is checked, <code>false</code> otherwise
     */
    protected boolean isChecked(String locator) throws Exception { 
        
        logger.info("Is the element checked: " + locator);
    
        commandList.addToList("isChecked: " + locator);
        
        try { return find(By.xpath(locator)).isSelected(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Gets the entire text of the page. 
     * 
     * @return The entire text of the page
     * 
     * @throws Exception
     */
    protected String getBodyText() throws Exception { 
    
        commandList.addToList("getBodyText");
        
        try { return this.driver.getPageSource(); }
        catch(Exception e) { throw e; }
    
    }
  
    /**
     * Check a toggle-button (checkbox/radio).
     * 
     * @param locator An element locator
     * 
     * @throws Exception 
     */
    protected void check(String locator) throws Exception { 
        
        logger.info("Checking the element at locator: " + locator);
        
        commandList.addToList("check: " + locator);
        
        try { if(!find(By.xpath(locator)).isSelected()) find(By.xpath(locator)).click(); }
        catch(Exception e) { throw e; }
    
    }
 
    /**
     * Take a screenshot.
     * 
     * @throws Exception 
     */
    public void doScreenshot() throws Exception {
        
        logger.info("Attempting to do a screenshot of the desktop");
    
        ScreenshotCapture screenshotCapture = new ScreenshotCapture();
       
        try {
            
           screenshotCapture.doScreenshot("screenshots" + File.separator
                                   + "image" + System.currentTimeMillis(), ".png");
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Maximize the browser window.
     * 
     * @throws Exception 
     */
    public void maximizeWindow() throws Exception { 
        
        logger.debug("Maximizing the browser window");
        
        commandList.addToList("maximizeWindow");
        
        try { driver.manage().window().maximize(); }
        catch(Exception e) { throw e; }
   
    }
    
    
    /**     
     * Verifies that the specified element is somewhere on the page. 
     * 
     * @param locator
     * 
     * @return 
     */
    public boolean isElementPresent(String locator) {
        
        logger.info("Checking if element is present at locator: " + locator);
    
        commandList.addToList("isElementPresent: " + locator);
        
        try { find(By.xpath(locator)); return true; }
        catch(Exception e) { return false; }
        
    
    }
    
    /**
     * Asserts that the specified element is somewhere on the page. 
     * 
     * @param message - message to describe assertion in case of fail
     * @param locator
     */
    protected void assertElementPresent(String message, String locator) throws Exception { 
        
        logger.info("Assert the element is present/visible: " + locator + "|" + message);
        
        commandList.addToList("assertElementPresent: " + locator + "|" + message);
        
        try { Assert.assertTrue(isElementPresent(locator), message); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Asserts that the specified element is not on the page. 
     * 
     * @param message - message to describe assertion in case of fail
     * @param locator
     */
    protected void assertElementNotPresent(String message, String locator) throws Exception {
        
        logger.info("Assert the element is not present/visible: " + locator + "|" + message);
    
        commandList.addToList("assertElementNotPresent: " + locator + "|" + message);
        
        try { Assert.assertFalse(isElementPresent(locator), message); }
        catch(Exception e) { throw new Exception(e); }
    
    }
    
    /**
     * Create the screenshot in JPG format. Use this method for screen capture
     * without using Selenium or if the selenium object being used does not
     * support browser window capture.
     * 
     * @param screenshotFilename the name of the screenshot image file
     * 
     * @throws Exception 
     */
    public void createScreenshot(String screenshotFilename) throws Exception {
       
        try { screenshotCapture.doScreenshot(screenshotFilename, ScreenshotCapture.JPG); }
        catch(Exception e) { throw e; }
       
    }
  
    /**
     * Stop the performance capture and mark the end time.
     * 
     * @param message 
     */
    public void stopPerformanceCapture(String message) {
        
        logger.debug("Stopping the performance capture");
        
        PerformanceCapture.getInstance().stop(message);
        
    }
    
    /**
     * Causes the currently executing thread to sleep (temporarily cease execution) 
     * for the specified number of milliseconds.
     * 
     * @param millis delay in milliseconds 
     */
    public void sleep(long millis) {
        
        logger.info("Sleep/delay for " + millis + " milliseconds");
        
        try {        	
        	
            Thread.sleep(millis);
        
            PerformanceCapture.getInstance().addSleepTime(millis);        	
       	
        }
        catch(Exception e) { }
        
    }
    
     /* Stop the performance capture and mark the end time.
     *  Use the page Class name as the label 
     */
    public void stopPerformanceCapturePageLoaded() {
        
        logger.debug("Stopping perfromance capture after page is loaded");
            	    
    	stopPerformanceCapture(getPageName());        
    
    }
    
    /**
     * 
     * @return the name of invoking Class
     */
    private String getPageName() {
    	
    	String fullClassName = getClass().getName();
    	
        int i = fullClassName.lastIndexOf(".");
        
    	return fullClassName.substring(i+1);
    	
    }
   
    /**
     * Click on a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void clickOnWebElementContainingText(String text, String className) throws Exception {
        
        logger.info("Clicking on web element containing text: " + text + "|" + className);
            
        commandList.addToList("clickOnWebElementContainingText: " + text + "|" + className);
        
        try {
          
            List<WebElement> elements = findElements(By.className(className));
            
            logger.info("count:" + elements.size());
            
            for(WebElement element:elements) {
                
            	String data = element.getAttribute("name").trim();//element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("haha:" + data + "|" + text);
                   
                if(data.contains(text)) {
                    
                    element.click();
                      
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Click on a web element matching the type and containing the text.
     * 
     * @param text1
     * @param text2
     * @param className
     * 
     * @throws Exception 
     */
    protected void clickOnWebElementContainingText(String text1, String text2, String className) throws Exception {
       
        logger.info("Clicking on web element containing text: " + text1 + "|" + text2 + "|" + className);
        
        commandList.addToList("clickOnWebElementContainingText: " + text1 + "|" + text2 + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
            
            for(WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text1) && data.contains(text2)) {
                   
                    element.click();
                       
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception    
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text1: " + text1 + " and text2: " + text2);
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Click on a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected void clickOnWebElementMatchingText(String text, String className) throws Exception {
        
        logger.info("Clicking on web element matching text: " + text + "|" + className);
        
        commandList.addToList("clickOnWebElementMatchingText: " + text + "|" + className);
        
        try {
        
            List<WebElement> elements = findElements(By.className(className));
            
            for(WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                if(data.equals(text.trim())) {
                   
                    element.click();
                       
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void validateWebElementContainingText(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
            
            logger.info("Elements size: " + elements.size());
           
            for(WebElement element:elements) {
                
                logger.info(element);
                
                String data = element.getText();
                
                if(data != null) data.trim();
                
                logger.info("Element text: " + data);
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and matching the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected void validateWebElementMatchingText(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingText:" + text + "," + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for(WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected void validateWebElementContainingTextValueAttribute(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for(WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected void validateWebElementMatchingTextValueAttribute(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for(WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     *
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected void clickOnWebElementContainingTextValueAttribute(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
        
        commandList.addToList("clickOnWebElementContainingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
          
            for(WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) {
                   
                    element.click();
                   
                    return;
                    
                };
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected void clickOnWebElementMatchingTextValueAttribute(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
        
        commandList.addToList("clickOnWebElementMatchingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
            
            for(WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(text)) {
                    
                    element.click();
                   
                    return;
                    
                };
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param name
     * 
     * @throws Exception 
     */
    protected void validateWebElementContainingTextByName(String name) throws Exception {
        
        logger.info("Validating that element of name: " + name + " exists");
            
        commandList.addToList("validateWebElementContainingTextByName:" + name);
        
        try {
            
            WebElement element = find(By.name(name));
            
            // if we get here, we could not find the element so throw an exception
            if(element == null) throw new Exception("Could not validate a screen component with screen element matching name: " + name);
                   
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a not web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected void validateAllWebElementsDoNotContainText(String text, String className) throws Exception {
        
        logger.info("Validate all web elements do not contain text:" + text + "|" + className);
        
        commandList.addToList("validateAllWebElementsDoNotContainText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
            
            for(WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) throw new Exception("Found a web element / screen component with text and matching type: " + className + " and text: " + text + ", it was not expected to be visible");
                
            }
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate that there is a not web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected void validateAllWebElementsDoNotContainTextByValueAttribute(String text, String className) throws Exception {
        
        logger.info("Validate all web elements do not contain text by value attribute:" + text + "|" + className);
        
        commandList.addToList("validateAllWebElementsDoNotContainTextByValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for(WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) throw new Exception("Found a web element / screen component with text and matching type: " + className + " and text: " + text + ", it was not expected to be visible");
                
            }
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Get list of text values from web elements matching xpath value
     * 
     * @throws Exception 
     */
    protected List<String> getListOfTextValuesFromAllMatchingXPath(String xpath) throws Exception {
        
        logger.info("Get list of text values from all matching XPath:" + xpath);
        
        commandList.addToList("getListOfTextValuesFromAllMatchingXPath:" + xpath);
        
        try {
          
            List<WebElement> elements = findElements(By.xpath(xpath));
            
            List<String> values = new ArrayList<String>();
           
            for(WebElement element:elements) values.add(element.getText().trim());
           
            return values;
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Verify that a set of values appears within a set of elements matching
     * <code>xpath</code>.
     * 
     * @param xpath
     * @param values
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected boolean verifyTextValuesForElementsWithXPath(String xpath, String[] values) throws Exception {
        
        logger.info("Verify text values for elements with XPath:" + xpath + "|" + values);
        
        commandList.addToList("verifyTextValuesForElementsWithXPath:" + xpath + "|" + values);
        
        try {
          
            List<String> textValues = getListOfTextValuesFromAllMatchingXPath(xpath);
            
            for(String value:values) if(!textValues.contains(value)) return false;
            
            // if we get here then each value was in the list so return true
            return true;
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Verify that a single of values appears within a set of elements matching
     * <code>xpath</code>.
     * 
     * @param xpath
     * @param value
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected boolean verifyTextValuesForElementsWithXPath(String xpath, String value) throws Exception {
        
        logger.info("Verify text values for elements with XPath:" + xpath + "|" + value);
        
        commandList.addToList("verifyTextValuesForElementsWithXPath:" + xpath + "|" + value);
        
        try {
            
            List<String> textValues = getListOfTextValuesFromAllMatchingXPath(xpath);
            
            return textValues.contains(value);
           
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Return the index for a certain web element type.
     * 
     * @param text
     * @param className
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected int getElementTypeIndexMatchingText(String text, String className) throws Exception {
        
        logger.info("Get element type index matching text:" + text + "|" + className);
        
        commandList.addToList("getElementTypeIndexMatchingText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
            
            int index = 1;
            
            for(WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(text)) return index;
                
                index++;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find the index of a screen component with text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { throw new Exception(e); }
        
    }
    
    /**
     * Return the index for a certain web element type.
     * 
     * @param text
     * @param className
     * 
     * @return 
     * 
     * @throws Exception 
     */
    protected int getElementTypeIndexContainingText(String text, String className) throws Exception {
        
        logger.info("Get element type index containing text:" + text + "|" + className);
        
        commandList.addToList("getElementTypeIndexContainingText:" + text + "|" + className);
        
        try {
           
            List<WebElement> elements = findElements(By.className(className));
            
            int index = 1;
            
            for(WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking if name attribute of type: " + className + " contains: " + text + "|" + data);
                   
                if(data.contains(text)) return index;
                
                index++;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find the index of a screen component with text in any screen element containing type/classname: " + className + " and text: " + text);
            
        }
        catch(Exception e) { throw new Exception(e); }
        
    }
    
    /**
     * Get the element id for a webelement of type <code>className</code> that contains text in
     * the "name" attribute.
     * 
     * @param className
     * @param text
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getWebElementIdForElementNameAttributeContainsText(String className, String text) throws Exception {
        
        logger.info("Get web element Id for element name attribute contains text:" + text + "|" + className);
        
        commandList.addToList("getWebElementIdForElementNameAttributeContainsText:" + text + "|" + className);
       
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for(WebElement element:elements) {
                   
                String value = element.getAttribute("name").trim();
                 
                if((value == null) || (value.trim().length() == 0)) continue; // null or blank, so ignore
                
                logger.info("Checking for " + value + "|" + text);
               
                if(value.trim().equals(text)) return ((RemoteWebElement) element).getId();
                
            }
          
            throw new Exception("Unable to find cell displayed value for text: " + text);
          
        }
        catch(Exception e) { throw new Exception(e); }
        
    }
    
    /**
     * Click on a web element with a text value matching <code>value</code>.
     * 
     * Only use this method in times where no other interaction method is 
     * possible, because:
     * 
     * 1) There could be times where webdriver implementations choose the 
     *    wrong element (duplicate values). WebDriver implementations may 
     *    work inconsistently if there are duplicate values in the DOM.
     * 2) Using it takes much longer because webdriver will iterate and check
     *    the value of every element in the DOM until it finds the value,
     *    which will add much more time to test execution.
     * 3) It's not backward compatible with with selenium 1.
     * 
     * @param value
     *
     * @throws Exception 
     */
    public void clickOnWebElementWithText(String value) throws Exception {
       
        logger.info("Clicking on any web element with text value: " + value);
        
        commandList.addToList("clickOnWebElementWithText:" + value);
        
        try { 
            
            find(By.linkText(value)).click();
           
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement.
     * 
     * @param element
     * 
     * @throws Exception 
     */
    public void clearWebElement(WebElement element) throws Exception { 
        
        logger.debug("Clear web element: " + element);
        
        commandList.addToList("clearWebElement:" + element);
    
        try { element.clear(); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement looked up using xpath.
     * 
     * @param locator
     * 
     * @throws Exception 
     */
    public void clearWebElement(String locator) throws Exception { 
        
        logger.debug("Clear web element: " + locator);
        
        commandList.addToList("clearWebElement:" + locator);
    
        try { clearWebElement(getWebElementWithLocator(locator)); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Get the element matching id attribute.
     * 
     * @param id
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementWithId(String id) throws Exception {
      
        try { return getWebElementWithId(id, false); }
        catch(Exception e) { throw new Exception(e); }
        
    }
    
    /**
     * Get the element matching id attribute.
     * 
     * @param id
     * @param disableImplicitWait
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementWithId(String id, boolean disableImplicitWait) throws Exception {
       
        logger.info("Preparing to find web element by id: " + id + "|" + disableImplicitWait);
        
        commandList.addToList("getWebElementWithId:" + id + "|" + disableImplicitWait);
        
        try { 
            
            if(disableImplicitWait) disableImplicitWait();
            
            return find(By.id(id)); 
        
        }
        catch(Exception e) { throw new Exception(e); }
        finally {
            
            // enable implicit wait regardless if it was turned off at the beginning of the method
            try { enableImplicitWait(); }
            catch(Exception le) { }
            
        }
        
    }
    
    /**
     * Get the element matching the xpath locator.
     * 
     * @param locator
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementWithLocator(String locator) throws Exception {
       
        try { return getWebElementWithLocator(locator, false); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get the element matching the xpath locator.
     * 
     * @param locator
     * @param disableImplicitWait
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementWithLocator(String locator, boolean disableImplicitWait) throws Exception {
       
        logger.debug("Preparing to find web element by locator: " + locator + "|" + disableImplicitWait);
        
        commandList.addToList("getWebElementWithLocator:" + locator + "|" + disableImplicitWait);
        
        try { 
            
            if(disableImplicitWait) disableImplicitWait();
            
            return find(By.xpath(locator)); 
        
        }
        catch(Exception e) { throw new Exception(e); }
        finally {
            
            // enable implicit wait regardless if it was turned off at the beginning of the method
            try { enableImplicitWait(); }
            catch(Exception le) { }
            
        }
        
    }
    
    /**
     * Enter data into rich text editor.
     * 
     * @param xpath
     * @param text
     * 
     * @throws Exception 
     */
    public void enterTextIntoRichTextEditor(String xpath, String text) throws Exception {
       
        logger.info("Enter data into rich text editor: " + xpath + "|" + text);
        
        commandList.addToList("enterTextIntoRichTextEditor:" + xpath + "|" + text);
        
        try { 
           
            WebElement element = find(By.tagName("body"));
       
            element.clear();
            
            element.sendKeys(text);
            
            driver.switchTo().window(driver.getWindowHandle()); 
        
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }

    /**
     * Get the webelement attribute by attribue
     * 
     * @param attributeLocator an element locator followed by an attribute
     * 
     * @param xpath
     * 
     * @return the value of the specified attribute 
     * 
     * @throws Exception 
     */
    public String getWebElementAttribute(String xpath, String attributeLocator) throws Exception {
       
        logger.info("Get web element attribute:" + xpath + "|" + attributeLocator);
        
        commandList.addToList("getWebElementAttribute:" + xpath + "|" + attributeLocator);
       
        try {
            
            WebElement element = find(By.xpath(xpath));
           
            return element.getAttribute(attributeLocator);

        }
        catch(Exception e) { throw new Exception(e); }
        
    }
    

    /**
     * Get the webelements attribute by attribue
     * 
     * @param attributeLocator an element locator followed by an attribute
     * 
     * @param xpath
     * 
     * @return the value of the specified attribute 
     * 
     * @throws Exception 
     */
    public List<String> getWebElementsAttribute(String xpath, String attributeLocator) throws Exception {
       
        logger.info("Get web elements attribute:" + xpath + "|" + attributeLocator);
        
        commandList.addToList("getWebElementsAttribute:" + xpath + "|" + attributeLocator);
        
        try {
            
            List<WebElement> elements = findElements(By.xpath(xpath));
            
            List<String> values = new ArrayList<String>();
            
            for (WebElement element:elements) values.add(element.getAttribute(attributeLocator));
           
            return values;

        }
        catch(Exception e) { throw new Exception(e); }
        
    }
    
    /**
     * Switch to work area.
     * 
     * @param xpath
     * 
     * @throws Exception 
     */
    public void switchToWorkarea(String xpath) throws Exception {
        
        logger.info("Switch to work area located at: " + xpath);
        
        commandList.addToList("switchToWorkarea:" + xpath);
        
        try {
            
            driver.switchTo().defaultContent();
        
            WebElement frame = find(By.xpath(xpath));
       
            driver.switchTo().frame(frame);
        
        }
        catch(Exception e) { throw new Exception(e); }
   
    }
    
    /**
     * Switch back to current window
     *
     * @throws Exception
     */
    public void switchBackToCurrentWindow() throws Exception {
        
        logger.info("Switch back to current window");
        
        commandList.addToList("switchBackToCurrentWindow");
       
        try { this.driver.switchTo().defaultContent(); }
        catch(Exception e) { throw new Exception(e); }
  
    }
    
    /**
     * This method will check if  elemet is displayed on the page for a certain
     * period of time.
     * 
     * If the element is found within this time, the checking will stop and 
     * successfully return without exception thrown.
     * 
     * @param locator
     * 
     * @throws Exception 
     */
    public void elementIsDisplayedWithTimeout(String locator) throws Exception {
        
        logger.info("Element is displayed with timeout:" + locator);
        
        commandList.addToList("elementIsDisplayedWithTimeout:" + locator);
        
        try {
            
            int intSyncTimeout = Integer.valueOf(timeout);
            
            for(int i = 0; i < intSyncTimeout; i++) {
                
                if(find(By.xpath(locator)).isDisplayed()) return;
                
            }
            
        }
        catch(Exception e) { throw new Exception(e); }
     
    }
    
    /**
     * Simulate an Enter key press on the active element
     * 
     * @throws Exception 
     */
    public void clickEnterKeyOnActiveElement() throws Exception {
        
        logger.info("Click enter key on active element");
        
        commandList.addToList("clickEnterKeyOnActiveElement");
        
        try { this.driver.switchTo().activeElement().sendKeys(Keys.ENTER); }
        catch(Exception e) { throw new Exception(e); }
        
     }
    
    /**
     * Select multiple elements from a text area field. 
      * 
      * It simulates the Ctrl + Click operation.
      * 
     * @param locator
     * 
     * @throws Exception 
     */
    public void selectMultipleElements(String locator) throws Exception {
        
        logger.info("Select multiple elements:" + locator);
        
        commandList.addToList("selectMultipleElements: " + locator);
            
        try {
                 
            Actions actions = new Actions(driver);
                  
            actions.keyDown(Keys.CONTROL).click(find(By.xpath(locator))).keyUp(Keys.CONTROL).build().perform();
                 
        }
        catch(Exception e) { throw new Exception(e); }
              
    }
    
    /**
     * Get the selected option for a Select element.
     * 
     * @param xpathLocator
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getSelectedOptionValue(String xpathLocator) throws Exception {
        
        logger.info("Get selected option value:" + xpathLocator);
        
        commandList.addToList("getSelectedOptionValue: " + xpathLocator);
        
        try { return new Select(find(By.xpath(xpathLocator))).getFirstSelectedOption().getText(); }
        catch(Exception e) { throw new Exception(e); }
        
    }
    
    /**
     * Get the value for an attribute of an object.
     * 
     * @param object
     * @param attributeLocator
     * 
     * @return
     * 
     * @throws Exception 
     */
      
    public String getObjectAttribute(String object, String attributeLocator) throws Exception {
          
        logger.info("Get object attribute:" + object + "|" + attributeLocator);
          
        commandList.addToList("getObjectAttribute: " + object + "|" + attributeLocator);
           
        try {
        
            WebElement objectName = find(By.xpath(object));
         
            return objectName.getAttribute(attributeLocator);
        
        }
        catch(Exception e) { throw new Exception(e); }

    }

    /**
     * Perform scroll to the given web element
     *
     * @param xpath
     *
     * @throws Exception
     */
    public void scrollingToElementofAPage(String xpath) throws Exception {
        
        commandList.addToList("scrollingToElementOfAPage: " + xpath);

        try {
            
            WebElement element = find(By.xpath(xpath));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            
        }
        catch(Exception e) { throw new Exception(e); }
    }

    /**
     * Perform drag and drop operation with given web element
     *
     * @param xpath
     * @param x_axis
     * @param y_axis
     *
     * @throws Exception
     */
    public void dragAndDropBy(String xpath, int x_axis, int y_axis) throws Exception {

        commandList.addToList("dragAndDropBy " + xpath + " x-axis|" + x_axis + " y-axis|" + y_axis);

        try {

            WebElement drag = find(By.xpath(xpath));

            Actions action = new Actions(driver);

            action.dragAndDropBy(drag, x_axis, y_axis).build().perform();

        }
        catch(Exception e) { throw new Exception(e); }
    }


    /**
     * wait until the element is clickable or not
     *
     * @param xpath
     *
     * @return
     */
    public boolean waitUntilElementBecomeClickable(String xpath) {
        
        commandList.addToList("waitUntilElementBecomeClickable: " + xpath);

        try {
            
            WebDriverWait wait = new WebDriverWait(driver, 5);
            
            WebElement element = find(By.xpath(xpath));
            
            wait.until(ExpectedConditions.elementToBeClickable(element));
            
            return true;
        
        }
        catch (Exception e) { return false; }
    
    }

    /**
     * Wait until all ajax connections are completed and closed and that javascript has been completed executed.
     * 
     * @return 
     */
    public boolean waitForJSandAjaxToLoad(long timeout) {
        
        logger.debug("Wait for javascript and ajax to load:" + timeout);
          
        commandList.addToList("waitForJSandAjaxToLoad: " + timeout);

        WebDriverWait wait = new WebDriverWait(driver, timeout);

        // If using jQuery
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
      
            @Override
            public Boolean apply(WebDriver driver) {
        
                try { return ((Long)((JavascriptExecutor)driver).executeScript("return jQuery.active") == 0); }
                catch (Exception e) { return true; }
      
            }
    
        };
        
        // If using prototype
        ExpectedCondition<Boolean> prototypeLoad = new ExpectedCondition<Boolean>() {
      
            @Override
            public Boolean apply(WebDriver driver) {
        
                try { return ((Long)((JavascriptExecutor)driver).executeScript("return Ajax.activeRequestCount") == 0); }
                catch (Exception e) { return true; }
      
            }
    
        };
        
        // If using angular.js
        ExpectedCondition<Boolean> angularJSLoad = new ExpectedCondition<Boolean>() {
      
            @Override
            public Boolean apply(WebDriver driver) {
        
                try { return ((Long)((JavascriptExecutor)driver).executeScript("return angular.element(document.body).injector().get(\'$http\').pendingRequests.length;") == 0); }
                catch (Exception e) { return true; }
      
            }
    
        };

        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
      
            @Override
            public Boolean apply(WebDriver driver) { return ((JavascriptExecutor)driver).executeScript("return document.readyState").toString().equals("complete"); }
    
        };

        return wait.until(jQueryLoad) && wait.until(angularJSLoad) && wait.until(prototypeLoad) && wait.until(jsLoad);

    }
    
    /**
     * This is a special method only for Cloudmondo requirement of dynamic naming of base url for testing
     * against specific environments determined at runtime.
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected String getModifiedStartURL() {
        
        String targetEnvironment = null;
        
        if(properties.get(StringCapabilities.TARGET_ENVIRONMENT_KEY.getCapability()) != null) targetEnvironment = properties.get(StringCapabilities.TARGET_ENVIRONMENT_KEY.getCapability());
        else if(System.getProperty("CM_TARGET_ENVIRONMENT") != null) { targetEnvironment = System.getProperty("CM_TARGET_ENVIRONMENT"); }
        else { targetEnvironment = properties.get("CM_TARGET_ENVIRONMENT"); } 
        
        try { return utils.getModifiedURL(startURL, targetEnvironment); }
        catch(Exception e) { logger.error(e); }
        
        return properties.get(StringCapabilities.URL.getCapability());

    }
    
    /**
     * Get the entire element hierarchy.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getElementDOM() throws Exception {
        
        try { return this.driver.getPageSource(); }
        catch(Exception e) { throw new Exception(e); }
        
    }
    
    /**
     * Print the element hierarchy.
     * 
     * @throws Exception 
     */
    public void printDOM() {
        
        try { /*logger.error("DOM|" + getElementDOM());*/ }
        catch(Exception e) { logger.error("Not able to get element DOM"); }
        
    }
    
    /**
     * Print the element hierarchy.
     * 
     * @throws Exception 
     */
    public void logDOM() {
        
        try { logger.error("DOM|" + getElementDOM()); }
        catch(Exception e) { logger.error("Not able to get element DOM"); }
        
    }
    
    /**
     * Scroll down
     * 
     * @throws Exception 
     */
    public void scrollDown() throws Exception {
        
        logger.info("scroll down");
        
        commandList.addToList("scroll down");
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollBy(0,250)", "");
                 
        }
        catch(Exception e) { throw new Exception(e); }
              
    }
    
    /**
     * Scroll down - specify pixels to scroll down
     * 
     * @param pixelsDown
     * 
     * @throws Exception 
     */
    public void scrollDown(int pixelsDown) throws Exception {
        
        logger.info("scroll down: " + pixelsDown);
        
        commandList.addToList("scroll down: " + pixelsDown);
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollBy(0," + String.valueOf(pixelsDown) + ")", "");
                 
        }
        catch(Exception e) { throw new Exception(e); }
              
    }
    
    /**
     * Scroll up
     * 
     * @throws Exception 
     */
    public void scrollUp() throws Exception {
        
        logger.info("scroll up");
        
        commandList.addToList("scroll up");
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollBy(0,-250)", "");
                 
        }
        catch(Exception e) { throw new Exception(e); }
              
    }
    
    /**
     * Scroll up - specify pixels to scroll up
     * 
     * @param pixelsUp
     * 
     * @throws Exception 
     */
    public void scrollUp(int pixelsUp) throws Exception {
        
        logger.info("scroll up");
        
        commandList.addToList("scroll up");
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollBy(0,-" + String.valueOf(pixelsUp) + ")", "");
                 
        }
        catch(Exception e) { throw new Exception(e); }
              
    }
    
    /**
     * Scroll down to the bottpm of the web page
     * 
     * @throws Exception 
     */
    public void scrollDownToBottomOfPage() throws Exception {
        
        logger.info("scroll down to botom of page");
        
        commandList.addToList("scroll down to bottom of page");
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                 
        }
        catch(Exception e) { throw new Exception(e); }
              
    }
    
    /**
     * Click on a link containing the text.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    protected void clickOnWebElementByPartialLinkText(String text) throws Exception {
        
        logger.info("Click on a link containing the text: " + text);
            
        commandList.addToList("clickOnWebElementByPartialLinkText: " + text);
        
        try { find(By.partialLinkText(text)).click(); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Validate a link matching the text is visible.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    protected void validateLinkIsVisible(String text) throws Exception {
        
        logger.info("Validate a link matching the text is visible: " + text);
            
        commandList.addToList("validateLinkIsVisible: " + text);
        
        try { find(By.linkText(text)); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Click on a link matching the text.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    protected void clickOnWebElementByLinkText(String text) throws Exception {
        
        logger.info("Click on a link matching the text: " + text);
            
        commandList.addToList("clickOnWebElementByPartialLinkText: " + text);
        
        try { find(By.linkText(text)).click(); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }

    /**
     * Get the element matching css selector. This can be used for many purposes including getting by element tag name
     * 
     * @param cssSelector
     * 
     * @return
     * 
     * @throws Exception 
     */
    public List<WebElement> getWebElementsWithCSS(String cssSelector) throws Exception {

        logger.debug("Preparing to find web element by css selector: " + cssSelector);

        commandList.addToList("getWebElementWithCSS:" + cssSelector);
        
        try { return findElements(By.cssSelector(cssSelector)); }
        catch(Exception e) { throw new Exception(e); }
        
    }

    /**
     * Click on a web element matching the css selector and containing the text.
     * 
     * @param text
     * @param cssSelector
     * 
     * @throws Exception 
     */
    public void clickOnWebElementContainingTextCSSSelector(String text, String cssSelector) throws Exception {
       
        logger.debug("Clicking on web element with css selector and containing text: " + text + "|" + cssSelector);
        
        commandList.addToList("clickOnWebElementContainingTextCSSSelector: " + text + "|" + cssSelector);
        
        try {
                    
            getWebElementWithCSSAndContainsText(text, cssSelector).click();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Click on a web element matching the css selector and containing the text.
     * 
     * @param text
     * @param cssSelector
     * 
     * @throws Exception 
     */
    public void clickOnWebElementMatchingTextCSSSelector(String text, String cssSelector) throws Exception {
       
        logger.info("Clicking on web element with css selector and matching text: " + text + "|" + cssSelector);
        
        commandList.addToList("clickOnWebElementMatchingTextCSSSelector: " + text + "|" + cssSelector);
        
        try {
                    
            getWebElementWithCSSAndMatchesText(text, cssSelector).click();
           
        }
        catch(Exception e) { throw e; }
        
    }

   /**
     * Get a web element matching the css selector and containing the text.
     * 
     * @param text
     * @param cssSelector
     * 
     * @throws Exception 
     * 
     * @return
     */
    public WebElement getWebElementWithCSSAndContainsText(String text, String cssSelector) throws Exception {

        logger.debug("Get the web element with css selector and containing text: " + text + "|" + cssSelector);

        commandList.addToList("getWebElementWithCSSAndContainsText: " + text + "|" + cssSelector);

         try {

                List<WebElement> elements = getWebElementsWithCSS(cssSelector);

                for (WebElement element:elements) {

                    String data = element.getText().trim();

                    if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore

                    logger.debug("Checking value: " + data);

                    if(data.contains(text)) return element;

                }
                
                // if we get here, we could not find the element so throw an exception
               throw new Exception("Could not find text in any screen element matching type: " + cssSelector + " and text: " + text);
        
         }
        catch(Exception e) { printDOM(); throw new Exception(e); }

        

    }
    
    /**
     * Get a web element matching the css selector and containing the text.
     * 
     * @param text
     * @param cssSelector
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementWithCSSAndMatchesText(String text, String cssSelector) throws Exception {

        logger.debug("Get the web element with css selector and containing text: " + text + "|" + cssSelector);

        commandList.addToList("getWebElementWithCSSAndContainsText: " + text + "|" + cssSelector);

        try {

                List<WebElement> elements = getWebElementsWithCSS(cssSelector);
                
                for(WebElement element:elements) {

                    String data = element.getText().trim();

                    if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore

                    logger.debug("Checking value: " + data);

                    if(data.trim().equals(text.trim())) return element;

                }

                // if we get here, we could not find the element so throw an exception
                throw new Exception("Could not find text in any screen element matching type: " + cssSelector + " and text: " + text);
   
        }    
        catch(Exception e) { printDOM(); throw new Exception(e); }

    }


    /**
     * Get a web element matching the css selector and matches a type and containing the text.
     * 
     * @param type
     * @param typeValue
     * @param cssSelector
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementWithCSSAndMatchesTypeAndContainsText(String type, String typeValue, String cssSelector) throws Exception {
       
        logger.info("Get a web element matching the css selector and matches a type and containing the text: " + type + "|" + typeValue + "|" + cssSelector);
        
        commandList.addToList("getWebElementWithCSSAndMatchesTypeAndContainsText: " + type + "|" + typeValue + "|" + cssSelector);
        
        try {
            
            List<WebElement> elements = getWebElementsWithCSS(cssSelector);
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("type");
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.debug("Checking value: " + data);
                   
                if(data.contains(typeValue)) return element;
                
            }
            
            // if we get here, we could not find the element so throw an exception    
            throw new Exception("Could not find text in any screen element matching type: " + cssSelector + " and text: " + typeValue + " and matching type: " + type);
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Get a web element matching the css selector and matches a data-atid and containing the text.
     * 
     * @param value
     * @param cssSelector
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementWithCSSAndMatchesDataAtIdAndContainsText(String value, String cssSelector) throws Exception {
       
        logger.info("Get a web element matching the css selector and matches a type and containing the text: " + value + "|" + cssSelector);
        
        commandList.addToList("getWebElementWithCSSAndMatchesDataAtIdAndContainsText: " + value + "|" + cssSelector);
        
        try {
            
            List<WebElement> elements = getWebElementsWithCSS(cssSelector);
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("data-atid");
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.debug("Checking value: " + data);
                   
                if(data.contains(value)) return element;
                
            }
            
            // if we get here, we could not find the element so throw an exception    
            throw new Exception("Could not find text in any screen element matching type: " + cssSelector + " and text: " + value + " and matching type: data-atid");
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Get a web element matching the css selector and matches a type and containing the text.
     * 
     * @param type
     * @param text
     * @param cssSelector
     * 
     * @throws Exception 
     */
    public void clickOnWebElementWithCSSAndMatchesTypeAndContainsText(String type, String text, String cssSelector) throws Exception {
       
        logger.info("Click on a web element matching the css selector and matches a type and containing the text: " + type + "|" + text + "|" + cssSelector);
        
        commandList.addToList("clickOnWebElementWithCSSAndMatchesTypeAndContainsText: " + type + "|" + text + "|" + cssSelector);
        
        try { getWebElementWithCSSAndMatchesTypeAndContainsText(type, text, cssSelector).click(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get a web element matching the css selector and matches a data-atid and containing the text.
     * 
     * @param text
     * @param cssSelector
     * 
     * @throws Exception 
     */
    public void clickOnWebElementWithCSSAndMatchesAtIdAndContainsText(String text, String cssSelector) throws Exception {
       
        logger.info("Click on a web element matching the css selector and matches a type and containing the text: " + text + "|" + cssSelector);
        
        commandList.addToList("clickOnWebElementWithCSSAndMatchesAtIdAndContainsText: " + text + "|" + cssSelector);
        
        try { getWebElementWithCSSAndMatchesDataAtIdAndContainsText(text, cssSelector).click(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get the element with the matching resource id attribute.
     * 
     * @param resourceId
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementAtResourceId(String resourceId) throws Exception {
       
        logger.info("Get the web element at resource id: " + resourceId);
        
        commandList.addToList("getWebElementAtResourceId:" + resourceId);
        
        try { return find(By.id(resourceId)); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Get the element with the matching data-atid attribute.
     * 
     * @param id
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementAtDataAtId(String id) throws Exception {
       
        try { return getWebElementWithLocator("//*[@data-atid='" + id + "']"); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Click on the element with the matching resource id attribute.
     * 
     * @param resourceId
     * 
     * @throws Exception 
     */
    public void clickOnWebElementWithResourceId(String resourceId) throws Exception {
       
        logger.info("Click on the element with the matching resource id attribute: " + resourceId);
        
        commandList.addToList("clickOnWebElementWithResourceId:" + resourceId);
        
        try { getWebElementAtResourceId(resourceId).click(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Type text <code>value</code> into a web element.
     * 
     * @param type
     * @param typeValue
     * @param cssSelector
     * @param value
     * 
     * @throws Exception 
     */
    public void enterTextIntoWebElementUsingCSSSelector(String type, String typeValue, String cssSelector, String value) throws Exception { 
        
        logger.debug("Type text value into a web element at the CSS selector: " + cssSelector + "|" + typeValue + "|" + value);
        
        commandList.addToList("enterTextIntoWebElementUsingCSSSelector: " + cssSelector + "|" + typeValue + "|" + value);
        
        try {
        
            WebElement element = getWebElementWithCSSAndMatchesTypeAndContainsText(type, typeValue, cssSelector);
        	  
            element.sendKeys(value);
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Type text <code>value</code> into a web element.
     * 
     * @param resourceId
     * @param value
     * 
     * @throws Exception
     */
    public void enterTextIntoWebElementUsingResourceId(String resourceId, String value) throws Exception { 
        
        logger.debug("Type text value into a web element at the resource id: " + resourceId + "|" + value);
        
        commandList.addToList("enterTextIntoWebElementUsingResourceId: " + resourceId + "|" + value);
        
        try {
        
            WebElement element = getWebElementAtResourceId(resourceId);
        	  
            element.sendKeys(value);
             
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Type text <code>value</code> into a web element.
     * 
     * @param dataAtIdValue
     * @param cssSelector
     * @param value
     * 
     * @throws Exception
     */
    public void enterTextIntoWebElementUsingDataAtId(String dataAtIdValue, String cssSelector, String value) throws Exception { 
        
        logger.debug("Type text value into a web element at the data-atid: " + cssSelector + "|" + dataAtIdValue + "|" + value);
        
        commandList.addToList("enterTextIntoWebElementUsingDataAtId: " + cssSelector + "|" + dataAtIdValue + "|" + value);
        
        try {
        
            WebElement element = getWebElementWithCSSAndMatchesDataAtIdAndContainsText(dataAtIdValue, cssSelector);
        	  
            element.sendKeys(value);
             
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Type text <code>value</code> into a web element.
     * 
     * @param dataAtIdValue
     * @param value
     * 
     * @throws Exception
     */
    public void enterTextIntoWebElementUsingDataAtId(String dataAtIdValue, String value) throws Exception { 
        
        logger.debug("Type text value into a web element at the data-atid: " + "|" + dataAtIdValue + "|" + value);
        
        commandList.addToList("enterTextIntoWebElementUsingDataAtId: " + "|" + dataAtIdValue + "|" + value);
        
        try {
        
            WebElement element = getWebElementWithLocator("//*[@data-atid='" + dataAtIdValue + "']");
        	  
            element.sendKeys(value);
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Get the text <code>value</code> into a web element.
     * 
     * @param resourceId
     * 
     * @return 
     * 
     * @throws Exception
     */
    public String getTextInWebElementUsingResourceId(String resourceId) throws Exception { 
        
        logger.debug("Get the text value in a web element at the resource id: " + resourceId);
        
        commandList.addToList("getTextInWebElementUsingResourceId: " + resourceId);
        
        try { return getWebElementAtResourceId(resourceId).getText(); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Get the text <code>value</code> into a web element.
     * 
     * @param id
     * 
     * @return 
     * 
     * @throws Exception
     */
    public String getTextInWebElementUsingDataAtId(String id) throws Exception { 
        
        logger.debug("Get the text value in a web element at the data-atid: " + id);
        
        commandList.addToList("getTextInWebElementUsingDataAtId: " + id);
        
        try { return getWebElementWithLocator("//*[@data-atid='" + id + "']").getText(); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Clear a web element by resource id.
     * 
     * @param resourceId
     * 
     * @throws Exception 
     */
    public void clearWebElementUsingResourceId(String resourceId) throws Exception { 
        
        logger.debug("Get the text value in a web element at the resource id: " + resourceId);
        
        commandList.addToList("getTextInWebElementUsingResourceId: " + resourceId);
        
        try { clearWebElement(getWebElementAtResourceId(resourceId)); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Clear a web element by data-atid.
     * 
     * @param value
     * 
     * @throws Exception 
     */
    public void clearWebElementUsingDataAtId(String value) throws Exception { 
        
        logger.debug("Get the text value in a web element at the data-atid: " + value);
        
        commandList.addToList("clearWebElementUsingDataAtId: " + value);
        
        try { clearWebElement(getWebElementWithLocator("//*[@data-atid='" + value + "']")); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
    
    }
    
    /**
     * Get the element with the matching resource id attribute.
     * 
     * @param resourceId
     * 
     * @throws Exception 
     */
    public void validateWebElementAtResourceId(String resourceId) throws Exception {
       
        try { getWebElementAtResourceId(resourceId); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Get the element with the matching data-atid attribute.
     * 
     * @param id
     * 
     * @throws Exception 
     */
    public void validateWebElementAtDataAtId(String id) throws Exception {
       
        try { getWebElementWithLocator("//*[@data-atid='" + id + "']"); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Get the element with the matching data-atid attribute.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void validateWebElementContainsText(String text) throws Exception {
       
        try { getWebElementWithLocator("//*[text()='" + text + "']"); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Get the element with the matching data-atid attribute.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void validateWebElementContainsText_XPATH(String text) throws Exception {
       
        try { getWebElementWithLocator("//*[contains(text(),'" + text + "')]"); }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Click on an element with text.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void clickOnElementContainsText_XPATH(String text) throws Exception {
        
        try { clickOnElementContainsText_XPATH(text, false); }
        catch(Exception e) { throw e; }
    }
    
    /**
     * Click on an element with text.
     * 
     * @param text
     * @param disableImplicitWait
     * 
     * @throws Exception 
     */
    public void clickOnElementContainsText_XPATH(String text, boolean disableImplicitWait) throws Exception {
        
        try { 
            
            if(disableImplicitWait) disableImplicitWait();
            
            click("//*[contains(text(),'" + text + "')]"); 
        
        }
        catch(Exception e) { throw e; }
        finally {
            
            // enable implicit wait regardless if it was turned off at the beginning of the method
            try { enableImplicitWait(); }
            catch(Exception le) { }
            
        }
    }
    
    /**
     * Click on an element matching text.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void clickOnElementMatchesText_XPATH(String text) throws Exception {
        
        try { clickOnElementMatchesText_XPATH(text, false); }
        catch(Exception e) { throw e; }
    }
    
    /**
     * Click on an element matching text.
     * 
     * @param text
     * @param disableImplicitWait
     * 
     * @throws Exception 
     */
    public void clickOnElementMatchesText_XPATH(String text, boolean disableImplicitWait) throws Exception {
        
        try { 
            
            if(disableImplicitWait) disableImplicitWait();
            
            click("//*[text()='" + text + "']"); 
        
        }
        catch(Exception e) { throw e; }
        finally {
            
            // enable implicit wait regardless if it was turned off at the beginning of the method
            try { enableImplicitWait(); }
            catch(Exception le) { }
            
        }
        
    }
    
    /**
     * Click on an element with text.
     * 
     * @param text
     * @param attributeName
     * 
     * @throws Exception 
     */
    public void clickOnElementContainsTextAndAttribute_XPATH(String text, String attributeName) throws Exception {
        
        try { clickOnElementContainsTextAndAttribute_XPATH(text, attributeName, false); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Click on an element with text.
     * 
     * @param text
     * @param attributeName
     * @param disableImplicitWait
     * 
     * @throws Exception 
     */
    public void clickOnElementContainsTextAndAttribute_XPATH(String text, String attributeName, boolean disableImplicitWait) throws Exception {
        
        try { 
            
            if(disableImplicitWait) disableImplicitWait();
            
            click("//*[@" + attributeName + "='" + text + "']"); 
        
        }
        catch(Exception e) { throw e; }
        finally {
            
            // enable implicit wait regardless if it was turned off at the beginning of the method
            try { enableImplicitWait(); }
            catch(Exception le) { }
            
        }
    
    }
    
    /**
     * Click on an element with text.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void clickOnElementContainsDataAtIdValue_XPATH(String text) throws Exception {
        
        try { clickOnElementContainsDataAtIdValue_XPATH(text, false); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Click on an element with text.
     * 
     * @param text
     * @param disableImplicitWait
     * 
     * @throws Exception 
     */
    public void clickOnElementContainsDataAtIdValue_XPATH(String text, boolean disableImplicitWait) throws Exception {
        
        try { 
            
            if(disableImplicitWait) disableImplicitWait();
            
            clickOnElementContainsTextAndAttribute_XPATH(text, "data-atid"); 
        
        }
        catch(Exception e) { throw e; }
        finally {
            
            // enable implicit wait regardless if it was turned off at the beginning of the method
            try { enableImplicitWait(); }
            catch(Exception le) { }
            
        }
    
    }
    
    /**
     * Choose a file and emulate a Drag And Drop
     * 
     * @param file
     * @param element
     * 
     * @throws Exception 
     */
    public void dropFile(String file, WebElement element) throws Exception {
        
        try { dropFile(file, element, 0, 0); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Choose a file and emulate a Drag And Drop
     * 
     * @param file
     * @param element
     * @param offsetX
     * @param offsetY
     * 
     * @throws Exception 
     */
    public void dropFile(String file, WebElement element, int offsetX, int offsetY) throws Exception {
        
        String JS_DROP_FILE =
        "var target = arguments[0]," +
        "    offsetX = arguments[1]," +
        "    offsetY = arguments[2]," +
        "    document = target.ownerDocument || document," +
        "    window = document.defaultView || window;" +
        "" +
        "var input = document.createElement('INPUT');" +
        "input.type = 'file';" +
        "input.style.display = 'none';" +
        "input.onchange = function () {" +
        "  var rect = target.getBoundingClientRect()," +
        "      x = rect.left + (offsetX || (rect.width >> 1))," +
        "      y = rect.top + (offsetY || (rect.height >> 1))," +
        "      dataTransfer = { files: this.files };" +
        "" +
        "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {" +
        "    var evt = document.createEvent('MouseEvent');" +
        "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);" +
        "    evt.dataTransfer = dataTransfer;" +
        "    target.dispatchEvent(evt);" +
        "  });" +
        "" +
        "  setTimeout(function () { document.body.removeChild(input); }, 25);" +
        "};" +
        "document.body.appendChild(input);" +
        "return input;";
    
        try {
    
            JavascriptExecutor jse = (JavascriptExecutor) driver;
    
            WebDriverWait wait = new WebDriverWait(driver, 30);

            WebElement input = (WebElement)jse.executeScript(JS_DROP_FILE, element, offsetX, offsetY);
    
            input.sendKeys((new File(file)).getAbsoluteFile().toString());
    
            wait.until(ExpectedConditions.stalenessOf(input));
            
        }
        catch(Exception e) { throw e; }

    }
    
    /*
     * These next set of methods are an attempt to speed up execution if locators are not found or reachable
    */
    
    /**
     * Bypass the default implicit wait to zero seconds
     * 
     * @throws Exception 
     */
    protected void disableImplicitWait() throws Exception {
        
        logger.debug("Bypass the default implicit wait to zero seconds");
        
        commandList.addToList("byPassImplicitWait");
        
        this.timeout = "0";
        
        try { driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Restore the default implicit wait to zero seconds
     * 
     * @throws Exception 
     */
    protected void enableImplicitWait() throws Exception {
        
        logger.debug("Re-enable the default implicit wait");
        
        commandList.addToList("enableImplicitWait");
        
        try { 
            
            // set the timeout
            if(properties.get("timeout") != null) { this.timeout = properties.get("timeout"); }
            else this.timeout = TIMEOUT_DEFAULT;
            
            logger.debug("Timeout is set to: " + this.timeout);
            
            driver.manage().timeouts().implicitlyWait(Long.valueOf(this.timeout), TimeUnit.SECONDS); 
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Hover the mouse over the link with text.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void hoverOverText(String text) throws Exception {
        
        info("Hover the mouse over the link with text: " + text);
        
        commandList.addToList("hoverOverText|" + text);
        
        try {
            
            Actions actions = new Actions(driver);

            WebElement element = find(By.linkText(text));

            actions.moveToElement(element).perform();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    
    /**
     * Allow for setting the timeout manually (if needed)
     * 
     * @throws Exception 
     */
    public void setTimeout() throws Exception {
        
        try { setTimeout(properties.get(StringCapabilities.TIMEOUT.getCapability())); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Hover on a link matching the text.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    protected void hoverOnWebElementByLinkText(String text) throws Exception {
        
        logger.info("Hover on a link matching the text: " + text);
            
        commandList.addToList("hoverOnWebElementByLinkText: " + text);
        
        try {
            
            Actions builder = new Actions(driver);
            
            builder.moveToElement(find(By.linkText(text))).build().perform();
            
        }
        catch(Exception e) { printDOM(); throw new Exception(e); }
        
    }
    
    /**
     * Scroll to an element that may be hidden in a select list or something else
     * 
     * @param text
     * 
     * @throws Exception 
     */
    protected void scrollToElementContainsText(String text) throws Exception {
        
        logger.info("Scroll to element contains text: " + text);
            
        commandList.addToList("scrollToElementContainsText: " + text);
      
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement element = find(By.xpath("//*[contains(text(),'" + text + "')]"));
        
        logger.info("found element to scroll to: " + element);

        js.executeScript("arguments[0].scrollIntoView(true);", element);
        
    }
    
    /**
     * Scroll to an element that may be hidden in a select list or something else
     * 
     * @param xpath
     * @param text
     * 
     * @throws Exception 
     */
    protected void selectOptionContainingText_XPATH(String xpath, String text) throws Exception {
        
        logger.info("Select option containing text (XPATH): " + xpath + "|" + text);
            
        commandList.addToList("selectOptionContainingText_XPATH: " + xpath + "|" + text);
            
        WebElement mySelectElement = find(By.xpath("//*[contains(text(),'" + text + "')]"));
 
        Select dropdown = new Select(mySelectElement);
 
        dropdown.selectByValue(text);

    }
    
    /**
     * Scroll to an element that may be hidden in a select list or something else
     * 
     * @param resourceId
     * @param text
     * 
     * @throws Exception 
     */
    protected void selectOptionContainingText_ResourceId(String resourceId, String text) throws Exception {
        
        logger.info("Select option containing text: " + resourceId + "|" + text);
            
        commandList.addToList("selectOptionContainingText_ResourceId: " + resourceId + "|" + text);
            
        WebElement mySelectElement = getWebElementAtResourceId(resourceId);
 
        Select dropdown= new Select(mySelectElement);
 
        dropdown.selectByValue(text);

    }
    
    public void clickAtCoordinates(int x, int y) throws Exception {
     
        new Actions(driver).moveByOffset(x, y).click().build().perform();
        
    }

    /**
     * Get a web element matching the css selector and and starts with the text.
     *
     * @param text
     * @param cssSelector
     * 
     * @return
     *
     * @throws Exception
     */
    public WebElement getWebElementWithCSSAndStartsWithText(String text, String cssSelector) throws Exception {

        logger.debug("Get the web element with css selector and starts with text: " + text + "|" + cssSelector);

        commandList.addToList("getWebElementWithCSSAndStartsWithText: " + text + "|" + cssSelector);
        
        try {

                List<WebElement> elements = getWebElementsWithCSS(cssSelector);

                for (WebElement element:elements) {

                    String data = element.getText().trim();

                    if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore

                    logger.debug("Checking value: " + data);

                    if(data.startsWith(text)) return element;

                }

                // if we get here, we could not find the element so throw an exception
                throw new Exception("Could not find text in any screen element matching type: " + cssSelector + " and text: " + text);

            }
            catch(Exception e) { printDOM(); throw new Exception(e); }

    }

    /**
     * Get a web element matching the locator and starts with text
     *
     * @param text
     * @param locator
     *
     * @throws Exception
     */
    public void clickOnWebElementWithXpathAndStartsWithText(String text, String locator) throws Exception {

        logger.debug("Get the web element with locator and starts with text: " + locator + "|" + text);

        commandList.addToList("clickOnOnWebElementWithXpathAndStartsWithText: " + locator + "|" + text);

        try {

                List<WebElement> elements = findElements(By.xpath(locator));

                for (WebElement element:elements) {

                    String data = element.getText().trim();

                    if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore

                    logger.debug("Checking value: " + data);

                    // if more matching is required, this can be enhanced using regular
                    //  expressions or longest match approach
                    if(data.startsWith(text)) {
                        
                        element.click();
                        
                        return;
                    
                    }
                
                }

                // if we get here, we could not find the element so throw an exception
                throw new Exception("Could not find text in any screen element matching locator: " + locator + " and starting with text: " + text);

            }
            catch(Exception e) { printDOM(); throw new Exception(e); }

    }
    
    /**
     * Use this method IF selenium is confused by the DOM when tricked into thinking
     * it will click on the wrong layered element
     * 
     * @param element
     * 
     * @throws Exception 
     */
    public void clickByCoordinates(WebElement element) throws Exception {
            
        Actions builder = new Actions(driver); 
          
        builder.moveToElement(element).click().build().perform();  
       
    }
    
    /**
     * Scroll to the web element
     * 
     * @param element
     * 
     * @throws Exception 
     */
    public void scrollToWebElement(WebElement element) throws Exception {
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

        Thread.sleep(500); 
    
    }
    
    /**
     * Opens an URL in the test frame. 
     * <p>
     * This accepts both relative and absolute URLs. 
     * <p>
     * The "open" command waits for the page to load before proceeding, ie. 
     * the "AndWait" suffix is implicit. 
     * <p>
     * Note: The URL must be on the same domain as the runner HTML due to
     * security restrictions in the browser (Same Origin Policy). If you need
     * to open an URL on another domain, use the Selenium Server to start a
     * new browser session on that domain. 
     * 
     * @param url The URL to open; may be relative or absolute.
     * 
     * @throws Exception
     */
    public void open(String url) throws Exception { 
        
        logger.debug("Open/Get a URL: " + url);
        
        info("Opening the url: " + url);
        
        commandList.addToList("open: " + url);
        
        PerformanceCapture.getInstance().start(getPageName());
        
        try {
        
            this.driver.navigate().to(url);
        
        }
        catch(Exception e) { 
            
            logger.error(e); 
            
            throw e;
        
        }
        finally { completeOpen(); }
        
    }
    
    /**
     * Perform any necessary final steps to the webdriver or basepage after open
     */
    private void completeOpen() {
          
                  
                try {
                        
                    WebDriverWait wait = new WebDriverWait(this.driver, Long.valueOf(this.timeout));
                         
                    Alert alert = wait.until(ExpectedConditions.alertIsPresent()); 
                        
                    alert.accept();
                    
                }
                catch(Exception le) { }
          
    }
    
}