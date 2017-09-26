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

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

/**
 * This class contains data about summary performance
 * 
 * @author mburnside
 */
public class SummaryPerformance {

    /**
     * Data collection object with page poad data for each page loaded.
     */	
    private Hashtable<String, SummaryPerformanceBean> pagePerformanceSummaries = new Hashtable<String, SummaryPerformanceBean>();
	
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(SummaryPerformance.class);
    
    /**
     * PerformanceCapture instance
     */
    private static SummaryPerformance instance = new SummaryPerformance();
    
    /**
     * Default Constructor
     */
    private SummaryPerformance() { }
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static SummaryPerformance getInstance() { return instance; }
    
    /**
     * Add to the record of performance data.
     * 
     * @param pagePerformance 
     */
    public void add(PerformanceCaptureBean pagePerformance) {
    	    
    	SummaryPerformanceBean pageSummary = pagePerformanceSummaries.get(pagePerformance.getPageName());
    	
        if(pageSummary == null) {
    	
            pageSummary = new SummaryPerformanceBean(pagePerformance.getPageName());    	
    	
            pagePerformanceSummaries.put(pageSummary.getPageName(), pageSummary);
    	
        }
    		    	
    	pageSummary.add(pagePerformance);
    
    }
    
    /**
     * Get the page summaries.
     * 
     * @return 
     */
    public ArrayList<SummaryPerformanceBean> getPageSummaries() {
    	
        return new ArrayList<SummaryPerformanceBean>(pagePerformanceSummaries.values());
    
    }
            
}