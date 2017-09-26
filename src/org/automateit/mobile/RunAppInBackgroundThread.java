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
package org.automateit.mobile;

import java.time.Duration;

import io.appium.java_client.AppiumDriver;

import org.apache.log4j.Logger;

/**
 * This allows to run the application in the backgorund while 
 * 
 * @author mburnside
 */
public class RunAppInBackgroundThread extends Thread {
    
    /**
     * The AppiumDriver instance
     */
    private AppiumDriver driver = null;
    
    /**
     * The time duration/delay to keep the app in the background
     */
    private int delay = 60;
    
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(RunAppInBackgroundThread.class);
    
    /**
     * Copy Constructor.
     * 
     * @param driver
     * @param delay
     * 
     * @throws Exception 
     */
    public RunAppInBackgroundThread(AppiumDriver driver, int delay) throws Exception {
    
        try { 
            
            this.driver = driver;
            
            this.delay = delay;
           
        }
        catch(Exception e) { throw e; }
    
    }
    
    @Override
    public void run() {
        
        try {
            
            logger.info("Running the mobile application background thread delay for this many seconds: " + this.delay);
            
            this.driver.runAppInBackground(Duration.ofSeconds(this.delay));
            
            logger.info("Returning the mobile application from background");
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}

