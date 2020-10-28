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

package org.automateit.assetaccessibility;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.automateit.util.CommonProperties;
import org.automateit.util.CommonSelenium;
import org.automateit.util.Utils;

import org.automateit.test.TestBase;

/**
 * This class is the entry point of convenience for any clients wanting to have
 * their allocated assets managed and assigned at varying degrees of granularity.
 * 
 * @author mburnside
 */
public class AssetManager extends TestBase {
    
     /**
     * The logging class to use for useful information
     */
    protected Logger logger = Logger.getLogger(AssetManager.class);
   
    /**
     * The test resources directory environment variable
     */
    public static final String ENV_VARIABLE_TEST_RESOURCES_DIR = "TEST_RESOURCES_DIR";
    
    /**
     * The test settings directory environment variable
     */
    public static final String ENV_VARIABLE_TEST_SETTINGS_DIR = "TEST_SETTINGS_DIR";
    
    /**
     * The resource directory. This is changed is set in the environment during 
     * object construction.
     */
    protected String resourcesDirectory = null;
    
    /**
     * The settings directory. This is changed is set in the environment during 
     * object construction.
     */
    protected String settingsDirectory = null;
    
    /**
     * The class with utilities
     */
    protected Utils utils = new Utils();
    
    /**
     * The list of asset finders to search for resource objects (or any type of asset)
     */
    private List<AssetFinder> assetFinders = new ArrayList<AssetFinder>();
    
    /**
     * A list of found assets ids
     */
    private Vector<String> lockedAssetsIds = new Vector<String>();
    
    /**
     * A list of found assets
     */
    private Vector<Asset> lockedAssets = new Vector<Asset>();
    
    private int anyInt = 0;
    
    /**
     * The ResourceLockControllerFactory needed to get 
     */
    ResourceLockControllerFactory resourceLockControllerFactory = new ResourceLockControllerFactory();
    
    /**
     * The string array of directories for files to look into
     */
    protected String[] directories = null;
    
    private boolean assetFound = false; // this is used to determine granularity between asset not found or locked
    
    public final static String ASSET_ALREADY_FOUND_AND_ALLOCATED = "Asset Already Found";
    
    public final static String ASSET_PREVIOUSLY_ALLOCATED_THIS_TASK = "Asset was previously RESERVED by this task";
    
    /**
     * Default Constructor.
     */
    public AssetManager() { setup(); }
    
    /**
     * Copy Constructor.
     * 
     * @param resourcesDirectory
     * @param settingsDirectory 
     */
    public AssetManager(String resourcesDirectory, String settingsDirectory) { 
        
        this.resourcesDirectory = resourcesDirectory;
        
        this.settingsDirectory = settingsDirectory;
        
        // check to see if the base directory exists. If not, it will be relative to one directory higher
        File f = new File(this.resourcesDirectory);
        
        if(!f.exists() || !f.isDirectory()) this.resourcesDirectory = resourcesDirectory.substring(1, resourcesDirectory.length());
       
        setup();
       
    }
    
    /**
     * Setup. 
     * 
     * The manager is setup with default File Name asset finders and a Jenkins asset finder.
     * 
     * If the following system variables exist, then they will overwrite and previously set
     * test resources directories and test setting directories
     * 
     * ENV_VARIABLE_TEST_RESOURCES_DIR
     * ENV_VARIABLE_TEST_SETTINGS_DIR
     * 
     */
    private void setup() {
        
        if(System.getenv(ENV_VARIABLE_TEST_RESOURCES_DIR) != null) resourcesDirectory = System.getenv(ENV_VARIABLE_TEST_RESOURCES_DIR);
        
        if(System.getenv(ENV_VARIABLE_TEST_SETTINGS_DIR) != null) settingsDirectory = System.getenv(ENV_VARIABLE_TEST_SETTINGS_DIR);
        
        String[] directories1 = { resourcesDirectory + settingsDirectory };
        
        this.directories = directories1;
        
        // add the FileAssetFinder
        FileNameAssetFinder fileNameAssetFinder = new FileNameAssetFinder(directories);
        
        fileNameAssetFinder.setLockNamePrefix(settingsDirectory + "-");
        
        assetFinders.add(fileNameAssetFinder);
        
    }
    
    /**
     * Add an asset finder.
     * 
     * @param assetFinder 
     */
    public void addAssetFinder(AssetFinder assetFinder) { 
        
        try { assetFinders.add(assetFinder); }
        catch(Exception e) { }
        
    }
    
    /**
     * Add a data driven input based asset finder to the manager.
     * 
     * @param keyName 
     */
    public void addDataDrivenInputAssetFinder(String keyName) { 
        
        String[] directories = { resourcesDirectory + settingsDirectory };
        
        assetFinders.add(new DataDrivenInputAssetFinder(directories, keyName)); 
    
    }
    
    /**
     * Add a data driven input based asset finder to the manager.
     * 
     * @param keyName 
     * @param keyValueRetrieveId
     */
    public void addDataDrivenInputAssetFinder(String keyName, String keyValueRetrieveId) { 
        
        String[] directories = { resourcesDirectory + settingsDirectory };
        
        assetFinders.add(new DataDrivenInputAssetFinder(directories, keyName, keyValueRetrieveId)); 
    
    }
    
    /**
     * Get the resource lock controller. The id will specify a unique, group, or general (any) controller
     * 
     * example: specific "test-xxy"
     *          group "nike"
     *          general "any" or any available
     *          
     * 
     * @param objectId
     * @return
     * @throws Exception 
     */
    public Asset findAsset(String objectId) throws Exception { return findAsset(true, objectId); }
    
    /**
     * Get the resource lock controller. The id will specify a unique, group, or general (any) controller
     * 
     * example: specific "test-xxy"
     *          group "nike"
     *          general "any" or any available
     *          
     * 
     * @param autoLock
     * @param objectId
     * 
     * @throws Exception 
     */
    public Asset findAsset(boolean autoLock, String objectId) throws Exception {
       
        assetFound = false;
        
        logger.info("Attempt to find asset: " + objectId + " and autolock set to: " + autoLock);
       
        logger.info("Has asset been previously found and allocated: " + objectId + "|" + containsAlreadyLockedAsset(objectId));
        
        if(isAssetAlreadyLocked(objectId)) throw new Exception(ASSET_PREVIOUSLY_ALLOCATED_THIS_TASK + ": " + objectId);
       
        if(objectId.trim().equalsIgnoreCase("any")) {
           
            try { return findAnyAvailableAssets(); }
            catch(Exception le) { }
            
        }
        else {
            
            for(AssetFinder finder : assetFinders) { 
           
                if(finder.find(objectId)) {
                    
                    assetFound = true;
                    
                    logger.info("Asset has been found: " + objectId);
                  
                    if(!finder.hasMultipleAssetChoices()) { 
                        
                        logger.info("Has multiple asset choices");
                     
                        try { 
                            
                            if(finder.getLockName() == null) {
                                
                                if(!isAssetAlreadyLocked(objectId)) return getLockedAsset(objectId, objectId, finder.getAssetDataFile(), objectId);
                                else continue; 
                                
                            } 
                            else {
                                
                                if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), finder.getAssetDataFile(), finder.getLockName());
                                else continue;
                                
                            }
                        
                        }            
                        catch(Exception se) { throw se;  }
                
                    }
                
                    else {
                        
                        logger.info("does NOT have multiple asset choices");
                     
                        for(String key : finder.getMultipleAssetsDataFiles().keySet() ) {
                            
                            assetFound = true;
                            
                            try { 
                                
                                if(finder.getLockName() == null) {
                                    
                                    if(!isAssetAlreadyLocked(finder.getLockName())) getLockedAsset(objectId, objectId, key, finder.getMultipleAssetsDataFiles().get(key));
                                
                                    else continue;   
                                } 
                                else {
                                    
                                    if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), key, finder.getLockName());
                                    else continue;
                                }
                            
                            }         
                            catch(Exception le) { throw le; }
                        
                        }
                
                    }
               
                } 
               
            }
        
        }
        
        // if we get here, then we didnt find the resource, so throw an exception
        if(CommonProperties.getInstance().getException() != null) {
            
            if(doesExceptionMessageContainAlreadyLockedAsset(CommonProperties.getInstance().getException(), objectId)) throw new Exception(ASSET_ALREADY_FOUND_AND_ALLOCATED);
            else throw CommonProperties.getInstance().getException();
            
        }
        if(lockedAssetsIds.contains(objectId)) throw new Exception(ASSET_PREVIOUSLY_ALLOCATED_THIS_TASK);
        if(assetFound) throw new Exception("Asset: \"" + objectId + "\" was found but is RESERVED by another task");
        else throw new Exception("Asset: \"" + objectId + "\" was NOT found");
        
    }
    
    /**
     * Get the resource lock controller. The id will specify a unique, group, or general (any) controller
     * 
     * example: specific "test-xxy"
     *          group "nike"
     *          general "any" or any available
     *          
     * 
     * @param objectId
     * @param lockName
     * @return
     * @throws Exception 
     */
    public Asset findAsset(String objectId, String lockName) throws Exception {
        
        assetFound = false;
        
        logger.info("Attempt to find asset: " + objectId + " and lockName set to: " + lockName);
        
        if(isAssetAlreadyLocked(objectId)) throw new Exception(ASSET_PREVIOUSLY_ALLOCATED_THIS_TASK + ": " + objectId);

        for(AssetFinder finder : assetFinders) { 
            
            if(finder.find(objectId)) {
                
                assetFound = true;
                
                if(!finder.hasMultipleAssetChoices()) { 
                    
                    logger.info("Has multiple asset choices");
                    
                    try { 
                        
                        if(isAssetAlreadyLocked(objectId)) throw new Exception(ASSET_PREVIOUSLY_ALLOCATED_THIS_TASK + ": " + objectId);
                                
                        if(finder.getLockName() == null) return getLockedAsset(objectId, lockName, finder.getAssetDataFile(), lockName);
                        else {
                            
                            if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), finder.getAssetDataFile(), finder.getLockName());
                            else throw new Exception("Asset is Already Locked by another User/Task: " + finder.getLockName());
                            
                        }
                    
                    } 
                    catch(Exception se) { throw se; }
                
                }
                else {
                    
                    assetFound = true;
                    
                    for(String key : finder.getMultipleAssetsDataFiles().keySet() ) {
                        
                        logger.info("does NOT have multiple asset choices");
    
                        try { 
                            
                            if(isAssetAlreadyLocked(objectId)) throw new Exception(ASSET_PREVIOUSLY_ALLOCATED_THIS_TASK + ": " + objectId);
                            
                            if(finder.getLockName() == null) return getLockedAsset(objectId, lockName, key, finder.getMultipleAssetsDataFiles().get(key)); 
                            else {
                                
                                if(!isAssetAlreadyLocked(finder.getLockName())) getLockedAsset(finder.getLockName(), finder.getLockName(), key, finder.getLockName());
                                else throw new Exception("Asset is Already Locked by another User/Task: " + finder.getLockName());
                            
                            }
                        
                        }
                        catch(Exception le) { throw le; }
                       
                    }
                    
                }
                
            } 
        
        }
        
        try { return findAllAvailableAssets(objectId, lockName); }
        catch(Exception le) { }
        
        // if we get here, then we didnt find the resource, so throw an exception
        
        if(assetFound) throw new Exception("Asset: \"" + objectId + "\" was found but is RESERVED by another task");
        else throw new Exception("Asset: \"" + objectId + "\" was NOT found");
        
    }
    
    /**
     * Get the resource lock controller. The id will specify a unique, group, or general (any) controller
     * 
     * example: specific "test-xxy"
     *          group "nike"
     *          general "any" or any available
     *          
     * 
     * @param objectId
     * 
     * @return
     * 
     * @throws Exception 
     */
    public Asset findAllAvailableAssets(String objectId) throws Exception {
        
        logger.info("Finding " + objectId + " in all available matching assets");
        
        if(isAssetAlreadyLocked(objectId)) throw new Exception(ASSET_PREVIOUSLY_ALLOCATED_THIS_TASK + ": " + objectId);

        for(AssetFinder finder : assetFinders) { 
            
            if(finder.findAll(objectId)) {
                
                logger.info("Has multiple asset choices found" + finder.hasMultipleAssetChoices() + "|" + finder.getNumberOfMultipleAssetChoices());
                
                if(!finder.hasMultipleAssetChoices()) { 
                    
                    try { 
                        
                        if(finder.getLockName() == null) return getLockedAsset(objectId, objectId, finder.getAssetDataFile(), objectId); 
                        else {
                            
                            if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), finder.getAssetDataFile(), finder.getLockName());
                            else continue;
                            
                        }
                    
                    }
                    catch(Exception le) { }
                
                }
                else {
                    
                    logger.info("Found " + finder.getMultipleAssetsDataFiles().size() + " matches for " + objectId);
                    
                    for(String key : finder.getMultipleAssetsDataFiles().keySet() ) {
                       
                        try { 
                            
                            if(finder.getLockName() == null) return getLockedAsset(objectId, objectId, key, finder.getMultipleAssetsDataFiles().get(key)); 
                            else {
                                
                                if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), key, finder.getLockName());
                                else continue;
                                
                            }
                        
                        }
                        catch(Exception le) { }
                       
                    }
                    
                }
                
            } 
        
        }
        
        // if we get here, then we didnt find the resource, so throw an exception
        throw new Exception("Resource: " + objectId + " not found or no assets of this type are available");
        
    }
    
    /**
     * Get the resource lock controller. The id will specify a unique, group, or general (any) controller
     * 
     * example: specific "test-xxy"
     *          group "nike"
     *          general "any" or any available
     *          
     * 
     * @param objectId
     * @param lockName
     * 
     * @return
     * 
     * @throws Exception 
     */
    public Asset findAllAvailableAssets(String objectId, String lockName) throws Exception {

        for(AssetFinder finder : assetFinders) { 
            
            if(finder.findAll(objectId)) {
                
                if(!finder.hasMultipleAssetChoices()) { return getLockedAsset(objectId, lockName, finder.getAssetDataFile(), lockName); }
                else {
                    
                    for(String key : finder.getMultipleAssetsDataFiles().keySet() ) {
    
                        try { 
                            
                            if(finder.getLockName() == null) return getLockedAsset(objectId, lockName, key, finder.getMultipleAssetsDataFiles().get(key)); 
                            else {
                                
                                if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), key, finder.getLockName());
                                else continue;
                                
                            }
                        
                        }
                        catch(Exception le) { }
                       
                    }
                    
                }
                
            } 
        
        }
        
        // if we get here, then we didnt find the resource, so throw an exception
        throw new Exception("Resources: " + objectId + "|" + lockName + " not found");
        
    }
    
    /**
     * Get the resource lock controller. The id will specify a unique, group, or general (any) controller
     * 
     * example: specific "test-xxy"
     *          group "nike"
     *          general "any" or any available
     *          
     * 
     * @param objectId
     * 
     * @return
     * 
     * @throws Exception 
     */
    public Asset findAnyAvailableAssets(String objectId) throws Exception {
       
        for(AssetFinder finder : assetFinders) { 
            
            if(finder.findAny()) {
                
                if(!finder.hasMultipleAssetChoices()) { return getLockedAsset(objectId, objectId, finder.getAssetDataFile(), objectId); }
                else {
                    
                    for(String key : finder.getMultipleAssetsDataFiles().keySet() ) {
    
                        try { 
                            
                            if(finder.getLockName() == null) return getLockedAsset(objectId, objectId, key, finder.getMultipleAssetsDataFiles().get(key)); 
                            else {
                                
                                if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), key, finder.getLockName());
                                else continue;
                                
                            }
                        
                        }
                        catch(Exception le) { }
                       
                    }
                    
                }
                
            } 
        
        }
        
        // if we get here, then we didnt find the resource, so throw an exception
        throw new Exception("Resources: " + objectId + " not found for any resources");
        
    }
    
    /**
     * Get the resource lock controller. The id will specify a unique, group, or general (any) controller
     * 
     * example: specific "test-xxy"
     *          group "nike"
     *          general "any" or any available
     *          
     * 
     * @return
     * @throws Exception 
     */
    public Asset findAnyAvailableAssets() throws Exception {

        String tmp = "id_any." + String.valueOf(anyInt);
        anyInt++;
        
        for(AssetFinder finder : assetFinders) { 
            
            if(finder.findAny()) {
                
                if(!finder.hasMultipleAssetChoices()) { return getLockedAsset(tmp, tmp, finder.getAssetDataFile(), tmp); }
                else {
                    
                    for(String key : finder.getMultipleAssetsDataFiles().keySet() ) {
    
                        try { 
                            
                            if(finder.getLockName() == null) return getLockedAsset(tmp, tmp, key, finder.getMultipleAssetsDataFiles().get(key));
                            else {
                                
                                if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), key, finder.getLockName());
                                else continue;
                               
                            }
                        
                        }
                        catch(Exception le) { }
                       
                    }
                    
                }
               
            } 
        
        }
        
        // if we get here, then we didnt find the resource, so throw an exception
        throw new Exception("Resources not found");
        
    }
    
    /**
     * Get the resource lock controller. The id will specify a unique, group, or general (any) controller
     * 
     * example: specific "test-xxy"
     *          group "nike"
     *          general "any" or any available
     *          
     * 
     * @param objectId
     * @param lockName
     * @return
     * @throws Exception 
     */
    public Asset findAnyAvailableAssets(String objectId, String lockName) throws Exception {

        for(AssetFinder finder : assetFinders) { 
            
            if(finder.findAny()) {
                
                if(!finder.hasMultipleAssetChoices()) { return getLockedAsset(objectId, lockName, finder.getAssetDataFile(), lockName); }
                else {
                    
                    for(String key : finder.getMultipleAssetsDataFiles().keySet() ) {
    
                        try { 
                            
                            if(finder.getLockName() == null) return getLockedAsset(objectId, lockName, key, finder.getMultipleAssetsDataFiles().get(key)); 
                            else {
                                
                                if(!isAssetAlreadyLocked(finder.getLockName())) return getLockedAsset(finder.getLockName(), finder.getLockName(), key, finder.getLockName());
                                else continue;
                                
                            }
                        
                        }
                        catch(Exception le) { }
                       
                    }
                    
                }
                
            } 
        
        }
        
        // if we get here, then we didnt find the resource, so throw an exception
        throw new Exception("Resources: " + objectId + "|" + lockName + " not found for any resources");
        
    }
    
    /**
     * Return an asset in locked state.
     * 
     * @param objectId
     * @param lockName
     * @param assetFile
     * @param alternativeLockName
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected Asset getLockedAsset(String objectId, String lockName, String assetFile, String alternativeLockName) throws Exception {
            
        logger.info("Getting locked asset: " + objectId);
        logger.info("Getting locked asset: " + lockName);
        logger.info("Getting locked asset: " + assetFile);
        logger.info("Getting locked asset: " + alternativeLockName);
      
        if(isAssetAlreadyLocked(objectId)) throw new Exception("Asset " + alternativeLockName + " already locked in this instance");
        
        logger.info("Asset found: " + assetFound);
        
        Asset asset = new Asset();
            
        ResourceLockController controller = resourceLockControllerFactory.getResourceLockController(true, objectId);
            
        controller.setLockName(lockName);
           
        asset.setResourceLockController(controller);    
        asset.setInputDataFile(assetFile); 
        asset.setLockNamePrefix(settingsDirectory + "-");   
        asset.setAlternativeLockName(alternativeLockName);
              
        logger.info("Asset lock attempt: " + lockName);
                
        asset.lock();
        
        logger.info("Asset locked: " + lockName);
        
        info("Asset locked: " + lockName);
        lockedAssetsIds.add(lockName);
        lockedAssets.add(asset);
        
        logger.info("added to found assets: " + lockName + "|" + lockedAssetsIds.size());
       
        assetFound = true;
        
        // if we get here, then we add the properties of the asset to Common Properties for universal access
        logger.info("Adding asset file to common properties: " + assetFile);
        
        try { CommonProperties.getInstance().addProperties(assetFile); }
        catch(Exception be) { be.printStackTrace(); }
        
        // this part included if this is a case of a mobile device asset being added
        try { CommonSelenium.getInstance().addDeviceInformation(assetFile); }
        catch(Exception be) { }
        
        logger.info("Need to add android properties: " + CommonProperties.getInstance().isAndroid());
        
        // include app properties from android if needed
        if(CommonProperties.getInstance().isAndroid()) {
            
            String androidPropertiesFilePath = "./conf/app.android.csv";
            if(CommonProperties.getInstance().getModule() != null) androidPropertiesFilePath = "./" + CommonProperties.getInstance().getModule() +  androidPropertiesFilePath;
            
            logger.info("Adding Android app specific properties:" + androidPropertiesFilePath);
           
            File f = new File(androidPropertiesFilePath);
            
            if(f.exists() && f.isFile()) CommonProperties.getInstance().addProperties(androidPropertiesFilePath);
            
        }
        
        logger.info("Need to add iOS properties: " + CommonProperties.getInstance().isIOS());
        
        // include app properties from android if needed
        if(CommonProperties.getInstance().isIOS()) {
            
            String iosPropertiesFilePath = "./conf/app.ios.csv";
            if(CommonProperties.getInstance().getModule() != null) iosPropertiesFilePath = "./" + CommonProperties.getInstance().getModule() +  iosPropertiesFilePath;
            
            logger.info("Adding iOS app specific properties: " + iosPropertiesFilePath);
            
            File f = new File(iosPropertiesFilePath);
            
            if(f.exists() && f.isFile()) CommonProperties.getInstance().addProperties(iosPropertiesFilePath);
        
        }
        
        logger.info("Returning successfully locked asset: " + lockName);
             
        return asset;
            
    }
    
    /**
     * Determines if we already have the locked asset
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean containsAlreadyLockedAsset(String objectId) { return lockedAssetsIds.contains(objectId); }
    
    /**
     * Determines if we already have the locked asset
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean doesExceptionMessageContainAlreadyLockedAsset(Exception e, String objectId) { 
        
        boolean hasId = false;
        
        Iterator<String> asset_ids = lockedAssetsIds.iterator();
        
        while (asset_ids.hasNext()) { 
            
            String id = asset_ids.next();
            
            if(e.getMessage().contains(id)) {
                
                hasId = true;
                
                break;
                
            }
            
        } 
       
        return hasId;
    
    }
    
    /**
     * Get the locked assets
     * 
     * @return 
     */
    public Vector<Asset> getLockedAssets() { return lockedAssets; }
    
    /**
     * Get the number of locked assets
     * 
     * @return 
     */
    public int getLockedAssetsSize() { return lockedAssets.size(); }
    
    /**
     * Run a test.
     * 
     * @param asset
     * @param assetWorkflow
     * 
     * @throws Exception 
     */
    public void runTest(Asset asset, AssetWorkflow assetWorkflow) throws Exception {
        
        try { assetWorkflow.execute(asset); }
        catch(Exception e) { throw e; }
        finally {
            
            if((asset != null) && asset.isLocked()) {
                
                try { asset.unlock(); }
                catch(Exception le) { }
                
            }
            
        }
        
    }
    
    private boolean isAssetAlreadyLocked(String objectid) {
        
        boolean found = false;
      
        Vector<Asset> assets = getLockedAssets();
        
        Iterator<Asset> assetIterator = assets.iterator();
        
        while(assetIterator.hasNext()) {
            
            Asset asset = assetIterator.next();
           
            if((asset.getAlternativeLockName() != null) && asset.getAlternativeLockName().trim().equals(objectid)) found = true;
           
        }
        
        return found;
        
    }
 
}