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

import org.automateit.test.TestBase;

/**
 * This Simple example class of working with an asset and interaction of other code with it
 */
public class SimpleWaitAssetWorkflow extends TestBase implements AssetWorkflow { 
    
    /**
     * Default Constructor
     */
    public SimpleWaitAssetWorkflow() { }
    
    /**
     * Execute the code. Unlock the asset when finished
     * 
     * @return 
     */
    public void execute(Asset asset) throws Exception { 
        
        // do something with the asset data
        //String mac = asset.getValueForKey("mac");
        
        // do something else
        
        delay(30000); 
    
    }
        
}

