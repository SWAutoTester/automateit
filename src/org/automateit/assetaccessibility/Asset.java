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

import java.util.regex.Pattern;

import org.automateit.data.DataDrivenInput;

import org.automateit.util.Utils;

/**
 * This class encapsulates all functionality and information about an asset
 */
public class Asset { 
    
    /**
     * The resource lock controller
     */
    private ResourceLockController resourceLockController = null;
    
    /**
     * The data driven input
     */
    private DataDrivenInput inputData = null;
    
    /**
     * The physical path to the file on the file system
     */
    private String inputDataFile = null;
    
    /**
     * The utilities class
     */
    private Utils utils = new Utils();
    
    /**
     * The lock name prefix (not required)
     */
    private String lockNamePrefix = "";
    
    /**
     * The alternative lock name to use if cant find in previous tries
     */
    private String alternativeLockName = null;
    
    private boolean isLocked = false;
    
    /**
     * Default Constructor
     */
    public Asset() { }
    
    /**
     * Copy Constructor
     * 
     * @param resourceLockController
     * @param inputDataFile 
     */
    public Asset(ResourceLockController resourceLockController, String inputDataFile) throws Exception { 
        
        this.resourceLockController = resourceLockController;
        
        setInputDataFile(inputDataFile);
        
        try { lock(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * The data file with lockNamePrefix information
     * 
     * @param lockNamePrefix 
     */
    protected void setLockNamePrefix(String lockNamePrefix) { this.lockNamePrefix = lockNamePrefix; }
    
    /**
     * Set the resource lock controller.
     * 
     * @param resourceLockController 
     */
    public void setResourceLockController(ResourceLockController resourceLockController) { this.resourceLockController = resourceLockController; }
    
    /**
     * Set the input data
     * 
     * @param inputData 
     */
    public void setInputData(DataDrivenInput inputData) { this.inputData = inputData; }
    
    /**
     * Set the input data file location on the file system
     * 
     * @param inputDataFile 
     */
    public void setInputDataFile(String inputDataFile) { 
        
        this.inputDataFile = inputDataFile; 
        
        try { setInputData(utils.setupDataDrivenInput(inputDataFile)); }
        catch(Exception e) { }
    
    }
    
    /**
     * Lock the asset.
     * 
     * @throws Exception 
     */
    public void lock() throws Exception { 
        
        String originalLockNamePrefix = this.lockNamePrefix;
        
        try { this.resourceLockController.lock(); }
        catch(Exception e) { 
            
            try { 
                
                try { 
                    
                    if(!this.lockNamePrefix.trim().equals("")) { this.resourceLockController.lock(this.lockNamePrefix); } 
                    else this.lockNamePrefix = originalLockNamePrefix;
                
                }
                catch(Exception le) {
                    
                    this.lockNamePrefix = originalLockNamePrefix;
                    
                    try {
                       
                        File f = new File(inputDataFile);
                        
                        String filename = f.getName();
                        String[] arrOfStr = filename.split(Pattern.quote(".")); 
                        
                        String newLockName = null;
                        
                        try { newLockName = this.lockNamePrefix + arrOfStr[0]; }
                        catch(Exception fe) { newLockName = this.lockNamePrefix + filename; }
                       
                        this.resourceLockController.setLockName(newLockName);
                       
                        this.resourceLockController.lock();
                        
                        isLocked = true;
                       
                    }
                    catch(Exception se) { 
                        
                        // try by the alternative lock name
                        if(getAlternativeLockName() != null) {
                           
                            try {
                            
                                this.resourceLockController.setLockName(getAlternativeLockName());
                            
                                this.resourceLockController.lock();
                               
                                isLocked = true;
                                
                            }
                            catch(Exception me) {
                               
                                throw me;
                                
                            }
                            
                            
                        }
                    
                    }
                    
                }
            
            }
            catch(Exception le) { throw le; }
            
        }
    
    }
    
    /**
     * Unlock the asset.
     * 
     * @throws Exception 
     */
    public void unlock() throws Exception { this.resourceLockController.unlock(); }
    
    /**
     * Determine if the asset is currently locked.
     * 
     * @return 
     */
    public boolean isLocked() { return this.isLocked; }
    
    /**
     * Get a metadata value for the key name. Returns <code>null</code> if key name not found.
     * 
     * @param keyName
     * 
     * @return 
     */
    public String getValueForKey(String keyName) { 
        
        try { return this.inputData.returnInputDataForDataIdAndColumnNumber(keyName, 1); }
        catch(Exception e) { return null; }
    
    }
    
    /**
     * Get a metadata value for the key name. Returns <code>null</code> if key name not found.
     * 
     * @param keyName
     * 
     * @return 
     */
    public boolean hasValueForKey(String keyName) { 
        
        try { 
            
            this.inputData.returnInputDataForDataIdAndColumnNumber(keyName, 1); 
            
            return true;
        
        }
        catch(Exception e) { return false; }
    
    }
    
    /**
     * Get the data driven object pointer
     * 
     * @return 
     */
    public DataDrivenInput getInputData() { return inputData; }
    
    /**
     * The data file with alternativeLockName information
     * 
     * @param alternativeLockName 
     */
    protected void setAlternativeLockName(String alternativeLockName) { this.alternativeLockName = alternativeLockName; }
    
    /**
     * Get the alternative lock name
     * 
     * @return 
     */
    public String getAlternativeLockName() { return this.alternativeLockName; }
    
    /**
     * Get the alternative lock name
     * 
     * @return 
     */
    public String getLockNamePrefix() { return this.lockNamePrefix; }
        

    /**
     * Unlock the asset when Asset is destoryed
     * 
     * mgb: this is prematurely unlocking the asset before all multi-task execution is finished
     *
     * @return
     */
    //public void finalize() throws Exception {
    //   if (isLocked()) unlock();
    //}
}
