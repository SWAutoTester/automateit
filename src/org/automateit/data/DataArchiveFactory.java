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

/**
 * This class is a lightweight factory that returns an obsfuscated instance of 
 * a <code>DataArchive</code> implementation.
 * 
 * @author mburnside
 */
public class DataArchiveFactory {
   
    /**
     * Excel spreadsheet - XLS file extension.
     */
    public static final int XLS = 0;
    
    /**
     * Excel spreadsheet - XLSX file extension.
     */
    public static final int XLSX = 1;
    
    /**
     * Pie Chart.
     */
    public static final int PIECHART = 2;
    
    /**
     * XY Bar Chart.
     */
    public static final int XYBARCHART = 3;
    
    /**
     * Comma separated values.
     */
    public static final int CSV = 4;
    
    /**
     * Comma separated values - to append to the data set.
     */
    public static final int CSV_APPEND = 5;
    
    /**
     * Pipe-delimited file.
     */
    public static final int PIPEDELIMITED = 6;
    
    /**
     * Pipe-delimited file - to append to the data set.
     */
    public static final int PIPEDELIMITED_APPEND = 7;
    
    /**
     * Default Constructor.
     */
    public DataArchiveFactory() { }

    /**
     * Get an implementation <code>DataArchive</code>.
     * 
     * @param id The id of the implementation to use
     * 
     * @return A DataArchive instance
     * 
     * @throws Exception 
     */
    public DataArchive getDataArchive(int id) throws Exception {
        
        try {
        
            switch (id) {
           
                case XLS:
                
                    return new XLSDataArchive();
                    
                case XLSX:
                
                    return new XLSXDataArchive();
                        
                case CSV:
                
                    return new CSVDataArchive();
                    
                case CSV_APPEND:
                
                    return new CSVAppendDataArchive();
                        
                case PIPEDELIMITED:
                
                    return new PipeDelimitedDataArchive();
                    
                case PIPEDELIMITED_APPEND:
                
                    return new PipeDelimitedAppendDataArchive();
            
                default:
                        
                    return new CSVDataArchive();
        
            }
            
        }
        catch(Exception e) { throw e; }
    
    }    

}