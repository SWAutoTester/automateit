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

import org.testng.ITestResult;

/**
 * This isa thew base class for alert handler sending message or event threads
 * 
 * @author mburnside
 */
public class SendMessageThreadBase extends Thread {
    
    /**
     * The properties object
     */
    protected Properties props = null;
    
    /**
     * A message to be relayed to the destination
     */
    protected String message = null;
    
    /**
     * The test results object with test result metadata
     */
    protected ITestResult result = null;
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param result The TestNG results object metadata
     */
    public SendMessageThreadBase(Properties props, ITestResult result) { 
        
        this.props = props; 
        this.result = result;
    
    }
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param message A message to be relayed to the destination
     */
    public SendMessageThreadBase(Properties props, String message) { 
        
        this.props = props; 
        this.message = message;
    
    }
    
}

