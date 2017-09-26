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

import org.apache.log4j.Logger;

/**
 * Data archive interface for archiving data in CSV (comma separated value) 
 * output. 
 * 
 * This class always creates a new file, overwriting any pre-existing file.
 * 
 * @author mburnside
 */
public class CSVDataArchive extends DelimitedDataArchiveBase implements DataArchive {
    
    /**
     * The comma is used as the delimiter for csv files
     */
    protected final String DELIMITER = ","; 
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(CSVDataArchive.class);
    
    /**
     * Default constructor.
     */
    public CSVDataArchive() { }
    

    /**
     * Save the data to a file. Creates a new file, overwriting any 
     * pre-existing file
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public void saveData(String filename) throws Exception {
        
        try { saveDataCreateNewFile(filename, DELIMITER); }
        catch(Exception e) { throw e; }
        
    }
    
}

