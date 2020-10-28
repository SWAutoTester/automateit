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
package org.automateit.event;

import java.util.ArrayList;
import java.util.List;
import org.testng.ITestResult;

import org.apache.log4j.Logger;

import org.automateit.util.Utils;

/**
 * An object that alerts listeners to important events
 * 
 * @author mburnside
 */
public class AlertEvent extends Thread {
    
    /**
     * The list of keywords/test names that indicate severity action
     */
    protected List<String> keywordList = null;
    
    /**
     * The list of exception messages selected as not really something to react to 
     */
    protected List<String> ignoreList = null;
    
    /**
     * The list of AlertHandler classes to handle the actual physical execution/handling of events
     */
    protected List<AlertHandler> alertHandlers = new ArrayList<AlertHandler>();
    
    /**
     * The utilities class to use
     */
    private Utils utils = new Utils();
    
    /**
     *  logging object
     */
    protected static Logger logger = Logger.getLogger(AlertEvent.class);
    
    /**
     * the file that contains the list of important keywords (test names) listed as severe
     */
    public final String ALERTFILEPATH = "./conf/alert.txt";
    
    /**
     * the file that contains the list of important keywords (test names) listed as ignore - this list is watching error message in the throwable exception (some exceptions we may want to ignore)
     */
    public final String ALERT_IGNORE_FILEPATH = "./conf/alert_ignore.txt";
    
    /**
     * Reference to this object
     */
    private static AlertEvent instance = new AlertEvent();
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static AlertEvent getInstance() { return instance; }
    
    /**
     * Default Constructor
     */
    protected AlertEvent() { 
        
        logger.debug("Creating new instance of AlertEvent");
        
        try { keywordList = utils.getListFromFile(ALERTFILEPATH); }
        catch(Exception e) { logger.debug(e); }
        
        try { ignoreList = utils.getListFromFile(ALERT_IGNORE_FILEPATH); }
        catch(Exception e) { logger.debug(e); }
        
    }
    
    /**
     * Inform the listeners of the details of the event.
     * 
     * @param result
     * @param ignoreAlertKeywords 
     */
    public void sendAlert(ITestResult result, boolean ignoreAlertKeywords) { 
        
        logger.info("Sending alert to alert handlers: " + alertHandlers.size() + "|" + ignoreAlertKeywords);
       
        // If the test name or other keyword is not in the list of things to alert for, then skip
        if(!ignoreAlertKeywords && !keywordList.contains(result.getName())) return;
       
        // if the throwable does not contain an error (is null) then do not send
        if(!ignoreAlertKeywords && (result.getThrowable() == null)) return;
       
        // If the error is in the list of errors to ignore then return without doing anything
        if(!ignoreAlertKeywords && errorMessageIgnore(result.getThrowable().getMessage())) return;
        
        for(AlertHandler alertHandler : alertHandlers) {
    
            try { alertHandler.execute(result); }
            catch(Exception e) { logger.info(e); }
            
        }
        
    }
    
    /**
     * Inform the listeners of the details of the event.
     * 
     * @param result 
     */
    public void sendAlert(ITestResult result) { sendAlert(result, false); }
    
    /**
     * Inform the listeners of the details of the event.
     * 
     * @param message
     */
    public void sendAlert(String message) {  
       
        logger.info("Sending alert to alert handlers: " + alertHandlers.size());
        
        for(AlertHandler alertHandler : alertHandlers) {
            
            logger.info("Sending alert to alert handler: " + alertHandler);
    
            try { alertHandler.execute(message); }
            catch(Exception e) { logger.error(e); }
            
        }
        
    }
    
    /**
     * Register an AlertEvent handler.
     * 
     * @param alertHandler
     */
    public void register(AlertHandler alertHandler) {  
    
        try { 
            
            if(alertHandler == null) return;
            
            if(!alertHandlers.contains(alertHandler)) {
                
                logger.debug("Adding a registered alert handler implementation: " + alertHandler);
                
                alertHandlers.add(alertHandler);
                
            }
        
        } 
        catch(Exception e) { logger.error("Unable to register alert event handler|" + alertHandler); }
        
    }
    
    /**
     * Check if any part of the error message text is 
     * 
     * @param errorMessage
     * 
     * @return
     */
    protected boolean errorMessageIgnore(String errorMessage) {
        
        for(String ignoreText : ignoreList) if(errorMessage.trim().toLowerCase().contains(ignoreText.trim().toLowerCase())) return true;
      
        // if we go through the entire list and didnt find it then return false
        return false;
        
    }
    
}

