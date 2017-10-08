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

package org.automateit.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class allows for use of a delimiter separated value file to provide 
 * input data to a set of tests.
 * 
 * @author mburnside
 */
public class DelimitedDataDrivenInputBase extends DataDrivenInputBase implements DataDrivenInput {
    
    /**
     * Copy constructor.
     * 
     * @param filename
     * @param delimiter
     * 
     * @throws Exception 
     */
    public DelimitedDataDrivenInputBase(String filename, String delimiter) throws Exception {

        String line = null;
        
        try {
        
            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        
            while ((line = reader.readLine()) != null) {
                
                if(hasDataSetId(line, delimiter)) { inputParameters.put(getDataSetId(line, delimiter).trim(), getDataRow(line, delimiter)); }
        
            }
            
        } catch ( Exception e ) { throw e; }

    }
    
    /**
     * Determine if the set of data has a data set id.
     * 
     * @param s
     * @param delimiter
     * 
     * @return 
     */
    protected boolean hasDataSetId(String s, String delimiter) { 
        
        try {
            
            StringTokenizer st = new StringTokenizer(s, delimiter);
        
            return (st.countTokens() > 0); 
            
        }
        catch(Exception e) { return false; }
    
    }
    
    /**
     * Get the data set id for a set of data.
     * 
     * @param s
     * @param delimiter
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected String getDataSetId(String s, String delimiter) throws Exception { 
        
        try {
            
            StringTokenizer st = new StringTokenizer(s, delimiter);
            
            return st.nextToken().trim();
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Get the row for a set of data separated by a <code>delimitr</code>.
     * 
     * @param s
     * @param delimiter
     * 
     * @return
     * 
     * @throws Exception 
     */
    protected Map getDataRow(String s, String delimiter) throws Exception {
        
        Map rowData = null;
        
        try { 
            
            rowData = new HashMap();
            
            String[] array = s.split(delimiter);
            
            for(int i = 0; i < array.length; i++) {
               
                if((array[i] != null) || (array[i].trim().length() > 0)) rowData.put(String.valueOf(i), array[i]);
            
            }
            
            return rowData;
          
        }
        catch(Exception e) { throw e; }
        
    }
    
}