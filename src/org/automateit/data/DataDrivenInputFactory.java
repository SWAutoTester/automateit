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

/**
 * This class is a lightweight factory that returns an obsfuscated instance of 
 * a <code>DataDrivenInput</code> implementation.
 * 
 * @author mburnside
 */
public class DataDrivenInputFactory {
   
    /**
     * Excel spreadsheet.
     */
    public static final int EXCEL = 0;
    
    /**
     * Comma separated values.
     */
    public static final int CSV = 1;
    
    /**
     * Pipe-delimited file.
     */
    public static final int PIPEDELIMITED = 2;
    
    /**
     * Default Constructor.
     */
    public DataDrivenInputFactory() { }

    /**
     * Get an implementation <code>DataDrivenInput</code>.
     * 
     * @param filename The file of input parameter values
     * @param id The id of the implementation to use
     * 
     * @return A DataDrivenInput instance
     * 
     * @throws Exception 
     */
    public DataDrivenInput getDataDrivenInput(String filename, int id) throws Exception {
        
        try {
        
            switch (id) {
           
                case EXCEL:
                
                    return new DataDrivenExcel(filename);
                        
                case CSV:
                
                    return new DataDrivenCSV(filename);
                        
                case PIPEDELIMITED:
                
                    return new DataDrivenPipeDelimited(filename);
            
                default:
                        
                    return new DataDrivenExcel(filename);
        
            }
            
        }
        catch(Exception e) { throw e; }
    
    }    

}