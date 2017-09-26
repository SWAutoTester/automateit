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

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * This class allows for use of an Excel spreadsheet to provide input data to
 * a set of tests.
 * 
 * The implementation for reading and parsing the files is POI and XSSF:
 * 
 * http://poi.apache.org/spreadsheet/
 * 
 * POI-HSSF and POI-XSSF - Java API To Access Microsoft Excel Format Files
 * 
 * @author mburnside
 */
public class DataDrivenExcel implements DataDrivenInput {
   
    /**
     * Map instance that has references to all data sets.
     */
    private Map inputParameters = new HashMap();
    
    /**
     * Copy constructor. Use of XSSF to parse the file and create the data sets
     * and put them into a collection.
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public DataDrivenExcel(String filename) throws Exception {

        try {

            XSSFWorkbook wb = new XSSFWorkbook(filename);
            
            XSSFSheet sheet = wb.getSheetAt(0);
            
            Iterator rows = sheet.rowIterator(); 
            
            while(rows.hasNext()) {                                
                
                Map rowData = new HashMap();
                
                XSSFRow row = (XSSFRow) rows.next();
                
                Iterator cells = row.cellIterator();
                
                while( cells.hasNext() ) {
                    
                    XSSFCell cell = (XSSFCell) cells.next();
                    
                    switch (cell.getCellType()) {
                        
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            
                            rowData.put(cell.getColumnIndex(), String.valueOf((new Double(cell.getNumericCellValue())).intValue()));
                            
                            if(cell.getColumnIndex() == 0) {
                                
                                inputParameters.put(String.valueOf((new Double(cell.getNumericCellValue())).intValue()), rowData);
                            
                            }
                            
                            break;
                        
                        case XSSFCell.CELL_TYPE_STRING: 
                            
                            rowData.put(cell.getColumnIndex(), cell.getStringCellValue());
                            
                            if(cell.getColumnIndex() == 0) {
                                
                                inputParameters.put(cell.getStringCellValue(), rowData);
                            
                            }
                            
                            break;
                        
                        default:
                            
                            break;
                    
                    }                                       
                
                }
                
            }
            
        } 
        catch (Exception e) { throw e; }

    }
    
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
        
        try { return (Map) inputParameters.get(dataId); }
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
        
        try { return (String)((Map)inputParameters.get(String.valueOf(dataId))).get(columnNumber); }
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
        
        try { return (String)((Map)inputParameters.get(dataId)).get(columnNumber); }
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

