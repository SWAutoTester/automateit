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

package com.automateit.util;

import java.util.Date;

/**
 * This class is a data transfer object that has details of web page load
 * performance.
 * 
 * @author mburnside
 */
public class PerformanceCaptureBean  {

    /**
     * Page load start time
     */
    private long startTime = 0;
    
    /**
     * Page load end time
     */
    private long endTime = 0;
    
    /**
     * Amount of time (milli-sec) hard-coded delay
     */
    private long sleepTime = 0;
    
    /**
     * The name of the page that was loaded
     */
    private String pageName = null;
    
    /**
     * The name of the page that was loaded from
     */
    private String fromPageName = null;
    
    /**
     * Default Constructor
     */
    public PerformanceCaptureBean() { }
    
    /**
     * Mark the start time.
     * 
     * @param fromPageName The name of the page that was loaded from
     */
    public void markStartTime(String fromPageName) { 
    	
    	this.startTime = getCurrentTime();
    	this.fromPageName = fromPageName;
    }
    
    /**
     * Mark the end time.
     * 
     * @param pageName The name of the page that has completed loading.
     */
    public void markEndTime(String pageName) { 
        
        this.endTime = getCurrentTime(); 
        this.pageName = pageName;
    
    }
    
    /**
     * Get the current time in <code>long</code> format.
     * 
     * @return the current time 
     */
    private long getCurrentTime() { return (new Date()).getTime(); }
    
    /**
     * Get the total time for page load.
     * 
     * @return 
     */
    public long getTime() { return endTime - startTime; }
    
    /**
     * Get the page name (or message).
     * 
     * @return 
     */
    public String getPageName() { return this.pageName; }
    
    /**     
     * @return The name of the page that was loaded from
     */
    public String getFromPageName() { return this.fromPageName; }
    
    /**
     * 
     * @param millis (in milli-sec) that page was put to sleep
     */
    public void addSleepTime(long millis) {
    	this.sleepTime += millis;    	
    }
    
    public long getSleepTime() { return this.sleepTime; }
    
    public String getSleepTimeString() {
    	    	
    	String s = "";
    	if (getSleepTime() > 0)
    		s = "* hard-coded delay for " + getSleepTime();
    	
    	return s;
    }
    
}
