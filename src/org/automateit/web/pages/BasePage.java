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

package org.automateit.web.pages;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.KeyEvent;

import java.io.File;

import org.testng.Assert;

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

import org.apache.log4j.Logger;

import org.automateit.util.CommandList;
import org.automateit.util.CommonProperties;
import org.automateit.util.CommonSelenium;
import org.automateit.util.PerformanceCapture;
import org.automateit.util.ScreenshotCapture;
import org.automateit.util.Utils;

import org.automateit.web.WebDriverFactory;

/**
 * BasePage is the class that all other page classes should extend for web-based UI testing.
 * 
 * It obfuscates the driver- and interface-specific programming from the automated
 * test developer and offers general functionality and convenience methods to make
 * programming easier and faster.
 * <p>
 * <b>
 * The current interface used is Selenium 3.0 which uses <code>WebDriver</code>.
 * </b>
 * @author mburnside
 */
public class BasePage {
    
    /**
     * JavaScript library that the application is using. This is required to query the active ajax connection
     */
    private String jsLibrary = null;
    
    /**
     * Perform screen shot. default is <code>false</code>.
     */
    private boolean doScreenshot = false;
    
    /**
     * Default timeout for page loading in seconds (s). Default is 25 seconds. 
     */
    protected String timeout = "25";
    
    /**
     * Add extra wait time for page to load after ajax calls are finished.
     */
    protected boolean addExtraWaitTimeAfterAjaxComplete = false;

    /**
     * Wait time for page to load after ajax calls are finished.
     */
    protected long extraWaitTimeAfterAjaxComplete = 2000; 
    
    /**
     * The web driver instance (if using selenium 2.0
     */
    protected WebDriver driver = null;

    /**
     * Properties that can be used for any class extending this class
     */
    protected CommonProperties properties = CommonProperties.getInstance();
	
    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    protected static Logger logger = Logger.getLogger(BasePage.class);
 
    /**
     * Maximize Browser Window. Default is <code>true</code>.
     */
    protected boolean maximizeBrowserWindow = true;
   
    /**
     * Use Robot to type in authorization prompt (use for IE workaround). Default is <code>false</code>.
     */
    protected boolean useIERobot = false;
    
    /**
     * The username value that the Robot types into the username field on the authorization prompt.
     */
    public String robotUsername = null;
    
    /**
     * The password value that the Robot types into the password field on the authorization prompt.
     */
    public String robotPassword = null;
    
    /**
     * The time (in seconds) to for webdriver to wait for elements to appear and 
     * rendered in the page. Default is 10 seconds.
     */
    protected String syncTimeout = "10";
    
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
    private boolean useForcePageLoadWaitTime = false;
    
    /**
     * Set the forced page load wait time.
     */
    private long forcePageLoadWaitTime = 10000;
    
    /**
     * Capture the selenium/webdriver commands. Default is <code>true</code>.
     */
    private boolean captureSeleniumCommands = true;
    
    /**
     * The utilities class to help with convenience methods
     */
    protected Utils utils = new Utils();
    
    /**
     * Initialize this class to prepare for testing. Loads the properties file 
     * located at <code>conf/seleniumconfiguration.properties</code>.
     * <p>
     * This method also calls "start" method.
     * 
     * This method uses the start page indicated by the baseURL property
     * 
     * @throws BasePageException
     */
    protected void init() throws BasePageException { 
        
        logger.info("Initializing a new BasePage -> selenium/webdriver and other settings using baseURL property (default initialization)");
        
        try {
            
            initializeCommonProperties();
        
            logger.info("Starting at baseURL: " + properties.getBaseURL());
            
            init(getModifiedStartURL());
            
            stopPerformanceCapturePageLoaded();
        
        }
        catch(Exception e) { throw new BasePageException(e); }

    }
    
    /**
     * Initialize this class to prepare for testing. Loads the properties file 
     * located at <code>conf/seleniumconfiguration.properties</code>.
     * <p>
     * This method also calls "start" method and uses a specified username and password.
     * 
     * @param username
     * @param password
     * 
     * This method uses the start page indicated by the baseURL property
     * 
     * @throws BasePageException
     */
    protected void init(String username, String password) throws BasePageException { 
        
        logger.info("Initializing a new BasePage -> selenium/webdriver and other settings using baseURL property (default initialization)");
        
        try {
            
            initializeCommonProperties();
        
            logger.info("Starting at baseURL: " + properties.getBaseURL());
            
            init(getModifiedStartURL(), username, password); 
            
            stopPerformanceCapturePageLoaded();
        
        }
        catch(Exception e) { throw new BasePageException(e); }

    }
    
    /**
     * Initialize this class to prepare for testing. Loads the properties file 
     * located at <code>conf/seleniumconfiguration.properties</code>.
     * <p>
     * This method also calls "start" method.
     * 
     * @param startURL The starting URL to open after the webdriver has been initialized and new session is created.
     * 
     * @throws BasePageException
     */
    protected void init(String startURL) throws BasePageException { 
      
        WebDriverFactory webDriverFactory = new WebDriverFactory();
       
        try {
          
            initializeCommonProperties();
            
            logger.info("Initializing a new BasePage -> WebDriver and other settings");
           
            logger.info("Properties loaded at: " + CommonProperties.getInstance().PROPERTIESFILEPATH + ", size: " + this.properties.size());
         
            logger.info("Creating Selenium 3.0 instance: " + properties.get("seleniumDriverId"));
                        
            this.driver = webDriverFactory.getWebDriver(properties.get("seleniumDriverId"), properties.getBaseURL());
                
            logger.info("Finished initializing Selenium/WebDriver 3.0: " + this.driver);
                
            CommonSelenium.getInstance().setWebDriver(driver);
            
            // set the timeout
            if(properties.get("timeout") != null) { this.timeout = properties.get("timeout"); }
            
            logger.info("Timeout is set to: " + this.timeout);
            
            // set the syncTimeout
            if(properties.get("syncTimeout") != null) { this.syncTimeout = properties.get("syncTimeout"); }
            
            logger.info("Sync Timeout is set to: " + this.syncTimeout);
            
            // extra time wait ajax complete if needed
            if((properties.get("addExtraWaitTimeAfterAjaxComplete") != null) && (properties.get("addExtraWaitTimeAfterAjaxComplete").equals("true"))) {
                
                addExtraWaitTimeAfterAjaxComplete = true;
                
                if(properties.get("extraWaitTimeAfterAjaxComplete") != null) {
                    
                    extraWaitTimeAfterAjaxComplete = (new Long(((String)properties.get("extraWaitTimeAfterAjaxComplete")).trim())).longValue();
                    logger.info("Add extra wait time after ajax is loaded: " + extraWaitTimeAfterAjaxComplete);
                }
                
            }
            
            logger.info("Maximize browser set to: " + this.maximizeBrowserWindow);
            
            // screenshot properties
            if(properties.getProperty("doScreenshots") != null) { this.doScreenshot  = (new Boolean(properties.getProperty("doScreenshots"))).booleanValue(); }
            
            logger.info("Do screenshots on each page load: " + this.doScreenshot);
            
            // set the captureSeleniumCommands
            if(properties.getProperty("captureSeleniumCommands") != null) { this.captureSeleniumCommands = (new Boolean(properties.getProperty("captureSeleniumCommands"))).booleanValue(); }
            
            logger.info("Capture selenium commands: " + this.captureSeleniumCommands);
            
            jsLibrary = properties.getProperty("jsLibrary");
            logger.info("jsLibrary: " + this.jsLibrary);
            
            // set using force page load wait time property
            if(properties.get("useForcePageLoadWaitTime") != null) { this.useForcePageLoadWaitTime = (new Boolean(properties.getProperty("useForcePageLoadWaitTime"))).booleanValue(); }
            
            logger.info("Using force page load wait time: " + this.useForcePageLoadWaitTime);
            
            // set force page load wait time (actual millisecond setting) property
            if(properties.get("forcePageLoadWaitTime") != null) { this.forcePageLoadWaitTime = (new Long(properties.getProperty("forcePageLoadWaitTime")).longValue()); }
            
            logger.info("Set force page load wait time (actual millisecond setting): " + this.forcePageLoadWaitTime);
            
            if(properties.get("useIERobot") != null) {
                
                this.useIERobot = (new Boolean(properties.getProperty("useIERobot"))).booleanValue();
                
                sleep(2000);
                
                if(properties.get("robot.username") != null) this.robotUsername = (String) properties.get("robot.username");
                
                if(properties.get("robot.password") != null) this.robotPassword = (String) properties.get("robot.password");
                
            }
           
            logger.info("Opening the start url: " + startURL);
            
            open(startURL);
            
            // set the maximizeBrowserWindow
            if(properties.getProperty("maximizeBrowserWindow") != null) { this.maximizeBrowserWindow = (new Boolean(properties.getProperty("maximizeBrowserWindow"))).booleanValue(); }
            
            if(this.maximizeBrowserWindow) maximizeWindow();
            
        }
        catch(Exception e) { 
        
            logger.error(e);
            
            throw new BasePageException(e); 
        
        }

    }
    
    /**
     * Initialize this class to prepare for testing. Loads the properties file 
     * located at <code>conf/seleniumconfiguration.properties</code>.
     * <p>
     * This method also calls "start" method and uses Smart Robot with given
     * username and password (ignore what is set in seleniumconfiguration.properties file.
     * 
     * @param startURL The starting URL to open after the selenium webdriver has been initialized and new session is created.
     * @param username
     * @param password
     * 
     * @throws BasePageException
     */
    protected void init(String startURL, String username, String password) throws BasePageException { 
      
        WebDriverFactory webDriverFactory = new WebDriverFactory();
       
        try {
          
            initializeCommonProperties();
            
            logger.info("Initializing a new BasePage -> WebDriver and other settings");
           
            logger.info("Properties loaded at: " + CommonProperties.getInstance().PROPERTIESFILEPATH + ", size: " + this.properties.size());
         
            logger.info("Creating Selenium 3.0 instance");
                        
            this.driver = webDriverFactory.getWebDriver(properties.get("seleniumDriverId"), properties.getBaseURL());
                
            logger.info("Finished initializing Selenium/WebDriver 3.0: " + this.driver);
                
            CommonSelenium.getInstance().setWebDriver(driver);
            
            // set the timeout
            if(properties.get("timeout") != null) { this.timeout = properties.get("timeout"); }
            
            logger.info("Timeout is set to: " + this.timeout);
            
            // set the syncTimeout
            if(properties.get("syncTimeout") != null) { this.syncTimeout = properties.get("syncTimeout"); }
            
            logger.info("Sync Timeout is set to: " + this.syncTimeout);
            
            // extra time wait ajax complete if needed
            if((properties.get("addExtraWaitTimeAfterAjaxComplete") != null) && (properties.get("addExtraWaitTimeAfterAjaxComplete").equals("true"))) {
                
                addExtraWaitTimeAfterAjaxComplete = true;
                
                if(properties.get("extraWaitTimeAfterAjaxComplete") != null) {
                    
                    extraWaitTimeAfterAjaxComplete = (new Long(((String)properties.get("extraWaitTimeAfterAjaxComplete")).trim())).longValue();
                    logger.info("Add extra wait time after ajax is loaded: " + extraWaitTimeAfterAjaxComplete);
                }
                
            }
            
            // set the maximizeBrowserWindow
            if(properties.getProperty("maximizeBrowserWindow") != null) { this.maximizeBrowserWindow = (new Boolean(properties.getProperty("maximizeBrowserWindow"))).booleanValue(); }
            
            if(this.maximizeBrowserWindow) maximizeWindow();
            
            logger.info("Maximize browser set to: " + this.maximizeBrowserWindow);
            
            // screenshot properties
            if(properties.getProperty("doScreenshots") != null) { this.doScreenshot  = (new Boolean(properties.getProperty("doScreenshots"))).booleanValue(); }
            
            logger.info("Do screenshots on each page load: " + this.doScreenshot);
            
            // set the captureSeleniumCommands
            if(properties.getProperty("captureSeleniumCommands") != null) { this.captureSeleniumCommands = (new Boolean(properties.getProperty("captureSeleniumCommands"))).booleanValue(); }
            
            logger.info("Capture selenium commands: " + this.captureSeleniumCommands);
            
            jsLibrary = properties.getProperty("jsLibrary");
            logger.info("jsLibrary: " + this.jsLibrary);
            
            // set using force page load wait time property
            if(properties.get("useForcePageLoadWaitTime") != null) { this.useForcePageLoadWaitTime = (new Boolean(properties.getProperty("useForcePageLoadWaitTime"))).booleanValue(); }
            
            logger.info("Using force page load wait time: " + this.useForcePageLoadWaitTime);
            
            // set force page load wait time (actual millisecond setting) property
            if(properties.get("forcePageLoadWaitTime") != null) { this.forcePageLoadWaitTime = (new Long(properties.getProperty("forcePageLoadWaitTime")).longValue()); }
            
            logger.info("Set force page load wait time (actual millisecond setting): " + this.forcePageLoadWaitTime);
            
            if(properties.get("useIERobot") != null) {
                
                this.useIERobot = (new Boolean(properties.getProperty("useIERobot"))).booleanValue();
                
                this.robotUsername = username;
                
                this.robotPassword = password;
                
            }
            
            logger.info("Opening the start url: " + startURL);
            
            open(startURL);
            
        }
        catch(Exception e) { 
        
            logger.error(e);
            
            throw new BasePageException(e); 
        
        }

    }
    
    /**
     * Initialize the CommonProperties object
     * @throws BasePageException 
     */
    protected void initializeCommonProperties() throws BasePageException {
        
        try { this.properties.load(this.properties.PROPERTIESFILEPATH); }
        catch(Exception e) { throw new BasePageException(e); }
        
    }
           
    /**
     * return the username set in property config
     */
    protected String getUsername() { return properties.getProperty("username"); }
    
    /**
     * return the password set in property config     
     */
    protected String getPassword() { return properties.getProperty("password"); }
    
    /**    
     * @return: true if using internet explorer browser
     */
    public boolean isBrowserIE() { return properties.isBrowserIE(); }
    
    /**    
     * @return: true if using safari
     */
    public boolean isBrowserSafari() { return properties.isBrowserSafari(); }
    
    /**
     * Check if Chrome browser is used or not
     *
     * @return: true if using chrome browser
     */
    public boolean isBrowserChrome() { return properties.isBrowserChrome(); }
    
    /**
     * Check if Firefox browser is used or not
     *
     * @return: true if using firefox browser
     */
    public boolean isBrowserFirefox() { return properties.isBrowserFirefox(); }
    
    /**
     * Check if Opera browser is used or not
     *
     * @return: true if using opera browser
     */
    public boolean isBrowserOpera() { return properties.isBrowserOpera(); }

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
     * Return the add extra wait time after ajax complete option.
     * 
     * @return 
     */
    protected boolean getAddExtraWaitTimeAfterAjaxComplete() { return this.addExtraWaitTimeAfterAjaxComplete; }
    
    /**
     * Return the extra wait time after ajax complete.
     * 
     * @return 
     */
    protected long getExtraWaitTimeAfterAjaxComplete() { return this.extraWaitTimeAfterAjaxComplete; }
    	
    /**
     * Get the javascript library
     * 
     * @return 
     */
    protected String getJSLibrary() { return this.jsLibrary; }
    
    /**
     * Return the webdriver interface.
     * 
     * @return The webdriver interface
     */
    public WebDriver getWebDriver() { return driver; }
    
    /**
     * Return the sync timeout.
     * 
     * @return 
     */
    public String getSyncTimeout() { return this.syncTimeout; }
   
    /**
     * Specifies the amount of time that webdrive waits for an element to appear
     * on the webpage by explicit wait method.
     * 
     * The default timeout is 10 seconds. 
     * 
     * @param syncTimeout The timeout in seconds, after which the action will return with an error 
     */
    protected void setSyncTimeout(String syncTimeout) { this.syncTimeout = syncTimeout; }
    
    /**
     * Return if we are forcing a sleep/delay and bypass ajax library to determine
     * page loading completion.
     * 
     * @return useForcePageLoadWaitTime
     */
    public boolean getUseForcePageLoadWaitTime() { return useForcePageLoadWaitTime; }
    
    /**
     * Return the forced sleep/delay which is used with 
     * <code>useForcePageLoadWaitTime</code> to bypass ajax library 
     * to determine page loading completion.
     * 
     * @return forcePageLoadWaitTime
     */
    public long getForcePageLoadWaitTime() { return forcePageLoadWaitTime; }
    
    /**
     * Get the <code>useIERobot</code> setting.
     * 
     * @return 
     */
    protected boolean getUseIERobot() { return this.useIERobot; }
    
    /**
     * Get the username value to type into the authorization prompt username field.
     * 
     * @return 
     */
    public String getRobotUsername() { return this.robotUsername; }
    
    /**
     * Get the password value to type into the authorization prompt password field.
     * 
     * @return 
     */
    public String getRobotPassword() { return this.robotPassword; }

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
     * @throws BasePageException
     */
    public void open(String url) throws BasePageException { 
        
        logger.info("Open/Get a URL: " + url);
        
        commandList.addToList("open: " + url);
        
        PerformanceCapture.getInstance().start(getPageName());
        
        try {
        
            if(useIERobot) openWithRobot(this.robotUsername, this.robotPassword, url);
            else { 
                
                this.driver.navigate().to(url); 
                
                // this is here because firefox always prompts for authenticated url in automation
                // so close it if it shows up
                if(isBrowserFirefox()) {
                    
                    try {
                        
                        WebDriverWait wait = new WebDriverWait(this.driver, (new Long(this.timeout)).longValue());
                       
                        Alert alert = wait.until(ExpectedConditions.alertIsPresent()); 
                    
                        alert.accept();
                        
                    }
                    catch(Exception le) { }
                    
                }
            
            }
        
        }
        catch(Exception e) { 
            
            logger.error(e); 
            
            throw new BasePageException(e);
        
        }
        
    }
    
    /**
     * Opens an URL in the test frame. 
     * <p>
     * This accepts both relative and absolute URLs. It uses a Smart Robot to
     * open a URL that is protected from loading by an authorization prompt.
     * <p>
     * The "open" command waits for the page to load before proceeding, ie. 
     * the "AndWait" suffix is implicit. 
     * <p>
     * Note: The URL must be on the same domain as the runner HTML due to
     * security restrictions in the browser (Same Origin Policy). If you need
     * to open an URL on another domain, use the Selenium Server to start a
     * new browser session on that domain. 
     * 
     * @param username
     * @param password
     * @param url The URL to open; may be relative or absolute.
     * 
     * @throws BasePageException
     */
    public void openWithRobot(String username, String password, String url) throws BasePageException { 
        
        logger.info("Opening a URL from authorization prompt: " + username + "|" + password + "|" + url);
        
        logger.info("Open/Get a URL: " + url);
        
        if(username == null) throw new BasePageException("Username value for Robot interaction is null");
        
        if(password == null) throw new BasePageException("Password value for Robot interaction is null");
        
        commandList.addToList("open: " + username + "|" + password + "|" + url);
        
        try {
            
            this.driver.get(url);
            
            sleep(5000);
            
            IERobot robot = new IERobot();

            robot.type(username);

            sleep(500);

            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            sleep(500);

            robot.type(password);

            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            sleep(500);

            PerformanceCapture.getInstance().start(getPageName());

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            sleep(500);
        }
        catch(Exception e) { 
            
            logger.error(e); 
            
            throw new BasePageException(e);
        
        }
        
    }
    
    /**
     * This method logs some info to the Command List for reporting purpose 
     * @param info
     */
    protected void logInfoToCommandList(String info) { commandList.addToList(info); }
        
    /**
     * Wait for a page to load for <code>s</code> number of milliseconds (ms).
     * 
     * @param s The number of milliseconds (ms) 
     */
    protected void waitForPageToLoad(String s) throws BasePageException { 
        
        logger.info("Waiting for page to load (ms): " + s);
        
        try { waitForPageToLoad(s, true); }
        catch(BasePageException e) { throw e; }
    
    }
    
    /**
     * Wait for a page to load for <code>s</code> number of milliseconds (ms).
     * 
     * @param s
     * @param checkAjaxComplete 
     * 
     * @throws BasePageException
     */
    protected void waitForPageToLoad(String s, boolean checkAjaxComplete) throws BasePageException { 
    
        logger.info("Waiting for page to load (ms) and check for any ajax calls to be completed: " + s + "|" + checkAjaxComplete);
        
        commandList.addToList("waitForPageToLoad: " + s); 
        
        if(this.useForcePageLoadWaitTime) {
            
            logger.info("Forcing a page load wait time of: " + this.forcePageLoadWaitTime);
            
            sleep(this.forcePageLoadWaitTime);
            
        }
        else {
            
            try {
            
                this.driver.manage().timeouts().implicitlyWait((new Long(this.timeout)).longValue(), TimeUnit.SECONDS);
                  
                logger.info("Page content has been loaded, now waiting for Ajax completion");
                   
                if(checkAjaxComplete) {
                    
                    logger.info("Waiting for Javascript and Ajax to complete loading on the page");
                    
                    if(!waitForJSandAjaxToLoad((new Long(this.timeout).longValue()))) logger.info("Error when checking if javascript and ajax has been loaded");
                
                }
              
                if(addExtraWaitTimeAfterAjaxComplete) {
                    
                    logger.info("Delay for Ajax completion for additional content loading from ajax call: " + extraWaitTimeAfterAjaxComplete);
                    
                    sleep(extraWaitTimeAfterAjaxComplete);
                    
                }
            
            }
            catch(Exception e) { throw new BasePageException(e); }
            
        }
    
    }
    
    /**
     * Assert a title equals exactly a string specified by <code>s</code>.
     * 
     * @param s The title
     * 
     * @throws BasePageException
     */
    protected void assertTitle(String s) throws BasePageException { 
        
        logger.info("Asserting title is correct: " + s);
        
        commandList.addToList("assertTitle: " + s + "|" + getTitle());
        
        try { Assert.assertEquals(s.trim(),getTitle().trim()); }
        catch(Exception e) { throw new BasePageException(e); }
        
    }

    /**
     * Return the page title.
     * 
     * @return The page title
     * 
     * @throws BasePageException
     */
    protected String getTitle() throws BasePageException { 
        
        try { return this.driver.getTitle(); }
        catch(Exception e) { throw new BasePageException(e); }
            
    }

    /**
     * Assert that text specified in <code>s</code> exists in the document.
     * 
     * @param s The text that should be present
     * 
     * @throws Exception 
     */
    public void assertText(String s) throws Exception {
        
        logger.info("Asserting expected text is present: " + s);
        
        commandList.addToList("assertText| " + s);
        
        try { Assert.assertTrue(this.driver.getPageSource().contains(s), "Expect text '" + s +"' in page but not found."); }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
    
    /**
     * Check to see if text specified in <code>s</code> exists in the document.
     * 
     * @param s The text that should be present
     * 
     * @return 
     */
    public boolean containsText(String s) {
        
        logger.info("Checking that expected text is present: " + s);
        
        commandList.addToList("isTextPresent: " + s);
        
        try { return this.driver.getPageSource().contains(s); }
        catch(Exception e) { return false; }
        
    }
    
    /**
     * This method will check if text is present on the page for a certain
     * period of time.
     * 
     * It works like this: check for the existence of text <code>s</code> every
     * 1 second until <code>syncTimeout</code> is passed and if the text is
     * not found after that time, then throw a <code>BasePageException</code> 
     * indicating that the text was not found in the DOM or rendered html.
     * 
     * If the text is found within this time, the checking will stop and 
     * successfully return without exception thrown.
     * 
     * @param s
     * 
     * @throws BasePageException 
     */
    public void assertTextWithTimeout(String s) throws BasePageException {
        
        try {
            
            int intSyncTimeout = (new Integer(this.syncTimeout)).intValue();
            
            for(int i = 0; i < intSyncTimeout; i++) {
                
                if(this.driver.getPageSource().contains(s)) return;
                
                sleep(1000);
                
            }
            
            // if we get here, then the time for checking is over and text was not found
            // so throw the exception
            throw new BasePageException("After " + this.syncTimeout + " seconds, the text: " + s + " was not found in the HTML DOM or rendered anywhere on the page");
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Simulates the user clicking the "close" button browser
     */
    public void close() { 
        
        logger.info("Closing the browser");
        
        commandList.addToList("close");
        
        try { if(this.driver != null) this.driver.quit(); }
        catch(Exception e) { }
        
    }

    /**
     * Inherits the session from the previous page class.
     * 
     * @param basePage 
     */
    protected void inheritSession(BasePage basePage) { 
        
        logger.info("Inheriting session from previous page: " + basePage);
       
        this.driver = basePage.getWebDriver();
       
        timeout = basePage.getTimeout();
        
        syncTimeout = basePage.getSyncTimeout();
        
        addExtraWaitTimeAfterAjaxComplete = basePage.getAddExtraWaitTimeAfterAjaxComplete();
        
        extraWaitTimeAfterAjaxComplete = basePage.getExtraWaitTimeAfterAjaxComplete();
        
        jsLibrary = basePage.getJSLibrary();
        
        useForcePageLoadWaitTime = basePage.getUseForcePageLoadWaitTime();
        
        forcePageLoadWaitTime = basePage.getForcePageLoadWaitTime();
      
        useIERobot = basePage.getUseIERobot();
        
        robotUsername = basePage.getRobotUsername();
        
        robotPassword = basePage.getRobotPassword();
        
        stopPerformanceCapturePageLoaded();
       
        try { if(doScreenshot) doScreenshot(); }
        catch(Exception e) { logger.error(e); }
                
    }

    /**
     * Return the html source.
     * 
     * @return The html source
     * 
     * @throws BasePageException
     */
    protected String getHtmlSource() throws BasePageException { 
        
        try { return this.driver.getPageSource(); }
        catch(Exception e) { throw new BasePageException(e); }
        
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
     * @throws BasePageException
     */
    public void type(String locator, String value) throws BasePageException { 
        
        logger.info("Entering data: " + value + " into element at locator: " + locator);
        
        commandList.addToList("type: " + locator + "|" + value);
        
        try { 
            
            if(locator == null) throw new BasePageException("Unable to enter data because xpath locator vlaue is null");
            if(value != null) this.driver.findElement(By.xpath(locator)).sendKeys(value); 
        
        }
        catch(Exception e) { throw new BasePageException(e); }
    
    }

    /**
     * Clicks on a link, button, checkbox or radio button using mouse event
     *
     * @param locator
     *
     * @throws BasePageException
     */
    public void mouseEventClick(String locator) throws BasePageException {
        
        commandList.addToList("mouseEventClick: " + locator);

        try {
            
            WebElement element = driver.findElement(By.xpath(locator));

            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("var evt = document.createEvent('MouseEvents');" + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);" + "arguments[0].dispatchEvent(evt);", element);
        
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
    
    }

    /**
     * Type text <code>value</code> into an element indicated by <code>locator</code>.
     * 
     * @param locator
     * @param value 
     * * @param checkAjax check after waiting for ajax to complete
     * 
     * @throws BasePageException
     */
    public void type(String locator, String value, boolean checkAjax) throws BasePageException { 
        
        logger.info("Entering data: " + value + " into element at locator: " + locator);
        
        commandList.addToList("type: " + locator + "|" + value+ "|ajaxCheck:" + checkAjax);
        
        try {
        	
            PerformanceCapture.getInstance().start(getPageName());
          
            waitForConditionElementXPathPresent(locator,syncTimeout);
                
            this.driver.findElement(By.xpath(locator)).sendKeys(value);
            
            waitForPageToLoad(timeout, checkAjax);
            
        }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
    
    /**
     * Type text <code>value</code> into a file chooser.
     * 
     * @param locator
     * @param value
     * 
     * @throws BasePageException
     */
    public void enterTextIntoFileChooser(String locator, String value) throws BasePageException { 
        
        logger.info("Entering data into the active element: " + value);
        
        commandList.addToList("enterTextIntoFileChooser: " + value);
        
        try {
        	
            PerformanceCapture.getInstance().start(getPageName());
            
            WebElement element = getWebElementWithLocator(locator);
        	  
            element.sendKeys(value);
             
            sleep(2000);
             
        }
        catch(Exception e) { throw new BasePageException(e); }
    
    }

    /**
     * Clicks on a link, button, checkbox or radio button. 
     * <p>
     * If the click action causes a new page to load (like a link usually does), call waitForPageToLoad. 
     * 
     * @param locator An element locator
     * @param checkAjax check after waiting for ajax to complete
     * 
     * @throws BasePageException
     */
    public void click(String locator, boolean checkAjax) throws BasePageException { 
        
        logger.info("Clicking on element with locator: " + locator + " turning off ajax completion checking");
    
        commandList.addToList("click: " + locator + "|ajaxCheck:" + checkAjax);
        
        try {
        
            PerformanceCapture.getInstance().start(getPageName());
          
            waitForConditionElementXPathPresent(locator,syncTimeout);
             
            this.driver.findElement(By.xpath(locator)).click();
           
            waitForPageToLoad(timeout, checkAjax);
            
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
    
    }
    
    /**
     * Clicks on a link, button, checkbox or radio button. 
     * <p>
     * If the click action causes a new page to load (like a link usually does), call waitForPageToLoad. 
     * 
     * @param locator An element locator
     * 
     * @throws BasePageException
     */
    public void click(String locator) throws BasePageException { 
        
        logger.info("Clicking on element with locator: " + locator);
    
        commandList.addToList("click: " + locator);
        
        try { click(locator, true); }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
    
    }
    
    /**
     * Ends the test session, killing the browser.
     * 
     * @throws BasePageException
     */
    public void stop() throws BasePageException { 
        
        logger.info("Stopping the selenium session");
    
        commandList.addToList("stop");
        
        try { this.driver.quit(); }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
     
    /**
     * Use this method to wait for ajax completion with web driver because implicit wait does not
     * work consistently using webdriver selenium wrapper class.
     * 
     * @param timeout 
     */
    public void waitForAjaxCompletion(long timeout) throws BasePageException {
        
        logger.info("Wait for Ajax and javascript execution completion with timeout: " + timeout);
        
        try { waitForJSandAjaxToLoad(timeout); }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
    
    /**
     * Wait for the condition of element id present in the rendered html. This is an
     * alternative method to use for waiting for page elements to load when 
     * ajax call to find 0 connections does leave enough wait time.
     * 
     * @param elementId
     * @param timeout
     * 
     * @throws BasePageException 
     */
    public void waitForConditionElementIDPresent(String elementId, String timeout) throws BasePageException {
        
        logger.info("Wait For Condition - element Id present: " + timeout + "|" + elementId);
        
        commandList.addToList("waitForConditionElementIDPresent: " + timeout + "|" + elementId);
        
        try {
            
            if(timeout == null) timeout = this.timeout; // protect aginst null conditions
            
            waitForAjaxCompletion((new Long(timeout)).longValue());
            
            WebElement myDynamicElement = (new WebDriverWait(driver, (new Long(timeout)).longValue())).until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));
            
        }
        catch(Exception e) { throw new BasePageException(e); }
         
    }
    
    /**
     * Wait for the condition of xpath element locator present in the rendered html. This is an
     * alternative method to use for waiting for page elements to load when 
     * ajax call to find 0 connections does leave enough wait time.
     * 
     * @param xpath
     * @param timeout timeout in seconds
     * 
     * @throws BasePageException
     */
    public void waitForConditionElementXPathPresent(String xpath, String timeout) throws BasePageException {
        
        logger.info("Wait For Condition - xpath present: " + timeout + "|" + xpath);
        
        commandList.addToList("waitForConditionElementXPathPresent: " + timeout + "|" + xpath);
        
        try {
            
            waitForAjaxCompletion((new Long(timeout)).longValue());
            
            WebElement myDynamicElement = (new WebDriverWait(driver, (new Long(timeout)).longValue())).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            
        }
        catch(Exception e) { throw new BasePageException(e); }
         
    }
    
    /**
     * Wait for the condition of classname element locator present in the rendered html. This is an
     * alternative method to use for waiting for page elements to load when 
     * ajax call to find 0 connections does leave enough wait time.
     * 
     * @param classname
     * @param timeout timeout in seconds
     * 
     * @throws BasePageException
     */
    public void waitForConditionElementClassnamePresent(String classname, String timeout) throws BasePageException {
        
        logger.info("Wait For Condition - xpath present: " + timeout + "|" + classname);
        
        commandList.addToList("waitForConditionElementXPathPresent: " + timeout + "|" + classname);
        
        try {
            
            waitForAjaxCompletion((new Long(timeout)).longValue());
            
            WebElement myDynamicElement = (new WebDriverWait(driver, (new Long(timeout)).longValue())).until(ExpectedConditions.presenceOfElementLocated(By.className(classname)));
            
        }
        catch(Exception e) { throw new BasePageException(e); }
         
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
     * @throws BasePageException
     */
    public boolean isTextPresent(String s) throws BasePageException { 
        
        logger.info("Asserting that text is present in the rendered page: " + s);
    
        commandList.addToList("isTextPresent: " + s); 
        
        try { return getHtmlSource().contains(s); }
        catch(Exception e) { throw new BasePageException(e); }
    
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
    public String getText(String locator) throws BasePageException { 
        
        logger.info("Get text from locator: " + locator);
    
        commandList.addToList("getText: " + locator);
        
        try { return this.driver.findElement(By.xpath(locator)).getText(); }
        catch(Exception e) { throw new BasePageException(e); }
        
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
    public String getValue(String locator) throws BasePageException { 
        
        logger.info("Get value from locator: " + locator);
    
        commandList.addToList("getValue: " + locator);
        
        try { return this.driver.findElement(By.xpath(locator)).getAttribute("value"); }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
    
    /**
     * Simulates the user clicking the "Refresh" button on their browser. 
     * 
     * @throws BasePageException
     */
    public void refresh() throws BasePageException { 
        
        logger.info("Simulate Refresh browser button click");
    
        commandList.addToList("refresh");
        
        try { this.driver.navigate().refresh(); }
        catch(Exception e) { throw new BasePageException(e); }
        
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
    protected boolean isChecked(String locator) throws BasePageException { 
        
        logger.info("Is the element checked: " + locator);
    
        commandList.addToList("isChecked: " + locator);
        
        try { return this.driver.findElement(By.xpath(locator)).isSelected(); }
        catch(Exception e) { throw new BasePageException(e); }
        
    }
    
    /**
     * Gets the entire text of the page. 
     * 
     * @return The entire text of the page
     * 
     * @throws BasePageException
     */
    protected String getBodyText() throws BasePageException { 
    
        commandList.addToList("getBodyText");
        
        try { return this.driver.getPageSource(); }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
  
    /**
     * Check a toggle-button (checkbox/radio).
     * 
     * @param locator An element locator
     * 
     * @throws BasePageException 
     */
    protected void check(String locator) throws BasePageException { 
        
        logger.info("Checking the element at locator: " + locator);
        
        commandList.addToList("check: " + locator);
        
        try { if(!this.driver.findElement(By.xpath(locator)).isSelected()) this.driver.findElement(By.xpath(locator)).click(); }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
 
    /**
     * Take a screenshot.
     * 
     * @throws BasePageException 
     */
    public void doScreenshot() throws BasePageException {
        
        logger.info("Attempting to do a screenshot of the desktop");
    
        ScreenshotCapture screenshotCapture = new ScreenshotCapture();
       
        try {
            
           screenshotCapture.doScreenshot("screenshots" + File.separator
                                   + "image" + System.currentTimeMillis(), ".png");
        
        }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
    
    /**
     * Maximize the browser window.
     * 
     * @throws BasePageException 
     */
    public void maximizeWindow() throws BasePageException { 
        
        logger.info("Maximizing the browser window");
        
        commandList.addToList("maximizeWindow");
        
        try { driver.manage().window().maximize(); }
        catch(Exception e) { throw new BasePageException(e); }
   
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
        
        try { this.driver.findElement(By.xpath(locator)); return true; }
        catch(Exception e) { return false; }
        
    
    }
    
    /**
     * Asserts that the specified element is somewhere on the page. 
     * 
     * @param message - message to describe assertion in case of fail
     * @param locator
     */
    protected void assertElementPresent(String message, String locator) throws BasePageException { 
        
        logger.info("Assert the element is present/visible: " + locator + "|" + message);
        
        commandList.addToList("assertElementPresent: " + locator + "|" + message);
        
        try { Assert.assertTrue(isElementPresent(locator), message); }
        catch(Exception e) { throw new BasePageException(e); }
    
    }
    
    /**
     * Asserts that the specified element is not on the page. 
     * 
     * @param message - message to describe assertion in case of fail
     * @param locator
     */
    protected void assertElementNotPresent(String message, String locator) throws BasePageException {
        
        logger.info("Assert the element is not present/visible: " + locator + "|" + message);
    
        commandList.addToList("assertElementNotPresent: " + locator + "|" + message);
        
        try { Assert.assertFalse(isElementPresent(locator), message); }
        catch(Exception e) { throw new BasePageException(e); }
    
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
        
        logger.info("Stopping the performance capture");
        
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
        
        logger.info("Stopping perfromance capture after page is loaded");
            	    
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
     * @throws BasePageException 
     */
    protected void clickOnWebElementContainingText(String text, String className) throws BasePageException {
        
        logger.info("Clicking on web element containing text: " + text + "|" + className);
            
        commandList.addToList("clickOnWebElementContainingText: " + text + "|" + className);
        
        try {
          
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
            	String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) {
                    
                    element.click();
                    
                    waitForPageToLoad(timeout);
                        
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Click on a web element matching the type and containing the text.
     * 
     * @param text1
     * @param text2
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void clickOnWebElementContainingText(String text1, String text2, String className) throws BasePageException {
       
        logger.info("Clicking on web element containing text: " + text1 + "|" + text2 + "|" + className);
        
        commandList.addToList("clickOnWebElementContainingText: " + text1 + "|" + text2 + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text1) && data.contains(text2)) {
                   
                    element.click();
                    
                    waitForPageToLoad(timeout);
                        
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception    
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text1: " + text1 + " and text2: " + text2);
            
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Click on a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void clickOnWebElementMatchingText(String text, String className) throws BasePageException {
        
        logger.info("Clicking on web element matching text: " + text + "|" + className);
        
        commandList.addToList("clickOnWebElementMatchingText: " + text + "|" + className);
        
        try {
        
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                if(data.equals(text.trim())) {
                   
                    element.click();
                    
                    waitForPageToLoad(timeout);
                        
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    public void validateWebElementContainingText(String text, String className) throws BasePageException {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and matching the text.
     * 
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void validateWebElementMatchingText(String text, String className) throws BasePageException {
        
        logger.info("Validating that element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingText:" + text + "," + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void validateWebElementContainingTextValueAttribute(String text, String className) throws BasePageException {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void validateWebElementMatchingTextValueAttribute(String text, String className) throws BasePageException {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     *
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void clickOnWebElementContainingTextValueAttribute(String text, String className) throws BasePageException {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
        
        commandList.addToList("clickOnWebElementContainingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
          
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) {
                   
                    element.click();
                    
                    waitForPageToLoad(timeout);
                  
                    return;
                    
                };
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void clickOnWebElementMatchingTextValueAttribute(String text, String className) throws BasePageException {
        
        logger.info("Validating that element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
        
        commandList.addToList("clickOnWebElementMatchingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(text)) {
                    
                    element.click();
                    
                    waitForPageToLoad(timeout);
                    
                    return;
                    
                };
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param name
     * 
     * @throws BasePageException 
     */
    protected void validateWebElementContainingTextByName(String name) throws BasePageException {
        
        logger.info("Validating that element of name: " + name + " exists");
            
        commandList.addToList("validateWebElementContainingTextByName:" + name);
        
        try {
            
            WebElement element = this.driver.findElement(By.name(name));
            
            // if we get here, we could not find the element so throw an exception
            if(element == null) throw new Exception("Could not validate a screen component with screen element matching name: " + name);
                   
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a not web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void validateAllWebElementsDoNotContainText(String text, String className) throws BasePageException {
        
        logger.info("Validate all web elements do not contain text:" + text + "|" + className);
        
        commandList.addToList("validateAllWebElementsDoNotContainText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) throw new Exception("Found a web element / screen component with text and matching type: " + className + " and text: " + text + ", it was not expected to be visible");
                
            }
            
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Validate that there is a not web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws BasePageException 
     */
    protected void validateAllWebElementsDoNotContainTextByValueAttribute(String text, String className) throws BasePageException {
        
        logger.info("Validate all web elements do not contain text by value attribute:" + text + "|" + className);
        
        commandList.addToList("validateAllWebElementsDoNotContainTextByValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) throw new Exception("Found a web element / screen component with text and matching type: " + className + " and text: " + text + ", it was not expected to be visible");
                
            }
            
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Get list of text values from web elements matching xpath value
     * 
     * @throws BasePageException 
     */
    protected List<String> getListOfTextValuesFromAllMatchingXPath(String xpath) throws BasePageException {
        
        logger.info("Get list of text values from all matching XPath:" + xpath);
        
        commandList.addToList("getListOfTextValuesFromAllMatchingXPath:" + xpath);
        
        try {
          
            List<WebElement> elements = this.driver.findElements(By.xpath(xpath));
            
            List<String> values = new ArrayList<String>();
           
            for(WebElement element:elements) values.add(element.getText().trim());
           
            return values;
            
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
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
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
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
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Return the index for a certain web element type.
     * 
     * @param text
     * @param className
     * 
     * @return
     * 
     * @throws BasePageException 
     */
    protected int getElementTypeIndexMatchingText(String text, String className) throws BasePageException {
        
        logger.info("Get element type index matching text:" + text + "|" + className);
        
        commandList.addToList("getElementTypeIndexMatchingText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            int index = 1;
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(text)) return index;
                
                index++;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find the index of a screen component with text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { throw new BasePageException(e); }
        
    }
    
    /**
     * Return the index for a certain web element type.
     * 
     * @param text
     * @param className
     * 
     * @return 
     * 
     * @throws BasePageException 
     */
    protected int getElementTypeIndexContainingText(String text, String className) throws BasePageException {
        
        logger.info("Get element type index containing text:" + text + "|" + className);
        
        commandList.addToList("getElementTypeIndexContainingText:" + text + "|" + className);
        
        try {
           
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            int index = 1;
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking if name attribute of type: " + className + " contains: " + text + "|" + data);
                   
                if(data.contains(text)) return index;
                
                index++;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find the index of a screen component with text in any screen element containing type/classname: " + className + " and text: " + text);
            
        }
        catch(Exception e) { throw new BasePageException(e); }
        
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
     * @throws BasePageException 
     */
    public String getWebElementIdForElementNameAttributeContainsText(String className, String text) throws BasePageException {
        
        logger.info("Get web element Id for element name attribute contains text:" + text + "|" + className);
        
        commandList.addToList("getWebElementIdForElementNameAttributeContainsText:" + text + "|" + className);
       
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                   
                String value = element.getAttribute("name").trim();
                 
                if((value == null) || (value.trim().length() == 0)) continue; // null or blank, so ignore
                
                logger.info("Checking for " + value + "|" + text);
               
                if(value.trim().equals(text)) return ((RemoteWebElement) element).getId();
                
            }
          
            throw new Exception("Unable to find cell displayed value for text: " + text);
          
        }
        catch(Exception e) { throw new BasePageException(e); }
        
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
     * @throws BasePageException 
     */
    public void clickOnWebElementWithText(String value) throws BasePageException {
       
        logger.info("Clicking on any web element with text value: " + value);
        
        commandList.addToList("clickOnWebElementWithText:" + value);
        
        try { 
            
            this.driver.findElement(By.linkText(value)).click();
            
            waitForPageToLoad(timeout);
        
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement.
     * 
     * @param element
     * 
     * @throws BasePageException 
     */
    public void clearWebElement(WebElement element) throws BasePageException { 
        
        logger.info("Clear web element: " + element);
        
        commandList.addToList("clearWebElement:" + element);
    
        try { element.clear(); }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement looked up using xpath.
     * 
     * @param locator
     * 
     * @throws BasePageException 
     */
    public void clearWebElement(String locator) throws BasePageException { 
        
        logger.info("Clear web element: " + locator);
        
        commandList.addToList("clearWebElement:" + locator);
    
        try { clearWebElement(getWebElementWithLocator(locator)); }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
    }
    
    /**
     * Get the element matching id attribute.
     * 
     * @param id
     * 
     * @return
     * 
     * @throws BasePageException 
     */
    public WebElement getWebElementWithId(String id) throws BasePageException {
       
        logger.info("Preparing to find web element by id: " + id);
        
        commandList.addToList("getWebElementWithId:" + id);
        
        try { return this.driver.findElement(By.id(id)); }
        catch(Exception e) { throw new BasePageException(e); }
        
    }
    
    /**
     * Get the element matching the xpath locator.
     * 
     * @param locator
     * 
     * @return
     * 
     * @throws BasePageException 
     */
    public WebElement getWebElementWithLocator(String locator) throws BasePageException {
       
        logger.info("Preparing to find web element by locator: " + locator);
        
        commandList.addToList("getWebElementWithId:" + locator);
        
        try { return this.driver.findElement(By.xpath(locator)); }
        catch(Exception e) { throw new BasePageException(e); }
        
    }
    
    /**
     * Enter data into rich text editor.
     * 
     * @param xpath
     * @param text
     * 
     * @throws BasePageException 
     */
    public void enterTextIntoRichTextEditor(String xpath, String text) throws BasePageException {
       
        logger.info("Enter data into rich text editor: " + xpath + "|" + text);
        
        commandList.addToList("enterTextIntoRichTextEditor:" + xpath + "|" + text);
        
        try { 
            
            String CurrentWindowHandle = driver.getWindowHandle();
       
            driver.findElement(By.tagName("body")).clear();
            driver.findElement(By.tagName("body")).sendKeys(text);
            driver.switchTo().window(CurrentWindowHandle); 
        
        }
        catch(Exception e) { printDOM(); throw new BasePageException(e); }
        
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
     * @throws BasePageException 
     */
    public String getWebElementAttribute(String xpath, String attributeLocator) throws BasePageException {
       
        logger.info("Get web element attribute:" + xpath + "|" + attributeLocator);
        
        commandList.addToList("getWebElementAttribute:" + xpath + "|" + attributeLocator);
       
        try {
            
            WebElement element = this.driver.findElement(By.xpath(xpath));
           
            return element.getAttribute(attributeLocator);

        }
        catch(Exception e) { throw new BasePageException(e); }
        
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
     * @throws BasePageException 
     */
    public List<String> getWebElementsAttribute(String xpath, String attributeLocator) throws BasePageException {
       
        logger.info("Get web elements attribute:" + xpath + "|" + attributeLocator);
        
        commandList.addToList("getWebElementsAttribute:" + xpath + "|" + attributeLocator);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.xpath(xpath));
            
            List<String> values = new ArrayList<String>();
            
            for (WebElement element:elements) values.add(element.getAttribute(attributeLocator));
           
            return values;

        }
        catch(Exception e) { throw new BasePageException(e); }
        
    }
    
    /**
     * Switch to work area.
     * 
     * @param xpath
     * 
     * @throws BasePageException 
     */
    public void switchToWorkarea(String xpath) throws BasePageException {
        
        logger.info("Switch to work area located at: " + xpath);
        
        commandList.addToList("switchToWorkarea:" + xpath);
        
        try {
            
            driver.switchTo().defaultContent();
        
            WebElement frame = getWebDriver().findElement(By.xpath(xpath));
       
            driver.switchTo().frame(frame);
        
        }
        catch(Exception e) { throw new BasePageException(e); }
   
    }
    
    /**
     * Switch back to current window
     *
     * @throws BasePageException
     */
    public void switchBackToCurrentWindow() throws BasePageException {
        
        logger.info("Switch back to current window");
        
        commandList.addToList("switchBackToCurrentWindow");
       
        try { this.driver.switchTo().defaultContent(); }
        catch(Exception e) { throw new BasePageException(e); }
  
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
     * @throws BasePageException 
     */
    public void elementIsDisplayedWithTimeout(String locator) throws BasePageException {
        
        logger.info("Element is displayed with timeout:" + locator);
        
        commandList.addToList("elementIsDisplayedWithTimeout:" + locator);
        
        try {
            
            int intSyncTimeout = (new Integer(this.syncTimeout)).intValue();
            
            for(int i = 0; i < intSyncTimeout; i++) {
                
                if(this.driver.findElement(By.xpath(locator)).isDisplayed()) return;

                sleep(1000);
            }
            
        }
        catch(Exception e) { throw new BasePageException(e); }
     
    }
    
    /**
     * Simulate an Enter key press on the active element
     * 
     * @throws BasePageException 
     */
    public void clickEnterKeyOnActiveElement() throws BasePageException {
        
        logger.info("Click enter key on active element");
        
        commandList.addToList("clickEnterKeyOnActiveElement");
        
        try { this.driver.switchTo().activeElement().sendKeys(Keys.ENTER); }
        catch(Exception e) { throw new BasePageException(e); }
        
     }
    
    /**
     * Select multiple elements from a text area field. 
      * 
      * It simulates the Ctrl + Click operation.
      * 
     * @param locator
     * 
     * @throws BasePageException 
     */
    public void selectMultipleElements(String locator) throws BasePageException {
        
        logger.info("Select multiple elements:" + locator);
        
        commandList.addToList("selectMultipleElements: " + locator);
            
        try {
                 
            Actions actions = new Actions(driver);
                  
            actions.keyDown(Keys.CONTROL).click(driver.findElement(By.xpath(locator))).keyUp(Keys.CONTROL).build().perform();
                 
        }
        catch(Exception e) { throw new BasePageException(e); }
              
    }
    
    /**
     * Get the selected option for a Select element.
     * 
     * @param xpathLocator
     * 
     * @return
     * 
     * @throws BasePageException 
     */
    public String getSelectedOptionValue(String xpathLocator) throws BasePageException {
        
        logger.info("Get selected option value:" + xpathLocator);
        
        commandList.addToList("getSelectedOptionValue: " + xpathLocator);
        
        try { return new Select(driver.findElement(By.xpath(xpathLocator))).getFirstSelectedOption().getText(); }
        catch(Exception e) { throw new BasePageException(e); }
        
    }
    
    /**
     * Get the value for an attribute of an object.
     * 
     * @param object
     * @param attributeLocator
     * 
     * @return
     * 
     * @throws BasePageException 
     */
      
    public String getObjectAttribute(String object, String attributeLocator) throws BasePageException {
          
        logger.info("Get object attribute:" + object + "|" + attributeLocator);
          
        commandList.addToList("getObjectAttribute: " + object + "|" + attributeLocator);
           
        try {
        
            WebElement objectName = driver.findElement(By.xpath(object));
         
            return objectName.getAttribute(attributeLocator);
        
        }
        catch(Exception e) { throw new BasePageException(e); }

    }

    /**
     * Perform scroll to the given web element
     *
     * @param xpath
     *
     * @throws BasePageException
     */
    public void scrollingToElementofAPage(String xpath) throws BasePageException {
        
        commandList.addToList("scrollingToElementOfAPage: " + xpath);

        try {
            
            WebElement element = driver.findElement(By.xpath(xpath));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            
        }
        catch(Exception e) { throw new BasePageException(e); }
    }

    /**
     * Perform drag and drop operation with given web element
     *
     * @param xpath
     * @param x_axis
     * @param y_axis
     *
     * @throws BasePageException
     */
    public void dragAndDropBy(String xpath, int x_axis, int y_axis) throws BasePageException {

        commandList.addToList("dragAndDropBy " + xpath + " x-axis|" + x_axis + " y-axis|" + y_axis);

        try {

            WebElement drag = driver.findElement(By.xpath(xpath));

            Actions action = new Actions(driver);

            action.dragAndDropBy(drag, x_axis, y_axis).build().perform();

        }
        catch(Exception e) { throw new BasePageException(e); }
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
            
            WebElement element = driver.findElement(By.xpath(xpath));
            
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
        
        logger.info("Wait for javascript and ajax to load:" + timeout);
          
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
     * @throws BasePageException 
     */
    protected String getModifiedStartURL() throws BasePageException { 
        
        try { return utils.getModifiedURL(properties.getBaseURL(), properties.get(Utils.TESTING_TARGET_VARIABLE_NAME)); }
        catch(Exception e) { throw new BasePageException(e); }

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
        catch(Exception e) { throw new BasePageException(e); }
        
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
     * Scroll down
     * 
     * @throws BasePageException 
     */
    public void scrollDown() throws BasePageException {
        
        logger.info("scroll down");
        
        commandList.addToList("scroll down");
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollBy(0,250)", "");
                 
        }
        catch(Exception e) { throw new BasePageException(e); }
              
    }
    
    /**
     * Scroll down - specify pixels to scroll down
     * 
     * @param pixelsDown
     * 
     * @throws BasePageException 
     */
    public void scrollDown(int pixelsDown) throws BasePageException {
        
        logger.info("scroll down: " + pixelsDown);
        
        commandList.addToList("scroll down: " + pixelsDown);
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollBy(0," + String.valueOf(pixelsDown) + ")", "");
                 
        }
        catch(Exception e) { throw new BasePageException(e); }
              
    }
    
    /**
     * Scroll up
     * 
     * @throws BasePageException 
     */
    public void scrollUp() throws BasePageException {
        
        logger.info("scroll up");
        
        commandList.addToList("scroll up");
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollBy(0,-250)", "");
                 
        }
        catch(Exception e) { throw new BasePageException(e); }
              
    }
    
    /**
     * Scroll up - specify pixels to scroll up
     * 
     * @param pixelsUp
     * 
     * @throws BasePageException 
     */
    public void scrollUp(int pixelsUp) throws BasePageException {
        
        logger.info("scroll up");
        
        commandList.addToList("scroll up");
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollBy(0,-" + String.valueOf(pixelsUp) + ")", "");
                 
        }
        catch(Exception e) { throw new BasePageException(e); }
              
    }
    
    /**
     * Scroll down to the bottpm of the web page
     * 
     * @throws BasePageException 
     */
    public void scrollDownToBottomOfPage() throws BasePageException {
        
        logger.info("scroll down to botom of page");
        
        commandList.addToList("scroll down to bottom of page");
            
        try {
                 
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                 
        }
        catch(Exception e) { throw new BasePageException(e); }
              
    }

}

