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
 
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.remote.MobileCapabilityType;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import org.apache.log4j.Logger;

import org.automateit.util.CommonSelenium;

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
public class BaseMobileWebPage extends BasePage {
    
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
     * Logging class
     */
    private Logger logger = Logger.getLogger(BaseMobileWebPage.class);
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @throws Exception 
     */
    public BaseMobileWebPage() throws Exception { }
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @param url
     * 
     * @throws Exception 
     */
    public BaseMobileWebPage(String url) throws Exception {
    
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
    public BaseMobileWebPage(String url, String udid) throws Exception {
    
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
    public BaseMobileWebPage(boolean init) throws Exception {
    
        try { if(init) initSetup(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Copy Constructor. Each screen class must use this constructor.
     * 
     * @param baseScreen
     * 
     * @throws Exception 
     */
    public BaseMobileWebPage(BaseMobileWebPage baseScreen) throws Exception {
    
        try { inheritSession(baseScreen); }
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
     * @throws BasePageException
     */
    protected void initSetup() throws BasePageException { 
        
        logger.debug("Initializing a new BasePage -> selenium/webdriver and other settings using baseURL property (default initialization)");
        
        try {
            
            initializeCommonProperties();
        
            logger.info("Starting at baseURL: " + properties.get(StringCapabilities.URL.getCapability()));
            
            initSetup(properties.get(StringCapabilities.URL.getCapability()));
        
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
    protected void initSetup(String startURL) throws BasePageException {
        
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
     * @throws BasePageException
     */
    protected void initSetup(String startURL, boolean goToURL) throws BasePageException { 
      
        try {
            
            initializeCommonProperties();
            
            this.url = startURL;
            
            logger.info("Initializing a new BaseMobilewebPage -> WebDriver and other settings");
            
            logger.info("startURL: " + startURL);
            logger.info("goToURL: " + goToURL);
           
            logger.info("Properties loaded at: " + properties.PROPERTIESFILEPATH + ", size: " + properties.size());
         
            logger.info("Creating Selenium 3.0 instance: " + properties.get(StringCapabilities.ID_SELENIUM_DRIVER.getCapability()));

            // screenshot properties
            if(properties.get(BooleanCapabilities.CAPTURE_SCREENSHOTS.getCapability()) != null) { doScreenshot = (new Boolean(properties.get(BooleanCapabilities.CAPTURE_SCREENSHOTS.getCapability()))).booleanValue(); }
            
            logger.info("Do screenshots on each page load: " + doScreenshot);
            
            // set the captureSeleniumCommands
            if(properties.get(BooleanCapabilities.CAPTURE_SELENIUM_COMMANDS.getCapability()) != null) { captureSeleniumCommands = (new Boolean(properties.get(BooleanCapabilities.CAPTURE_SELENIUM_COMMANDS.getCapability()))).booleanValue(); }
            
            logger.info("Capture selenium commands: " + captureSeleniumCommands);
            
            //jsLibrary = properties.get(properties.get(StringCapabilities.JS_LIBRARY.getCapability()));
            //logger.info("jsLibrary: " + this.jsLibrary);
           
            logger.info("Opening the start url: " + this.url);
           
            // we are adding this because of a concern that the webdriver fails
            // to create the session, so we are attempting 5 times
            for(int i = 0; i < 5; i++) {
                
                try { 
                
                    createWebDriver(startURL, goToURL); 
                    
                    break;
            
                }    
                catch(Exception le) { if(i == 5) throw le; }
                
            }
           
        }
        catch(Exception e) { 
        
            logger.error(e);
            
            throw new BasePageException(e); 
        
        }

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
            
            CommonSelenium.getInstance().setWebDriver(driver);
            
            hasBeenInit = true;
            
            logger.info("Finished initializing Selenium/WebDriver: " + driver);
           
        }
        catch(Exception e) { logger.error("Error creating webdriver"); logger.error(e); throw e; }

    }
    
    /**
     * The implementation class must have a validate method.
     * 
     * @throws Exception 
     */
    public void validate() throws Exception { }
    
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
            
            delay(5000);
            
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
    
}