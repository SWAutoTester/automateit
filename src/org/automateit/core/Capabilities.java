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
 *  
 **/
package org.automateit.core;

import org.openqa.selenium.remote.DesiredCapabilities;

import org.automateit.util.CommonProperties;

/**
 * This class contains constant used in Desired Capabilities for web driver initialization.
 * 
 * @author mburnside@Automate It!
 */
public class Capabilities {
    
    /**
     * Default Constructor
     */
    public Capabilities() { }
   
    /**
     * Get all of the configured desired capabilities
     * 
     * @return 
     */
    public DesiredCapabilities get() {
       
       DesiredCapabilities capabilities = new DesiredCapabilities();
       
       // add all desired capabilities needing to be set by String value
       StringCapabilities.addCapabilities(capabilities, CommonProperties.getInstance());
       
       // add all desired capabilities needing to be set by String value
       BooleanCapabilities.addCapabilities(capabilities, CommonProperties.getInstance());
       
       return capabilities;
   
    }
            
}