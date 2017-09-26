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

package com.automateit.util;

import java.io.File;

import javax.imageio.ImageIO;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;

/**
 * Screenshot capture class. Use this to capture screenshots.
 * 
 * @author mburnside
 */
public class ScreenshotCapture {

    public static final String PNG = "png";
    
    public static final String JPG = "jpg";
    
    public static final String GIF = "gif";
    
    /**
     * Default Constructor
     */
    public ScreenshotCapture() { }
    
    /**
     * Take a screenshot.
     * 
     * @throws Exception 
     * 
     * @return The name of the screenshot
     */
    public String doScreenshot() throws Exception {
        
        File screenshotFile = null;
    
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
    
        Rectangle screenBounds = new Rectangle(0, 0, screenDim.width, screenDim.height);

        try { 
            
            Robot robot = new Robot();
        
            BufferedImage image =  robot.createScreenCapture(screenBounds);
        
            screenshotFile = new File("screenshots" + File.separator
                                   + "image" + System.currentTimeMillis() + ".png");
            ImageIO.write(image, "png", screenshotFile);
        
        }
        catch(Exception e) { throw e; }
    
        return screenshotFile.getPath();
        
    }
    
    /**
     * Do a screenshot.
     * 
     * @param filename The filename (must include full path) to save to.
     * @param extension The extension of image type (png, jpg, gif, etc)
     * 
     * @throws Exception 
     */
    public void doScreenshot(String filename, String extension) throws Exception {
    
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
    
        Rectangle screenBounds = new Rectangle(0, 0, screenDim.width, screenDim.height);

        try { 
            
            Robot robot = new Robot();
        
            BufferedImage image =  robot.createScreenCapture(screenBounds);
        
            File screenshotFile = new File(filename + "." + extension);
            ImageIO.write(image, extension, screenshotFile);
        
        }
        catch(Exception e) { throw e; }
        
    }

}
