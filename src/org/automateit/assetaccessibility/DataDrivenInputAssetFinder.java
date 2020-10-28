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
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.automateit.util.Utils;
import org.automateit.util.CommonProperties;
import org.automateit.data.DataDrivenInput;

/**
 * This interface is implemented by mechanism that find various assets in different ways
 */
public class DataDrivenInputAssetFinder extends AssetFinderBase implements AssetFinder { 
    
    /**
     * The list of directories to check for the asset
     */
    private String[] directories = null;
    
    /**
     * The class with utilities
     */
    protected Utils utils = new Utils();
    
    /**
     * The data set id to look for
     */
    protected String dataSetId = null;
    
    /**
     * The data set id which has the info that matches the desired data from the match
     */
    protected String retrievedValueDataSetId = null;
    
    private String explicitLockName = null;
    
    //private ResourceLockController controller = null;
    
    /**
     * A list of found assets
     */
    private Vector<String> filesChecked = new Vector<String>();
    
    /**
     * The logging class to use for useful information
     */
    protected Logger logger = Logger.getLogger(DataDrivenInputAssetFinder.class);
    
    /**
     * Copy Constructor.
     * 
     * @param directories 
     */
    public DataDrivenInputAssetFinder(String[] directories) { 
        
        this.directories = directories; 
        
        try { CommonResourceLockController.getInstance().setController((new ResourceLockControllerFactory()).getResourceLockController()); }
        catch(Exception e) { }
    
    }
    
    /**
     * Copy Constructor.
     * 
     * @param directories
     * @param dataSetId
     */
    public DataDrivenInputAssetFinder(String[] directories, String dataSetId) { 
        
        this.directories = directories; 
        this.dataSetId = dataSetId;
        
        utils.shuffle(directories);
       
        try { CommonResourceLockController.getInstance().setController((new ResourceLockControllerFactory()).getResourceLockController()); }
        catch(Exception e) { }
    
    }
    
    /**
     * Copy Constructor.
     * 
     * @param directories
     * @param dataSetId
     * @param retrievedValueDataSetId
     */
    public DataDrivenInputAssetFinder(String[] directories, String dataSetId, String retrievedValueDataSetId) { 
        
        this.directories = directories; 
        this.dataSetId = dataSetId;
        this.retrievedValueDataSetId = retrievedValueDataSetId;
        
        utils.shuffle(directories);
        
        try { CommonResourceLockController.getInstance().setController((new ResourceLockControllerFactory()).getResourceLockController()); }
        catch(Exception e) { }
    
    }
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean find(String objectId) { 
       
        if(directories == null) return false;
        
        logger.info("Finding object id by data set id: " + dataSetId + "|" + objectId);
        
        for(String directory : directories) { 
            
            try {
               
                logger.info("Directory to search for " + directory);
               
                File[] files = utils.getListOfAllFilesInDirectory(directory);
                
                utils.shuffle(files);
                
                logger.info("Number of files found in directory to search for " + objectId + "|" + files.length);
            
                for(File f : files) { 
                    
                    logger.info("Checking file: " + f.getAbsolutePath());
                   
                    try {
                    
                        DataDrivenInput ddi = utils.setupDataDrivenInput(f.getAbsolutePath());
                       
                        if(ddi.hasDataId(dataSetId)) {
                       
                            String value = ddi.returnInputDataForDataIdAndColumnNumber(dataSetId, 1).trim();
                            
                            logger.info("Check if value matches: " + objectId + "|" + value);
                            
                            boolean match = value.trim().toLowerCase().contains(objectId.toLowerCase().trim());
                       
                            if(match) {
                                
                                if(retrievedValueDataSetId != null) explicitLockName = ddi.returnInputDataForDataIdAndColumnNumber(retrievedValueDataSetId, 1).trim();
                                else explicitLockName = value;
                                
                                // if the resource is already locked, then dont return it as found
                                if(CommonResourceLockController.getInstance().isLocked(explicitLockName)) {
                                    
                                    Exception e = new Exception("Asset is currently locked by another task: " + getAlternativeLockName());
                                    CommonProperties.getInstance().setException(e);
                                    throw e;
                                    
                                }
                            
                                logger.info("Data value found for data set id: " + dataSetId + "|" + value + "|" + objectId);
                               
                                setAssetDataFile(f.getAbsolutePath());
                               
                                if(retrievedValueDataSetId == null) setAlternativeLockName(objectId);
                                else setAlternativeLockName(ddi.returnInputDataForDataIdAndColumnNumber(retrievedValueDataSetId, 1).trim());
                            
                                explicitLockName = getAlternativeLockName();
                                
                                return true;
                            
                            }
                         
                        }
                       
                    }
                    catch(Exception e) { if(e.getMessage().startsWith("Asset is currently locked:")) throw e; }
                    
                }
                
            }      
            catch(Exception e) { }
                  
        }
        
        // if we get here, then we could not find it
        return false;
    
    }
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean findAll(String objectId) { 
       
        if(directories == null) return false;
        
        logger.info("Finding object id by data set id: " + dataSetId + "|" + objectId);
        
        for(String directory : directories) { 
            
            try {
                
                logger.info("Directory to search for " + directory);
               
                File[] files = utils.getListOfAllFilesInDirectory(directory);
                
                utils.shuffle(files);
                
                logger.info("Number of files found in directory to search for " + objectId + "|" + files.length);
            
                for(File f : files) { 
                    
                    try {
                    
                        logger.info("Checking file: " + f.getAbsolutePath());
                     
                        DataDrivenInput ddi = utils.setupDataDrivenInput(f.getAbsolutePath());
                    
                        logger.info("Checking file has data set id: " + ddi.hasDataId(dataSetId));
                    
                        if(ddi.hasDataId(dataSetId)) {
                       
                            String value = ddi.returnInputDataForDataIdAndColumnNumber(dataSetId, 1).trim();
                            
                            // if the resource is already locked, then dont return it as found
                            if(CommonResourceLockController.getInstance().isLocked(explicitLockName)) continue;
                        
                            boolean match = value.trim().toLowerCase().contains(objectId.toLowerCase().trim());
                       
                            if(match) {
                            
                                logger.info("Data value found for data set id: " + dataSetId + "|" + value + "|" + objectId);
                                
                                if(retrievedValueDataSetId == null) {
                                
                                    addMultipleAssetFileName(f.getAbsolutePath(), objectId);
                            
                                    addMultipleAssetFileName(f.getAbsolutePath(), value.trim());
                                    
                                    explicitLockName = value;
                            
                                }
                                else { addMultipleAssetFileName(f.getAbsolutePath(), ddi.returnInputDataForDataIdAndColumnNumber(retrievedValueDataSetId, 1).trim()); }
                            
                            }
                         
                        }
                        
                    }
                    catch(Exception le) { }
                    
                }
                
            }      
            catch(Exception e) { }
                  
        }
        
        return hasMultipleAssetChoices();
    
    }
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @return 
     */
    public boolean findAny() { 
        
        if(directories == null) return false;
        
        logger.info("Finding any object id by data set id: " + dataSetId);
        
        for(String directory : directories) { 
            
            try {
               
                File[] files = utils.getListOfAllFilesInDirectory(directory);
                
                utils.shuffle(files);
            
                for(File f : files) { 
                    
                    DataDrivenInput ddi = utils.setupDataDrivenInput(f.getAbsolutePath());
                    
                    if(ddi.hasDataId(dataSetId)) {
                       
                        String value = ddi.returnInputDataForDataIdAndColumnNumber(dataSetId, 1).trim();
                           
                        logger.info("Data value found for data set id: " + dataSetId + "|" + value);
                       
                        explicitLockName = value;
                        
                        // if the resource is already locked, then dont return it as found
                        if(CommonResourceLockController.getInstance().isLocked(explicitLockName)) continue;
                        
                        if(retrievedValueDataSetId == null) { addMultipleAssetFileName(f.getAbsolutePath(), explicitLockName.trim()); }    
                        else addMultipleAssetFileName(f.getAbsolutePath(), ddi.returnInputDataForDataIdAndColumnNumber(retrievedValueDataSetId, 1).trim());
                        
                    }
                        
                }
                
            }      
            catch(Exception e) { }
                  
        }
        
        return hasMultipleAssetChoices();
    
    }
    
    /**
     * Get lock name
     */
    public String getLockName() { return explicitLockName; }
    
    private boolean isAlreadyFound(String objectid) {
        
        boolean found = false;
        
        Iterator<String> fileIterator = filesChecked.iterator();
        
        while(fileIterator.hasNext()) {
            
            String filename = fileIterator.next();
          
            if((filename != null) && filename.trim().equals(objectid)) found = true;
            
        }
        
        return found;
        
    }

}

