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

import java.util.*; 

import com.nexmo.client.NexmoClient;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;

//import static com.nexmo.quickstart.Util.*;

import org.testng.ITestResult;

import org.apache.log4j.Logger;

import org.automateit.util.CommonProperties;

/**
 * This will send an SMS while not interfering with execution of main program
 * 
 * @author mburnside
 */
public class SendSMSThread_Nexmo extends SendMessageThreadBase {
    
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(SendSMSThread_Nexmo.class);
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with SMS metadata
     * @param result The TestNG test result metadata object
     */
    public SendSMSThread_Nexmo(Properties props, ITestResult result) { super(props, result); }
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param message A message to be relayed to the destination
     */
    public SendSMSThread_Nexmo(Properties props, String message) { super(props, message); }
    
    //@Override
    public void run() {
        
        TextMessage textMessage = null;
        
        try {
            
            logger.info("Sending SMS with these properties: " + this.props);
           
            AuthMethod auth = new TokenAuthMethod(props.getProperty("api_key"), props.getProperty("api_secret"));
        
            NexmoClient client = new NexmoClient(auth);

            if(result != null) textMessage = new TextMessage(props.getProperty("sender_phone_number"), props.getProperty("receiver_phone_number"), "Test Failed: " + CommonProperties.getInstance().getTargetEnvironment() + ": " + CommonProperties.getInstance().getProductId() + ": " + result.getName());
            else textMessage = new TextMessage(props.getProperty("sender_phone_number"), props.getProperty("receiver_phone_number"), "Alert Message: "  + CommonProperties.getInstance().getTargetEnvironment() + ": " + CommonProperties.getInstance().getProductId() + ": " + message);

            SmsSubmissionResult[] responses = client.getSmsClient().submitMessage(textMessage);

            for(SmsSubmissionResult response : responses) {
            
                logger.debug("Received response from NexMo:");
                logger.debug(response);
        
            }
            
        }
        catch(Exception e) { logger.error(e); }
        
    }
    
}

