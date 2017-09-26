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

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Data archive interface for archiving to XLSX (Excel) files. Do not use this 
 * class to save to XLS format.
 * 
 * @author mburnside
 */
public class XLSXDataArchive extends XLSDataArchiveBase implements DataArchive {
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(XLSXDataArchive.class);
    
    /**
     * Default constructor.
     */
    public XLSXDataArchive() { }
    
    /**
     * Save the data to a file.
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public void saveData(String filename) throws Exception {
        
        try {
            
            log.debug("Attempting save data to filename: " + filename);
            
            saveData(new XSSFWorkbook(), filename);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}

