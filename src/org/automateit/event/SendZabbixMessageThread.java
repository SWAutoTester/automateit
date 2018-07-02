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

import java.util.Properties; 

import io.github.hengyunabc.zabbix.sender.ZabbixSender;
import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;

import org.testng.ITestResult;

import org.apache.log4j.Logger;

/**
 * This will send a Zabbix message while not interfering with execution of main program
 * 
 * @author mburnside
 */
public class SendZabbixMessageThread extends SendMessageThreadBase {
    
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(SendZabbixMessageThread.class);
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param result The TestNG results object metadata
     */
    public SendZabbixMessageThread(Properties props, ITestResult result) { super(props, result); }
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param message A message to be relayed to the destination
     */
    public SendZabbixMessageThread(Properties props, String message) { super(props, message); }
    
    //@Override
    public void run() {
        
        try {
            
            logger.info("Sending a Zabbix message with these properties: " + this.props);
            
            ZabbixSender zabbixSender = new ZabbixSender(props.getProperty("host"), (new Integer(props.getProperty("port")).intValue()));

            DataObject dataObject = new DataObject();
		
            dataObject.setHost(props.getProperty("host_target"));
		
            dataObject.setKey(props.getProperty("key"));
		
            // we use 0 (false) or 1 (true)
            dataObject.setValue("0");
            
            if(result != null) logger.info("Sending data object|" + props.getProperty("host_target") + "|" + result.getName() + "|0");
            else logger.info("Sending data object|" + props.getProperty("host_target") + "|" + message + "|0");
		
            // TimeUnit is SECONDS
            dataObject.setClock(System.currentTimeMillis()/1000);
		
            SenderResult result = zabbixSender.send(dataObject);

            logger.info("result:" + result);
		
            if(result.success()) logger.info("Zabbix - Sent successfully.");
            else logger.info("Zabbix - Unable to Send (unsuccessful).");
            
        }
        catch(Exception e) { logger.error(e); }
        
    }
    
}

