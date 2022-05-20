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

import java.io.File;

import org.apache.log4j.Logger;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import org.automateit.util.CommonProperties;
import org.automateit.util.ScreenshotCapture;

/**
 * This class is added to testng task to listen for events.
 * 
 * One of things it does it take a screenshot when a test fails.
 * 
 * @author mburnside
 */
public class TestNGListener extends TestListenerAdapter {
    
    /**
     * The screenshots directory
     */
    private final String SCREENSHOTS_DIR = "screenshots";
    
    /**
     * Properties that can be used for any class extending this class
     */
    protected CommonProperties properties = CommonProperties.getInstance();
    
    /**
     * The count to use
     */
    private int count = 0;
    
    /**
     *  logging object, logging conf is defined in conf/log4j.properties
     */
    protected static Logger log = Logger.getLogger(TestNGListener.class);

   @Override
   public void onTestFailure(ITestResult result) {

       try {
 
           ScreenshotCapture screenshotCapture = new ScreenshotCapture();
           
           File file = new File("");
           
           String path = file.getAbsolutePath();
           
           String screenshotFileLocation = getScreenshotFileLocation(path, result);
           
           log.debug("Failed test case screenshot file location: " + screenshotFileLocation);
           
           screenshotCapture.doScreenshot(path + File.separator + SCREENSHOTS_DIR + File.separator + result.getName(), ScreenshotCapture.JPG);
           
           Reporter.setCurrentTestResult(result);

           Object[] parameters = result.getParameters();
           
           Reporter.log("<p><font face=arial size=2 color=000099");
           
           if(parameters.length > 0) Reporter.log("<p>Total number of input parameters: " + parameters.length + "<p>");
           
           for(int i = 0; i < parameters.length; i++) Reporter.log("Parameter: " + parameters[i]);
           
           Reporter.log("<b>Screenshot</b><br>");
           
           Reporter.log("<p><a href='" + screenshotFileLocation + "'>" +
                   "<img src='" + screenshotFileLocation + "' height='100' width='100'/></a>");
           
           Reporter.log("<p>");   
           
           Reporter.log("<font size=1>Click thumbnail image to view screenshot</font><p><br></font>");
               
           Reporter.setCurrentTestResult(null);
           
       }
       catch(Exception e) { log.error(e); }

   }

   @Override
   public void onTestSkipped(ITestResult result) { }

   @Override
   public void onTestSuccess(ITestResult result) { 
   
       try {
 
           ScreenshotCapture screenshotCapture = new ScreenshotCapture();
           
           File file = new File("");
           
           String path = file.getAbsolutePath();
           
           String screenshotFileLocation = getScreenshotFileLocation(path, result);
           
           log.debug("Success test case screenshot file location: " + screenshotFileLocation);
           
           screenshotCapture.doScreenshot(path + File.separator + SCREENSHOTS_DIR + File.separator + result.getName(), ScreenshotCapture.JPG);
           
           Reporter.setCurrentTestResult(result);
           
           Object[] parameters = result.getParameters();
           
           Reporter.log("<p><font face=arial size=2 color=000099");
           
           if(parameters.length > 0) Reporter.log("<p>Total number of input parameters: " + parameters.length + "<p>");
           
           for(int i = 0; i < parameters.length; i++) Reporter.log("Parameter: " + parameters[i]);
           
           
           Reporter.log("<b>Screenshot</b><br>");
           
           Reporter.log("<p><a href='" + screenshotFileLocation + "'>" +
                   "<img src='" + screenshotFileLocation + "' height='100' width='100'/></a>");
           
           Reporter.log("<p>");   
           
           Reporter.log("<font size=1>Click thumbnail image to view screenshot</font><p><br></font>");
               
           Reporter.setCurrentTestResult(null);
           
       }
       catch(Exception e) { log.error(e); }
   
   }
   
   /**
     * Determine if we should use local file path for reporting. 
     * 
     * This property is set in automateit.properties file.
     * 
     * @return 
     */
    private boolean useLocalFilePathForReporting() {
        
        if((properties.getProperty("useLocalFilePathForReporting") != null) && "true".equals(properties.getProperty("useLocalFilePathForReporting"))) return true;
        else return false;
       
    }
    
    /**
     * Get the String location for file location in the anchor link
     * 
     * @param path
     * @param result
     * 
     * @return 
     */
    private String getScreenshotFileLocation(String path, ITestResult result) {
           
        if(useLocalFilePathForReporting()) { 
         
            return "file:///" + path + File.separator + SCREENSHOTS_DIR + File.separator + result.getName() + "." + ScreenshotCapture.JPG;
           
        }
        else {
          
            return SCREENSHOTS_DIR + File.separator + result.getName() + "." + ScreenshotCapture.JPG;
           
        }
        
    }

}



