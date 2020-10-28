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

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import org.automateit.videorecorder.VideoController;

import org.automateit.util.CommonProperties;
import org.automateit.util.Utils;

/**
 * This class is added to testng task to listen for events.
 * 
 * It will handle the management and stopping/reporting of the recording
 * of the tests.
 *
 * @author mburnside
 */
public class VideoCaptureListener extends TestListenerAdapter {
    
    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    protected static Logger logger = Logger.getLogger(VideoCaptureListener.class);

    /**
     * testng utils class
     */
    private TestNGUtils testNGUtils = new TestNGUtils();
    
    /**
     * The video controller to use
     */
    private VideoController videoController = VideoController.getInstance();
    
    /**
     * utils class
     */
    private Utils utils = new Utils();
    
    /**
     * div id number. needed for selenium command list toggle.
     */
    private int divIdNumber = 0;
    
    /**
     * Do actions after a test case execution failure.
     * 
     * @param result 
     */
    @Override
    public void onTestFailure(ITestResult result) { if(utils.doReportingOnTestFail()) doReporting(result); }

    /**
     * Do actions after a test case execution skipped.
     * 
     * @param result 
     */
    @Override
    public void onTestSkipped(ITestResult result) { }

    /**
     * Do actions after a test case execution success (no reported fail
     * during test case execution).
     * 
     * @param result 
     */
    @Override
    public void onTestSuccess(ITestResult result) { if(utils.doReportingOnTestSuccess()) doReporting(result); }
   
    /**
     * Do actions before a test case execution 
     * 
     * @param result 
     */
    @Override
    public void onTestStart(ITestResult result) { 
        
        try { 
            
            if(CommonProperties.getInstance().getProperty("recordVideo") != null) {
                
                logger.info("Loading webdriver property: " + "recordVideo" + "|" + CommonProperties.getInstance().get("recordVideo"));
                
                boolean recordVideo = (new Boolean(CommonProperties.getInstance().get("recordVideo"))).booleanValue();
                
                CommonProperties.getInstance().setRecordVideo(recordVideo);
                
                try { if(recordVideo) { VideoController.getInstance().start(); } }
                catch(Exception ae) { logger.error("Error while starting the video recorder: " + ae.getMessage()); }
                    
            }
            
        } 
        catch(Exception e) { }
    
    }
    
    /**
     * This method is invoked after all the tests have run and all their Configuration methods have been called.
     */
    @Override
    public void onFinish(ITestContext context) { }
    
    /**
     * This method does all reporting into TestNG/ReportsNG.
     * 
     * @param result 
     */
    private void doReporting(ITestResult result) {
          
        try {
            
            logger.info("Report the recording of the video|" + CommonProperties.getInstance().getRecordVideo());
            
            if(!CommonProperties.getInstance().getRecordVideo()) return;
            
            videoController.stop();
            
            String newVideoFilename = utils.getFirstToken(result.getName(), " ");
            
            videoController.save(newVideoFilename);
        
            testNGUtils.appendVideoToReport(result, videoController.getVideoFilepath());
       
        }
        catch(Exception e) { }
   
    }
    
}



