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

import org.apache.log4j.Logger;

/**
 * Data archive interface for archiving data in CSV (comma separated value) 
 * output. 
 * 
 * This class always writes to a pre-existing file.
 * 
 * @author mburnside
 */
public class CSVAppendDataArchive extends CSVDataArchive {
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(CSVAppendDataArchive.class);
    
    /**
     * Default constructor.
     */
    public CSVAppendDataArchive() { }
    

    /**
     * Save the data to a file. Append to a pre-existing file.
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    @Override
    public void saveData(String filename) throws Exception {
        
        try { saveDataAppendToFile(filename, DELIMITER); }
        catch(Exception e) { throw e; }
        
    }
    
}

