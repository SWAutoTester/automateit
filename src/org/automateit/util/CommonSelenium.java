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

package org.automateit.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.openqa.selenium.WebDriver;

import org.automateit.core.StringCapabilities;

import org.automateit.data.DataDrivenInput;

/**
 * Common class that retrieves the Selenium implementation class configured
 * through dynamic properties.
 *
 * @author mburnside@Automate It!
 */

public class CommonSelenium {

    /**
     * The instance of this singleton class
     */
    private static CommonSelenium instance = new CommonSelenium();
    
    /**
     * Web Driver object
     */
    private WebDriver driver = null;
    
    /**
     *  logging object
     */
    protected static Logger logger = Logger.getLogger(CommonSelenium.class);
    
    /**
     * device information
     */
    private Map<String, Properties> deviceInformation = new HashMap<String, Properties>();
    
    /**
     * device information file
     */
    private Map<String, String> deviceInformationFiles = new HashMap<String, String>();
    
    /**
     * device information file list
     */
    private List<String> deviceInformationFileList = new ArrayList<String>();
    
    /**
     * The utilities object
     */
    private Utils utils = new Utils();
    
    /**
     * Default Constructor
     */
    protected CommonSelenium() {}
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static CommonSelenium getInstance() { return instance; }
    
    /**
     * Set the web driver object to be shared
     * 
     * @param driver 
     */
    public void setWebDriver(WebDriver driver) { this.driver = driver; }
    
    /**
     * Get the web driver object being used in this session
     * 
     * @return 
     */
    public WebDriver getWebDriver() { return this.driver; }
    
    /**
     * Close the web browser without needing page class.
     */
    public void closeWebBrowser() {
        
        CommandList.getInstance().addToList("quit");
        
        try { if(this.driver != null) this.driver.quit(); }
        catch(Exception e) { }
        
    }
    
    /**
     * Close the mobile app.
     * 
     * @throws Exception 
     */
    public void closeApp() throws Exception {
        
        try { closeWebBrowser(); }
        catch(Exception e) { }
        
    }
    
    /**
     * Add device information.
     * 
     * If the device info file <code>filename</code> does not contain the key "udid", then it is assumed
     * that the device info file is invalid and is not added to the list of devices.
     * 
     * @param filename 
     */
    public void addDeviceInformation(String filename) {
       
        try {
            
            if(filename.endsWith(".properties")) {
            
                Properties props = utils.loadProperties(filename);
            
                if(props.containsKey(StringCapabilities.UDID.getCapability())) {
            
                    String key = props.getProperty(StringCapabilities.UDID.getCapability());
                
                    CommandList.getInstance().addToList("addDeviceInformation:" + filename);
        
                    deviceInformation.put(key, props);
                
                    deviceInformationFiles.put(key, filename);
                
                    deviceInformationFileList.add(filename);
                    
                }
                
            }
            else {
                    
                 // then we try to add it as a data driven input file   
                 DataDrivenInput data = utils.setupDataDrivenInput(filename); 
                       
                 if(data.hasDataId(StringCapabilities.UDID.getCapability())) {
                        
                     String key = data.returnInputDataForDataIdAndColumnNumber(StringCapabilities.UDID.getCapability(), 1);
                     
                     deviceInformationFiles.put(key, filename);
                  
                     deviceInformationFileList.add(filename);   
                    
                 }
                     
            }
                
        }
        catch(Exception e) { logger.error("Unable to load properties for: " + filename); }
        
    }
    
    /**
     * Get the device information
     * 
     * @param udid
     * 
     * @return 
     */
    public Properties getDeviceInformation(String udid) { return deviceInformation.get(udid); }
    
    /**
     * Get the device information filepath
     * 
     * @param udid
     * 
     * @return 
     */
    public String getDeviceInformationFile(String udid) { return deviceInformationFiles.get(udid); }
    
    /**
     * Get the device information filepath
     * 
     * @param index
     * 
     * @return 
     */
    public String getDeviceInformationFile(int index) { return deviceInformationFileList.get(index); }
    
}