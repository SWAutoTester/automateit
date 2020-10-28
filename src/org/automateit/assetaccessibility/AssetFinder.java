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

/**
 * This interface is implemented by mechanism that find various assets in different ways
 */
public interface AssetFinder { 
    
    /**
     * Get lock name
     */
    public String getLockName();
    
    /**
     * Get the data file location where the resource was found.
     * 
     * @return 
     */
    public String getAssetDataFile();
    
    /**
     * Find the resource. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean find(String objectId);
    
    /**
     * Find the matching resources values with <code>objectId</code>. If one is not found, return <code>false</code>.
     * 
     * @param objectId
     * 
     * @return 
     */
    public boolean findAll(String objectId);
    
    /**
     * Find any resource. If one is not found, return <code>false</code>.
     * 
     * @return 
     */
    public boolean findAny();
    
    /**
     * Determine if the asset finder found multiple matching assets
     * 
     * @return 
     */
    public boolean hasMultipleAssetChoices();
    
    /**
     * Return the Map of all found asset data
     * 
     * @return 
     */
    public Map<String, String> getMultipleAssetsDataFiles();
    
    /**
     * Get the number of found multiple matching assets
     * 
     * @return 
     */
    public int getNumberOfMultipleAssetChoices();

}

