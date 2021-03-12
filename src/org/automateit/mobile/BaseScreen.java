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
 * @author mburnside@Automate It!
 **/

package org.automateit.mobile;
 
import java.net.URL;
import java.time.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import org.testng.Assert; 

import org.openqa.selenium.By; 
import org.openqa.selenium.WebElement; 
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.MobileElement;

import org.automateit.core.Capabilities;
import org.automateit.core.ViewBase;
import org.automateit.core.BooleanCapabilities;
import org.automateit.core.StringCapabilities;
import org.automateit.data.DataDrivenInput;
import org.automateit.core.CommonWebDriver;
 
/**
 * This class is the base class for all other screen classes to use.
 * 
 * It abstracts away all of the webdriver-specific programming and provides 
 * easy to use convenience methods for rapid development of mobile apps using
 * webdriver and the screen design pattern.
 * 
 * @author mburnside
 */
public class BaseScreen extends ViewBase {
    
    /**
     * The webdriver class to use
     */
    protected AppiumDriver<WebElement> driver = null;
    
    /**
     * The default timeout for logcat
     */
    private static final int DEFAULT_LINES_LOGCAT = 5000;
    
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(BaseScreen.class);
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     */
    public BaseScreen() { init(); }
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @param init
     */
    public BaseScreen(boolean init) { if(init) init(); }
       
    /**
     * Copy Constructor. Each screen class must use this constructor.
     * 
     * @param baseScreen
     */
    public BaseScreen(BaseScreen baseScreen) { inheritSession(baseScreen); }
    
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
     */
    protected void inheritSession(BaseScreen baseScreen) { this.driver = baseScreen.getWebDriver(); }
    
    /**
     * Setup the web driver to use for this set of tests.
     */
    public void init() { 
        
        if(!loggingSetup) setupLogging();
        
        if(hasBeenInitialized) {
            
            logger.debug("Returning from setup - already has been initialized");
            
            return;
            
        }
       
        try { createNewWebDriver(); }
        catch(Exception e) { 
        
            logger.error(e.getMessage());
        
            if(this.driver != null) quit();
           
            createNewWebDriver();
            
        }
      
    } 
    
    /**
     * Finish the setup after capabilities have been added
     */
    protected void finishSetup() {
        
        createNewWebDriver();
            
        this.driver.manage().timeouts().implicitlyWait((new Long(properties.get("timeout"))).longValue(), TimeUnit.SECONDS);
       
    }
    
    /**
     * Reset the setup (force new web driver session and object to drive the UI).
     * 
     * Use this method to re-enter the app in a logged-out state. Gives more control
     * when starting tests.
     */
    protected void resetSetup() {
        
        this.hasBeenInitialized = false;
        
        init();
       
    }
    
    /**
     * Stop the session.
     */
    public void stopSession() { 
        
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
    public void stop() { stopSession(); }
  
    /**
     * Perform a swipe on the screen.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     */
    public void swipe(int startX, int startY, int endX, int endY, int duration) {
        
        logger.info("Attempting to perform swipe on screen at: start(" + startX + "," + startY + ") finish:(" + endX + "," + endY + ") for duration of: " + duration + " seconds");
            
        commandList.addToList("swipe: " + startX + "," + startY + "-" + endX + "," + endY);
        
        scroll(startX, startY, endX, endY, duration);
       
    }
    
    /**
     * Perform a swipe on the screen.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     */
    public void swipe(String startX, String startY, String endX, String endY, String duration) { swipe((new Integer(startX)).intValue(), (new Integer(startY)).intValue(), (new Integer(endX)).intValue(), (new Integer(endY)).intValue(), (new Integer(duration)).intValue()); }
        
    /**
     * Perform a tap/click on the screen.
     * 
     * @param x
     * @param y
     */
    public void tap(int x, int y) {
        
        logger.info("Attempting to perform tap on screen at: " + x + "," + y);
            
        commandList.addToList("tap: " + x + "," + y);
        
        TouchAction touchAction = new TouchAction(this.driver);
            
        touchAction.tap(PointOption.point(x, y)).perform();
       
    }
    
    /**
     * Perform a tap/click on the screen.
     * 
     * @param x
     * @param y
     */
    public void tap(String x, String y) { tap((new Integer(x)).intValue(), (new Integer(y)).intValue()); }
       
    /**
     * Perform a tap/click on the screen.
     * 
     * @param webelement
     */
    public void tap(WebElement webelement) {
        
        logger.info("Attempting to perform tap on screen at web element: " + webelement);
            
        commandList.addToList("tap: " + webelement);
        
        TouchAction touchAction = new TouchAction(this.driver);
           
        touchAction.tap(PointOption.point(webelement.getLocation().getX(), webelement.getLocation().getY())).perform();
        
    }
    
    /**
     * Perform a tap/click on the screen.
     * 
     * @param locator xpath/css locator
     */
    public void tap(String locator) throws Exception {
        
        logger.info("Attempting to perform tap on screen at web element at location: " + locator);
            
        commandList.addToList("tap: " + locator);
        
        TouchAction touchAction = new TouchAction(this.driver);
            
        WebElement webelement = getWebElementByXPath(locator);
           
        touchAction.tap(PointOption.point(webelement.getLocation().getX(), webelement.getLocation().getY())).perform();
         
    }
    
    /**
     * Perform a scroll with duration of 3 seconds.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void scroll(int startX, int startY, int endX, int endY) { scroll(startX, startY, endX, endY, 3); }
    
    /**
     * Perform a scroll with duration of 3 seconds.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void scroll(String startX, String startY, String endX, String endY) { scroll((new Integer(startX)).intValue(), (new Integer(startY)).intValue(), (new Integer(endX)).intValue(), (new Integer(endY)).intValue(), (new Integer(3)).intValue()); }
    
    /**
     * Perform a scroll.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     */
    public void scroll(int startX, int startY, int endX, int endY, int duration) {
        
        logger.info("Attempting to perform scroll on mobile screen; start: " + startX + ":" + startY + ", end: " + endX + ":" + endY + ", duration: " + duration);
            
        commandList.addToList("scroll: start: " + startX + ":" + startY + ", end: " + endX + ":" + endY + ", duration: " + duration);
        
        TouchAction touchAction = new TouchAction(this.driver);
           
        touchAction.press(PointOption.point(startX, startY)).waitAction((new WaitOptions()).withDuration(Duration.ofSeconds(duration))).moveTo(PointOption.point(endX, endY)).release().perform(); 
       
    }
    
    /**
     * Perform a scroll.
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param duration
     */
    public void scroll(String startX, String startY, String endX, String endY, String duration) { scroll((new Integer(startX)).intValue(), (new Integer(startY)).intValue(), (new Integer(endX)).intValue(), (new Integer(endY)).intValue(), (new Integer(duration)).intValue()); }
    
    /**
     * Scroll down one entire screen length.
     */
    public void scrollDown() {
        
        logger.info("Scroll Down");
            
        commandList.addToList("Scroll Down");
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
 
        HashMap<String, String> scrollObject = new HashMap<String, String>();

        scrollObject.put("direction", "down");
   
        js.executeScript("mobile: scroll", scrollObject);

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
             
            List<WebElement> elements = findElements(By.className(className));
            
            for(WebElement element:elements) {
                
                if(!element.isDisplayed() || !element.isEnabled()) continue;
               
                String data1 = element.getAttribute("name");
               
                if(data1 == null) continue;
                
                String data = data1.trim();
               
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                logger.info("Checking element text value: " + data + "|" + data.contains(text));
                
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
        
        logger.info("clickOnWebElementContainingText:" + text + "|" + className);
            
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
        
        logger.info("clickOnSecondWebElementContainingText:" + text + "|" + className);
            
        commandList.addToList("clickOnSecondWebElementContainingText:" + text + "|" + className);
        
        boolean foundFirstOccurrance = false;
        
        try {
             
            List<WebElement> elements = findElements(By.className(className));
            
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
    public void clickOnWebElementContainingText(String text1, String text2, String className) throws Exception {
        
        logger.info("clickOnWebElementContainingText:" + text1 + "|" + text2 + "|" + className);
            
        commandList.addToList("clickOnWebElementContainingText:" + text1 + "|" + text2 + "|" + className);
       
        try {
            
            List<WebElement> elements = findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data + "|" + text1 + "|" + text2);
                   
                if(data.contains(text1) || data.contains(text2)) {
                   
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
    public void clickOnWebElementMatchingText(String text, String className) throws Exception {
        
        logger.info("clickOnWebElementMatchingText:" + text + "|" + className);
        
        commandList.addToList("clickOnWebElementMatchingText:" + text + "|" + className);
        
        try { clickOnWebElementMatchingText(text, className, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Click on a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void clickOnWebElementMatchingText(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("clickOnWebElementMatchingText:" + text + "|" + className + "|" + ignoreCase);
        
        commandList.addToList("clickOnWebElementMatchingText(ignoreCase):" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name");
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                
                if(ignoreCase) {
                    
                    if(data.toLowerCase().trim().equals(text.trim().toLowerCase())) {
                    
                        element.click();
                    
                        delay(2000);
                        
                        return;
                
                    }
                    
                }
                else {
                    
                    if(data.trim().equals(text.trim())) {
                    
                        element.click();
                    
                        delay(2000);
                        
                        return;
                
                    }
                    
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
    public void clickOnWebElementMatchingText(String text1, String text2, String className) throws Exception {
        
        logger.info("clickOnWebElementMatchingText:" + text1 + "|" + text2 + "|" + className);
        
        commandList.addToList("clickOnWebElementMatchingText:" + text1 + "|" + text2 + "|" + className);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name");
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                
                if(data.trim().equals(text1.trim()) || data.trim().equals(text2.trim())) {
                    
                    element.click();
                    
                    delay(2000);
                        
                    return;
                
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find text in any screen element matching type: " + className + " and text: " + text1 + "|" + text2);
            
            
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
        
        try { validateWebElementContainingText(text, className, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text. - ignore case
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void validateWebElementContainingText(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingText(ignoreCase):" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for (WebElement element:elements) {
               
                if(element == null) continue;
                
                String data1 = element.getAttribute("name");
                
                if((data1 == null) || (data1.trim().length() == 0)) continue; // blank, so ignore
                
                String data = data1.trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data + "|"+ text);
                   
                if(ignoreCase) { if(data.toLowerCase().contains(text.toLowerCase())) return; }
                else { if(data.contains(text)) return; }
                
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
        
        try { validateWebElementMatchingText(text, className, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element matching the type and matching the text.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void validateWebElementMatchingText(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("Validating that element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingText(ignoreCase):" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = this.driver.findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getAttribute("name").trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(ignoreCase) { if(data.toLowerCase().equals(text.toLowerCase())) return; }
                else { if(data.equals(text)) return; }
                
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
        
        try { validateWebElementContainingTextValueAttribute(text, className, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void validateWebElementContainingTextValueAttribute(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingTextValueAttribute(ignoreCase):" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                if(element == null) continue;
                
                String data1 = element.getText();
                
                if(data1 == null) continue;
                
                String data = data1.trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute:" + text + "|" + data);
                 
                if(ignoreCase) { if(data.toLowerCase().contains(text.toLowerCase())) return; }
                else { if(data.contains(text)) return; }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element containing type: " + className + " and text: " + text);
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void validateWebElementContainingText_ContentDescription(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingText_ContentDescription:" + text + "|" + className);
        
        try { validateWebElementContainingText_ContentDescription(text, className, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void validateWebElementContainingText_ContentDescription(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementContainingText_ContentDescription:" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                if(element == null) continue;
                
                String data1 = element.getAttribute("contentDescription");
                
                if(data1 == null) continue;
                
                String data = data1.trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(ignoreCase) { if(data.toLowerCase().contains(text.toLowerCase())) return; }
                else { if(data.contains(text)) return; }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text + " in the Content Description attribute");
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * 
     * @throws Exception 
     */
    public void validateWebElementMatchingText_ContentDescription(String text, String className) throws Exception {
        
        logger.info("Validating that element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingText_ContentDescription:" + text + "|" + className);
        
        try { validateWebElementMatchingText_ContentDescription(text, className, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void validateWebElementMatchingText_ContentDescription(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("Validating that element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingText_ContentDescription:" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                if(element == null) continue;
                
                String data1 = element.getAttribute("contentDescription");
                
                if(data1 == null) continue;
                
                String data = data1.trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(ignoreCase) { if(data.toLowerCase().equals(text.toLowerCase())) return; }
                else { if(data.contains(text)) return; }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not validate a screen component with text in any screen element matching type: " + className + " and text: " + text + " in the Content Description attribute");
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * 
     * @returns
     * 
     * @throws Exception 
     */
    public WebElement getWebElementContainingText_ContentDescription(String text, String className) throws Exception {
            
        commandList.addToList("getWebElementContainingText_ContentDescription:" + text + "|" + className );
        
        return getWebElementContainingText_ContentDescription(text, className, false);
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * 
     * @returns
     * 
     * @throws Exception 
     */
    public WebElement getWebElementMatchingText_ContentDescription(String text, String className) throws Exception {
            
        commandList.addToList("getWebElementMatchingText_ContentDescription:" + text + "|" + className );
        
        return getWebElementMatchingText_ContentDescription(text, className, false);
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * 
     * @returns
     * 
     * @throws Exception 
     */
    public void clickOnWebElementContainingText_ContentDescription(String text, String className) throws Exception {
            
        commandList.addToList("clickOnWebElementContainingText_ContentDescription:" + text + "|" + className );
        
        getWebElementContainingText_ContentDescription(text, className, false).click();
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     *  
     * @throws Exception 
     */
    public void clickOnWebElementContainingText_ContentDescription(String text, String className, boolean ignoreCase) throws Exception {
            
        commandList.addToList("clickOnWebElementContainingText_ContentDescription:" + text + "|" + className  + "|" + ignoreCase);
        
        getWebElementContainingText_ContentDescription(text, className, ignoreCase).click();
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * 
     * @returns
     * 
     * @throws Exception 
     */
    public void clickOnWebElementMatchingText_ContentDescription(String text, String className) throws Exception {
            
        commandList.addToList("clickOnWebElementMatchingText_ContentDescription:" + text + "|" + className );
        
        getWebElementMatchingText_ContentDescription(text, className, false).click();
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     *  
     * @throws Exception 
     */
    public void clickOnWebElementMatchingText_ContentDescription(String text, String className, boolean ignoreCase) throws Exception {
            
        commandList.addToList("clickOnWebElementMatchingText_ContentDescription:" + text + "|" + className  + "|" + ignoreCase);
        
        getWebElementMatchingText_ContentDescription(text, className, ignoreCase).click();
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public WebElement getWebElementContainingText_ContentDescription(String text, String className, boolean ignoreCase) throws Exception {
            
        commandList.addToList("getWebElementContainingText_ContentDescription:" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                if(element == null) continue;
                
                String data1 = element.getAttribute("contentDescription");
                
                if(data1 == null) continue;
                
                String data = data1.trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(ignoreCase) { if(data.toLowerCase().contains(text.toLowerCase())) return element; }
                else { if(data.contains(text)) return element; }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find a screen component with text in any screen element matching type: " + className + " and text: " + text + " in the Content Description attribute");
                   
        }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text - the Content Description attribute.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public WebElement getWebElementMatchingText_ContentDescription(String text, String className, boolean ignoreCase) throws Exception {
            
        commandList.addToList("getWebElementMatchingText_ContentDescription:" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                if(element == null) continue;
                
                String data1 = element.getAttribute("contentDescription");
                
                if(data1 == null) continue;
                
                String data = data1.trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(ignoreCase) { 
                    
                    if(data.toLowerCase().equals(text.toLowerCase())) {
                        
                        return element;
                    
                    } 
                    
                }
                else { 
                    
                    if(data.equals(text)) {
        
                        return element;
                
                    }
                    
                }
                
            }
            
            // if we get here, we could not find the element so throw an exception
            throw new Exception("Could not find a screen component with text in any screen element matching type: " + className + " and text: " + text + " in the Content Description attribute");
                   
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
        
        try { validateWebElementMatchingTextValueAttribute(text, className, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void validateWebElementMatchingTextValueAttribute(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("Validating that element of type: " + className + " contains text: " + text + " appears on the screen somewhere.");
            
        commandList.addToList("validateWebElementMatchingTextValueAttribute(ignoreCase):" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
           
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(ignoreCase) { if(data.toLowerCase().equals(text.toLowerCase())) return; }
                else { if(data.equals(text)) return; }
                
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
        
        try { validateElementWithResourceIdContainingText(resourceId, expectedText, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element at resource id containing the text expected.
     * 
     * @param resourceId
     * @param expectedText
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void validateElementWithResourceIdContainingText(String resourceId, String expectedText, boolean ignoreCase) throws Exception {
        
        logger.info("Validate that there is a web element at resource id containing the text expected: " + resourceId + "|" + expectedText + "|" + ignoreCase);
            
        commandList.addToList("validateElementWithResourceIdContainingText(ignoreCase):" + resourceId + "|" + expectedText + "|" + ignoreCase);
        
        try { 
            
            if(resourceId == null) throw new BaseScreenException("Validate that there is a web element at resource id containing the text expected because resource id is null: " + resourceId);
            
            if(expectedText == null) throw new BaseScreenException("Validate that there is a web element at resource id containing the text expected because expected text is null: " + expectedText);
            
            String realValue = getValueAtWebElementWithResourceId(resourceId);
            
            logger.info("Text value of resource id: " + resourceId + "|" + realValue + "|" + expectedText);
            
            if(ignoreCase) { Assert.assertTrue(realValue.trim().toLowerCase().contains(expectedText.trim().toLowerCase())); }
            else { Assert.assertTrue(realValue.trim().contains(expectedText.trim())); }
                 
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
        
        try { validateElementWithResourceIdMatchingText(resourceId, expectedText, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element at resource id matching the text expected.
     * 
     * @param resourceId
     * @param expectedText
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void validateElementWithResourceIdMatchingText(String resourceId, String expectedText, boolean ignoreCase) throws Exception {
        
        logger.info("Validate that there is a web element at resource id matching the text expected: " + resourceId + "|" + expectedText + "|" + ignoreCase);
            
        commandList.addToList("validateElementWithResourceIdMatchingText:" + resourceId + "|" + expectedText + "|" + ignoreCase);
        
        try { 
            
            if(resourceId == null) throw new BaseScreenException("Validate that there is a web element at resource id matching the text expected because resource id is null: " + resourceId);
            
            if(expectedText == null) throw new BaseScreenException("Validate that there is a web element at resource id matching the text expected because expected text is null: " + expectedText);
            
            String realValue = getValueAtWebElementWithResourceId(resourceId);
            
            logger.info("Text value of resource id: " + resourceId + "|" + realValue + "|" + expectedText);
            
            if(ignoreCase) { Assert.assertTrue(realValue.trim().toLowerCase().contains(expectedText.trim().toLowerCase())); }
            else { Assert.assertTrue(realValue.trim().contains(expectedText.trim())); }
                 
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
        
        try { clickOnWebElementContainingTextValueAttribute(text, className, false); }
        catch(Exception e) { printDOM(); throw e; }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     *
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void clickOnWebElementContainingTextValueAttribute(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("Clicking on web element of type: " + className + " containing text: " + text + " appears on the screen somewhere.");
        
        commandList.addToList("clickOnWebElementContainingTextValueAttribute(ignoreCase):" + text + "|" + className + "|" + ignoreCase);
        
        try {
            
            List<WebElement> elements = findElements(By.className(className));
          
            for (WebElement element:elements) {
                
                if(!element.isDisplayed()) continue;
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                  
                if(ignoreCase) { 
                
                    if(data.toLowerCase().contains(text.toLowerCase())) {
                    
                        element.click();
                    
                        delay(3000);
                    
                        return;
                    
                    };
                    
                }
                else {
                    
                    if(data.contains(text)) {
                    
                        element.click();
                    
                        delay(3000);
                    
                        return;
                    
                    };
                    
                }
                
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
        
        try { clickOnWebElementMatchingTextValueAttribute(text, className, false); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }
    
    /**
     * Validate that there is a web element matching the type and containing the text.
     * 
     * @param text
     * @param className
     * @param ignoreCase
     * 
     * @throws Exception 
     */
    public void clickOnWebElementMatchingTextValueAttribute(String text, String className, boolean ignoreCase) throws Exception {
        
        logger.info("Clicking on web element of type: " + className + " matches text: " + text + " appears on the screen somewhere.");
        
        commandList.addToList("clickOnWebElementMatchingTextValueAttribute(ignoreCase):" + text + "|" + className + "|" + ignoreCase);
        
        try {
          
            List<WebElement> elements = findElements(By.className(className));
            
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                
                logger.info("Checking text value of attribute: " + data);
                   
                if(ignoreCase) { 
                
                    if(data.toLowerCase().equals(text.toLowerCase())) {
                    
                        element.click();
                    
                        delay(3000);
                    
                        return;
                    
                    };
                    
                }
                else {
                    
                    if(data.equals(text)) {
                    
                        element.click();
                    
                        delay(3000);
                    
                        return;
                    
                    };
                    
                }
                
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
            
            WebElement element = find(By.name(name));
            
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
            
            List<WebElement> elements = findElements(By.className(className));
            
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
            
            List<WebElement> elements = findElements(By.className(className));
            
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
           
            List<WebElement> elements = findElements(By.className(className));
            
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
            
            List<WebElement> elements = findElements(By.className(className));
            
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
            
            List<WebElement> elements = findElements(By.className(className));
           
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
            
            List<WebElement> elements = findElements(By.className(elementType));
           
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
            
            List<WebElement> elements = findElements(By.className(elementType));    
            
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
            
            List<WebElement> elements = findElements(By.className(elementType));    
            
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
        
        try { return find(By.xpath(xpath)); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
    }

    /**
     * Get a list of web elements using xpath.
     * 
     * @param xpath 
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected List<WebElement> getWebElementsByXPath(String xpath) throws Exception {

        try { return findElements(By.xpath(xpath)); }

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
        
        try { return find(By.name(name)); }
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
    protected List<WebElement> getWebElementsByClassName(String name) throws Exception {
        
        try { return findElements(By.className(name)); }
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
            
            List<WebElement> elements = findElements(By.className(className));    
            
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
            
            List<WebElement> elements = findElements(By.className(className));    
            
            for(WebElement element:elements) {
                
                String data = element.getText().trim();
                
                //logger.info("Data found1: " + value + "|" + data);
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.trim().contains(value.trim())) return element;
                
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
            
            List<WebElement> elements = findElements(By.className(className));    
            
            for (WebElement element:elements) {
                
                String data = element.getText().trim();
                
                if((data == null) || (data.trim().length() == 0)) continue; // blank, so ignore
                   
                if(data.trim().equals(value.trim())) return element;
                
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
            
            delay(2000); // the reason for the DELAYS is because the OS may not respond to super-fast back clicks - found issues during unit testing.
        
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
            
            String commandPrefix = "";
            
            String androidHome = System.getenv("ANDROID_HOME");
        
            if(androidHome != null) commandPrefix = androidHome + File.separator + "platform-tools" + File.separator;
            
            Runtime.getRuntime().exec(commandPrefix + "adb shell input keyevent 66"); 
            
            delay(2000); 
        
        }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement.
     * 
     * @param element
     * 
     * @throws BaseScreenException
     */
    public void clearWebElement(WebElement element) throws BaseScreenException { 
        
        logger.debug("Clear web element");
        
        commandList.addToList("clearWebElement");
        
        try { element.clear(); }
        catch(Exception e) { 
            
            if(properties.isIOS()) throw new BaseScreenException(e); 
            else { //android nexus 6 dictionary)
                
                try { clickOnWebElementContainingTextValueAttribute("DELETE", "android.widget.TextView"); }
                catch(Exception le) { throw new BaseScreenException(le); }
            
            }
            
        }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement looked up using xpath.
     * 
     * @param locator
     * 
     * @throws BaseScreenException
     */
    public void clearWebElement(String locator) throws BaseScreenException { 
        
        logger.debug("Clear web element: " + locator);
        
        commandList.addToList("clearWebElement:" + locator);
    
        try { clearWebElement(getWebElementByXPath(locator)); }
        catch(Exception e) { throw new BaseScreenException(e); }
        
    }
    
    /**
     * Clears/erases data/text in a WebElement looked up using the resource id.
     * 
     * @param resourceId
     * 
     * @throws Exception 
     */
    public void clearWebElementByResourceId(String resourceId) throws Exception { 
        
        logger.debug("Clear web element by resource id: " + resourceId);
        
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
        
        logger.debug("Clear web element by value and classname: " + value + "|" + classname);
        
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
          
            List<WebElement> elements = findElements(By.className(elementType));
            
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
        
        try { return find(By.id(resourceId)); }
        catch(Exception e) { printDOM(); throw new BaseScreenException(e); }
        
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
     * Click on the web element using the name of the name of the element
     * 
     * @param name
     * 
     * @throws Exception 
     */
    public void clickUsingElementName(String name) throws Exception {
        
        logger.info("Clicking on web element with name: " + name);
             
        commandList.addToList("clickUsingElementName:" + name);
        
        try { getWebElementByName(name).click(); }
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
            
            if(properties.isAndroid()) webElement.click();
            
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
            
            if(properties.isAndroid()) webElement.click();
            
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
        if(properties.isAndroid()) {
            
            try { driver.hideKeyboard(); }
            catch(Exception e) { }
            
        }
        else if(properties.isIOS()) {
            
            try { clickUsingResourceId("Done"); }    
            catch(Exception e2) { }
            
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
            
            find(By.xpath(locator));
            
            return true;
        
        }
        catch(Exception e) { return false; }
        
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
   
        commandList.addToList("scrollToTopInDropDown|" + className);

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
            List<WebElement> newElements = findElements(By.className(className));
            
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
 
        commandList.addToList("scrollNextInDropDown|" + className);

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
                
                List<WebElement> elements = findElements(By.className(className));

		try { webElement = getWebElementAtLocationByClassNameAndValueAttributeValue(className, text); }
                catch (Exception e) { }

                if(webElement != null) break;

                if(!scrollNextInDropDown(className, elements)) break;
                
            } while(webElement == null);

            return webElement;

        } 
        catch (Exception e) { throw e; }
        
    }

    /**
     * Get coordinates of given element
     *
     * @param className
     * @param text
     *
     * @throws Exception
     */
    public Point getCoordinates(String className, String text) throws Exception {

        WebElement webElement = null;

        try {

                try { webElement = getWebElementAtLocationByClassNameAndValueAttributeValueEquals(className, text); }
                catch (Exception e) { }

                return webElement.getLocation();

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

	    try { webElement = getWebElementAtLocationByClassNameAndValueAttributeValue(className, text); }
            catch (Exception e) { }

            // If option with required text is not present then try to scroll and check
            if(webElement == null && elements.size() > 0) {
                
                scrollToTopInDropDown(className, elements);
                
                webElement = searchByScrollingInDropDown(className, text);
            
            }

            // Select the option if found in dropdown
            if(webElement != null) {
                
                logger.info("Text From drop down list to be chosen: " + webElement.getText().trim());
                
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
     * Get the entire element hierarchy.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getElementDOM() throws Exception {
        
        try { return driver.getPageSource(); }
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
        
        try { if(driver != null) driver.get(url); }
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
             
            List<WebElement> elements = findElements(By.className(className));
            
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
    public void quit() { 
        
        logger.info("Closing webdriver");
            
        commandList.addToList("quit");
        
        try {
            
            if(this.driver != null) driver.quit(); 
            
            logger.info("Webdriver closed");
            
        }
        catch(Exception e) { }
        
    }
    
    /**
     * Allow for setting the timeout manually (if needed)
     * 
     * @param timeout
     * 
     * @throws Exception 
     */
    public void setTimeout(String timeout) throws Exception {
        
        try { 
            
            logger.info("The timeout value is set to (seconds): " + timeout);
            driver.manage().timeouts().implicitlyWait((new Long(timeout)).longValue(), TimeUnit.SECONDS);
        
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
     * This method does the actual creation of the web driver (appium driver)
     */
    protected void createNewWebDriver() {
        
        // we need to get the noreset and reinstallapp properties before they get potentially overwritten
        // by the next load of properties
        boolean noReset = false;
        
        boolean reInstallApp = true;
        
        if(properties.getForceAppResetInstall()) {
          
            noReset = (new Boolean(properties.get(BooleanCapabilities.NO_RESET.getCapability()))).booleanValue();
        
            reInstallApp = (new Boolean(properties.get(BooleanCapabilities.REINSTALL_APP.getCapability()))).booleanValue();
          
        }
       
        try {
            
            if(properties.isAndroid()) {
            
                String androidPropertiesFilePath = "./conf/app.android.csv";
            
                if(properties.getModule() != null) androidPropertiesFilePath = "./" + properties.getModule() + "/" +  androidPropertiesFilePath;
            
                logger.info("Adding Android app specific properties:" + androidPropertiesFilePath);
            
                properties.addProperties(androidPropertiesFilePath);
            
            }
            
            if(properties.isIOS()) {
            
                String iosPropertiesFilePath = "./conf/app.ios.csv";
            
                if(properties.getModule() != null) iosPropertiesFilePath = "./" + properties.getModule() + "/" +  iosPropertiesFilePath;
            
                logger.info("Adding iOS app specific properties:" + iosPropertiesFilePath);
            
                properties.addProperties(iosPropertiesFilePath);
            
            }
            
            commandList.addToList("createNewWebDriver: " + properties.get(StringCapabilities.DEVICE.getCapability()));
            
            logger.info("Preparing to create a new web driver instance for device type: " + properties.get(StringCapabilities.DEVICE.getCapability()));
            logger.info("Preparing to create a new web driver instance at URL: " + properties.get(StringCapabilities.URL.getCapability()));
            
            
            
            Capabilities capabilities = new Capabilities();
            
            DesiredCapabilities desiredCapabilities = capabilities.get();
            
            // now we override the noReset and reInstallApp
            if(properties.getForceAppResetInstall()) {
            
                desiredCapabilities.setCapability(BooleanCapabilities.NO_RESET.getCapability(), noReset);
            
                desiredCapabilities.setCapability(BooleanCapabilities.REINSTALL_APP.getCapability(), reInstallApp);
                
            }
            
            if(properties.isAndroid()) this.driver = new AndroidDriver(new URL(properties.get(StringCapabilities.URL.getCapability())), desiredCapabilities);
            else this.driver = new IOSDriver(new URL(properties.get(StringCapabilities.URL.getCapability())), desiredCapabilities);
            
            setTimeout();
            
            setWebDriver(this.driver);
            
            hasBeenInitialized = true;
            
            logger.info("Successfully made new web driver instance, now set the timeout value: " + properties.get("timeout"));
            this.driver.manage().timeouts().implicitlyWait((new Long(properties.get("timeout"))).longValue(), TimeUnit.SECONDS);
          
            logger.info("New webdriver created successfully: " + this.driver);
            
        }
        catch(Exception e) { logger.error(e); }
        
    }
    
    /**
     * Dial a phone number on the device.
     * 
     * @param number
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String dialPhoneNumber(String number) throws Exception {
        
        try { 
            
            if(properties.isAndroid()) return utils.executeShellCommand("adb shell service call phone 2 s16 " + "+" + number); 
            else return null;
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Answer an active (ringing) phone call on the device.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String answerActivePhoneCall() throws Exception {
        
        try { 
            
            if(properties.isAndroid()) return utils.executeShellCommand("adb shell input keyevent 5"); 
            else return null;
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get most recent call info in the call logs on the device.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getMostRecentCallInfo() throws Exception {
        
        try { 
            
            if(properties.isAndroid()) return utils.executeShellCommand("adb shell sqlite3 /data/data/com.android.providers.contacts/databases/contacts2.db \"SELECT * FROM calls order by _id desc limit 1;\""); 
            else return null;
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get most recent call info in the call logs on the device.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getAllCallInfo() throws Exception {
        
        try { 
            
            if(properties.isAndroid()) return utils.executeShellCommand("adb shell sqlite3 /data/data/com.android.providers.contacts/databases/contacts2.db \"select * from calls;\""); 
            else return null;
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get logcat data from last N number of lines in the log
     * 
     * @param deviceId
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getLogcatData(String deviceId) throws Exception {
        
        try { return getLogcatData(deviceId, DEFAULT_LINES_LOGCAT); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get logcat data from last N number of lines in the log
     * 
     * @param deviceId
     * @param lines
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getLogcatData(String deviceId, int lines) throws Exception {
        
        try { 
            
            if(properties.isAndroid()) return utils.executeShellCommand("adb -s " + deviceId + " logcat -t " + String.valueOf(lines)); 
            else return null;
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Verify text in logcat data
     * 
     * @param deviceId
     * @param expectedText
     * @param lines
     * 
     * @return
     * 
     * @throws Exception 
     */
    public boolean verifyLogcatData(String deviceId, int lines, String expectedText) throws Exception {
        
        try { return getLogcatData(deviceId, lines).contains(expectedText); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Verify text in logcat data - default lines checked is 5000
     * 
     * @param deviceId
     * @param expectedText
     * 
     * @return
     * 
     * @throws Exception 
     */
    public boolean verifyLogcatData(String deviceId, String expectedText) throws Exception {
        
        try { return verifyLogcatData(deviceId, DEFAULT_LINES_LOGCAT, expectedText); }
        catch(Exception e) { throw e; }
        
    }
    
    public void androidClickHome() throws Exception { ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME)); }
    
    /**
     * Verify that the Notification with expected text in thew title has been received.
     * 
     * Note: There are random times where the notification pulldown does not always
     * stay visible (auto-retracts), so there is an attempt to open the Messages area
     * of the OS and search there.
     * 
     * @param messageText
     * 
     * @throws Exception 
     */
    public void validateNotificationReceived(String messageText) throws Exception {
        
        commandList.addToList("validateNotificationReceived|" + messageText);
        
        if(isAndroid()) {
            
            androidClickHome();
            
            ((AndroidDriver) driver).openNotifications();
            
            delay(5000);
   
            List<WebElement> allnotifications = this.driver.findElements(By.id("android:id/title"));
   
            logger.info("Number of notifications: " + allnotifications.size());
 
            for(WebElement webElement : allnotifications) {
       
                logger.info("Notification text to search: " + webElement.getText());
       
                if(((MobileElement)webElement).getText().contains(messageText)) return;
          
            }
            
            printDOM();
            
            validateMessageReceived(messageText);
        
        }
        else throw new BaseScreenException("Verifications of Notifications received on iOS not yet supported");
        
    }
    
    /**
     * Verify that the Messages with expected text in the title has been received.
     * 
     * @param messageText
     * 
     * @throws Exception 
     */
    public void validateMessageReceived(String messageText) throws Exception {
        
        commandList.addToList("validateMessageReceived|" + messageText);
        
        if(isAndroid()) {
            
            androidClickHome();
            
            try { clickOnWebElementContainingTextValueAttribute("Apps", "android.widget.TextView"); }
            catch(Exception le) { }
            
            delay(1000);
            
            addScreenshotToReport();
            
            clickOnWebElementContainingTextValueAttribute("Messages", "android.widget.TextView");
   
            delay(2000);
            
            addScreenshotToReport();
      
            clickOnWebElementContainingTextValueAttribute(messageText, "android.widget.TextView");
            
            delay(2000);
            
            addScreenshotToReport();
            
            try { clickOnWebElementContainingTextValueAttribute("CANCEL", "android.widget.Button"); }
            catch(Exception le) { }
            
            addScreenshotToReport();
           
        }

        else throw new BaseScreenException("Verifications of Notification received on iOS not yet supported");
        
    }
    
    /**
     * Delete the Messages with expected text in the title has been received.
     * 
     * @param messageText
     * 
     * @throws Exception 
     */
    public void deleteMessage(String messageText) throws Exception {
        
        commandList.addToList("validateMessageReceived|" + messageText);
        
        if(isAndroid()) {
            
            clickOnWebElementContainingText_ContentDescription("More options",  "android.widget.Button");
            
            delay(2000);
            
            addScreenshotToReport();
            
            clickOnWebElementContainingText_ContentDescription("Delete",  "android.widget.TextView");
            
            delay(2000);
            
            addScreenshotToReport();
            
            try { 
                
                clickOnWebElementContainingTextValueAttribute("All",  "android.widget.TextView");  
                
                delay(2000); 
                
                addScreenshotToReport();
                
                clickOnWebElementContainingText_ContentDescription("DELETE",  "android.widget.Button");
                
                delay(2000);
                
                addScreenshotToReport();
            
            }
            catch(Exception le) { }
            
            clickOnWebElementContainingTextValueAttribute("DELETE", "android.widget.Button");
            
            delay(2000);
            
            addScreenshotToReport();
          
        }

        else throw new BaseScreenException("Verifications of Notification received on iOS not yet supported");
        
    }
    
    /**
     * Validate the Notification is received and return to Message Menu.
     * 
     * @param messageText
     * 
     * @throws Exception 
     */
    public void validateNotificationReceivedAndGoBack(String messageText) throws Exception {
        
        info("Validating notification message: " + messageText);
        
        try { validateMessageReceived(messageText); }
        catch(Exception e) { throw e; }
        finally {
            // go back twice to get to original Notifications/Message screen
            try { androidGoBack(); addScreenshotToReport(); androidGoBack(); addScreenshotToReport(); }
            catch(Exception le) { }
            
        }
        
    }
    
    /**
     * Validate the Notification is received and then delete it from the device.
     * 
     * @param messageText
     * 
     * @throws Exception 
     */
    public void validateNotificationReceivedAndDelete(String messageText) throws Exception {
           
        validateMessageReceived(messageText); 
            
        deleteMessage(messageText);
        
    }
    
    /**
     * Run the command on an android device OS terminal
     * 
     * @param command
     * 
     * @return The console output from the command
     * 
     * @throws Exception 
     */
    public String runCommandOnAndroidDevice(String command) throws Exception {
        
        int MAX_ATTEMPTS = 5;
        
        info("Running command on android device terminal prompt: " + command);
        
        logger.info("Running command on android device terminal prompt: " + command);
        
        Map<String, Object> args = new HashMap<>();
        
        // the command string comes in as a space-separated string exactly as a user would enter in a command line terminal prompt.
        String[] exeCommand = command.split(" ");
        
        // first part of the string is the actual executable command, the remainder are parameters
        String commandString = exeCommand[0];
        
        List<String> params = new ArrayList<String>();
        
        // now add to the parameeters to send to the command.
        for(int i = 1; i < exeCommand.length; i++) params.add(i - 1, exeCommand[i]);

        args.put("command", commandString);

        args.put("args", params);

        // note mgb 12 July 2020: I am seeing where sometimes (not always) the execute script fails
        // to run. so , I am adding extra handling to make a few extra attempts before indicating fail
        // the command line exeuctor should run with no issues, but mobile devices can act
        // in a very unpredictable way.
        // 
        // trying a few extra times should limit the false-fails we see in the tests because of these
        // android OS errors
        for(int i = 0; i < MAX_ATTEMPTS; i++) {
            
            logger.info("Executing command: mobile: shell" + args);
            
            try { return this.driver.executeScript("mobile: shell", args).toString(); }
            catch(Exception e) { logger.error(e); delay(5000); } // wait 5 seconds and try again
            
        }
        
        throw new Exception("Unable to successfully run command after " + MAX_ATTEMPTS + " attempts: " + command);

    }
    
    /**
     * Enter text into the element with text that is contained in the Content Description attribute value.
     * 
     * @param text
     * @param contentDescriptionText
     * @param className
     * 
     * @throws Exception 
     */
    public void enterTextInWebElementContainingText_ContentDescription(String text, String contentDescriptionText, String className) throws Exception {
            
        commandList.addToList("enterTextInWebElementContainingText_ContentDescription:" + text + "|" + contentDescriptionText + "|" + className );
        
        WebElement webElement = getWebElementContainingText_ContentDescription(contentDescriptionText, className, false);
        
        webElement.click();
            
        delay(1000);
            
        webElement.sendKeys(text);
            
        minimizeKeyboard();
        
    }
    
    /**
     * Clear the text in the element with text that is contained in the Content Description attribute value
     * 
     * @param contentDescriptionText
     * @param className
     * 
     * @throws Exception 
     */
    public void clearTextInWebElementContainingText_ContentDescription(String contentDescriptionText, String className) throws Exception {
            
        commandList.addToList("clearTextInWebElementContainingText_ContentDescription:" + contentDescriptionText + "|" + className );
        
        clearWebElement(getWebElementContainingText_ContentDescription(contentDescriptionText, className));
        
    }
    
    /**
     * Enter text into the element with text that is matching in the Content Description attribute value.
     * 
     * @param text
     * @param contentDescriptionText
     * @param className
     * 
     * @throws Exception 
     */
    public void enterTextInWebElementMatchingText_ContentDescription(String text, String contentDescriptionText, String className) throws Exception {
            
        commandList.addToList("enterTextInWebElementMatchingText_ContentDescription:" + text + "|" + contentDescriptionText + "|" + className );
        
        WebElement webElement = getWebElementMatchingText_ContentDescription(contentDescriptionText, className, false);
        
        webElement.click();
            
        delay(1000);
            
        webElement.sendKeys(text);
            
        minimizeKeyboard();
        
    }
    
    /**
     * Clear the text in the element with text that is matching in the Content Description attribute value
     * 
     * @param contentDescriptionText
     * @param className
     * 
     * @throws Exception 
     */
    public void clearTextInWebElementMatchingText_ContentDescription(String contentDescriptionText, String className) throws Exception {
            
        commandList.addToList("clearTextInWebElementMatchingText_ContentDescription:" + contentDescriptionText + "|" + className );
        
        clearWebElement(getWebElementMatchingText_ContentDescription(contentDescriptionText, className));
        
    }
    
    /**
     * Android simulate a key press
     * 
     * mburnside: This method is necessary because on some android devices, previously existing method does not actually click the Enter key
     * 
     * @param key 
     */
    public void androidSendKey(AndroidKey key) { ((AndroidDriver)this.driver).pressKey(new KeyEvent(key)); }
    
    /**
     * Android simulate Enter key press on keyboard
     */
    public void androidPressEnterKey() { androidSendKey(AndroidKey.ENTER); }
    
    /**
     * Minimize/dismiss the keyboard from view.
     */
    public void androidMinimizeKeyboard() { ((AndroidDriver)driver).hideKeyboard(); }
    
    /**
     * Get the device model
     * 
     * @return 
     */
    public String getDeviceModel() { return ((AndroidDriver)driver).getCapabilities().getCapability("deviceModel").toString(); }
     
}