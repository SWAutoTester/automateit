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

package org.automateit.videorecorder;

import java.awt.*;
import java.util.*;
import java.io.*;

import org.monte.media.*;
import org.monte.media.math.*;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

import org.monte.screenrecorder.ScreenRecorder;

import org.apache.log4j.Logger;

import org.automateit.util.Utils;

/**
 * This video recorder uses Monte Test Recorder API to capture video.
 * 
 * More information about this 3rd party API can be found here:
 * 
 * http://www.ontestautomation.com/creating-a-video-capture-of-your-selenium-tests-using-monte-screen-recorder/
 * 
 * http://www.randelshofer.ch/monte/
 *  
 * @author mburnside
 */
public class MonteVideoRecorder implements VideoRecorder {
    
    /**
     *  Logging object
     */
    protected static Logger logger = Logger.getLogger(MonteVideoRecorder.class);
    
    /**
     * The 3rd party implementation object that captures the video
     */
    private ScreenRecorder videoRecorder = null;
    
    /**
     * The graphics configurator
     */
    private GraphicsConfiguration gc = null;
    
    /**
     * utils class
     */
    private Utils utils = new Utils();
    
    /**
     * The default filename that the stopped video is saved as
     */
    public static final String DEFAULT_FILENAME = "test";
    
    /**
     * The default file extension that the stopped video is saved as
     */
    public static final String DEFAULT_FILE_EXTENSION = ".avi";
    
    /**
     * The saved video recording filename
     */
    public String savedFilename = null;
    
    /**
     * Default Constructor
     */
    public MonteVideoRecorder() { 
        
        try { this.gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(); }
        catch(Exception e) { }
    
    }
    
    /**
     * Start this video recorder.
     * 
     * @throws Exception 
     */
    public void start() throws Exception {
        
        try { 
            
            logger.info("Starting the video recorder");
            
            
            if(this.videoRecorder == null) {
                
                // initialize the screen recorder:
                // - default graphics configuration
                // - full screen recording
                // - record in AVI format
                // - 15 frames per second
                // - black mouse pointer
                // - no audio
                // - save capture to predefined location
         
                videoRecorder = new ScreenRecorder(gc,
            
                    gc.getBounds(),
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                    CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                    DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                    QualityKey, 1.0f,
                    KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null,
                    new File(utils.getBaseScreenshotsDirectory()));
   
                videoRecorder.start();
            
            }
            
            logger.info("Video recorder is started|" + this.videoRecorder);
        
        }
        catch(Exception e) { logger.error("Error while starting the video recording: " + e); e.printStackTrace(); throw e; }
        
    }
    
    /**
     * Stops this video recorder.
     * 
     * @throws Exception 
     */
    public void stop() throws Exception {
        
        try { 
            
            logger.info("Stopping the video recorder|" + this.videoRecorder);
            
            this.videoRecorder.stop();
            
            this.videoRecorder = null;
        
        }
        catch(Exception e) { logger.error("Error while stopping video recording: " + e); throw e; }
        
    }
    
    /**
     * Save the video to a file.
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public void save(String filename) throws Exception { 
        
        try { 
            
            logger.info("Copying default video recording file saved as: " + getDefaultNamedCreatedVideoFile() + " to file: " + filename);
            
            this.savedFilename = utils.getBaseScreenshotsDirectory() + filename;
            
            utils.copyFile(utils.getBaseScreenshotsDirectory() + getDefaultNamedCreatedVideoFile(), this.savedFilename + DEFAULT_FILE_EXTENSION); 
            
            utils.deleteFile(utils.getBaseScreenshotsDirectory() + getDefaultNamedCreatedVideoFile());
        
        }
        catch(Exception e) { logger.error("Error while saving video recording: " + e); throw e; }
    
    }
    
    /**
     * Get the name of the saved video with full path.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getVideoFilepath() throws Exception {
        
        try { 
            
            logger.info("Returning the name of the newly created video file: " + this.savedFilename + DEFAULT_FILE_EXTENSION);
            
            return this.savedFilename + DEFAULT_FILE_EXTENSION; 
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Return the first (and hopefully the only) file name starting with
     * <code>ScreenRecording</code> so we can rename it.
     * 
     * @return
     * 
     * @throws Exception 
     */
    private String getDefaultNamedCreatedVideoFile() throws Exception {
        
        try { 
            
            File dir = new File(utils.getBaseScreenshotsDirectory());

            File [] files = dir.listFiles(new FilenameFilter() {
    
                @Override
                public boolean accept(File dir, String name) { return name.startsWith("ScreenRecording"); }

            });
            
            return files[0].getName();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}

