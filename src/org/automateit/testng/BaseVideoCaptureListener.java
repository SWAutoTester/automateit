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
import org.automateit.videorecorder.VideoRecorder;
import org.automateit.util.Utils;

/**
 * This class is a base class for video recoder listeners (common shared objects).
 * 
 * @author mburnside
 */
public class BaseVideoCaptureListener extends TestListenerAdapter {
    
    /**
     *  Logging object
     */
    private static Logger logger = Logger.getLogger(BaseVideoCaptureListener.class);
    
    /**
     * testng utils class
     */
    protected final TestNGUtils testNGUtils = new TestNGUtils();
    
    /**
     * The video controller to use
     */
    protected VideoController videoController = VideoController.getInstance();
    
    /**
     * The video recorder to use
     */
    protected VideoRecorder videoRecorder = null;
    
    /**
     * utils class
     */
    protected final Utils utils = new Utils();
   
    /**
     * Default Constructor
     */
    public BaseVideoCaptureListener() { }
    
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
            
            logger.info("\n\nReport the recording of the video|" + this.videoRecorder + "\n\n");
            
            if(videoRecorder == null) return;
            
            videoRecorder.stop();
            
            String newVideoFilename = utils.getFirstToken(result.getName(), " ");
            
            this.videoRecorder.save(newVideoFilename);
        
            testNGUtils.appendVideoToReport(result, this.videoRecorder.getVideoFilepath());
            
            this.videoRecorder = null;
       
        }
        catch(Exception e) { logger.error(e); }
   
    }
    
}



