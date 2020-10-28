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

import org.automateit.util.Utils;

/**
 * This class finds assets by looking into data in the file and looks for matching text and name-value pairs
 */
public class FileMetaDataAssetFinder extends AssetFinderBase implements AssetFinder { 
    
    /**
     * The list of directories to check for the asset
     */
    private String[] directories = null;
    
    /**
     * The class with utilities
     */
    protected Utils utils = new Utils();
    
    /**
     * The logging class to use for useful information
     */
    protected Logger logger = Logger.getLogger(FileMetaDataAssetFinder.class);
    
    /**
     * Copy Constructor.
     * 
     * @param directories 
     */
    public FileMetaDataAssetFinder(String[] directories) { this.directories = directories; utils.shuffle(directories);}
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean find(String objectId) { 
        
        if(directories == null) return false;
        
        logger.info("Finding object id: " + objectId);
        
        for(String directory : directories) { 
            
            try {
                
                File[] files = utils.getListOfAllFilesInDirectory(directory);
                
                utils.shuffle(files);
                
                for(File f : files) { 
                    
                    if(utils.isTextInFile(f.getAbsolutePath(), objectId)) {
                        
                        setAssetDataFile(f.getAbsolutePath());
                        
                        return true;
                    
                    } 
                
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
    public String getLockName() { return null; }

}

