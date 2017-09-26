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

package com.automateit.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * This class is the base class for reading data form input files.
 * 
 * @author mburnside
 */
public class DataDrivenInputBase {
   
    /**
     * Map instance that has references to all data sets.
     */
    protected Map inputParameters = new HashMap();
    
    /**
     * Return a set of input data for a given <code>dataId</code> integer.
     * 
     * @param dataId The data Id of type <code>integer</code>
     * 
     * @return The data set for the given <code>dataId</code>
     * 
     * @throws Exception 
     */
    public Map returnInputDataForDataId(int dataId) throws Exception {
        
        try { return (Map) inputParameters.get(String.valueOf(dataId)); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Return a set of input data for a given <code>dataId</code>.
     * 
     * @param dataId The data Id of type <code>String</code>
     * 
     * @return The data set for the given <code>dataId</code>
     * 
     * @throws Exception 
     */
    public Map returnInputDataForDataId(String dataId) throws Exception {
        
        try { 
            
            if(!hasDataId(dataId)) throw new Exception("Unable to return value because data set id does not exist in the input file: " + dataId);
            
            return (Map)inputParameters.get(dataId); 
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Convenience method for obtaining a single reference to data.
     * 
     * @param dataId
     * @param columnNumber
     * 
     * @return The reference to data (not a set of data)
     *  return null if dataId exists but cell for the particular columnNumber is blank
     *  
     * @throws Exception 
     */
    public String returnInputDataForDataIdAndColumnNumber(int dataId, int columnNumber) throws Exception {
        
        try { 
            
            if(!hasDataId(dataId)) throw new Exception("Unable to return value because data set id does not exist in the input file: " + dataId);
            
            return (String)((Map)inputParameters.get(String.valueOf(dataId))).get(columnNumber); 
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Convenience method for obtaining a single reference to data.
     * 
     * @param dataId
     * @param columnNumber
     * 
     * @return The reference to data (not a set of data)
     *  return null if dataId exists but cell for the particular columnNumber is blank
     *  
     * @throws Exception 
     */
    public String returnInputDataForDataIdAndColumnNumber(String dataId, int columnNumber) throws Exception {
        
        try { 
            
            if(!hasDataId(dataId)) throw new Exception("Unable to return value because data set id does not exist in the input file: " + dataId);
            
            return (String)((Map)inputParameters.get(dataId)).get(String.valueOf(columnNumber)); 
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Return true if the dataId exists, otherwise false
     * 
     * @param dataId
     * 
     * @return
     */
    public boolean hasDataId(int dataId) { return inputParameters.containsKey(String.valueOf(dataId)); }
    
    /**
     * Return true if the dataId exists, otherwise false
     * 
     * @param dataId
     * 
     * @return
     */
    public boolean hasDataId(String dataId) { return inputParameters.containsKey(dataId); }
    
    /**
     * Print out all input
     * 
     * @throws Exception 
     */
    public void printAllInput() throws Exception {
        
        try {
      
            Collection c = inputParameters.values();
  
            //obtain an Iterator for Collection
            Iterator itr = c.iterator();
   
            //iterate through HashMap values iterator
            while(itr.hasNext()) System.out.println(itr.next());
        
        }
        catch(Exception e) { throw e; }
        
    }
    
}
