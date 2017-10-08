package org.automateit.example.test;

import org.apache.log4j.Logger;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.automateit.test.TestBase;

import org.automateit.data.DataDrivenInput;
import org.automateit.util.Utils;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing - reading and writing data files.
 * 
 * @author mburnside
 */
public class DataTests extends TestBase {
    
    protected Utils utils = new Utils();
    
    protected DataDrivenInput xlsInput = null;
    
    protected DataDrivenInput csvInput = null;
    
    protected DataDrivenInput pipeDelimitedFileInput = null;
    
    public static final String DATA_SET_ID_1_NAME = "data_set_id_1";
    
    public static final String DATA_SET_ID_2_NAME = "data_set_id_2";
    
    protected static Logger logger = Logger.getLogger(DataTests.class);
    
    /**
     * Test a failed login use case - using spaced.
     *
     * @throws Exception 
     */
    @BeforeTest(description = "Load the data from the files and prepare to read", groups = { "example_read_input_data" })
    public void loadInputDataFiles() throws Exception {
      
        try { 
           
            xlsInput = setupDataDrivenInput("data/example.xlsx");
            
            csvInput = setupDataDrivenInput("data/example.csv");
            
            pipeDelimitedFileInput = setupDataDrivenInput("data/example.txt");
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    
    /**
     * Print data from an Excel file
     *
     * @throws Exception 
     */
    @Test(description = "Print data from an Excel file", groups = { "example_read_input_data" })
    public void test_A_Print_Data_From_Excel_File() throws Exception {
      
        try { 
            
            logger.info("Excel data for data set id: " + DATA_SET_ID_1_NAME + "|" + xlsInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_1_NAME, 1)); 
            logger.info("Excel data for data set id: " + DATA_SET_ID_2_NAME + "|" + xlsInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 1) + "|" + xlsInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 2) + "|" + xlsInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 3)); 
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Print data from a CSV file
     *
     * @throws Exception 
     */
    @Test(description = "Print data from an CSV file", groups = { "example_read_input_data" })
    public void test_B_Print_Data_From_CSV_File() throws Exception {
      
        try { 
            
            logger.info("CSV data for data set id: " + DATA_SET_ID_1_NAME + "|" +  csvInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_1_NAME, 1)); 
            logger.info("CSV data for data set id: " + DATA_SET_ID_2_NAME + "|" +  csvInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 1) + "|" + xlsInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 2)); 
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Print data from a CSV file
     *
     * @throws Exception 
     */
    @Test(description = "Print data from an Excel file", groups = { "example_read_input_data" })
    public void test_C_Print_Data_From_Pipe_Delimited_File() throws Exception {
      
        try { 
            
            logger.info("Pipe Delimited File data for data set id: " + DATA_SET_ID_1_NAME + "|" +  pipeDelimitedFileInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_1_NAME, 1) + "|" + pipeDelimitedFileInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_1_NAME, 2)); 
            logger.info("Pipe Delimited File data for data set id: " + DATA_SET_ID_2_NAME + "|" +  pipeDelimitedFileInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 1) + "|" + pipeDelimitedFileInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 2) + "|" + pipeDelimitedFileInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 3) + "|" + pipeDelimitedFileInput.returnInputDataForDataIdAndColumnNumber(DATA_SET_ID_2_NAME, 4)); 
        
        }
        catch(Exception e) { throw e; }
    
    }
    
}