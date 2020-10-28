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

/**
 * This class is a lightweight factory that returns an obsfuscated instance of 
 * a <code>ResourceLockController</code> implementation.
 * 
 * @author mburnside
 */
public class ResourceLockControllerFactory {
   
    /**
     * Jenkins identifier.
     */
    public static final String ID_JENKINS = "jenkins";
    
    /**
     * Mock object identifier (used for unit testing).
     */
    public static final String ID_MOCK = "mock";
    
    /**
     * Default Constructor.
     */
    public ResourceLockControllerFactory() { }

    /**
     * Get an implementation <code>ResourceLockController</code>.
     * 
     * @param id The id of the implementation to use
     * 
     * @return A ResourceLockController instance
     * 
     * @throws Exception 
     */
    public ResourceLockController getResourceLockController(String id) throws Exception {
        
        ResourceLockController controller = null;
                
        try {
        
            switch(id) {
                 
                case ID_JENKINS:
                    
                    controller = new JenkinsResourceLockController();
                    
                default:
                        
                    controller = new JenkinsResourceLockController();
                   
            }
            
            CommonResourceLockController.getInstance().setController(controller);
            
            return controller;
            
        }
        catch(Exception e) { throw e; }
    
    }    
    
    /**
     * Get an implementation <code>ResourceLockController</code>.
     * 
     * @param id The id of the implementation to use
     * @param configurationFile The location of the configuration file to use
     * 
     * @return A ResourceLockController instance
     * 
     * @throws Exception 
     */
    public ResourceLockController getResourceLockController(String id, String configurationFile) throws Exception {
        
        ResourceLockController controller = null;
        
        try {
        
            switch(id) {
               
                case ID_JENKINS:
                    
                    controller = new JenkinsResourceLockController(configurationFile);
                        
                default:
                        
                    controller = new JenkinsResourceLockController(configurationFile);
                   
            }
            
            CommonResourceLockController.getInstance().setController(controller);
           
            return controller;
            
        }
        catch(Exception e) { throw e; }
    
    }    
    
    /**
     * Get an implementation <code>ResourceLockController</code>.
     * 
     * @param id The id of the implementation to use
     * @param configurationFile The location of the configuration file to use
     * @param lockName The name of the resource lock
     * 
     * @return A ResourceLockController instance
     * 
     * @throws Exception 
     */
    public ResourceLockController getResourceLockController(String id, String configurationFile, String lockName) throws Exception {
        
        ResourceLockController controller = null;
        
        try {
        
            switch(id) {
                
                case ID_JENKINS:
                
                    controller = new JenkinsResourceLockController(configurationFile, lockName);
                   
                default:
                        
                    controller = new JenkinsResourceLockController(configurationFile, lockName);
                   
            }
            
            CommonResourceLockController.getInstance().setController(controller);
            
            return controller;
            
        }
        catch(Exception e) { throw e; }
    
    }    
    
    /**
     * Get an implementation <code>ResourceLockController</code>.
     * 
     * @param id The id of the implementation to use
     * @param configurationFile The location of the configuration file to use
     * @param lockName The name of the resource lock
     * 
     * @return A ResourceLockController instance
     * 
     * @throws Exception 
     */
    public ResourceLockController getResourceLockController(String id, String configurationFile, String lockName, String resourceName) throws Exception {
        
        ResourceLockController controller = null;
        
        try {
        
            switch(id) {
              
                case ID_JENKINS:
                
                    controller = new JenkinsResourceLockController(configurationFile, lockName, resourceName);
                    
                default:
                        
                    controller = new JenkinsResourceLockController(configurationFile, lockName, resourceName);
                   
            }
            
            CommonResourceLockController.getInstance().setController(controller);
            
            return controller;
            
        }
        catch(Exception e) { throw e; }
    
    }    
    
    /**
     * Get the default implementation <code>ResourceLockController</code>.
     * 
     * @return The default ResourceLockController implementation instance
     * 
     * @throws Exception 
     */
    public ResourceLockController getResourceLockController() throws Exception {
        
        ResourceLockController controller = null;
        
        try { 
            
            controller = getResourceLockController("1000");
            
            CommonResourceLockController.getInstance().setController(controller);
                    
            return controller;
            
        }
        catch(Exception e) { throw e; }
    
    } 
    
    /**
     * Get the default implementation <code>ResourceLockController</code> using a custom <code>lockName</code>
     * 
     * @param useUniqueLockName Something that is not indicated in the configuration file
     * @param lockName The lock name to use
     * 
     * @return The default ResourceLockController implementation instance
     * 
     * @throws Exception 
     */
    public ResourceLockController getResourceLockController(boolean useUniqueLockName, String lockName) throws Exception {
       
        ResourceLockController controller = null;
        
        try { 
            
            String lock_name = "1000";
            
            if(lockName != null) lock_name = lockName;
            
            controller = new JenkinsResourceLockController(useUniqueLockName, lock_name);
          
            CommonResourceLockController.getInstance().setController(controller);
                    
            return controller;
           
        }
        catch(Exception e) { throw e; }
    
    } 
    
    /**
     * Get the default implementation <code>ResourceLockController</code> using a custom <code>lockName</code>
     * 
     * @param useUniqueLockName Something that is not indicated in the configuration file
     * @param lockName The lock name to use
     * @param resourceName
     * 
     * @return The default ResourceLockController implementation instance
     * 
     * @throws Exception 
     */
    public ResourceLockController getResourceLockController(boolean useUniqueLockName, String lockName, String resourceName) throws Exception {
        
        ResourceLockController controller = null;
        
        try { 
            
            String lock_name = "1000";
            
            if(lockName != null) lock_name = lockName;
            
            controller = new JenkinsResourceLockController(useUniqueLockName, lock_name, resourceName);
            
            CommonResourceLockController.getInstance().setController(controller);
                   
            return controller;
         
        }
        catch(Exception e) { throw e; }
    
    } 

}