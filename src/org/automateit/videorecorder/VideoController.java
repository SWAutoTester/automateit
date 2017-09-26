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

package com.automateit.videorecorder;

import org.apache.log4j.Logger;

import com.automateit.util.CommonProperties;
import com.automateit.util.Utils;

/**
 * This class is the control center for operating a video capture for any
 * tests being run by this framework
 * 
 * @author mburnside
 */
public class VideoController {
    
    /**
     *  Logging object
     */
    protected static Logger logger = Logger.getLogger(VideoController.class);
    
    /**
     * utils class
     */
    private Utils utils = new Utils();
    
    /**
     * The video recorder implementation to use
     */
    private VideoRecorder videoRecorder = null;
    
    /**
     * The video recorder implementation to use
     */
    private static VideoController instance = new VideoController();
    
    /**
     * Default Constructor.
     */
    public VideoController() { }
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static VideoController getInstance() { return instance; }
    
    /**
     * Start the video recording.
     * 
     * @throws Exception 
     */
    public void start() throws Exception {
        
        logger.info("Start the video recorder");
        
        this.videoRecorder = getVideoRecorder();
        
        try { this.videoRecorder.start(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Stops this video recorder.
     * 
     * @throws Exception 
     */
    public void stop() throws Exception { 
        
        logger.info("Stopping the video recorder");
        
        try { this.videoRecorder.stop(); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Save the video to a file.
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public void save(String filename) throws Exception { 
        
        logger.info("Saving the video recording to file: " + filename);
        
        try { this.videoRecorder.save(filename); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Get the name of the saved video with full path.
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String getVideoFilepath() throws Exception {
        
        try { return this.videoRecorder.getVideoFilepath(); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Return an instance of the video recorder to use.
     * 
     * @return
     * 
     * @throws Exception 
     */
    private VideoRecorder getVideoRecorder() throws Exception {
        
        logger.info("Setting for video recorder id: " + CommonProperties.getInstance().getVideoRecorderId());
        
        try { 
            
            if(CommonProperties.getInstance().getVideoRecorderId() == null) return new MonteVideoRecorder();
            
            if(CommonProperties.getInstance().getVideoRecorderId().trim().equals("avi")) return new MonteVideoRecorder();
            else return new ATUVideoRecorder();
        
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Get a video recorder implementation.
     * 
     * @param recorderId
     * 
     * @return
     * 
     * @throws Exception 
     */
    public synchronized VideoRecorder getVideoRecorder(String recorderId) throws Exception {
        
        logger.info("Getting for video recorder id: " + recorderId);
        
        try { 
            
            if(recorderId == null) throw new Exception("Unable to get  video recorder with null recorder id");
                
            if(recorderId.equals(VideoRecorder.MOV)) return new ATUVideoRecorder();
            
            if(recorderId.equals(VideoRecorder.AVI)) return new MonteVideoRecorder();
           
            // if we get here, then we dont now which type of recorder instance to return
            throw new Exception("Unable to find a VideoRecoder instance class for recorder id: " + recorderId);
        
        }
        catch(Exception e) { throw e; }
        
    }
    
}