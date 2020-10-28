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
public enum BooleanCapabilities {
    
    REINSTALL_APP("reInstallApp"),
    FULL_RESET("fullReset"),
    NO_RESET("noReset"),
    AUTO_ACCEPT_ALERTS("autoAcceptAlerts"),
    AUTO_GRANT_PERMISSIONS("autoGrantPermissions"),
    SHOW_XCODE_LOG("showXcodeLog"),
    SCREENSHOT_ON_TEST_FAIL("setCaptureScreetShotOnFailure"),
    CAPTURE_NETWORK_TRAFFIC("captureNetworkTraffic"),
    CAPTURE_SELENIUM_COMMANDS("captureSeleniumCommands"),
    CAPTURE_SCREENSHOTS("doScreenshots"),
    REPORTING_TEST_SUCCESS("doReportingOnTestSuccess"),
    REPORTING_TEST_FAIL("doReportingOnTestFail"),
    CAPTURE_PAGE_LOAD_PERFORMANCE("capturePageLoadPerformance"),
    REPORT_USE_LOCAL_FILES("useLocalFilePathForReporting"),
    USE_FORCE_PAGE_LOAD_TIME("useForcePageLoadWaitTime"),
    ENABLE_EXTRA_WAIT_AJAX_COMPLETE("addExtraWaitTimeAfterAjaxComplete"),
    RECORD_VIDEO("recordVideo"),
    MAXIMIZE_BROWSER_WINDOW("maximizeBrowserWindow"),
    USE_HTTP_AUTH("useHTTPAuth"),
    USE_NEW_WEBDRIVER_AGENT("useNewWDA");
    
    /**
     * Logging class
     */
    private static Logger logger = Logger.getLogger(BooleanCapabilities.class);
 
    private String capability;
    
    BooleanCapabilities(String capability) { this.capability = capability; }
 
    public String getCapability() { return capability; }
    
    /**
     * Add all configured capabilities
     * 
     * @param capabilities
     * @param properties 
     */
    public static void addCapabilities(DesiredCapabilities capabilities, Properties properties) {
        
        for(BooleanCapabilities cap : BooleanCapabilities.values()) { addCapability(capabilities, properties, cap.getCapability()); }
    
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
            
            if(properties.get(capabilityName) == null) {
                
                logger.info("Desired Capabilities property was not found or set in the configuration and will not be used: " + capabilityName);
                
                return;
                
            }
            else {
                
                logger.info("Setting Desired Capabilities property: " + capabilityName + "|" + properties.get(capabilityName));
               
                capabilities.setCapability(capabilityName, (new Boolean(String.valueOf(properties.get(capabilityName)))).booleanValue());
                    
            }
            
        }
        catch(Exception e) { }
        
    }
    
}