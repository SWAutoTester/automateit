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
 * You should have received a copy of thhe GNU General Public License
 * along with Automate It!.  If not, see <http://www.gnu.org/licenses/>.
 **/

package org.automateit.assetaccessibility;

import java.util.Date;

import org.apache.log4j.Logger;

import org.automateit.test.TestBase;
import org.automateit.data.DataDrivenInput;

/**
 * This abstract class is used by resource lock controllers implementations to be used
 * by this framework.
 */
public abstract class ResourceLockController extends TestBase { 
    
    /**
     * The logging class to use for useful information
     */
    protected Logger logger = Logger.getLogger(ResourceLockController.class);
    
    /**
     * The name and path of the configuration file default
     */
    public final static String CONFIGURATION_FILENAME_DEFAULT_LOCATION = "./data/resourcelock.csv";
    
    /**
     * The endpoint url key name
     */
    public final static String CONFIGURATION_KEY_ENDPOINT_URL = "endpoint_url";
    
    /**
     * The backend resource locking service endpoint url
     */
    protected String serviceLocationEndpointURL = null;
    
    /**
     * The configuration file to use. Default value is <code>CONFIGURATION_FILENAME_DEFAULT_LOCATION</code>
     */
    protected String configurationFile = CONFIGURATION_FILENAME_DEFAULT_LOCATION;
    
    /**
     * The key lock name key name
     */
    public final static String CONFIGURATION_KEY_LOCK_NAME = "lock_name";
    
    /**
     * The key lock name key name
     */
    public final static String CONFIGURATION_LOCK_NAME_DEFAULT_VALUE = "n8_etcd";
    
    /**
     * The lock name key name
     */
    protected String lockName = CONFIGURATION_LOCK_NAME_DEFAULT_VALUE;
    
    /**
     * The input data for this object which is used for configuring the execution properties
     */
    protected DataDrivenInput input = null;
    
    /**
     * The key lock name key name
     */
    public final static String CONFIGURATION_KEY_TTL = "ttl";
    
    /**
     * The time-to-life value for the lock. Default is 0.
     */
    protected long lockTtl = 0;
    
    /**
     * The lock name key name
     */
    protected String resourceName = null;
    
    /**
     * The thread wait time key name
     */
    public final static String CONFIGURATION_WAIT_TIME = "wait_time";
    
    /**
     * The status URI key name
     */
    public final static String CONFIGURATION_STATUS_URI = "/plugin/lockable-resources/api/json";
    
    /**
     * The status URI key name
     */
    protected String statusURI = CONFIGURATION_STATUS_URI;
    
    /**
     * The key lock name key name
     */
    public final static String CONFIGURATION_KEY_STATUS_URI = "status_uri";
    
    /**
     * The thread wait time
     */
    protected long waitTime = 5000;
    
    /**
     * Lets us know if an unlock resource request has been sent previously.
     */
    protected boolean unlockSent = false;
    
    /**
     * The wait time before giving up (0 seconds default)
     */
    protected long waitTimeOutTime = 0;
    
    /**
     * The wait time out key name
     */
    public final static String CONFIGURATION_WAIT_TIMEOUT = "wait_timeout";
    
    /**
     * The time that the object was instantiated
     */
    protected long startTime = 0;
    
    /**
     * The time that the object stops waiting for the resource to be unlocked.
     */
    protected long waitExpirationTime = 0;
    
    /**
     * Default Constructor.
     * 
     * This object will be constructed using the default values and properties.
     * 
     * @throws Exception 
     */
    public ResourceLockController() throws Exception { this(true); }
    
    /**
     * Copy Constructor.
     * 
     * This object will be constructed using the default values and properties
     * unless <code>init</code> is set to false, in which no setup will occur.
     * 
     * @param configurationFile
     * 
     * @throws Exception 
     */
    public ResourceLockController(String configurationFile) throws Exception {
        
        if(configurationFile != null) this.configurationFile = configurationFile;
       
        try { setup(); }
        catch(Exception e) { throw e; }
        
    } 
    
    /**
     * Copy Constructor. This is used if the client wants to identify a different 
     * lock name than what is configured by default.
     * 
     * This object will be constructed using the default values and properties
     * unless <code>init</code> is set to false, in which no setup will occur.
     * 
     * @param configurationFile
     * @param lockName
     * 
     * @throws Exception 
     */
    public ResourceLockController(String configurationFile, String lockName) throws Exception {
        
        if(configurationFile != null) this.configurationFile = configurationFile;
        if(lockName != null) this.lockName = lockName;
       
        try { setup(); }
        catch(Exception e) { throw e; }
        
    } 
    
    /**
     * Copy Constructor. This is used if the client wants to identify a different 
     * lock name than what is configured by default.
     * 
     * This object will be constructed using the default values and properties
     * unless <code>init</code> is set to false, in which no setup will occur.
     * 
     * @param configurationFile
     * @param lockName
     * @param resourceName
     * 
     * @throws Exception 
     */
    public ResourceLockController(String configurationFile, String lockName, String resourceName) throws Exception {
        
        if(configurationFile != null) this.configurationFile = configurationFile;
        if(lockName != null) this.lockName = lockName;
        this.resourceName = resourceName;
       
        try { setup(); }
        catch(Exception e) { throw e; }
        
    } 
    
    /**
     * Copy Constructor. This is used if the client wants to identify a different 
     * lock name than what is configured by default.
     * 
     * This object will be constructed using the default values and properties
     * unless <code>init</code> is set to false, in which no setup will occur.
     * 
     * @param useUniqueLockName Something that is not indicated in the configuration file
     * @param lockName The lock name to use
     * 
     * @throws Exception 
     */
    public ResourceLockController(boolean useUniqueLockName, String lockName) throws Exception {
        
        if(configurationFile != null) this.configurationFile = configurationFile;
        if(lockName != null) this.lockName = lockName;
       
        try { setup(); }
        catch(Exception e) { throw e; }
        
    } 
    
    /**
     * Copy Constructor. This is used if the client wants to identify a different 
     * lock name than what is configured by default.
     * 
     * This object will be constructed using the default values and properties
     * unless <code>init</code> is set to false, in which no setup will occur.
     * 
     * @param useUniqueLockName Something that is not indicated in the configuration file
     * @param lockName The lock name to use
     * @param resourceName
     * 
     * @throws Exception 
     */
    public ResourceLockController(boolean useUniqueLockName, String lockName, String resourceName) throws Exception {
        
        if(configurationFile != null) this.configurationFile = configurationFile;
        if(lockName != null) this.lockName = lockName;
        this.resourceName = resourceName;
       
        try { setup(); }
        catch(Exception e) { throw e; }
        
    } 
    
    /**
     * Conditional Constructor.
     * 
     * This object will be constructed using the default values and properties
     * unless <code>init</code> is set to false, in which no setup will occur.
     * 
     * @param init
     * 
     * @throws Exception 
     */
    public ResourceLockController(boolean init) throws Exception {
        
        if(!init) return;
        
        try { setup(); }
        catch(Exception e) { throw e; }
        
    } 
    
    /**
     * Setup this object for execution based of properties and values in the selected
     * configuration file.
     * 
     * @throws Exception 
     */
    private void setup() throws Exception {
        
        try {  
            
            logger.debug("Setting up Resource Lock configuration from file: " + configurationFile);
        
            input = setupDataDrivenInput(configurationFile);
            
            serviceLocationEndpointURL = getConfiguredValueForKey(CONFIGURATION_KEY_ENDPOINT_URL); 
            
            logger.debug("Resource Lock service endpoint URL: " + serviceLocationEndpointURL);
            
            // if lock name is not provided in the data file, the default value will be used
            try { lockName = getConfiguredValueForKey(CONFIGURATION_KEY_LOCK_NAME); }
            catch(Exception le) { }
            
            logger.debug("Lock name: " + lockName);
            
            // if ttl value is not provided in the data file, the default value will be used
            try { lockTtl = (new Long(getConfiguredValueForKey(CONFIGURATION_KEY_TTL))).longValue(); }
            catch(Exception e) { }
            
            logger.debug("Lock TTL: " + lockTtl);
            
            // set the thread wait time
            try { waitTime = (new Long(getConfiguredValueForKey(CONFIGURATION_WAIT_TIME))).longValue(); }
            catch(Exception e) { }
            
            logger.debug("Thread wait time: " + waitTime);
            
            // set the thread wait time out
            try { waitTimeOutTime = (new Long(getConfiguredValueForKey(CONFIGURATION_WAIT_TIMEOUT))).longValue(); }
            catch(Exception e) { }
            
            logger.debug("Wait time out: " + waitTimeOutTime);
            
            // set the status uri
            try { statusURI = getConfiguredValueForKey(CONFIGURATION_KEY_STATUS_URI); }
            catch(Exception e) { }
            
            logger.debug("Status URI: " + statusURI);
            
            startTime = (new Date()).getTime();
           
        }
        catch(Exception e) { throw e; }
         
    }
    
    /**
     * Set the configuration file.
     * 
     * @param configurationFile 
     */
    protected void setConfigurationFile(String configurationFile) { this.configurationFile = configurationFile; }
    
    /**
     * Set the backend resource locking endpoint URL.
     * 
     * @param serviceLocationEndpointURL 
     */
    protected void setServiceLocationEndpointURL(String serviceLocationEndpointURL) { this.serviceLocationEndpointURL = serviceLocationEndpointURL; }
    
    /**
     * Get a value from the configuration file.
     * 
     * @param keyName 
     * 
     * @return The value indicated in the configuration file for the <code>keyName</code>
     */
    protected String getConfiguredValueForKey(String keyName) throws Exception { 
        
        try { return input.returnInputDataForDataIdAndColumnNumber(keyName, 1); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Determine if the wait time has expired.
     * 
     * @return 
     */
    public boolean hasWaitTimeExpired() {
        
        logger.debug("Checking if wait time has expired: " + waitTimeOutTime);
         
         // wait time was never set, so it is not set to expire at any time in the future
         if(waitTimeOutTime == 0) return false;
         
         // since wait time out is greater than 0, then it is active, so check 
         // that the time at this exact monent is not greater than the wait time 
         // expiration set
         long now = (new Date()).getTime();
         
         boolean expired = now > waitExpirationTime;
         
         logger.debug("Checking wait time expiration current time: " + expired);
         
         if(expired) { logger.debug("Wait time has expired"); return true; }
         
         return false;
        
     }
    
    /**
     * Reset the wait expiration time
     */
    public void resetWaitTimeExpired() {
        
        if(waitTimeOutTime != 0) waitExpirationTime = startTime + waitTimeOutTime; 
            
        logger.debug("Wait expiration time: " + waitExpirationTime);
        
    }
    
    /**
     * Set the lock name.
     * 
     * @param lockName 
     */
    public void setLockName(String lockName) { this.lockName = lockName; }
    
    /**
     * Get the time-to-life value. If this is not specified in the configuration file
     * using the key name <code>ttl</code>, then it uses the default value (0)
     * 
     * @return 
     */
    public long getTTL() { return lockTtl; }
    
    /**
     * Get the thread wait time value. 
     * 
     * @return 
     */
    public long getWaitTime() { return waitTime; }
    
    /**
     * Get the wait time out value. 
     * 
     * @return 
     */
    public long getWaitTimeOut() { return waitTimeOutTime; }
    
    /**
     * Lock the resource
     * 
     * @throws Exception 
     */
    public abstract void lock() throws Exception;
    
    /**
     * Lock the resource
     * 
     * @param lockName
     * 
     * @throws Exception 
     */
    public abstract void lock(String lockName) throws Exception;
    
    /**
     * Unlock the resource.
     * 
     * @throws Exception 
     */
    public abstract void unlock() throws Exception;
    
    /**
     * Check if the resource is locked.
     * 
     * @return 
     */
    public abstract boolean isResourceLocked();
    
    /**
     * Check if the resource is locked.
     * 
     * @param objectId
     * 
     * @return 
     */
    public abstract boolean isResourceLocked(String objectId);

}

