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

import atu.testrecorder.ATUTestRecorder;

import org.apache.log4j.Logger;

import org.automateit.util.Utils;

/**
 * This video recorder uses ATUTestRecorder API to capture video.
 * 
 * More information about this 3rd party API can be found here:
 * 
 * http://www.software-testing-tutorials-automation.com/2015/02/how-to-record-selenium-webdriver-test.html
 * 
 * http://automationtestingutilities.blogspot.in/2014/03/how-to-record-selenium-test-execution.html
 *  
 * @author mburnside
 */
public class ATUVideoRecorder implements VideoRecorder {
    
    /**
     *  Logging object
     */
    protected static Logger logger = Logger.getLogger(ATUVideoRecorder.class);
    
    /**
     * The 3rd party implementation object that captures the video
     */
    private ATUTestRecorder videoRecorder = null;
    
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
    public static final String DEFAULT_FILE_EXTENSION = ".mov";
    
    /**
     * The saved video recording filename
     */
    public String savedFilename = null;
    
    /**
     * Default Constructor
     */
    public ATUVideoRecorder() { }
    
    /**
     * Start this video recorder.
     * 
     * @throws Exception 
     */
    public void start() throws Exception {
        
        try { 
            
            logger.info("Starting the video recorder");
            
            if(this.videoRecorder == null) this.videoRecorder = new ATUTestRecorder(utils.getBaseScreenshotsDirectory(), "test", false);
            
            this.videoRecorder.start(); 
            
            logger.info("Video recorder is started|" + this.videoRecorder);
        
        }
        catch(Exception e) { logger.error("Error while starting the video recording: " + e); throw e; }
        
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
            
            logger.info("Copying default video recording file saved as: " + DEFAULT_FILENAME + DEFAULT_FILE_EXTENSION + " to file: " + filename);
            
            this.savedFilename = utils.getBaseScreenshotsDirectory() + filename;
            
            utils.copyFile(utils.getBaseScreenshotsDirectory() + DEFAULT_FILENAME + DEFAULT_FILE_EXTENSION, this.savedFilename + DEFAULT_FILE_EXTENSION); 
        
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
    
}

