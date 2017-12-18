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
package org.automateit.ocr;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import java.io.File;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;

import org.apache.log4j.Logger;

/**
 * This class is the control center for operating a video capture for any
 * tests being run by this framework
 * 
 * @author mburnside
 */
public class OCRProcessor {
    
    /**
     * Scale factor of the price image for Tesseract OCR. It strongly affects
     * the precision of the OCR. Recommended: 2 for AffineTransform; 3 for
     * Graphics.
     */
    public static final int GRAPHICS_SCALE = 3;
    
    /**
     * Scale factor of the price image for Tesseract OCR. It strongly affects
     * the precision of the OCR. Recommended: 2 for AffineTransform; 3 for
     * Graphics.
     */
    public static final int AFFLINE_SCALE = 2;
    
    /**
     *  Logging object
     */
    protected static Logger logger = Logger.getLogger(OCRProcessor.class);
    
    /**
     * The buffered imaged for the initial image
     */
    private BufferedImage image = null;
    
    /**
     * The buffered imaged for the scaled image
     */
    private BufferedImage scaledImage = null;
    
    /**
     * The datapath to the tesseract data files
     */
    private String datapath = "resources/tessdata";
    
    /**
     * The language to read in the image
     */
    private String language = "eng";
    
    /**
     * Default Constructor.
     */
    public OCRProcessor() { }
    
    /**
     * Set the datapath to the tesseract data files
     * 
     * @param datapath 
     */
    public void setDatapath(String datapath) { this.datapath = datapath; }
    
    /**
     * Set the language to read in the image
     * 
     * @param language 
     */
    public void setLanguage(String language) { this.language = language; }
   
    /**
     * Get the text in the image.
     * 
     * @param filename
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getTextInImage(String filename) throws Exception {
        
        logger.info("Perform OCR on this iamge: " + filename);
        
        try {
           
            image = ImageIO.read(new File(filename).toURI().toURL());
            
            logger.info("Image width: " + image.getWidth());
            logger.info("Image height: " + image.getHeight());

            if(image.getWidth() < 20 || image.getHeight() < 20) throw new Exception("Image is too small too perform OCR");
            
            // mburnside note about this commente code: leave it here for now. I commented it out because affline transform not working for
            // some screenshots, although works with demo images in the framework 
            // for now, just use graphics api and not affline transform
            
            //if(filename.trim().toLowerCase().endsWith(".png")) {
                
            //    scaledImage = new BufferedImage(image.getWidth() * AFFLINE_SCALE, image.getHeight() * AFFLINE_SCALE, BufferedImage.TYPE_INT_RGB);
                
            //    scaleImageUsingAfflineTransform();
            
            //}
            //else {
                
                scaledImage = new BufferedImage(image.getWidth() * GRAPHICS_SCALE, image.getHeight() * GRAPHICS_SCALE, BufferedImage.TYPE_INT_RGB);
                
                scaleImageUsingGraphics();
                
            //}
	
            Tesseract.getInstance().setDatapath(datapath);
            Tesseract.getInstance().setLanguage(language);
            
            String text = Tesseract.getInstance().doOCR(scaledImage);
            
            logger.info("Returning text from OCR image processing:" + text);
            
            return text;
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scale the image using the Java Graphics API.
     * 
     * @throws Exception 
     */
    protected void scaleImageUsingGraphics() throws Exception {
        
        try {
            
            logger.info("Preparing original image for OCR using Java Graphics API");
            
            Graphics2D grph = (Graphics2D) scaledImage.getGraphics();
		    
            grph.scale(GRAPHICS_SCALE, GRAPHICS_SCALE);
	    
            grph.drawImage(image, 0, 0, null);
	    
            grph.dispose();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Scale the image using the Affline Transform API.
     * 
     * @throws Exception 
     */
    protected void scaleImageUsingAfflineTransform() throws Exception {
        
        /**
         * Type of transformation in case AffineTransform is used. It strongly
         * affects the precision of the OCR. Types are
         * AffineTransformOp.TYPE_BICUBIC (recommended),
         * AffineTransformOp.TYPE_BILINEAR and
         * AffineTransformOp.TYPE_NEAREST_NEIGHBOR.
         */
        
        try {
            
            logger.info("Preparing original image for OCR using Affline Transform");
            
            AffineTransform at = new AffineTransform();
	      
            at.scale(AFFLINE_SCALE, AFFLINE_SCALE);
	 
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
             
            logger.info("Scaled Image: " + scaledImage);
            
            logger.info("Image: " + image);
           
            scaledImage = scaleOp.filter(image, scaledImage);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}