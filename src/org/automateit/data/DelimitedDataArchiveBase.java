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

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
        
/**
 * Data archive base class for archiving to any kind of delimited files.
 * 
 * @author mburnside
 */
public class DelimitedDataArchiveBase extends DataArchiveBase {
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(DelimitedDataArchiveBase.class);
    
    /**
     * Save the data to a file.
     * 
     * @param filename The filename to save the data to
     * @param append To append to existing file or create a new file
     * @param delimiter the delimiter (text separator) to use
     * 
     * @throws Exception 
     */
    public void saveData(String filename, boolean append, String delimiter) throws Exception {
        
        try {
            
            PrintWriter writer = new PrintWriter(new FileWriter(new File(filename), append));
            
            for(int i = 0; i < list.size(); i++) {
                
                String[] data = list.get(i); 
    
                for(int j = 0; j < data.length; j++) writer.print(data[j] + delimiter);
                
                writer.print("\n");
        
            }
                       
            writer.close();
            
            writer = null;
        
        }
        catch(Exception e) { log.error(e); throw e; }
        
    }
    
    /**
     * Save the data to a file. Creates a new file, overwriting any 
     * pre-existing file
     * 
     * @param filename
     * @param delimiter the delimiter (text separator) to use
     * 
     * @throws Exception 
     */
    public void saveDataCreateNewFile(String filename, String delimiter) throws Exception {
        
        try {
            
            log.debug("Attempting save data to filename in csv format (create new file): " + filename);
            
            saveData(filename, false, delimiter);
            
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Save the data to a file. Appends to the pre-existing file.
     * 
     * @param filename
     * @param delimiter the delimiter (text separator) to use
     * 
     * @throws Exception 
     */
    public void saveDataAppendToFile(String filename, String delimiter) throws Exception {
        
        try {
            
            log.debug("Attempting save data to filename in csv format (append to file): " + filename);
            
            saveData(filename, true, delimiter);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    
}

