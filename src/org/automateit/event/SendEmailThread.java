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
import javax.mail.*;  
import javax.mail.internet.*;  

import org.testng.ITestResult;

import org.apache.log4j.Logger;

import org.automateit.util.CommonProperties;

/**
 * This will send an email while not interfering with execution of main program
 * 
 * @author mburnside
 */
public class SendEmailThread extends SendMessageThreadBase {
 
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(SendEmailThread.class);
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param result The TestNG results object metadata
     */
    public SendEmailThread(Properties props, ITestResult result) { super(props, result); }
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param message A message to be relayed to the destination
     */
    public SendEmailThread(Properties props, String message) { super(props, message); }
    
    //@Override
    public void run() {
        
        try {
            
            logger.info("Sending email with these properties: " + this.props);
            
            Authenticator auth = new Authenticator() {
                
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("mail.auth.name"), props.getProperty("mail.auth.password"));
			}
                        
            };
            
            Session session = Session.getInstance(props, auth);
         
            MimeMessage mimeMessage = new MimeMessage(session);  
         
            mimeMessage.setFrom(new InternetAddress(props.getProperty("mail.from")));  
         
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(props.getProperty("mail.to")));  
         
            mimeMessage.setSubject("QA: " + CommonProperties.getInstance().getTargetEnvironment() + ": " + result.getName() + " " + props.getProperty("mail.subject"));  
         
            if(result != null) mimeMessage.setText("Test Failed: " + result.getName() + "\n\n" + "--------------" + "\n\n" + result.getThrowable());  
            else mimeMessage.setText("Message: " + message);
   
            Transport.send(mimeMessage);  
            
        }
        catch(Exception e) { logger.error(e); }
        
    }
    
}

