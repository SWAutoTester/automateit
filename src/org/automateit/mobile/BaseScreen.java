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

package org.automateit.mobile;
 
import java.io.FileInputStream;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.testng.Assert; 

import org.openqa.selenium.By; 
import org.openqa.selenium.WebElement; 
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.remote.MobileCapabilityType;

import org.automateit.core.Capabilities;
import org.automateit.util.CommandList;
import org.automateit.util.CommonProperties;
import org.automateit.util.CommonSelenium;
import org.automateit.data.DataDrivenInput;
import org.automateit.util.Utils;
        
/**
 * This class is the base class for all other screen classes to use.
 * 
 * It abstracts away all of the webdriver-specific programming and provides 
 * easy to use convenience methods for rapid development of mobile apps using
 * webdriver and the screen design pattern.
 * 
 * @author mburnside
 */
public class BaseScreen {
    
    /**
     * The webdriver class to use
     */
    protected AppiumDriver<WebElement> driver = null;

    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(BaseScreen.class);
    /**
     * Properties that can be used for any class extending this class
     */
    protected CommonProperties properties = CommonProperties.getInstance();
    
    /**
     * If the web driver has been initialized
     */
    protected boolean hasBeenInitialized = false;
    
    /**
     * If the logging mechanism has been initialized
     */
    protected boolean loggingSetup = false;
    
    /**
     * Indicates for the framework to record the video
     */
    protected boolean recordVideo = false;
    
    /**
     * The web driver capabilities object
     */
    protected DesiredCapabilities capabilities = new DesiredCapabilities();
    
    /**
     * Utilities for convenience methods
     */
    protected Utils utils = new Utils();
    
    /**
     * Capture WebDriver commands. Default is <code>false</code>.
     */
    protected boolean captureWebDriverCommands = false;
    
    /**
     * Command List
     */
    protected CommandList commandList = CommandList.getInstance();
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @throws Exception 
     */
    public BaseScreen() throws Exception {
    
        try { init(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @param init
     * 
     * @throws Exception 
     */
    public BaseScreen(boolean init) throws Exception {
    
        try { if(init) init(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Copy Constructor. Each screen class must use this constructor.
     * 
     * @param baseScreen
     * 
     * @throws Exception 
     */
    public BaseScreen(BaseScreen baseScreen) throws Exception {
    
        try { inheritSession(baseScreen); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Get the web driver object.
     * 
     * @return 
     */
    protected AppiumDriver getWebDriver() { return this.driver; }
    
    /**
     * Inherit all session values from the previous screen.
     * 
     * @param baseScreen
     * 
     * @throws Exception 
     */
    protected void inheritSession(BaseScreen baseScreen) throws Exception {
        
        try { this.driver = baseScreen.getWebDriver(); }
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
     * Setup the web driver to use for this set of tests.
     * 
     * @throws Exception 
     */
    public void init() throws Exception { 
        
        if(!loggingSetup) setupLogging();
        
        if(hasBeenInitialized) {
            
            logger.info("Returning from setup - already has been initialized - did not do a total reset");
            
            return;
        }
       
        try {
       
            logger.info("Loading webdriver property: " + "device" + "|" + properties.get("device"));
            capabilities.setCapability("device", properties.get("device"));
            
            logger.info("Loading webdriver property: " + "deviceName" + "|" + properties.get("deviceName"));
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, properties.get("deviceName"));
            
            logger.info("Loading webdriver property: " + CapabilityType.BROWSER_NAME + "|\"\"");
            capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
            
            logger.info("Loading webdriver property: " + "app-package" + "|" + properties.get("app-package"));
            capabilities.setCapability("app-package", properties.get("app-package"));
            
            logger.info("Loading webdriver property: " + "app-wait-package" + "|" + properties.get("app-wait-package"));
            capabilities.setCapability("app-wait-package", properties.get("app-wait-package"));
            
            logger.info("Loading webdriver property: " + "app-activity" + "|" + properties.get("app-activity"));
            capabilities.setCapability("app-activity", properties.get("app-activity"));
            
            logger.info("Loading webdriver property: " + "app-wait-activity" + "|" + properties.get("app-wait-activity"));
            capabilities.setCapability("app-wait-activity", properties.get("app-wait-activity"));
            
            logger.info("Loading webdriver property: " + CapabilityType.VERSION + "|" + properties.get("version"));
            capabilities.setCapability(CapabilityType.VERSION, properties.get("version"));
            
            logger.info("Loading webdriver property: " + CapabilityType.PLATFORM + "|" + properties.get("platform"));
            capabilities.setCapability(CapabilityType.PLATFORM, properties.get("platform"));
            
            logger.info("Loading webdriver property: " + "platformName" + "|" + properties.get("platformName"));
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, properties.get("platformName"));
            
            logger.info("Loading webdriver property: " + "app" + "|" + properties.get("app_location"));
            capabilities.setCapability(MobileCapabilityType.APP, properties.get("app_location"));  
            
            if(properties.getProperty("automationName") != null) {
                
                logger.info("Loading webdriver property: " + "automationName" + "|" + properties.get("automationName"));
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, properties.get("automationName"));
            
            }
            
            if(properties.getProperty("platformVersion") != null) {
                
                logger.info("Loading webdriver property: " + "platformVersion" + "|" + properties.get("platformVersion"));
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, properties.get("platformVersion"));
            
            }
            
            if(properties.getProperty("bundleId") != null) {
                
                logger.info("Loading webdriver property: " + "bundleId" + "|" + properties.get("bundleId"));
                capabilities.setCapability("bundleId", properties.get("bundleId"));
            
            }
            
            if(properties.getProperty("realDeviceLogger") != null) {
                
                logger.info("Loading webdriver property: " + "realDeviceLogger" + "|" + properties.get("realDeviceLogger"));
                capabilities.setCapability("realDeviceLogger", properties.get("realDeviceLogger"));
            
            }
            
            if(properties.getProperty("xcodeConfigFile") != null) {
                
                logger.info("Loading webdriver property: " + "xcodeConfigFile" + "|" + properties.get("xcodeConfigFile"));
                capabilities.setCapability("xcodeConfigFile", properties.get("xcodeConfigFile"));
            
            }
            
            if(properties.getProperty("reInstallApp") != null) {
                
                logger.info("Loading webdriver property: " + "reInstallApp" + "|" + properties.get("reInstallApp"));
                
                boolean reset = (new Boolean(properties.get("reInstallApp"))).booleanValue();
                
                capabilities.setCapability(MobileCapabilityType.NO_RESET, !reset); // android
                if(isIOS()) capabilities.setCapability(MobileCapabilityType.FULL_RESET, reset); //ios
                    
            }
            
            // this code will make sure the app does not get reset if set to true by an outside mechanism/class
            // that overrides the property
            if(properties.getForceAppNoReset()) {
                
                logger.info("Forcing the app not to be re-installed/reset");
               
                capabilities.setCapability("noReset", true);
                if(isIOS()) capabilities.setCapability("fullReset", false); //ios
                    
            }
            
            if(properties.getProperty("udid") != null) {
                
                logger.info("Loading webdriver property: " + "udid" + "|" + properties.get("udid"));
                capabilities.setCapability(MobileCapabilityType.UDID, properties.get("udid"));
            
            }
            
            if(properties.getProperty("newCommandTimeout") != null) {
                
                logger.info("Loading webdriver property: " + "newCommandTimeout" + "|" + properties.get("newCommandTimeout"));
                capabilities.setCapability("newCommandTimeout", properties.get("newCommandTimeout"));
            
            }
            
            logger.info("Loading webdriver property: " + "URL" + "|" + properties.get("URL"));
              
            logger.info("Preparing to create a new web driver instance");
            if(properties.get("device").trim().equals("ANDROID")) this.driver = new AndroidDriver(new URL(properties.get("URL")), capabilities);
            else this.driver = new IOSDriver(new URL(properties.get("URL")), capabilities);
            
            logger.info("Successfully made new web driver instance, now set the timeout value: " + properties.get("timeout"));
            this.driver.manage().timeouts().implicitlyWait((new Long(properties.get("timeout"))).longValue(), TimeUnit.SECONDS);
            
            // set the captureWebDriverCommands
            if(properties.getProperty("captureWebDriverCommands") != null) { this.captureWebDriverCommands = (new Boolean(properties.getProperty("captureWebDriverCommands"))).booleanValue(); }
            
            logger.info("Capture webdriver commands: " + this.captureWebDriverCommands);
            
            hasBeenInitialized = true;
            
            CommonSelenium.getInstance().setWebDriver(this.driver);
            
            logger.info("Setup web driver setup for tests execution");
            
        }
        catch(Exception e) { 
        
            logger.error(e.getMessage());
        
            if(this.driver != null) quit();
            
            logger.info("Preparing to create a new web driver instance");
            
            if(properties.get("device").trim().equals("ANDROID")) this.driver = new AndroidDriver(new URL(properties.get("URL")), capabilities);
            else this.driver = new IOSDriver(new URL(properties.get("URL")), capabilities);
            
            CommonSelenium.getInstance().setWebDriver(this.driver);
            
            logger.info("Successfully made new web driver instance, now set the timeout value: " + properties.get("timeout"));
            this.driver.manage().timeouts().implicitlyWait((new Long(properties.get("timeout"))).longValue(), TimeUnit.SECONDS);
            
        }
      
    } 
    
    /**
     * Finish the setup after capabilities have been added.
     * 
     * @throws Exception 
     */
    protected void finishSetup() throws Exception {
        
        try {
          
            if(properties.get("device").trim().equals("ANDROID")) this.driver = new AndroidDriver(new URL(properties.get("URL")), capabilities);
            else this.driver = new IOSDriver(new URL(properties.get("URL")), capabilities);
            
            CommonSelenium.getInstance().setWebDriver(this.driver);
            
            driver.manage().timeouts().implicitlyWait((new Long(properties.get("timeout"))).longValue(), TimeUnit.SECONDS);
            
        }
        catch(Exception e) { throw e;}
        
    }
    
    /**
     * Reset the setup (force new web driver session and object to drive the UI).
     * 
     * Use this method to re-enter the app in a logged-out state. Gives more control
     * when starting tests.
     * 
     * @throws Exception 
     */
    protected void resetSetup() throws Exception {
        
        this.hasBeenInitialized = false;
        
        try { init(); } 
        catch(Exception e) { throw e; }
        
        delay(3000);
        
    }
    
    /**
     * Stop the session.
     */
    protected void stopSession() { 
        
        logger.info("Stopping webdriver session");
            
        commandList.addToList("stopSession(quit)");
        
        try {
            
            if(this.driver != null) quit(); 
            
            logger.info("Stopping webdriver session finished");
            
            this.hasBeenInitialized = false;
        
        }
        catch(Exception e) { }
        
    }
    
    /**
     * Stop the session.
     */
    public void stop() { 
       
        try { stopSession(); }
        catch(Exception e) { }
        
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
     * Perform a swipe on the screen.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     * 
     * @throws Exception 
     */
    public void swipe(int startX, int startY, int endX, int endY, int duration) throws Exception {
        
        logger.info("Attempting to perform swipe on screen at: start(" + startX + "," + startY + ") finish:(" + endX + "," + endY + ") for duration of: " + duration + " seconds");
            
        commandList.addToList("swipe: " + startX + "," + startY + "-" + endX + "," + endY);
        
        try { 
            
            scroll(startX, startY, endX, endY, duration);
            
            delay(1500);   
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Perform a swipe on the screen.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     * 
     * @throws Exception 
     */
    public void swipe(String startX, String startY, String endX, String endY, String duration) throws Exception {
        
        try { swipe((new Integer(startX)).intValue(), (new Integer(startY)).intValue(), (new Integer(endX)).intValue(), (new Integer(endY)).intValue(), (new Integer(duration)).intValue()); }
        catch(Exception e) { throw e; }
        
    }
 
    /**
     * Perform a tap/click on the screen.
     * 
     * @param x
     * @param y
     * 
     * @throws Exception 
     */
    public void tap(int x, int y) throws Exception {
        
        logger.info("Attempting to perform tap on screen at: " + x + "," + y);
            
        commandList.addToList("tap: " + x + "," + y);
        
        try {
                
            TouchAction touchAction = new TouchAction(this.driver);
            
            touchAction.tap(x, y).perform();
            
            delay(1500);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Perform a tap/click on the screen.
     * 
     * @param x
     * @param y
     * 
     * @throws Exception 
     */
    public void tap(String x, String y) throws Exception {
        
        try { tap((new Integer(x)).intValue(), (new Integer(y)).intValue()); }
        catch(Exception e) { throw e; }
        
    }
       
    /**
     * Perform a tap/click on the screen.
     * 
     * @param webelement
     * 
     * @throws Exception 
     */
    public void tap(WebElement webelement) throws Exception {
        
        logger.info("Attempting to perform tap on screen at web element: " + webelement);
            
        commandList.addToList("tap: " + webelement);
        
        try {
              
            TouchAction touchAction = new TouchAction(this.driver);
            
            touchAction.tap(webelement).perform();
            
            delay(1500);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Perform a tap/click on the screen.
     * 
     * @param locator xpath/css locator
     * 
     * @throws Exception 
     */
    public void tap(String locator) throws Exception {
        
        logger.info("Attempting to perform tap on screen at web element at location: " + locator);
            
        commandList.addToList("tap: " + locator);
        
        try {
            
            TouchAction touchAction = new TouchAction(this.driver);
                
            touchAction.tap(getWebElementByXPath(locator)).perform();
            
            delay(1500);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Perform a scroll with duration of 3 seconds.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * 
     * @throws Exception 
     */
    public void scroll(int startX, int startY, int endX, int endY) throws Exception {
        
        try { scroll(startX, startY, endX, endY, 3); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Perform a scroll with duration of 3 seconds.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * 
     * @throws Exception 
     */
    public void scroll(String startX, String startY, String endX, String endY) throws Exception {
        
        try { scroll((new Integer(startX)).intValue(), (new Integer(startY)).intValue(), (new Integer(endX)).intValue(), (new Integer(endY)).intValue(), (new Integer(3)).intValue()); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Perform a scroll.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     * 
     * @throws Exception 
     */
    public void scroll(int startX, int startY, int endX, int endY, int duration) throws Exception {
        
        logger.info("Attempting to perform scroll on mobile screen; start: " + startX + ":" + startY + ", end: " + endX + ":" + endY + ", duration: " + duration);
            
        commandList.addToList("scroll: start: " + startX + ":" + startY + ", end: " + endX + ":" + endY + ", duration: " + duration);
        
        try {
                
            TouchAction touchAction = new TouchAction(this.driver);
            
            //touchAction.longPress(startX,startY, Duration.ofSeconds(duration)).moveTo(endX,endY).release().perform(); 
            
            touchAction.press(startX,startY).waitAction(Duration.ofSeconds(duration)).moveTo(endX,endY).release().perform(); 
            
            delay(1500);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Perform a scroll.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     * 
     * @throws Exception 
     */
    public void scroll(String startX, String startY, String endX, String endY, String duration) throws Exception {
        
        try { scroll((new Integer(startX)).intValue(), (new Integer(startY)).intValue(), (new Integer(endX)).intValue(), (new Integer(endY)).intValue(), (new Integer(duration)).intValue()); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll down one entire screen length.
     * 
     * @throws Exception 
     */
    public void scrollDown() throws Exception {
        
        logger.info("Scroll Down");
            
        commandList.addToList("Scroll Down");
        
        try {
             
            JavascriptExecutor js = (JavascriptExecutor) driver;

            HashMap<String, String> scrollObject = new HashMap<String, String>();

            scrollObject.put("direction", "down");

            js.executeScript("mobile: scroll", scrollObject);

            delay(1500);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll down one entire screen length.
     * 
     * @throws Exception 
     */
    public void scrollUp() throws Exception {
        
        logger.info("Scroll Up");
            
        commandList.addToList("Scroll Up");
        
        try {
             
            JavascriptExecutor js = (JavascriptExecutor) driver;

            HashMap<String, String> scrollObject = new HashMap<String, String>();

            scrollObject.put("direction", "up");

            js.executeScript("mobile: scroll", scrollObject);

            delay(1500);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Return the web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementContainingText(String text, String className) throws Exception {
        
        logger.info("getWebElementContainingText:" + text + "," + className);
            
        commandList.addToList("getWebElementContainingText:" + text + "|" + className);
        
        try {
             
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for(WebElement element:elements) {
                
                if(!element.isDisplayed() || !element.isEnabled()) continue;
               
                String data1 = element.getAttribute("name");
                
                if(data1 == null) continue;
                
                String data = data1.trim();
               
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                logger.info("Checking element text value: " + data);
                
                if(data.contains(text)) return element;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get the full displayed value of the web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getFullValueOfWebElementContainingText(String text, String className) throws Exception {
        
        logger.info("clickOnWebElementContainingText:" + text + "," + className);
            
        commandList.addToList("clickOnWebElementContainingText:" + text + "|" + className);
        
        try { return getWebElementContainingText(text, className).getAttribute("name").trim(); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
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
        
        logger.info("clickOnWebElementContainingText:" + text + "," + className);
            
        commandList.addToList("clickOnWebElementContainingText:" + text + "|" + className);
        
        try { getWebElementContainingText(text, className).click(); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Click on a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void clickOnSecondWebElementContainingText(String text, String className) throws Exception {
        
        logger.info("clickOnSecondWebElementContainingText:" + text + "," + className);
            
        commandList.addToList("clickOnSecondWebElementContainingText:" + text + "|" + className);
        
        boolean foundFirstOccurrance = false;
        
        try {
             
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for(WebElement element:elements) {
                
                if(!element.isDisplayed() || !element.isEnabled()) continue;
               
                String data1 = element.getAttribute("name");
                
                if(data1 == null) continue;
                
                String data = data1.trim();
               
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                logger.info("Checking element text value: " + data);
                
                if(data.contains(text)) {
                    
                    if(foundFirstOccurrance) {
                        
                        element.click();
                        
                        delay(2000);
                      
                        return;
                    }
                    else { foundFirstOccurrance = true; }
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
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
        
        logger.info("clickOnWebElementContainingText:" + text1 + "|" + text2 + "|" + className);
            
        commandList.addToList("clickOnWebElementContainingText:" + text1 + "|" + text2 + "|" + className);
       
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(data.contains(text1) && data.contains(text2)) {
                   
                    element.click();
                    
                    delay(2000);
                        
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception    
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text1: " + text1 + " and text2: " + text2);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
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
        
        logger.info("clickOnWebElementMatchingText:" + text + "|" + className);
        
        commandList.addToList("clickOnWebElementMatchingText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name");
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                
                if(data.trim().equals(text.trim())) {
                    
                    element.click();
                    
                    delay(2000);
                        
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text);
            
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
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
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
               
                if(element == null) continue;
                
                String data1 = element.getAttribute("name");
                
                if((data1 == null) || (data1.trim().length() == 0)) continue; // blank, so ignore
                
                String data = data1.trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data + "|"+ text);
                   
                if(data.contains(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and matching the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void validateWebElementMatchingText(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(data.equals(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void validateWebElementContainingTextValueAttribute(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                if(element == null) continue;
                
                String data1 = element.getText();
                
                if(data1 == null) continue;
                
                String data = data1.trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(data.contains(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void validateWebElementMatchingTextValueAttribute(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(data.equals(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element at resource id containing the text expected.
     * 
     * @param resourceId
     * @param expectedText
     * 
     * @throws Exception 
     */
    public void validateElementWithResourceIdContainingText(String resourceId, String expectedText) throws Exception {
        
        logger.info("Validate that there is a web element at resource id containing the text expected: " + resourceId + "|" + expectedText);
            
        commandList.addToList("validateElementWithResourceIdContainingText:" + resourceId + "|" + expectedText);
        
        try { 
            
            if(resourceId == null) throw new BaseScreenException("Validate that there is a web element at resource id containing the text expected because resource id is null: " + resourceId);
            
            if(expectedText == null) throw new BaseScreenException("Validate that there is a web element at resource id containing the text expected because expected text is null: " + expectedText);
            
            Assert.assertTrue(getValueAtWebElementWithResourceId(resourceId).trim().contains(expectedText.trim())); 
                 
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element at resource id matching the text expected.
     * 
     * @param resourceId
     * @param expectedText
     * 
     * @throws Exception 
     */
    public void validateElementWithResourceIdMatchingText(String resourceId, String expectedText) throws Exception {
        
        logger.info("Validate that there is a web element at resource id matching the text expected: " + resourceId + "|" + expectedText);
            
        commandList.addToList("validateElementWithResourceIdMatchingText:" + resourceId + "|" + expectedText);
        
        try { 
            
            if(resourceId == null) throw new BaseScreenException("Validate that there is a web element at resource id matching the text expected because resource id is null: " + resourceId);
            
            if(expectedText == null) throw new BaseScreenException("Validate that there is a web element at resource id matching the text expected because expected text is null: " + expectedText);
            
            Assert.assertTrue(getValueAtWebElementWithResourceId(resourceId).trim().equals(expectedText.trim())); 
                 
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     *
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void clickOnWebElementContainingTextValueAttribute(String text, String className) throws Exception {
        
        logger.info("Clicking on web element of type: " + className + " containing text: " + text + " appears on the screen somewhere.");
        
        commandList.addToList("clickOnWebElementContainingTextValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
          
            for (WebElement element:elements) {
                
                if(!element.isDisplayed()) continue;
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(data.contains(text)) {
                    
                    element.click();
                    
                    delay(3000);
                    
                    return;
                    
                };
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void clickOnWebElementMatchingTextValueAttribute(String text, String className) throws Exception {
        
        logger.info("Clicking on web element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
        
        commandList.addToList("clickOnWebElementMatchingTextValueAttribute:" + text + "|" + className);
        
        try {
          
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(data.equals(text)) {
                   
                    element.click();
                    
                    delay(3000);
                    
                    return;
                    
                };
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param name
     * 
     * @throws Exception 
     */
    public void validateWebElementContainingTextByName(String name) throws Exception {
        
        logger.info("Validating that element of name: " + name + " exists");
            
        commandList.addToList("validateWebElementContainingTextByName:" + name);
        
        try {
            
            WebElement element = this.driver.findElement(By.name(name));
            
            // if we get here, we could not find the element so throw an exception
            if(element == null) throw new Exception("Could not validate a screen component with screen element matching name: " + name);
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a not web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void validateAllWebElementsDoNotContainText(String text, String className) throws Exception {
        
        commandList.addToList("validateAllWebElementsDoNotContainText:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) throw new Exception("Found a web element / screen component with text and matching type: " + className + " and text: " + text + ", it was not expected to be visible");
                
            }
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a not web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void validateAllWebElementsDoNotContainTextByValueAttribute(String text, String className) throws Exception {
        
        commandList.addToList("validateAllWebElementsDoNotContainTextByValueAttribute:" + text + "|" + className);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) throw new Exception("Found a web element / screen component with text and matching type: " + className + " and text: " + text + ", it was not expected to be visible");
                
            }
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Return the index for a certain web element type.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected int getElementTypeIndexMatchingText(String text, String className) throws Exception {
        
        commandList.addToList("getElementTypeIndexMatchingText:" + className + "|" + text);
        
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
            throw new Exception("Could the index of a screen component with text in any screen element matching type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Return the index for a certain web element type.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    protected int getElementTypeIndexContainingText(String text, String className) throws Exception {
        
        commandList.addToList("getElementTypeIndexContainingText:" + className + "|" + text);
        
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
            throw new Exception("Could the index of a screen component with text in any screen element containing type: " + className + " and text: " + text);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
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
       
        logger.info("Preparing to find the id of : " + text);
        
        commandList.addToList("getWebElementIdForElementNameAttributeContainsText:" + className + "|" + text);
        
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
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get the element id for a web element of type "text" that contains text in
     * the "value" attribute.
     * 
     * @param text
     * @param elementType
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getWebElementIdForTextElementValueAttributeContainsText(String text, String elementType) throws Exception {
       
        logger.info("Preparing to find the id of : " + text);
        
        commandList.addToList("getWebElementIdForTextElementValueAttributeContainsText:" + elementType + "|" + text);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(elementType));
           
            for (WebElement element:elements) {
                   
                String value = element.getText().trim();
                 
                if((value == null) || (value.trim().length() == 0)) continue; // null or blank, so ignore
                
                logger.info("Checking for " + value + "|" + text);
               
                if(value.trim().equals(text)) return ((RemoteWebElement) element).getId();
                
            }
          
            throw new Exception("Unable to find web element id for displayed value element: " + text + "|" + elementType);
          
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Return the number of times there is matching text for a certain web element type for the Name attribute.
     * 
     * @param text
     * @param elementType
     * 
     * @throws Exception 
     */
    protected int getElementTypeMatchingNameAttributeTextCount(String text, String elementType) throws Exception {
        
        commandList.addToList("getElementTypeMatchingNameAttributeTextCount:" + text + "|" + elementType);
        
        int count = 0;
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(elementType));    
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.trim().equals(text)) count++;
                
            }
            
            return count;
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Return the number of times there contains text for a certain web element type for the Name attribute.
     * 
     * @param text
     * @param elementType
     * 
     * @throws Exception 
     */
    protected int getElementTypeContainsNameAttributeTextCount(String text, String elementType) throws Exception {
        
        commandList.addToList("getElementTypeContainsNameAttributeTextCount:" + text + "|" + elementType);
        
        int count = 0;
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(elementType));    
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(text)) count++;
                
            }
            
            return count;
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get a web element at xpath locator.
     * 
     * @param xpath 
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected WebElement getWebElementByXPath(String xpath) throws Exception {
        
        try { return this.driver.findElement(By.xpath(xpath)); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get a web element matching the exact "name" attribute value.
     * 
     * @param name
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected WebElement getWebElementByName(String name) throws Exception {
        
        try { return this.driver.findElement(By.name(name)); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get a web element for class name and "name" attribute value.
     * 
     * @param className
     * @param name
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected WebElement getWebElementAtLocationByClassNameAndNameAttributeValue(String className, String name) throws Exception {
        
        try {
            
            commandList.addToList("getWebElementAtLocationByClassNameAndNameAttributeValue:" + className + "|" + name);
            
            List<WebElement> elements = this.driver.findElements(By.className(className));    
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(name)) return element;
                
            }
            
            // if we get here it was not found
            throw new Exception("Unable to find web element using classname: " + className + " and name: " + name);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get a web element for class name and attribute value (from webelement.getText()).
     * 
     * @param className 
     * @param value
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected WebElement getWebElementAtLocationByClassNameAndValueAttributeValue(String className, String value) throws Exception {
        
        try {
            
            commandList.addToList("getWebElementAtLocationByClassNameAndValueAttributeValue:" + className + "|" + value);
            
            List<WebElement> elements = this.driver.findElements(By.className(className));    
            
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.contains(value)) return element;
                
            }
            
            // if we get here it was not found
            throw new Exception("Unable to find/get web element using classname: " + className + " and value: " + value);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get a web element for class name and attribute value (from webelement.getText()).
     * 
     * @param className 
     * @param value
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected WebElement getWebElementAtLocationByClassNameAndValueAttributeValueEquals(String className, String value) throws Exception {
        
        try {
            
            commandList.addToList("getWebElementAtLocationByClassNameAndValueAttributeValue:" + className + "|" + value);
            
            List<WebElement> elements = this.driver.findElements(By.className(className));    
            
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.equals(value)) return element;
                
            }
            
            // if we get here it was not found
            throw new Exception("Unable to find/get web element using classname: " + className + " and value: " + value);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Fetch the web element containing given text //mburnside review
     *
     * @param elements
     * @param text
     *
     * @return web element
     *
     * @throws Exception
     */
    public WebElement getElementsByText(List<WebElement> elements, String text) throws Exception {

        logger.info("Get the element contains text: " + text + " appears on the screen somewhere.");

        commandList.addToList("getElement(contain):" + text);

        for (WebElement element:elements) {

            String data = element.getText().trim();

            if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
            
            if(data.equals(text.trim())) return element;
            
        }
        
        return null;

    }
   
    /**
     * Go Back (using Android device back button).
     * 
     * @throws Exception 
     */
    public void androidGoBack() throws Exception {
        
        logger.info("androidGoBack");
        
        commandList.addToList("androidGoBack");
    
        try { 
            
            this.driver.navigate().back();
            
            delay(2000); 
        
        }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Simulate Enter or Return key (using Android enter key).
     * 
     * @throws Exception 
     */
    public void androidClickEnterKey() throws Exception { 
        
        logger.info("androidClickEnterKey");
        
        commandList.addToList("androidClickEnterKey");
    
        try { 
            
            Runtime.getRuntime().exec("adb shell input keyevent 66"); 
            
            delay(2000); 
        
        }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement.
     * 
     * @param element
     * 
     * @throws BasePageException 
     */
    public void clearWebElement(WebElement element) throws Exception { 
        
        logger.info("Clear web element: " + element);
        
        commandList.addToList("clearWebElement:" + element);
        
        try { element.clear(); }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement looked up using xpath.
     * 
     * @param locator
     * 
     * @throws BasePageException 
     */
    public void clearWebElement(String locator) throws Exception { 
        
        logger.info("Clear web element: " + locator);
        
        commandList.addToList("clearWebElement:" + locator);
    
        try { clearWebElement(getWebElementByXPath(locator)); }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement looked up using the resource id.
     * 
     * @param resourceId
     * 
     * @throws BasePageException 
     */
    public void clearWebElementByResourceId(String resourceId) throws Exception { 
        
        logger.info("Clear web element by resource id: " + resourceId);
        
        commandList.addToList("clearWebElementByResourceId:" + resourceId);
    
        try { clearWebElement(getWebElementAtResourceId(resourceId)); }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement
     * 
     * @param value
     * @param classname
     * 
     * @throws Exception 
     */
    public void clearWebElementByValueAndClassname(String value, String classname) throws Exception { 
        
        logger.info("Clear web element by value and classname: " + value + "|" + classname);
        
        commandList.addToList("clearWebElementByValueAndClassname:" + value + "|" + classname);
    
        try { clearWebElement(getWebElementAtLocationByClassNameAndValueAttributeValue(classname, value)); }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get the element that contains the text in the "name" attribute.
     * 
     * @param elementType
     * @param attributeName
     * @param attributeValue
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementWithNameAttributeContainsText(String elementType, String attributeName, String attributeValue) throws Exception {
       
        logger.info("Preparing to find web element for element type: " + elementType + " and attribute value: " + attributeValue + " and attribute name: " + attributeName);
        
        commandList.addToList("getWebElementWithNameAttributeContainsText:" + elementType + "|" + attributeName + "|" + attributeValue);
        
        try {
          
            List<WebElement> elements = this.driver.findElements(By.className(elementType));
            
            for (WebElement element:elements) {
                   
                try {
                   
                    String value = element.getAttribute(attributeName).trim();
                  
                    if((value == null) || (value.trim().length() == 0)) continue; // null or blank, so ignore
                 
                    if(value.trim().equals(attributeValue)) return element;
                }
                catch(Exception e2) { }
             
            }
          
            throw new Exception("Unable to find web element for element type: " + elementType + " and attribute value: " + attributeValue + " and attribute name: " + attributeName);
          
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
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
       
        logger.info("Preparing to find web element by id: " + id);
        
        commandList.addToList("getWebElementWithId:" + id);
        
        try { return getWebElementAtResourceId(id); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Get the element at the matching resource id attribute.
     * 
     * @param id
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement getWebElementAtResourceId(String resourceId) throws Exception {
       
        logger.info("Get the web element at resource id: " + resourceId);
        
        commandList.addToList("getWebElementAtResourceId:" + resourceId);
        
        try { return this.driver.findElement(By.id(resourceId)); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
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
     * Click on the web element at xpath locator.
     * 
     * @param xpath
     * 
     * @throws Exception 
     */
    public void click(String xpath) throws Exception {
        
        logger.info("Clicking on web element at location: " + xpath);
            
        commandList.addToList("click:" + xpath);
        
        try { getWebElementByXPath(xpath).click(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Click on the web element using the resource id
     * 
     * @param id
     * 
     * @throws Exception 
     */
    public void clickUsingResourceId(String id) throws Exception {
        
        logger.info("Clicking on web element with resource id: " + id);
             
        commandList.addToList("clickUsingResourceId:" + id);
        
        try { getWebElementAtResourceId(id).click(); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Return the value of the web element using the resource id
     * 
     * @param id
     * 
     * @throws Exception 
     */
    public String getValueAtWebElementWithResourceId(String id) throws Exception {
        
        logger.info("Get value at web element with resource id: " + id);
             
        commandList.addToList("getValueAtWebElementWithResourceId:" + id);
        
        try { return getWebElementAtResourceId(id).getText(); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Verify the existence of the web element using the resource id
     * 
     * @param id
     * 
     * @throws Exception 
     */
    public void verifyWebElementWithResourceId(String id) throws Exception {
        
        logger.info("Verify value at web element with resource id: " + id);
             
        commandList.addToList("verifyWebElementWithResourceId:" + id);
        
        try { getWebElementAtResourceId(id); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Enter text into a web element.
     * 
     * @param xpath
     * @param text
     * 
     * @throws Exception 
     */
    public void enterDataIntoWebElement(String xpath, String text) throws Exception {
        
        commandList.addToList("enterDataIntoWebElement:" + text + "|" + xpath);
            
        logger.info("Enter data into a component: " + text);
    
        try { 
            
            WebElement webElement = getWebElementByXPath(xpath);
            
            if(webElement == null) throw new Exception("Unable to enter data into WebElement because it is null");
            
            if(text == null) return;
            
            if(isAndroid()) webElement.click();
            
            try { Thread.sleep(1000); } catch(Exception e) { }
            
            webElement.sendKeys(text);
            
            minimizeKeyboard();
            
            try { Thread.sleep(1000); } catch(Exception e) { }
           
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Enter text into a web element.
     * 
     * @param xpath
     * @param text
     * 
     * @throws Exception 
     */
    public void enterDataIOS(String xpath, String text) throws Exception {
        
        commandList.addToList("enterDataIntoWebElement:" + text + "|" + xpath);
            
        logger.info("Enter data into a component: " + text);
    
        try { 
            
            WebElement webElement = getWebElementByXPath(xpath);
            
            if(webElement == null) throw new Exception("Unable to enter data into WebElement because it is null");
            
            if(text == null) return;
            
            try { Thread.sleep(1000); } catch(Exception e) { }
            
            webElement.sendKeys(text);
            
            try { Thread.sleep(1000); } catch(Exception e) { }
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Enter text into a web element with name (textfield attribute name value).
     * 
     * @param name
     * @param text
     * 
     * @throws Exception 
     */
    public void enterSearchDataIOSByElementName(String name, String text) throws Exception {
        
        commandList.addToList("enterDataIOSByElementName:" + text + "|" + name);
            
        logger.info("Enter data into a component: " + text);
    
        try { 
            
            WebElement webElement = getWebElementAtLocationByClassNameAndNameAttributeValue("XCUIElementTypeSearchField", name);
            
            if(webElement == null) throw new Exception("Unable to enter data into WebElement because it is null");
            
            if(text == null) return;
            
            webElement.click();
            
            try { Thread.sleep(1000); } catch(Exception e) { }
            
            webElement.sendKeys(text);
            
            try { Thread.sleep(1000); } catch(Exception e) { }
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Enter text into a web element.
     * 
     * @param xpath
     * @param text
     * 
     * @throws Exception 
     */
    public void enterDataAndSearchIOS(String xpath, String text) throws Exception {
        
        commandList.addToList("enterDataAndSearchIOS:" + text + "|" + xpath);
            
        logger.info("Enter data into a component: " + text);
    
        try { 
            
            enterDataIOS(xpath, text);
       
            clickOnWebElementContainingText("Search", "XCUIElementTypeButton");
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Enter text into a web element.
     * 
     * @param resourceId
     * @param text
     * 
     * @throws Exception 
     */
    public void enterDataIntoWebElementByResourceId(String resourceId, String text) throws Exception {
        
        commandList.addToList("enterDataIntoWebElementByResourceId:" + text + "|" + resourceId);
            
        logger.info("Enter data into a component by resource id: " + text + "|" + resourceId);
    
        try { 
            
            WebElement webElement = getWebElementAtResourceId(resourceId); 
            
            if(webElement == null) throw new Exception("Unable to enter data into WebElement because it is null");
            
            if(text == null) return;
            
            if(isAndroid()) webElement.click();
            
            try { Thread.sleep(1000); } catch(Exception e) { }
            
            webElement.sendKeys(text);
            
            minimizeKeyboard();
            
            try { Thread.sleep(1000); } catch(Exception e) { }
           
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    
    /**
     * Minimize/dismiss the keyboard from view.
     * 
     * @throws Exception 
     */
    public void minimizeKeyboard() throws Exception {
        
        commandList.addToList("minimize keyboard");
        
        logger.info("minimize keyboard");
        
        //android
        if(isAndroid()) {
            
            try { driver.hideKeyboard(); }
            catch(Exception e) { }
            
        }
        else if(isIOS()) {
            
            // this is a very special case because I tried all of the other techniques from webdriver/appium before and nothing 
            // was working, so for xcode 8, for now explicity click on the Done button
            // we will change this when the traditional methods of hide keyboard strategies work again (3rd party api dependent)
            try { clickOnWebElementMatchingText("Done", "XCUIElementTypeButton"); }    
            catch(Exception e2) { 
                
                try { click("//XCUIElementTypeApplication[1]/XCUIElementTypeWindow[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeToolbar/XCUIElementTypeButton[3]"); }  
                catch(Exception le) { }
            
            }
            
        }   
         
    }
    
    /**
     * Verify that a web element is visible on the screen.
     * 
     * @param xpath
     * 
     * @throws Exception 
     */
    protected void assertWebElementIsVisible(String xpath) throws Exception {
        
        logger.info("Asserting a webelement is visible on the screen: " + xpath);
        
        commandList.addToList("assertWebElementIsVisible|" + xpath);
        
        try { Assert.assertNotNull(getWebElementByXPath(xpath)); }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Verify that a web element is visible on the screen.
     * 
     * @param xpath
     * 
     * @throws Exception 
     */
    protected void assertTextValueOfWebElementIsEqual(String xpath, String expectedValue) throws Exception {
        
        logger.info("Asserting text value of webelement attribute is equal to: " + expectedValue);
        
        commandList.addToList("assertTextValueOfWebElementIsEqual|" + xpath + "|" + expectedValue);
        
        try { Assert.assertEquals(getWebElementByXPath(xpath).getText().trim(), expectedValue); }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Return the display value of the web element at xpath locator.
     * 
     * @param xpath
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getDisplayedValue(String xpath) throws Exception {
        
        logger.info("Get display value from web element at location: " + xpath);
            
        commandList.addToList("getDisplayedValue:" + xpath);
        
        try { return getWebElementByXPath(xpath).getText(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**     
     * Verifies that the specified element is visible somewhere on the screen. 
     * 
     * @param locator
     * @return 
     */
    public boolean isVisible(String locator) {
        
        logger.info("Checking if element visible on the screen: " + locator);
    
        commandList.addToList("isVisible: " + locator);
            
        try { 
            
            this.driver.findElement(By.xpath(locator));
            
            return true;
        
        }
        catch(Exception e) { return false; }
        
    }
    
    /**
     * Get the platform type.
     * 
     * @return 
     */
    public String getPlatFormType() { return properties.get("platformName"); }
    
    /**
     * Indicates this is an android device.
     * 
     * @return 
     */
    public boolean isAndroid() {
        
        if(getPlatFormType() == null) return false;
        else return (getPlatFormType().toLowerCase().trim().equals("android"));
        
    }
    
    /**
     * Indicates this is an IOS device.
     * 
     * @return 
     */
    public boolean isIOS() {
        
        if(getPlatFormType() == null) return false;
        else return (getPlatFormType().toLowerCase().trim().equals("ios"));
        
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
        catch(Exception e) { throw new BaseScreenException(e); }
        
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
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Scroll the screen.
     * 
     * @param startXDataSetId
     * @param startYDataSetId
     * @param endXDataSetId
     * @param endYDataSetId
     * 
     * @throws Exception 
     */
    public void performScroll(String dataInputFile, String startXDataSetId, String startYDataSetId, String endXDataSetId, String endYDataSetId) throws Exception { 
    
        try { 
            
            if(dataInputFile == null) throw new Exception("Unable to perform scroll because data driven input file is null");
            
            performScroll(setupDataDrivenInput(dataInputFile), startXDataSetId, startYDataSetId, endXDataSetId, endYDataSetId);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll the screen.
     * 
     * @param scrollData
     * @param startXDataSetId
     * @param startYDataSetId
     * @param endXDataSetId
     * @param endYDataSetId
     * 
     * @throws Exception 
     */
    public void performScroll(DataDrivenInput scrollData, String startXDataSetId, String startYDataSetId, String endXDataSetId, String endYDataSetId) throws Exception { 
    
        try { 
           
            if(scrollData == null) throw new Exception("Unable to perform scroll because data driven input object is null");
            if(startXDataSetId == null) throw new Exception("Unable to perform scroll because startXDataSetId is null");
            if(startYDataSetId == null) throw new Exception("Unable to perform scroll because startYDataSetId is null");
            if(endXDataSetId == null) throw new Exception("Unable to perform scroll because endXDataSetId is null");
            if(endYDataSetId == null) throw new Exception("Unable to perform scroll because endYDataSetId is null");
            
            logger.info("Validating that we have data for scrolling: " + startXDataSetId + ", does data set id exist in data input file: " + scrollData.hasDataId(startXDataSetId));
            logger.info("Validating that we have data for scrolling: " + startYDataSetId + ", does data set id exist in data input file: " + scrollData.hasDataId(startYDataSetId));
            logger.info("Validating that we have data for scrolling: " + endXDataSetId + ", does data set id exist in data input file: " + scrollData.hasDataId(endXDataSetId));
            logger.info("Validating that we have data for scrolling: " + endYDataSetId + ", does data set id exist in data input file: " + scrollData.hasDataId(endYDataSetId));
            
            if(scrollData.hasDataId(startXDataSetId) && scrollData.hasDataId(startYDataSetId) && scrollData.hasDataId(endXDataSetId) && scrollData.hasDataId(endYDataSetId)) {
                
                scroll(scrollData.returnInputDataForDataIdAndColumnNumber(startXDataSetId, 1),
                        scrollData.returnInputDataForDataIdAndColumnNumber(startYDataSetId, 1),
                        scrollData.returnInputDataForDataIdAndColumnNumber(endXDataSetId, 1),
                        scrollData.returnInputDataForDataIdAndColumnNumber(endYDataSetId, 1));
                
            }
            else throw new Exception("The input data file is missing one of the following data set id's: " + startXDataSetId + "|" + startYDataSetId + "|" + endXDataSetId + "|" + endYDataSetId);
           
        }
        catch(Exception e) { throw e; }
        
    }
 
    /**
     * Scroll to top in the drop down
     *
     * @param className
     * @param elements
     *
     * @throws Exception
     */
    public void scrollToTopInDropDown(String className, List<WebElement> elements) throws Exception {
        
        logger.info("Perform scroll to top into drop down");
   
        commandList.addToList("scrollToTopInDropDown|" + className + "|" + elements);

        try {

            // get text of all present elements
            List<String> options = new ArrayList<String>();
            
            for(int i = 0; i < elements.size(); i++) options.add(elements.get(i).getText().trim());

            // scroll down
            scroll(elements.get(0).getLocation().getX(),
                    elements.get(0).getLocation().getY(),
                    elements.get(elements.size() - 1).getLocation().getX(),
                    elements.get(elements.size() - 1).getLocation().getY());

            // get text of all elements after scroll down
            List<WebElement> newElements = this.driver.findElements(By.className(className));
            
            List<String> newOptions = new ArrayList<String>();
            
            for(int i = 0; i < newElements.size(); i++) newOptions.add(newElements.get(i).getText().trim());

            options.removeAll(newOptions);

            if(options.size() > 0) scrollToTopInDropDown(className, newElements);
            
        } 
        catch(Exception e) { throw e; }
        
    }

    /**
     * Scroll down one step into drop down
     *
     * @param className
     * @param elements
     *
     * @return true in case of successful scroll false in case if scroll ends
     *
     * @throws Exception
     */
    public boolean scrollNextInDropDown(String className, List<WebElement> elements) throws Exception {
        
        logger.info("Perform scroll one step into drop down");
 
        commandList.addToList("scrollNextInDropDown|" + className + "|" + elements);

        try {

            List<String> options = new ArrayList<String>();
            
            for(int i = 0; i < elements.size(); i++) options.add(elements.get(i).getText().trim());
            
            scroll(elements.get(elements.size() - 1).getLocation().getX(),
                    elements.get(elements.size() - 1).getLocation().getY(),
                    elements.get(0).getLocation().getX(),
                    elements.get(0).getLocation().getY());

            List<WebElement> newElements = this.driver.findElements(By.className(className));

            List<String> newOptions = new ArrayList<String>();
            
            for(int i = 0; i < newElements.size(); i++) newOptions.add(newElements.get(i).getText().trim());
            
            options.removeAll(newOptions);

            if(options.size() > 0) return true;
            
            return false;

        } 
        catch(Exception e) { throw e; }
    }

    /**
     * Search for the given text into the drop down options by scrolling up-down
     * 
     * @param className
     * @param text
     * 
     * @return
     * 
     * @throws Exception 
     */
    public WebElement searchByScrollingInDropDown(String className, String text) throws Exception {
        
        logger.info("Search for the element of type: " + className + " conatains text: " + text + " by scrolling into the drop down");
    
        commandList.addToList("searchByScrollingInDropDown:" + text + "|" + className);

        WebElement webElement = null;

        try {

            // Scroll and check if the required text is present
            do {
                
                List<WebElement> elements = this.driver.findElements(By.className(className));

		try { webElement = getWebElementAtLocationByClassNameAndValueAttributeValueEquals(className, text); }
                catch (Exception e) { }

                if(webElement != null) break;

                if(!scrollNextInDropDown(className, elements)) break;
                
            } while(webElement == null);

            return webElement;

        } 
        catch (Exception e) { throw e; }
        
    }

    /**
     * Search for the given text into the drop down options and select the same
     * if found
     *
     * @param text
     * @param className
     *
     * @throws Exception
     */
    public void searchAndSelectTextFromDropDown(String text, String className) throws Exception {

        WebElement webElement = null;

        try {

            logger.info("Search for the element of type: " + className + " conatains text: " + text + " appears in the drop down and select the same");

            commandList.addToList("searchAndSelectTextFromDropDown:" + text + "|" + className);

            // fetch all visible options found in drop down
            List<WebElement> elements = this.driver.findElements(By.className(className));

	    try { webElement = getWebElementAtLocationByClassNameAndValueAttributeValueEquals(className, text); }
            catch (Exception e) { }

            // If option with required text is not present then try to scroll and check
            if(webElement == null && elements.size() > 0) {
                
                scrollToTopInDropDown(className, elements);
                
                webElement = searchByScrollingInDropDown(className, text);
            
            }

            // Select the option if found in dropdown
            if(webElement != null) {
                
                logger.info("text to be select is " + webElement.getText().trim());
                
                webElement.click();
            
            }
            else {throw new Exception("Could not find text in dropdown"); }

        } 
        catch (Exception e) { throw e; }
        
    }
    
    /**
     * Background the app (like clicking on the Home button).
     * 
     * @param seconds
     * 
     * @throws Exception 
     */
    public void backgroundApp(int seconds) throws Exception { 
        
        try {
            
            logger.info("Starting run app in background thread");
            
            (new RunAppInBackgroundThread(this.driver, seconds)).start(); 
            
            logger.info("Run app in background thread running");
            
            delay(5000);
        
        }
        catch(Exception e) { throw new BaseScreenException(e); }
    
    }
    
    /**
     * Background the app (like clicking on the Home button).
     * 
     * @param seconds
     * 
     * @throws Exception 
     */
    public void backgroundApp(String seconds) throws Exception { 
        
        try { backgroundApp((new Integer(seconds)).intValue()); }
        catch(Exception e) { throw new BaseScreenException(e); }
    
    }
    
    /**
     * Click on Settings Icon
     * 
     * @throws Exception 
     */
    public void clickOnSettingsIcon() throws Exception { 
        
        try {
            
            clickOnWebElementMatchingText("Settings", "XCUIElementTypeIcon");
        
            delay(2000);
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Click on Settings -> Wi-Fi
     * 
     * @throws Exception 
     */
    public void clickOnWiFiSection() throws Exception { 
        
        try { 
            
            clickOnWebElementMatchingText("Wi-Fi", "XCUIElementTypeStaticText");
        
            delay(2000);
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Click on Settings -> Wi-Fi
     */
    public void clickOnSettingsSection_WiFi() throws Exception { 
        
        try {
            
            clickOnWebElementMatchingText("Wi-Fi", "XCUIElementTypeStaticText");
        
            delay(2000);
    
        }
        catch(Exception e) { throw e; }
    }
    
    /**
     * Click on the Wi-Fi toggle button
     * 
     * @throws Exception 
     */
    public void turnOnWiFi() throws Exception { 
        
        try {
            
            clickOnWebElementMatchingText("Wi-Fi", "XCUIElementTypeSwitch");
        
            delay(2000);
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Determine if wifi is enabled and turned on
     * 
     * @throws Exception 
     */
    public void isWiFiOn() throws Exception { 
        
        try {
            
            validateWebElementContainingText("Location accuracy is improved when Wi-Fi is turned on", "XCUIElementTypeOther");
        
            delay(2000);
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Click on the WiFi selected button
     * 
     * @param wifiName 
     * @throws Exception 
     */
    public void clickOnButtonForWiFiSelected(String wifiName) throws Exception { 
        
        try {
            
            clickOnWebElementMatchingText(wifiName, "XCUIElementTypeStaticText");
        
            delay(2000);
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Click on Forget This Network
     * 
     * @throws Exception 
     */
    public void clickOnForgetThisNetwork() throws Exception { 
        
        try { clickOnWebElementMatchingText("Forget This Network", "XCUIElementTypeStaticText"); }
        catch(Exception e) { throw new BaseScreenException(e); }
    
    }
    
    /**
     * Click on Forget This Network - Confirm
     * 
     * @throws Exception 
     */
    public void clickOnForgetThisNetworkConfirm() throws Exception { 
        
        try { clickOnWebElementMatchingText("Forget", "XCUIElementTypeButton"); }
        catch(Exception e) { throw new BaseScreenException(e); }
    
    }
    
    /**
     * Click on Forget This Network - Cancel
     * 
     * @throws Exception 
     */
    public void clickOnForgetThisNetworkCancel() throws Exception { 
        
        try { clickOnWebElementMatchingText("Cancel", "XCUIElementTypeButton"); }
        catch(Exception e) { throw new BaseScreenException(e); }
    
    }
    
    /**
     * Click on WiFi Name Screen - Back button
     * 
     * @throws Exception 
     */
    public void clickOnWiFiNameScreenBackButton() throws Exception { 
        
        try { clickOnWebElementMatchingText("Wi-Fi", "XCUIElementTypeButton"); }
        catch(Exception e) { throw new BaseScreenException(e); }
    
    }
    
    /**
     * Click on WiFi Screen - Back to Settings button
     * 
     * @throws Exception 
     */
    public void clickOnWiFiScreenBackToSettingsButton() throws Exception { 
        
        try { clickOnWebElementMatchingText("Settings", "XCUIElementTypeButton"); }
        catch(Exception e) { throw new BaseScreenException(e); }
    
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
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Print the element hierarchy.
     * 
     * @throws Exception 
     */
    public void printDOM() throws Exception {
        
        try { logger.error("DOM|" + getElementDOM()); }
        catch(Exception e) { logger.error("Not able to get element DOM"); }
        
    }
    
    /**
     * Open the URL in a browser.
     * 
     * @param url
     * 
     * @return
     * 
     * @throws Exception 
     */
    public void open(String url) throws Exception {
        
        try { if(this.driver != null) this.driver.get(url); }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Verify that an element contains a text value in the attribute of a class.
     * 
     * @param text
     * @param className
     * @param attributeName
     *
     * @throws Exception 
     */
    public void verifyContainingText(String text, String className, String attributeName) throws Exception {
        
        logger.info("verifyContainingText:" + text + "|" + className + "|" + attributeName);
            
        commandList.addToList("verifyContainingText:" + text + "|" + className + "|" + attributeName);
        
        try {
             
            List<WebElement> elements = this.driver.findElements(By.className(className));
            
            logger.debug("Elements found for classname: " + className + "|" + elements);
            logger.debug("Number of elements found for classname: " + className + "|" + elements.size());
            
            for(WebElement element:elements) {
                
                if(!element.isDisplayed() || !element.isEnabled()) continue;
               
                String data1 = element.getAttribute(attributeName);
                
                logger.debug("data1 value:" + data1);
                
                if(data1 == null) continue;
                
                String data = data1.trim();
               
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                logger.debug("Checking element text value: " + data);
                
                if(data.contains(text)) return;
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text + " from attribute: " + attributeName);
            
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Scroll across a region on the screen.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     * 
     * @throws Exception 
     */
    public void scrollAcrossRegion(int startX, int startY, int endX, int endY, int duration) throws Exception {
        
        logger.info("scrollAcrossRegion|" + startX + "|" + startY + "|" + endX + "|" + endY + "|" + duration);
            
        commandList.addToList("scrollAcrossRegion|" + startX + "|" + startY + "|" + endX + "|" + endY + "|" + duration);
        
        try { swipe(startX, startY, endX, endY, duration); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll up on the entire device screen height - middle
     * 
     * @throws Exception 
     */
    public void swipeUpDeviceScreen() throws Exception {
        
        logger.info("swipeUpDeviceScreen");
            
        commandList.addToList("swipeUpDeviceScreen");
        
        try { 
            
            Dimension size = driver.manage().window().getSize();
    
            int height = size.getHeight();
    
            int width = size.getWidth();
            
            logger.info("Screen size is: " + width + "|" + height);
            
            swipe(width/2, height - 50, width/2, -(height - 100), 1);
           
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll down on the entire device screen height - middle
     * 
     * @throws Exception 
     */
    public void swipeDownDeviceScreen() throws Exception {
        
        logger.info("swipeDownDeviceScreen");
            
        commandList.addToList("swipeDownDeviceScreen");
        
        try { 
            
            Dimension size = driver.manage().window().getSize();
    
            int height = size.getHeight();
    
            int width = size.getWidth();
            
            logger.info("Screen size is: " + width + "|" + height);
            
            swipe(width/2, 50, width/2, height - 100, 1);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll down.
     * 
     * @throws Exception 
     */
    public void scrollPullDown_Down() throws Exception {
        
        logger.info("scrollPullDown_Down");
            
        commandList.addToList("scrollPullDown_Down");
        
        try { scrollAcrossRegion(50, 500, 50, 50, 2); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll up.
     * 
     * @throws Exception 
     */
    public void scrollPullDown_Up() throws Exception {
        
        logger.info("scrollPullDown_Up");
            
        commandList.addToList("scrollPullDown_Down");
        
        try { scrollAcrossRegion(50, 50, 50, 500, 2); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll up on the entire device screen height - middle
     * 
     * @throws Exception 
     */
    public void swipeUpDeviceScreen_Left() throws Exception {
        
        logger.info("swipeUpDeviceScreen_Left");
            
        commandList.addToList("swipeUpDeviceScreen_Left");
        
        try { 
            
            Dimension size = driver.manage().window().getSize();
    
            int height = size.getHeight();
    
            int width = size.getWidth();
            
            logger.info("Screen size is: " + width + "|" + height);
            
            swipe(50, height - 50, 50, -(height - 100), 1);
           
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scroll down on the entire device screen height - middle
     * 
     * @throws Exception 
     */
    public void swipeDownDeviceScreen_Left() throws Exception {
        
        logger.info("swipeDownDeviceScreen_Left");
            
        commandList.addToList("swipeDownDeviceScreen_Left");
        
        try { 
            
            Dimension size = driver.manage().window().getSize();
    
            int height = size.getHeight();
    
            int width = size.getWidth();
            
            logger.info("Screen size is: " + width + "|" + height);
            
            swipe(50, 50, 50, height - 100, 1);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Stop the session.
     */
    protected void quit() { 
        
        logger.info("Closing webdriver");
            
        commandList.addToList("quit");
        
        try {
            
            if(this.driver != null) driver.quit(); 
            
            logger.info("Webdriver closed");
            
        }
        catch(Exception e) { }
        
    }

}