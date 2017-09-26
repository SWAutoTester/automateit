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

package org.automateit.testng;

import org.apache.log4j.Logger;

import org.testng.ITestResult;

import org.automateit.videorecorder.VideoController;
import org.automateit.videorecorder.VideoRecorder;

/**
 * This class is added to testng task to listen for events.
 * 
 * It will record test cases in MV (Quicktime format).
 *
 * @author mburnside
 */
public class MOVVideoCaptureListener extends BaseVideoCaptureListener {
    
    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    protected static Logger logger = Logger.getLogger(MOVVideoCaptureListener.class);

    /**
     * Do actions before a test case execution 
     * 
     * @param result 
     */
    @Override
    public void onTestStart(ITestResult result) { 
        
        try { 
            
            videoRecorder = VideoController.getInstance().getVideoRecorder(VideoRecorder.MOV);
            
            videoRecorder.start(); 
        
        }           
        catch(Exception e) { logger.error("Error while starting the video recorder: " + e.getMessage());}
         
    }
    
}



