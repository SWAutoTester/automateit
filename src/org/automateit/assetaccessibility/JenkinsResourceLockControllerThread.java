package org.automateit.assetaccessibility;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * This is an implementation class for the Jenkins-based resource locking mechanism - ttl (has not been tested)
 */
public class JenkinsResourceLockControllerThread extends Thread {
     
    /**
     * The jenkins resource lock controller instance we will need
     */
    private JenkinsResourceLockController jenkinsResourceLockController = null;
    
    /**
     * This is the TTL end value, when the app does the unlock
     */
    private long endTTL = 0;
    
    /**
     * The logging class to use for useful information
     */
    protected Logger logger = Logger.getLogger(JenkinsResourceLockControllerThread.class);
    
    /**
     * Copy Constructor.
     * 
     * @param jenkinsResourceLockController
     * 
     * @throws Exception 
     */
    public JenkinsResourceLockControllerThread(JenkinsResourceLockController jenkinsResourceLockController) throws Exception { 
        
        this.jenkinsResourceLockController = jenkinsResourceLockController; 
        
        try { if(jenkinsResourceLockController.getTTL() != 0) { endTTL = jenkinsResourceLockController.getTTL() + (new Date()).getTime(); } }
        catch(Exception e) { }
    
    }
    
    /**
     * The executable part of the thread
     */
    public void run() {
        
        try { 
            
            if(endTTL != 0) { 
                
                while((new Date()).getTime() < endTTL) { sleep(jenkinsResourceLockController.getWaitTime()); } 
                
                // once the current time has passed the end TTL, send the unlock
                jenkinsResourceLockController.unlock();
            
            }
            
        }
        catch(Exception e) { }
        
    }
    
   
}
