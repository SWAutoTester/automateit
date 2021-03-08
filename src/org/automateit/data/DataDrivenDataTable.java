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

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import io.cucumber.datatable.DataTable;

import org.apache.log4j.Logger;

/**
 * This class allows for DataDrivenInput API access to the Cucumber DataTable.
 * 
 * It transforms the Cucumber-native DataTable collection object format
 * to the AutomateIt! DataDrivenInput API spec.
 * 
 * This allows for the advantages:
 * 
 * 1) Consistent API as we already use for DataDrivenInput (CSV, etc)
 * 2) Obfuscates the number of columns to explicitly program for, allowing
 *    this object to handle the complexity
 * 3) The User does not need to know any Cucumber-DataTable specific API
 *    to create data objects and access the data in those objects
 * 4) Allows the user to input any size row and columns and not hard code - 
 *    handles tables of ANY size (rows and columns)
 * 5) Avoid Java POJO/DTO maintenance when adding/removing properties of the object
 * 6) write much less code in code that uses the API
 * 
 * Example code:
 * 
 * Gherkin Scenario:
 * 
 * Scenario: login to app
 * Given resources
 * | type   | udid      | alias |
 * | android_tablet | 012345 | stage_tablet_android |
 * | iPhone7  | 678910  | stage_phone_1 |
 * | android5.0  | 76349 | stage_phone_2 |
 * 
 * Java code (Access as defined and done by Cucumber Step Definitions):
 * 
 * @Given("^I have the following resources map$")
 * public void resourcesDataMap(DataTable table) {
 * 
 *    DataDrivenInput inputData = new DataDrivenDataTable("alias", dataTable);
 * 
 *    String tabletType = inputData.get("stage_tablet_android", 0);
 *    String tabletUdid = inputData.get("stage_tablet_android", 1);
 */
public class DataDrivenDataTable extends DataDrivenInputBase implements DataDrivenInput {
   
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(DataDrivenDataTable.class);
    
    private int columnPrimaryKeyIndex = 0; // default is first column name in header (top) column
   
    /**
     * Copy constructor. 
     * 
     * @param columnPrimaryKeyName
     * @param dataTable
     */
    public DataDrivenDataTable(String columnPrimaryKeyName, DataTable dataTable) {

        try {
            
            setColumnPrimaryKeyIndex(columnPrimaryKeyName, dataTable.row(0));
            
            for(int i = 1; i < dataTable.height(); i++) {
                
                // get the row of the data table at index
                List<String> row​ = dataTable.row(i);
                
                inputParameters.put((String)row.get(columnPrimaryKeyIndex), getDataRow(row​));
                
            }
           
        } 
        catch (Exception e) { logger.error(e); }

    }
    
    /**
     * Get the row
     * 
     * @param row
     * 
     * @return
     */
    protected Map getDataRow(List<String> row​) {
        
        Map rowData = null;
       
        rowData = new HashMap();
        
        String[] array = row.toArray(new String[0]);
           
        for(int i = 0; i < array.length; i++) if((array[i] != null) || (array[i].trim().length() > 0)) { rowData.put(String.valueOf(i), array[i]); }
            
        return rowData;
       
    }
    
    /**
     * Identify the index of the column in the header row (the tope row)
     * of the DataTable.
     * 
     * @param columnPrimaryKeyName
     * @param row 
     */
    private void setColumnPrimaryKeyIndex(String columnPrimaryKeyName, List<String> row) {
        
        logger.debug("Setting the DataTable header column primary key index: " + columnPrimaryKeyName + "|" + row.size());
      
        String[] array = row.toArray(new String[0]);
       
        logger.debug("Table headers: " + array);
       
        for(int i = 0; i < array.length; i++) {
            
            logger.debug("Table header match check: " + array[i].trim());
            
            // if the column name that we identify as the primary key is the same
            // name as the column header name value, then that is the index. This 
            // index is then used as the data driven inout row primary key (because
            // that column contains unique values) 
            if(columnPrimaryKeyName.trim().equals(array[i].trim())) {
                
                logger.debug("DataTable header match check found at index: " + i);
                
                columnPrimaryKeyIndex = i;
                
                break;
                
            }
            
        }
        
    }

}

