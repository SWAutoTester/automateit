package org.automateit.assetaccessibility;

import java.util.Map;

import org.apache.log4j.Logger;

import org.automateit.util.Utils;

/**
 * This is a mock implementation class used for unit testing
 */
public class MockResourceLockController extends ResourceLockController implements AssetFinder {
  
    /**
     * The utilities object
     */
    protected Utils utils = new Utils();
    
    private boolean resourceSuccessfullyLocked = false;
    
    /**
     * The logging class to use for useful information
     */
    protected Logger logger = Logger.getLogger(MockResourceLockController.class);
   
    /**
     * Default Constructor.
     */
    public MockResourceLockController() throws Exception { super(); }
    
    /**
     * Copy Constructor.
     * 
     * @param configurationFile
     */
    public MockResourceLockController(String configurationFile) throws Exception { super(configurationFile); }
    
    /**
     * Copy Constructor.
     * 
     * @param configurationFile
     * @param lockName
     */
    public MockResourceLockController(String configurationFile, String lockName) throws Exception { super(configurationFile, lockName); }
    
    /**
     * Copy Constructor.
     * 
     * @param useUniqueLockName Something that is not indicated in the configuration file
     * @param lockName The lock name to use
     */
    public MockResourceLockController(boolean useUniqueLockName, String lockName) throws Exception { super(useUniqueLockName, lockName); }
    
    /**
     * Copy Constructor.
     * 
     * @param configurationFile
     * @param lockName
     * @param resourceName
     */
    public MockResourceLockController(String configurationFile, String lockName, String resourceName) throws Exception { super(configurationFile, lockName, resourceName); }
    
    /**
     * Copy Constructor.
     * 
     * @param useUniqueLockName Something that is not indicated in the configuration file
     * @param lockName The lock name to use
     * @param resourceName The resource name to be found locked in the service response HTML
     */
    public MockResourceLockController(boolean useUniqueLockName, String lockName, String resourceName) throws Exception { super(useUniqueLockName, lockName, resourceName); }
      
    /**
     * Lock the resource
     * 
     * @throws Exception 
     */
    public void lock() throws Exception { }
    
    /**
     * Lock the resource
     * 
     * @param slockName
     * @throws Exception 
     */
    public void lock(String slockName) throws Exception { }
    
    /**
     * Lock the resource
     * 
     * @param nameToLock
     * 
     * @throws Exception 
     */
    public void executeLock(String nameToLock) throws Exception { }
    
    /**
     * Unlock the resource.
     * 
     * @throws Exception 
     */
    public void unlock() throws Exception { }
    
    /**
     * Check if the resource is locked.
     * 
     * @return 
     */
    public boolean isResourceLocked() { return true; }
    
    /**
     * Check if the resource is locked.
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean isResourceLocked(String objectId) { return true; }
    
    /**
     * Wait on hold until the resource is free
     */
    public void waitForResourceFree() throws Exception {
        
        try { 
            
            while(isResourceLocked()) { 
            
                logger.info("Resource " + lockName + " is currently locked by another asset, waiting to retry execute lock after resource is available again");
                
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
        
        return false;
    
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
