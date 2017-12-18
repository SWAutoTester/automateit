package org.automateit.example.test;

import org.apache.log4j.Logger;

import org.testng.annotations.Test;

import org.automateit.test.TestBase;

import org.automateit.ocr.OCRProcessor;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing - optical character recognition (OCR).
 * 
 * @author mburnside
 */
public class OCRTests extends TestBase {
  
    protected static Logger logger = Logger.getLogger(OCRTests.class);
   
    /**
     * Extract text from a PNG image 
     *
     * @throws Exception 
     */
    @Test(description = "Extract text from an image - PNG", groups = { "example_ocr" })
    public void test_A_Extract_Text_From_Image_OCR_PNG() throws Exception {
      
        try { 
            
            OCRProcessor ocrProcessor = new OCRProcessor();
            
            ocrProcessor.getTextInImage("resources/images/ocrimage.PNG"); 
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Extract text from a JPG image 
     *
     * @throws Exception 
     */
    @Test(description = "Extract text from an image - JPG", groups = { "example_ocr" })
    public void test_B_Extract_Text_From_Image_OCR_JPG() throws Exception {
      
        try { 
            
            OCRProcessor ocrProcessor = new OCRProcessor();
            
            ocrProcessor.getTextInImage("resources/images/ocrimage.jpg"); 
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Extract text from a TIFF image 
     *
     * @throws Exception 
     */
    @Test(description = "Extract text from an image - TIFF", groups = { "example_ocr" })
    public void test_C_Extract_Text_From_Image_OCR_TIFF() throws Exception {
      
        try { 
            
            OCRProcessor ocrProcessor = new OCRProcessor();
            
            ocrProcessor.getTextInImage("resources/images/ocrimage.tiff"); 
        
        }
        catch(Exception e) { throw e; }
    
    }
    
}