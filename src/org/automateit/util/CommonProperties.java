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

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.automateit.core.StringCapabilities;
import org.automateit.core.BooleanCapabilities;
import org.automateit.data.DataDrivenInput;

/**
 * The common properties object. This class load properties files in java
 * standard files.
 * 
 * It also now can load properties by data from a data driven input object.
 * 
 * Warning: adding extra properties from other data sources other than the
 * master configuration file (./conf/configuration.properties)
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
     * The flow name keyname
     */
    private static final String FLOW_NAME = "flow_name";
    
    /**
     * The module we are in
     */
    private String module = null;
    
    /**
     * the properties file path
     */
    public static final String PROPERTIESFILEPATH = "./conf/automateit.properties";
    
    /**
     * The logging properties file path
     */
    public static final String LOGGINGFILEPATH = "./conf/log4j.properties";
    
    /**
     * The utility class for convenience methods
     */
    protected Utils utils = new Utils();
    
    /**
     * An exception that can be accessed from anywhere
     */
    public Exception exception = null;

    /**
     *  logging object
     */
    protected static Logger logger = Logger.getLogger(CommonProperties.class);
    
    /**
     * Indicator if the properties have been successfully loaded. This variable
     * is set to <code>true</code> if a properties file load has
     * occurred successfully.
     */
    private boolean hasbeenSuccessfullyLoaded = false;
    
    /**
     * Indicates whether logging has been setup.
     */
    private boolean hasLoggingBeenSetup = false;
    
    private boolean forceAppResetInstall = false;
   
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
        catch(Exception e) { logger.debug(e); }
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
     * Load the framework properties
     * 
     * @param filename The path to the file to use (relative or absolute path)
     *  
     * @throws Exception 
     */
    public void load(String filename) throws Exception {
        
        try { 
      
            if(hasbeenSuccessfullyLoaded) return;
            
            logger.info("Loading initial properties from file: " + filename);
          
            super.load(new FileInputStream(filename)); 
            
            hasbeenSuccessfullyLoaded = true;
            
            setupLogging();
            
            hasLoggingBeenSetup = true;
            
            logger.info("Loading initial properties from complete. The number of properties configured: " + this.size());
         
        }
        catch(Exception e) { logger.error(e); throw e; }
        
    }
    
    /**
     * Reload (forced) of the framework properties
     * 
     * @param filename The path to the file to use (relative or absolute path)
     *  
     * @throws Exception 
     */
    public void reload(String filename) throws Exception {
        
        this.hasbeenSuccessfullyLoaded = false; 
        
        load(filename);
        
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
        
        String propertiesFile = PROPERTIESFILEPATH;
        
        if(module != null) propertiesFile = "." + File.separator + module + File.separator + propertiesFile;
       
        try { load(propertiesFile); printAllProperties(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Initialize the CommonProperties object
     * 
     * @throws Exception 
     */
    public void resetCommonProperties() throws Exception {
        
        String propertiesFile = PROPERTIESFILEPATH;
        
        if(module != null) propertiesFile = "." + File.separator + module + File.separator + propertiesFile;
       
        try { reload(propertiesFile); printAllProperties(); }
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
    public void setAppNoReset(boolean noReset) { 
        
        forceAppResetInstall = true;
      
        setProperty(BooleanCapabilities.NO_RESET.getCapability(), String.valueOf(noReset)); 
    
    }
    
    /**
     * Set app not to be reset/reinstalled
     * 
     * @param noReset 
     */
    public void setAppNoReset(String noReset) { 
        
        forceAppResetInstall = true;
       
        setProperty(BooleanCapabilities.NO_RESET.getCapability(), noReset); 
    
    }
    
    /**
     * Get app not to be reset/reinstalled 
     * 
     * @return 
     */
    public boolean getAppNoReset() { 
        
        if(!containsKey(BooleanCapabilities.NO_RESET.getCapability())) return false;
        else return Boolean.valueOf(containsKey(BooleanCapabilities.NO_RESET.getCapability()));
    
    }
    
    /**
     * Set app not to be reset/reinstalled
     * 
     * @param reInstallApp 
     */
    public void setReInstallApp(boolean reInstallApp) { 
        
        forceAppResetInstall = true;
       
        setProperty(BooleanCapabilities.REINSTALL_APP.getCapability(), String.valueOf(reInstallApp)); 
    
    }
    
    /**
     * Set app not to be reset/reinstalled
     * 
     * @param reInstallApp 
     */
    public void setReInstallApp(String reInstallApp) { 
        
        forceAppResetInstall = true;
      
        setProperty(BooleanCapabilities.REINSTALL_APP.getCapability(), reInstallApp); 
    
    }
    
    /**
     * Get app not to be reset/reinstalled 
     */
    public boolean getReInstallApp() { 
       
        if(!containsKey(BooleanCapabilities.REINSTALL_APP.getCapability())) return false;
        else return Boolean.valueOf(get(BooleanCapabilities.REINSTALL_APP.getCapability()));
    
    }
    
    /**
     * Set the Target Environment we testing against
     * 
     * @param targetEnvironment 
     */
    public void setTargetEnvironment(String targetEnvironment) { setProperty(StringCapabilities.TARGET_ENVIRONMENT_KEY.getCapability(), targetEnvironment); }
    
    /**
     * Get the Target Environment we testing against
     * 
     * @return
     */
    public String getTargetEnvironment() { return get(StringCapabilities.TARGET_ENVIRONMENT_KEY.getCapability()); }
    
    /**
     * Set record video of tests
     * 
     * @param recordVideo
     */
    public void setRecordVideo(boolean recordVideo) { setProperty(BooleanCapabilities.RECORD_VIDEO.getCapability(), String.valueOf(recordVideo)); }
    
    /**
     * Get the setting for recording video of test execution
     */
    public boolean getRecordVideo() { 
        
        if(!containsKey(BooleanCapabilities.RECORD_VIDEO.getCapability())) return false;
        else return Boolean.valueOf(containsKey(BooleanCapabilities.RECORD_VIDEO.getCapability()));
    
    }
    
    /**
     * Get the setting for video recorder id of test execution
     * 
     * @return 
     */
    public String getVideoRecorderId() { return get(StringCapabilities.ID_VIDEO_RECORDER.getCapability()); }
    
    /**
     * Get the base URL from all of the configured properties.
     * 
     * @return
     */
    public String getURL() { return get(StringCapabilities.URL.getCapability()); }
    
    /**
     * Set the Product Identifier
     * 
     * @param productId
     */
    public void setProductId(String productId) { setProperty(StringCapabilities.ID_PRODUCT.getCapability(), productId); }
    
    /**
     * Get the Product Identifier.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getProductId() throws Exception { return get(StringCapabilities.ID_PRODUCT.getCapability()); }
      
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
        
        String loggingFile = LOGGINGFILEPATH;
        
        if(module != null) loggingFile = "." + File.separator + module + File.separator + loggingFile;
        
        try { setupLogging(loggingFile); }
        catch(Exception e) { throw e; }

    }
    
    /**
     * Setup logging with log4j.
     * 
     * @param log4jPropertiesFile
     * 
     * @throws Exception 
     */
    public void setupLogging(String log4jPropertiesFile) throws Exception {
        
        // we need to add the module path to the log4j properties file path if module is identified
        if((this.module != null) && !log4jPropertiesFile.contains(this.module)) log4jPropertiesFile = "." + File.separator + module + File.separator + log4jPropertiesFile;
        
        logger.info("Log4j Properties file to load: " + log4jPropertiesFile);
       
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
        
        String propertiesFile = PROPERTIESFILEPATH;
        
        if(module != null) propertiesFile = "." + File.separator + module + File.separator + propertiesFile;
        
        try { store(new PrintWriter(propertiesFile), "Saved from Automate It! common properties framework"); }
        catch(Exception e) { throw e; }
        
    }
   
    /**
     * Get the browser name.
     * 
     * @return
     */
    public String getBrowserName() {
       
        if(isBrowserChrome()) return "chrome";
        if(isBrowserFirefox()) return "firefox";
        if(isBrowserIE()) return "ie";
        if(isBrowserSafari()) return "safari";
        if(isBrowserOpera()) return "opera";
        
        return null;
        
    }
    
    /**
     * Set the Auto Accept Alert setting
     * 
     * @param autoAcceptAlerts
     */
    public void setAutoAcceptAlerts(boolean autoAcceptAlerts) { setProperty(BooleanCapabilities.AUTO_ACCEPT_ALERTS.getCapability(), String.valueOf(autoAcceptAlerts)); }
    
    /**
     * Get the Auto Accept Alert setting
     * 
     * @return 
     */
    public boolean getAutoAcceptAlerts() { 
        
        if(!containsKey(BooleanCapabilities.AUTO_ACCEPT_ALERTS.getCapability())) return false;
        else return Boolean.valueOf(get(BooleanCapabilities.AUTO_ACCEPT_ALERTS.getCapability()));
        
    }
    
    /**
     * Set the Auto Grant Permissions setting
     * 
     * @param autoGrantPermissions
     */
    public void setAutoGrantPermissions(boolean autoGrantPermissions) { setProperty(BooleanCapabilities.AUTO_GRANT_PERMISSIONS.getCapability(), String.valueOf(autoGrantPermissions)); }
    
    /**
     * Get the Auto Grant Permissions setting
     * 
     * @return
     */
    public boolean getAutoGrantPermissions() { 
        
        if(!containsKey(BooleanCapabilities.AUTO_GRANT_PERMISSIONS.getCapability())) return false;
        else return Boolean.valueOf(get(BooleanCapabilities.AUTO_GRANT_PERMISSIONS.getCapability()));
        
    }
    
    /**
     * Add to the properties from a data driven input
     * 
     * @param ddi
     * 
     * @throws Exception 
     */
    public void addProperties(DataDrivenInput ddi) throws Exception {
        
        List<String> dataSetIds = ddi.getDataIds();
        
        for(String dataSetId : dataSetIds) {
            
            logger.debug("Adding new Property to Common Properties: " + dataSetId + "|" + ddi.returnInputDataForDataIdAndColumnNumber(dataSetId, 1));
          
            setProperty(dataSetId, ddi.returnInputDataForDataIdAndColumnNumber(dataSetId, 1));
            
        }
        
    }

    /**
     * Add to the properties from a filename
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public void addProperties(String filename) throws Exception {
        
        logger.info("Attempt to add properties by file: " + filename);
       
        addProperties(utils.setupDataDrivenInput(filename));
       
    }
    
    /**
     * Get the data driven object representation of all of the properties
     */
    public DataDrivenInput getDataDrivenInput() throws Exception { return utils.getDataDrivenInputFromProperties(this); }
    
    /**
     * Get the device type
     * 
     * @return 
     */
    private String getDeviceType() { 
        
        String device = get(StringCapabilities.DEVICE.getCapability());
        
        logger.debug("Device Type: " + device);
        
        return device;
        
    }
    
    /**
     * Indicates this is an android device.
     * 
     * @return 
     */
    public boolean isAndroid() {
        
        String device = getDeviceType();
        
        if(device == null) return false;
        else return (device.trim().toLowerCase().contains("android"));
        
    }
    
    /**
     * Indicates this is an IOS device.
     * 
     * @return 
     */
    public boolean isIOS() {
        
        String device = getDeviceType();
        
        if(device == null) return false;
        else return (device.trim().toLowerCase().contains("ios"));
        
    }
    
    /**
     * Print all properties - useful for debug
     */
    public void printAllProperties() {
        
        Enumeration keys = this.keys();
        
        while(keys.hasMoreElements()) {
    
            String key = (String)keys.nextElement();
    
            String value = get(key);
    
            logger.debug("Configured Property/Value: " + key + "|" + value);

        }
    
    }
    
    /**
     * Set the flow name
     * 
     * @param flowName
     */
    public void setFlowName(String flowName) { setProperty(FLOW_NAME, flowName); }
    
    /**
     * Get the flow name
     * 
     * @return 
     */
    public String getFlowName() { 
        
        if(!containsKey(FLOW_NAME)) return null;
        else return get(FLOW_NAME);
        
    }
    
    /**
     * Set the module name
     * 
     * @param module
     */
    public void setModule(String module) { 
        
        this.module = module; 
        
        try { 
            
            // reset the logging state and reload the new properties per the module that is being used
            hasLoggingBeenSetup = false;
                    
            setupLogging(LOGGINGFILEPATH); 
        
        }
        catch(Exception e) { logger.error(e); }
        
    }
    
    /**
     * Get the module name
     * 
     * @return module
     */
    public String getModule() { return this.module; }
    
    /**
     * Set the Exception
     * 
     * @param exception
     */
    public void setException(Exception exception) { this.exception = exception; }
    
    /**
     * Get the flow name
     * 
     * @return 
     */
    public Exception getException() { return this.exception; }
    
    /**
     * Determine if the user has previously 
     * 
     * @return 
     */
    public boolean getForceAppResetInstall() { return this.forceAppResetInstall; }
    
    /**
     * Add to the framework properties
     * 
     * @param dataDrivenInput
     *  
     * @throws Exception 
     */
    public void load(DataDrivenInput dataDrivenInput) throws Exception {
        
        try { 
       
            logger.info("Loading properties from Data Driven Input");
          
            List<String> dataSetIds = dataDrivenInput.getDataIds();
            
            ListIterator<String> iterator = dataSetIds.listIterator();
            
            while(iterator.hasNext()) {
                
                String dataSetId = iterator.next();
                
                setProperty(dataSetId, dataDrivenInput.get(dataSetId, 1));
                
            }
         
        }
        catch(Exception e) { logger.error(e); throw e; }
        
    }
    
}