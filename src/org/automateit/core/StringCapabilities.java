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

import java.util.Properties;

import org.apache.log4j.Logger;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * This class contains constants used in Desired Capabilities for web driver and framework setting and initialization.
 * 
 * @author mburnside@Automate It!
 */
public enum StringCapabilities {
    
    DEVICE("device"),
    DEVICE_NAME("deviceName"),
    AUTOMATION_NAME("automationName"),
    VERSION("version"),
    PLATFORM_VERSION("platformVersion"),
    PLATFORM_NAME("platformName"),
    PLATFORM("platform"),
    BUNDLE_ID("bundleId"),
    UDID("udid"),
    URL("URL"),
    APP_PACKAGE("app-package"),
    APP_WAIT_PACKAGE("app-wait-package"),
    APP_ACTIVITY("app-activity"),
    APP_WAIT_ACTIVITY("app-wait-activity"),
    APP_PACKAGE_CAPS("appPackage"),
    APP_WAIT_PACKAGE_CAPS("appWaitPackage"),
    APP_ACTIVITY_CAPS("appActivity"),
    APP_WAIT_ACTIVITY_CAPS("appWaitActivity"),
    APP("app"),
    BROWSER_NAME("browserName"),
    REAL_DEVICE_LOGGER("realDeviceLogger"),
    XCODE_CONFIGURATION_FILE("xcodeConfigFile"),
    ID_SELENIUM_DRIVER("seleniumDriverId"),
    DIRECTORY_SCREENSHOT("screenshotsDirectory"),
    DIRECTORY_PERFORMANCE("performanceDataDirectory"),
    FORCE_PAGE_LOAD_WAIT_TIME("forcePageLoadWaitTime"),
    EXTRA_WAIT_AJAX_COMPLETE("extraWaitTimeAfterAjaxComplete"),
    ID_PRODUCT("productId"),
    TARGET_ENVIRONMENT_KEY("target_environment"),
    ID_VIDEO_RECORDER("videoRecorderId"),
    JS_LIBRARY("jsLibrary"),
    TIMEOUT("timeout"),
    YOUIENGINEAPPADDRESS("youiEngineAppAddress"),
    HTTP_AUTH_USERNAME("http.auth.username"),
    HTTP_AUTH_PASSWORD("http.auth.password"),
    NEW_COMMAND_TIMEOUT("newCommandTimeout");
    
    /**
     * Logging class
     */
    private static Logger logger = Logger.getLogger(StringCapabilities.class);
 
    private String capability = null;
   
    StringCapabilities(String capability) { this.capability = capability; }
 
    public String getCapability() { return capability; }
    
    /**
     * Add all configured capabilities
     * 
     * @param capabilities
     * @param properties 
     */
    public static void addCapabilities(DesiredCapabilities capabilities, Properties properties) {
        
        for(StringCapabilities cap : StringCapabilities.values()) { addCapability(capabilities, properties, cap.getCapability()); }
    
    }
    
    /**
     * Add a single capability
     * 
     * @param capabilities
     * @param properties
     * @param capabilityName 
     */
    public static void addCapability(DesiredCapabilities capabilities, Properties properties, String capabilityName) {
        
        try {
            
            logger.info("Checking to see if we add property value to Desired Capabilities: " + capabilityName + "|" + properties.get(capabilityName));
            
            if(properties.get(capabilityName) == null) {
                
                logger.info("Desired Capabilities property was not found or set in the configuration and will not be used: " + capabilityName);
                
                return;
                
            }
            else {
                
                logger.info("Setting Desired Capabilities property: " + capabilityName + "|" + properties.get(capabilityName));
               
                capabilities.setCapability(capabilityName, properties.get(capabilityName));
                    
            }
            
        }
        catch(Exception e) { }
        
    }
    
}