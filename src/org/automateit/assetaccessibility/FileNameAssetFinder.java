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

import org.apache.log4j.Logger;

import org.automateit.util.CommonProperties;
import org.automateit.util.Utils;

import org.automateit.data.DataDrivenInput;

/**
 * This interface is implemented by mechanism that find various assets in different ways
 */
public class FileNameAssetFinder extends AssetFinderBase implements AssetFinder { 
    
    /**
     * The list of directories to check for the asset
     */
    private String[] directories = null;
    
    /**
     * The data set id if looking for a specific value
     */
    private String dataSetId = null;
    
    private String lockName = null;
    
    /**
     * The list of all possible file types to match the filename
     */
    private String[] fileExtensions = { ".csv", ".txt" };
    
    /**
     * 
     */
    public final static String NAME_DATASETID = "fnaf_datasetid";
    
    /**
     * The class with utilities
     */
    protected Utils utils = new Utils();
    
    /**
     * The logging class
     */
    protected static Logger logger = Logger.getLogger(FileNameAssetFinder.class);
    
    /**
     * Copy Constructor.
     * 
     * @param directories 
     */
    public FileNameAssetFinder(String[] directories) { this.directories = directories; utils.shuffle(directories); }
    
    /**
     * Copy Constructor.
     * 
     * @param directories 
     * @param dataSetId
     */
    public FileNameAssetFinder(String[] directories, String dataSetId) { 
        
        this.directories = directories; 
        this.dataSetId = dataSetId;
        
        CommonProperties.getInstance().setProperty(NAME_DATASETID, dataSetId);
       
        utils.shuffle(directories);
      
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
        
        for(String directory : directories) { 
            
            logger.debug("Checking for asset filename" + objectId + "|" + directory);
            
            // the explicit filename
            if(utils.isFileInDirectory(directory, objectId)) {
                
                try { 
                            
                    DataDrivenInput ddi = utils.setupDataDrivenInput(directory + File.separator + objectId);
                           
                    setAlternativeLockName(ddi.returnInputDataForDataIdAndColumnNumber(this.dataSetId, 1).trim());
                     
                }
                catch(Exception le) { }
                
                return true;
                
            } 
            
            String value = null;
            
            // the filename including the possible file extension
            for(String extension : fileExtensions) {
                
                if(utils.isFileInDirectory(directory, objectId + extension)) {
                    
                    setAssetDataFile(directory + File.separator + objectId + extension);
                     
                    if(CommonProperties.getInstance().get(NAME_DATASETID) == null) { 
                        
                        setAlternativeLockName(objectId); 
                    
                    } 
                    else {
                        
                        try { 
                            
                            DataDrivenInput ddi = utils.setupDataDrivenInput(directory + File.separator + objectId + extension);
                            
                            if(CommonProperties.getInstance().get(NAME_DATASETID) == null) { 
                                
                                addMultipleAssetFileName(directory + File.separator + objectId + extension, objectId); 
                            
                            }    
                            else { 
                                
                                value = ddi.returnInputDataForDataIdAndColumnNumber(CommonProperties.getInstance().get(NAME_DATASETID), 1);
                                
                                lockName = value;
     
                                // if the resource is already locked, then dont return it as found
                                if(CommonResourceLockController.getInstance().isLocked(value)) {
                                    
                                    Exception e = new Exception("Asset is currently locked by another task: " + value);
                                    CommonProperties.getInstance().setException(e);
                                    
                                    continue;
                                    
                                }
                               
                                addMultipleAssetFileName(directory + File.separator + objectId + extension, value.trim()); 
                                setAlternativeLockName(value.trim());
                                  
                            
                            }
                            
                        }
                        catch(Exception le) { }
                        
                    }
                    
                    if(CommonResourceLockController.getInstance().isLocked(value)) return false;
                    else return true;
                
                }
                
            } 
        
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
    public boolean findAll(String objectId) { return false; }
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @return 
     */
    public boolean findAny() { return false; }
    
    /**
     * Get lock name
     */
    public String getLockName() { return this.lockName; }

}

