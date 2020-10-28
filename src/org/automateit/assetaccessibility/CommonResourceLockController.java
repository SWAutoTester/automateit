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

import org.apache.log4j.Logger;

/**
 * Common class that has access to the selected Resource Lock Controller
 *
 * @author mburnside@Automate It!
 */

public class CommonResourceLockController {

    /**
     * The instance of this singleton class
     */
    private static CommonResourceLockController instance = new CommonResourceLockController();
    
    /**
     * Web Driver object
     */
    private ResourceLockController controller = null;
    
    /**
     *  logging object
     */
    protected static Logger log = Logger.getLogger(CommonResourceLockController.class);
    
    /**
     * Default Constructor
     */
    protected CommonResourceLockController() {}
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static CommonResourceLockController getInstance() { return instance; }
    
    /**
     * Set the controller
     * 
     * @param controller
     */
    public void setController(ResourceLockController controller) { this.controller = controller; }
    
    /**
     * Is the resource locked
     * 
     * @param lockId
     * 
     * @return 
     */
    public boolean isLocked(String lockId) { return this.controller.isResourceLocked(lockId); }
    
}