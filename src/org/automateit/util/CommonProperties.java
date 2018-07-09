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

import java.util.Properties;
import java.io.FileInputStream;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 * The common properties object. This class load properties files in java
 * standard files.
 *
 * @author mburnside
 */
public class CommonProperties extends Properties {
    
    /**
     * Indicates the browser is chrome
     */
    public static final int CHROME = 0;
    
    /**
     * Indicates the browser is firefox
     */
    public static final int FIREFOX = 1;
    
    /**
     * Indicates the browser is IE
     */
    public static final int IE = 2;
    
    /**
     * Indicates the browser is Safari
     */
    public static final int SAFARI = 3;
    
    /**
     * Indicates the browser is Opera
     */
    public static final int OPERA = 4;
    
    /**
     * The browser type. Default is CHOME.
     */
    private int browserType = 0;
    
    /**
     * The target environment we are testing against (Stage, Production, Development, Sandbox, whatever)
     */
    private String targetEnvironment = null;
    
    /**
     * The key name in value pair for product id
     */
    private final static String PRODUCT_ID_KEY = "productId";
    
    /**
     * the properties file path
     */
    public final String PROPERTIESFILEPATH = "./conf/configuration.properties";
   
    /**
     * The utility class for convenience methods
     */
    protected Utils utils = new Utils();

    /**
     *  logging object
     */
    protected static Logger logger = Logger.getLogger(CommonProperties.class);
    
    /**
     * Indicator if the properties have been successfully loaded. This variable
     * is set to <code>true</code> if a properties file load has
     * occured successfully.
     */
    private boolean hasbeenSuccessfullyLoaded = false;
    
    /**
     * Indicates whether logging has been setup.
     */
    private boolean hasLoggingBeenSetup = false;
    
    /**
     * Indicates whether or not to reset/reinstall the app
     */
    private boolean noReset = false;
    
    /**
     * Indicates whether to force a situation where the app in not reset/reinstalled, even
     * if configured to in the configuration file
     */
    private boolean forceNoReset = false;
    
    /**
     * A boolean that indicates if the test execution will be recorded
     */
    private boolean recordVideo = false;
    
    /**
     * Reference to this object
     * 
     * @return
     */
    private static CommonProperties instance = new CommonProperties();
    
    /**
     * Default Constructor
     */
    protected CommonProperties() { 
        
        try { if(!hasbeenSuccessfullyLoaded) initializeCommonProperties(); }
        catch(Exception e) { logger.error(e); }
    }
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static CommonProperties getInstance() { return instance; }
    
    /**
     * Indicator if the properties have been successfully loaded.
     * 
     * @return 
     */
    public boolean hasbeenSuccessfullyLoaded() { return hasbeenSuccessfullyLoaded; }
    
    /**
     * Get a properties object given a filename.
     * 
     * @param filename The path to the file to use (relative or absolute path)
     *  
     * @throws Exception 
     */
    public void load(String filename) throws Exception {
        
        try { 
         
            super.load(new FileInputStream(filename)); 
            
            hasbeenSuccessfullyLoaded = true;
            
            setupLogging();
            
            hasLoggingBeenSetup = true;
         
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get a property value for a given <code>key</code>.
     * 
     * @param key
     * 
     * @return A property value for a given key
     */
    public String get(String key) { return getProperty(key); }
    
    /**
     * Initialize the CommonProperties object
     * 
     * @throws Exception 
     */
    public void initializeCommonProperties() throws Exception {
        
        try { load(PROPERTIESFILEPATH); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Set the browser type.
     * 
     * @param browserType
     * 
     * @throws Exception 
     */
    public void setBrowserType(int browserType) throws Exception {
        
        try { this.browserType = browserType; }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Determine of we are testing using Chrome browser.
     * 
     * @return 
     */
    public boolean isBrowserChrome() { return (this.browserType == CHROME); }
    
    /**
     * Determine of we are testing using Firefox browser.
     * 
     * @return 
     */
    public boolean isBrowserFirefox() { return (this.browserType == FIREFOX); }
    
    /**
     * Determine of we are testing using IE browser.
     * 
     * @return 
     */
    public boolean isBrowserIE() { return (this.browserType == IE); }
    
    /**
     * Determine of we are testing using Safari browser.
     * 
     * @return 
     */
    public boolean isBrowserSafari() { return (this.browserType == SAFARI); }
    
    /**
     * Determine of we are testing using Opera browser.
     * 
     * @return 
     */
    public boolean isBrowserOpera() { return (this.browserType == OPERA); }
    
    /**
     * Set app not to be reset/reinstalled
     * 
     * @param noReset 
     */
    public void setAppNoReset(boolean noReset) { this.noReset = noReset; }
    
    /**
     * Get app not to be reset/reinstalled 
     */
    public boolean getAppNoReset() { return this.noReset; }
    
    /**
     * Force app not to be reset/reinstalled
     * 
     * @param noReset 
     */
    public void setForceAppNoReset(boolean forceNoReset) { this.forceNoReset = forceNoReset; }
    
    /**
     * Set the Target Environment we testing against
     * 
     * @param targetEnvironment 
     */
    public void setTargetEnvironment(String targetEnvironment) { this.targetEnvironment = targetEnvironment; }
    
    /**
     * Get Force app not to be reset/reinstalled 
     * 
     * @return
     */
    public boolean getForceAppNoReset() { return this.forceNoReset; }
    
    /**
     * Get the Target Environment we testing against
     * 
     * @return
     */
    public String getTargetEnvironment() { return this.targetEnvironment; }
    
    /**
     * Set record video of tests
     * 
     * @param recordVideo
     */
    public void setRecordVideo(boolean recordVideo) { this.recordVideo = recordVideo; }
    
    /**
     * Get the setting for recording video of test execution
     */
    public boolean getRecordVideo() { return this.recordVideo; }
    
    /**
     * Get the setting for video recorder id of test execution
     * 
     * @return 
     */
    public String getVideoRecorderId() { return get("videoRecorderId"); }
    
    /**
     * Get the base URL from all of the configured properties.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getBaseURL() throws Exception {
       
        try { return get("baseURL"); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Set the Product Identifier
     * 
     * @param productId
     */
    public void setProductId(String productId) { setProperty(PRODUCT_ID_KEY, productId); }
    
    /**
     * Get the Product Identifier.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getProductId() throws Exception {
       
        try { return get(PRODUCT_ID_KEY); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Indicates if logging has been setup.
     * 
     * @return 
     */
    public boolean hasLoggingBeenSetup() { return this.hasLoggingBeenSetup; }
    
    /**
     * Setup logging with log4j.
     * 
     * Default location id at <code>./conf/log4j.properties</code>
     * 
     * @throws Exception 
     */
    public void setupLogging() throws Exception {
        
        try { setupLogging("./conf/log4j.properties"); }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Setup logging with log4j.
     * 
     * @param log4jPropertiesFile
     * 
     * @throws Exception 
     */
    public void setupLogging(final String log4jPropertiesFile) throws Exception {
        
        try {
            
            if(!this.hasLoggingBeenSetup) {
                
                utils.setupLogging(log4jPropertiesFile);
            
                this.hasLoggingBeenSetup = true;
            
            }
            
        }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Save to configuration.properties file.
     * 
     * @throws Exception 
     */
    public void save() throws Exception {
        
        try { store(new PrintWriter(PROPERTIESFILEPATH), "Saved from Automate It! common properties framework"); }
        catch(Exception e) { throw e; }
        
    }
   
}