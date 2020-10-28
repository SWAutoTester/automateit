package org.automateit.assetaccessibility;

import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonArray;

import org.apache.log4j.Logger;

import org.automateit.util.Utils;

/**
 * This is an implementation class for the Jenkins-based resource locking mechanism
 */
public class JenkinsResourceLockController extends ResourceLockController implements AssetFinder {
    
    /**
     * The path to the lock URI key name
     */
    public final static String KEYNAME_URI_PATH_LOCK = "lock_uri";
    
    /**
     * The path to the lock URI key name
     */
    public final static String KEYNAME_URI_PATH_UNLOCK = "unlock_uri";
    
    /**
     * The URI path root
     */
    public final static String URI_PATH_ROOT = "/lockable-resources";
    
    /**
     * The path to the lock URI
     */
    public final static String DEFAULT_URI_PATH_LOCK = URI_PATH_ROOT + "/reserve?resource=";
    
    /**
     * The path to the lock URI
     */
    public final static String DEFAULT_URI_PATH_UNLOCK = URI_PATH_ROOT + "/unreserve?resource=";
    
    /**
     * The active lock URI
     */
    protected String lockURI = DEFAULT_URI_PATH_LOCK;
    
    /**
     * The active lock URI
     */
    protected String unlockURI = DEFAULT_URI_PATH_UNLOCK;
    
    /**
     * The default wait time for checking in (ms)
     */
    public static final long WAIT_TIME_MS = 5000;
    
    /**
     * The utilities object
     */
    protected Utils utils = new Utils();
    
    private boolean resourceSuccessfullyLocked = false;
    
    /**
     * The logging class to use for useful information
     */
    protected Logger logger = Logger.getLogger(JenkinsResourceLockController.class);
   
    /**
     * Default Constructor.
     */
    public JenkinsResourceLockController() throws Exception { super(); }
    
    /**
     * Copy Constructor.
     * 
     * @param configurationFile
     */
    public JenkinsResourceLockController(String configurationFile) throws Exception { super(configurationFile); }
    
    /**
     * Copy Constructor.
     * 
     * @param configurationFile
     * @param lockName
     */
    public JenkinsResourceLockController(String configurationFile, String lockName) throws Exception { super(configurationFile, lockName); }
    
    /**
     * Copy Constructor.
     * 
     * @param useUniqueLockName Something that is not indicated in the configuration file
     * @param lockName The lock name to use
     */
    public JenkinsResourceLockController(boolean useUniqueLockName, String lockName) throws Exception { super(useUniqueLockName, lockName); }
    
    /**
     * Copy Constructor.
     * 
     * @param configurationFile
     * @param lockName
     * @param resourceName
     */
    public JenkinsResourceLockController(String configurationFile, String lockName, String resourceName) throws Exception { super(configurationFile, lockName, resourceName); }
    
    /**
     * Copy Constructor.
     * 
     * @param useUniqueLockName Something that is not indicated in the configuration file
     * @param lockName The lock name to use
     * @param resourceName The resource name to be found locked in the service response HTML
     */
    public JenkinsResourceLockController(boolean useUniqueLockName, String lockName, String resourceName) throws Exception { super(useUniqueLockName, lockName, resourceName); }
      
    /**
     * Lock the resource
     * 
     * @throws Exception 
     */
    public void lock() throws Exception {
        
        try { executeLock(lockName); }
        catch(Exception e) { logger.info(e); throw e; }
            
    }
    
    /**
     * Lock the resource
     * 
     * @param slockName
     * @throws Exception 
     */
    public void lock(String slockName) throws Exception {
        
        String previousLockName = lockName;
        
        String newLockName = slockName + previousLockName;
        
        try { 
            
            executeLock(newLockName); 
           
            logger.info("New lock name is: " + lockName);
        
        }
        catch(Exception e) { throw e; }
            
    }
    
    /**
     * Lock the resource
     * 
     * @param nameToLock
     * 
     * @throws Exception 
     */
    public void executeLock(String nameToLock) throws Exception {
        
        String originalLockName = lockName;
        
        try {
            
            resetWaitTimeExpired();
            
            waitForResourceFree();
            
            String[] expectedTextInResponse = { getVerifyResourceCountText(0) };
           
            try { lockURI = getConfiguredValueForKey(KEYNAME_URI_PATH_LOCK); }
            catch(Exception le) { lockURI = DEFAULT_URI_PATH_LOCK; } 
            
            String url = serviceLocationEndpointURL + lockURI + nameToLock;
            
            logger.info("Attempting lock at: " + url);
            logger.info("Resource name: " + this.resourceName);
            
            if(this.resourceName != null) {
                
                utils.verifyResponseFromURL(url, expectedTextInResponse);
                
                resourceSuccessfullyLocked = true;
            
                setLockName(nameToLock);
                
                logger.info("Resource is locked: " + nameToLock);
                
            }
            else {
                
                utils.accessURL(url);
                
                resourceSuccessfullyLocked = true;
                
                logger.info("Resource is locked: " + nameToLock);
            
                setLockName(nameToLock);
                
            }
            
            if(!resourceSuccessfullyLocked && !isResourceLocked()) throw new Exception("Unable to lock resource: " + nameToLock);
           
        }
        catch(java.io.FileNotFoundException fnfe) { if(!resourceSuccessfullyLocked) throw fnfe; } // suppress these types of errors
        catch(Exception e) { if(!resourceSuccessfullyLocked) setLockName(originalLockName); logger.info(e); throw e; }
        
    }
    
    /**
     * Unlock the resource.
     * 
     * @throws Exception 
     */
    public void unlock() throws Exception {
        
        try {
            
            resetWaitTimeExpired();
            
            if(unlockSent) return;
            
            String[] expectedTextInResponse = { getVerifyResourceCountText(1) };
            
            try { unlockURI = getConfiguredValueForKey(KEYNAME_URI_PATH_UNLOCK); }
            catch(Exception le) { unlockURI = DEFAULT_URI_PATH_UNLOCK; } 
            
            String url = serviceLocationEndpointURL + unlockURI + lockName;
            
            logger.info("Attempting unlock at: " + url);
            
            if(this.resourceName != null) utils.verifyResponseFromURL(url, expectedTextInResponse);
            else utils.accessURL(url);
            
            logger.info("Resource is unlocked: " + lockName);
            
            unlockSent = true;
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Verify that the resource had the expected count after the lock/unlock operation.
     * 
     * For lock, the expected count is 0.
     * For unlock, the expected count 1.
     * 
     * @param expectedCount
     * @throws Exception 
     */
    private String getVerifyResourceCountText(int expectedCount) throws Exception {
        
        try {
            
            if(resourceName == null) return null;
            
            if(expectedCount == 0) return resourceName + "</td><td class=\"pane\" style=\"color: red;\">" + String.valueOf(expectedCount) + "</td></tr>";
            if(expectedCount >= 1) return resourceName + "</td><td class=\"pane\" style=\"color: darkorange;\">" + String.valueOf(expectedCount) + "</td></tr>";
            else return null;
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Check if the resource is locked.
     * 
     * @return 
     */
    public boolean isResourceLocked() {
        
        if(lockName == null) return false;
        
        logger.info("Checking for resource locked: " + lockName);
        
        try {
            
            JsonArray results = utils.getJSONResult(serviceLocationEndpointURL + statusURI, "resources");
            
            logger.info("Jenkins has " + results.size() + " lockable resources");
            
            for(JsonObject result : results.getValuesAs(JsonObject.class)) {
                
                String name = result.getString("name", "");
               
                if(lockName.trim().equals(name)) {
                   
                    // check if the reserved status is true
                    boolean lockStatus = (new Boolean(result.getBoolean("reserved"))).booleanValue();
                   
                    return lockStatus;
                
                }
          
            }
            
            // if we get here, then we didnt find the resource name in the list of JSON name attributes    
            return false;
            
        }
        catch(Exception e) { logger.info(e); logger.info("Resource " + lockName + " is currently unlocked"); return false; }
        
    }
    
    /**
     * Check if the resource is locked.
     * 
     * @return 
     */
    public boolean isResourceLocked(String objectId) {
        
        if(lockName == null) return false;
        
        logger.info("Checking for resource locked: " + objectId);
        
        try {
            
            JsonArray results = utils.getJSONResult(serviceLocationEndpointURL + statusURI, "resources");
            
            logger.info("Jenkins has " + results.size() + " lockable resources");
            
            for(JsonObject result : results.getValuesAs(JsonObject.class)) {
                
                String name = result.getString("name", "");
               
                if(objectId.trim().equals(name)) {
                   
                    // check if the reserved status is true
                    boolean lockStatus = (new Boolean(result.getBoolean("reserved"))).booleanValue();
                   
                    return lockStatus;
                
                }
          
            }
            
            // if we get here, then we didnt find the resource name in the list of JSON name attributes    
            return false;
            
        }
        catch(Exception e) { logger.info(e); logger.info("Resource " + objectId + " is currently unlocked"); return false; }
        
    }
    
    /**
     * Wait on hold until the resource is free
     */
    public void waitForResourceFree() throws Exception {
        
        try { 
            
            while(isResourceLocked()) { 
            
                logger.info("Resource " + lockName + " is currently locked by another asset, waiting to retry execute lock after resource is available again");
                System.out.println("Resource " + lockName + " is currently locked by another asset, waiting to retry execute lock after resource is available again");
                
                try { Thread.sleep(getWaitTime()); }
                catch(Exception e) { }
                
                if(hasWaitTimeExpired()) throw new Exception("Wait time for resource available has expired: " + lockName);
            
            }
           
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean find(String objectId) { 
        
        if(objectId == null) return false;
        
        logger.info("Checking for resource in Jenkins list of lockable resources: " + objectId);
        
        try {
            
            JsonArray results = utils.getJSONResult(serviceLocationEndpointURL + statusURI, "resources");
            
            logger.info("Jenkins has " + results.size() + " lockable resources");
            
            for(JsonObject result : results.getValuesAs(JsonObject.class)) {
                
                String name = result.getString("name", "");
               
                if(objectId.trim().equals(name)) return true;
               
            }
            
            // if we get here, then we didnt find the resource name in the list of JSON name attributes    
            return false;
            
        }
        catch(Exception e) { logger.debug(e); return false; }
    
    }
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean findAll(String objectId) { return false; }
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @return 
     */
    public boolean findAny() { return false; }
    
    /**
     * Get the data file location where the resource was found.
     * 
     * @return 
     */
    public String getAssetDataFile() { return null; }
    
    /**
     * Determine if the asset finder found multiple matching assets
     * 
     * @return 
     */
    public boolean hasMultipleAssetChoices() { return false; }
    
    /**
     * Return the Map of all found asset data
     * 
     * @return 
     */
    public Map<String, String> getMultipleAssetsDataFiles() { return null; }
    
    /**
     * Get the number of found multiple matching assets
     * 
     * @return 
     */
    public int getNumberOfMultipleAssetChoices() { return 0; }
    
    /**
     * Get lock name
     */
    public String getLockName() { return null; }
    
}
