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

import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;
        
/**
 * Data archive base class for archiving to XLS or XLSX (Excel) files.
 * 
 * @author mburnside
 */
public class XLSDataArchiveBase extends DataArchiveBase {
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(XLSDataArchiveBase.class);
    
    /**
     * Save the data to a file.
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public void saveData(Workbook workbook, String filename) throws Exception {
        
        try {
            
            CreationHelper createHelper = workbook.getCreationHelper();
    
            Sheet sheet = workbook.createSheet("Saved Data");
    
            for(int i = 0; i < list.size(); i++) {
                
                Row row = sheet.createRow((short)i);
                
                String[] data = list.get(i); 
    
                for (int j = 0; j < data.length; j++) {
                    
                    log.debug("For row: " + i + ", creating column: " + j + " with data: " + data[j]);
                    
                    row.createCell(j).setCellValue(createHelper.createRichTextString(data[j]));
            
                }
        
            }
            
            FileOutputStream fileOut = new FileOutputStream(filename);
            
            workbook.write(fileOut);
            
            fileOut.close();
        
        }
        catch(Exception e) { log.error(e); throw e; }
        
    }
    
}

