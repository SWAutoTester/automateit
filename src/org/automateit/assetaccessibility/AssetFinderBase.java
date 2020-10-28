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

import java.util.Map;
import java.util.HashMap;

/**
 * This interface is implemented by mechanism that find various assets in different ways
 */
public abstract class AssetFinderBase { 
    
    /**
     * The data file with the info for the asset/resource
     */
    private String assetDataFile = null;
    
    /**
     * The lock name prefix (not required)
     */
    private String lockNamePrefix = "";
    
    /**
     * A possible alternative lock name that gets tried if all others do not
     */
    private String alternativeLockName = null;
    
    /**
     * A list of any and all assets to be tried after matching
     */
    private Map<String, String> multipleAssetsDataFiles = new HashMap<String, String>();
    
    /**
     * The data file with lockNamePrefix information
     * 
     * @param lockNamePrefix 
     */
    protected void setLockNamePrefix(String lockNamePrefix) { this.lockNamePrefix = lockNamePrefix; }
    
    /**
     * Get the lock name prefix
     * 
     * @return 
     */
    public String getLockNamePrefix() { return this.lockNamePrefix; }
    
    /**
     * The data file with asset information
     * 
     * @param assetDataFile 
     */
    protected void setAssetDataFile(String assetDataFile) { this.assetDataFile = assetDataFile; }
    
    /**
     * Get the data file location where the resource was found.
     * 
     * @return 
     */
    public String getAssetDataFile() { return this.assetDataFile; }
    
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
     * Add an asset data file to the list of potential assets
     * 
     * @param assetDataFile 
     * @param alternativeLockName
     */
    protected void addMultipleAssetFileName(String assetDataFile, String alternativeLockName) { this.multipleAssetsDataFiles.put(assetDataFile, alternativeLockName); }
    
    /**
     * Determine if the asset finder found multiple matching assets
     * 
     * @return 
     */
    public boolean hasMultipleAssetChoices() { return (this.multipleAssetsDataFiles.size() > 0); }
    
    /**
     * Get the number of found multiple matching assets
     * 
     * @return 
     */
    public int getNumberOfMultipleAssetChoices() { return this.multipleAssetsDataFiles.size(); }
    
    /**
     * Return the Map of all found asset data
     * 
     * @return 
     */
    public Map<String, String> getMultipleAssetsDataFiles() { return this.multipleAssetsDataFiles; }
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public abstract boolean find(String objectId);
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public abstract boolean findAll(String objectId);
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @return 
     */
    public abstract boolean findAny();

}

