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

package org.automateit.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class keeps track of page loading performance.
 * 
 * @author mburnside
 */
public class PerformanceCapture {
    
    private PerformanceCaptureBean bean = null;
    
    /**
     * Queue object for collecting commands
     */
    private List<PerformanceCaptureBean> list = new LinkedList<PerformanceCaptureBean>();
    
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(PerformanceCapture.class);
    
    /**
     * PerformanceCapture instance
     */
    private static PerformanceCapture instance = new PerformanceCapture();
    
    /**
     * Default Constructor
     */
    public PerformanceCapture() { }
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static PerformanceCapture getInstance() { return instance; }
    
    /**
     * Stop the timer and mark the end time of the page load.
     * 
     * @param pageName the name of the page that just loaded 
     */
    public void stop(String pageName) { 
        
        if(bean == null) return;
        
        logger.info("Marking the end time for page loading: " + pageName);
        
        this.bean.markEndTime(pageName);
        
        list.add(this.bean);
        
        this.bean = null;
        
    }
    
    /**
     * Mark the start of page load.
     * 
     * @param fromPageName the name of the page that was loaded from
     */
    public void start(String fromPageName) {
        
        this.bean = new PerformanceCaptureBean();
        
        this.bean.markStartTime(fromPageName);
        
    }
    
    public void addSleepTime(long millis) { if(this.bean!=null) this.bean.addSleepTime(millis); } 
    
    /**
     * Removes all commands in the list.
     */
    public void clear() { list.clear(); }
    
    /**
     * Get all of the commands in the list.
     * 
     * @return 
     */
    public List<PerformanceCaptureBean> getAllInList() { return this.list; }
    
    /**
     * Get the size of the list.
     * 
     * @return 
     */
    public int getSize() { return list.size(); }
    
    /**
     * Indicates if command list contains no commands
     * 
     * @return 
     */
    public boolean isEmpty() { return (getSize() == 0); }
            
}