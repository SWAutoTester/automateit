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

import org.apache.log4j.Logger;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import org.automateit.util.Utils;

/**
 * The Alert Handler interface - Prometheus Push Gateway. 
 * 
 * This class im implemented as both a TestNG listener to make it easily added to the configuration
 * with needed to hard-code.
 * 
 * It registers itself during test startup.
 * 
 * @author mburnside
 */
public class PrometheusPushGatewayAlertHandler extends TestListenerAdapter implements AlertHandler {
    
    /**
     * The test name variable name
     */
    public static final String TEST_CLASS = "test_class";
    
    /**
     * The test name variable name
     */
    public static final String TEST_NAME = "test_name";
    
    /**
     * The test result value in boolean string representation
     */
    public static final String TEST_RESULT = "test_result";
    
    /**
     * The test result value in long representation
     */
    public static final String TEST_RESULT_VALUE = "test_result_value";
    
    /**
     * The test result value in Long representation
     */
    public static final String TEST_DURATION = "test_duration";
    
    /**
     * The test start time value in Long representation
     */
    public static final String TEST_START_TIME = "test_start_time";
    
    /**
     * The test end time value in Long representation
     */
    public static final String TEST_END_TIME = "test_end_time";
    
    /**
     * The test group run
     */
    public static final String TEST_GROUP = "TEST_GROUPS";
    
    /**
     * The target environment
     */
    public static final String TEST_ENVIRONMENT = "CM_TARGET_ENVIRONMENT";
    
    /**
     * The location of the email properties file
     */
    public static final String PROPERTIES_FILE = "./conf/prometheus_push_gateway.properties";
    
    /**
     * The test status signifying all tests pass
     */
    public static final String TEST_STATUS_PASS = "0";
    
    /**
     * The test status signifying one or all tests failed or skipped
     */
    public static final String TEST_STATUS_FAIL = "1";
    
    /**
     * The framework utilities object that helps do stuff easy
     */
    private final Utils utils = new Utils();
   
    /**
     *  logging object
     */
    private static final Logger logger = Logger.getLogger(PrometheusPushGatewayAlertHandler.class);
    
    /**
     * Default Constructor
     */
    public PrometheusPushGatewayAlertHandler() { }
    
    /**
     * Uniquely handle an alert event.
     * 
     * @param result
     * 
     * @throws Exception 
     */
    public synchronized void execute(ITestResult result) throws Exception { 
        
        try { 
            
            Properties props = utils.loadProperties(PROPERTIES_FILE);
            
            String testClassName = result.getTestClass().getName();
            
            logger.info("Sending data to PushGateway for test: " + testClassName + "|" + result.isSuccess());
            
            props.put(TEST_CLASS, testClassName.replace(".","_"));
            
            props.put(TEST_NAME, result.getName());
          
            props.put(TEST_DURATION, String.valueOf((Long.valueOf(result.getEndMillis() - result.getStartMillis())).intValue() / 1000));
            
            props.put(TEST_START_TIME, String.valueOf(result.getStartMillis()));
            
            props.put(TEST_END_TIME, String.valueOf(result.getEndMillis()));
           
            if(System.getenv(TEST_GROUP) != null) props.put(TEST_GROUP, System.getenv(TEST_GROUP));
            else props.put(TEST_GROUP, "none");
            
            if(System.getenv(TEST_ENVIRONMENT) != null) props.put(TEST_ENVIRONMENT, System.getenv(TEST_ENVIRONMENT));
            else props.put(TEST_ENVIRONMENT, "none");
            
            if(result.isSuccess()) { props.put(TEST_RESULT, "PASS"); props.put(TEST_RESULT_VALUE, TEST_STATUS_PASS); }
            else { props.put(TEST_RESULT, "FAIL"); props.put(TEST_RESULT_VALUE, TEST_STATUS_FAIL); }
            
            (new SendPrometheusPushGatewayMessageThread(props, result)).start();
            
            try { Thread.sleep(2000); }
            catch(Exception le) { }
        
        }
        catch(Exception e) { e.printStackTrace(); logger.debug(e); }  
       
    }
    
    /**
     * Uniquely handle an alert event.
     * 
     * @param message
     * 
     * @throws Exception 
     */
    public synchronized void execute(String message) throws Exception { 
        
        try { 
            
            Properties props = utils.loadProperties(PROPERTIES_FILE);
            
            (new SendPrometheusPushGatewayMessageThread(props, message)).start(); 
        
        }
        catch(Exception e) { logger.debug(e); }  
    
    }
    
    /**
     * Do actions after a test case execution failure.
     * 
     * @param result 
     */
    @Override
    public void onTestFailure(ITestResult result) { 
        /*
        try { execute(result); }
        catch(Exception e) { logger.debug(e); } 
        */
    
    }

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
    public void onTestSuccess(ITestResult result) { 
    /*
        try { execute(result); }
        catch(Exception e) { logger.debug(e); } 
        */
    }
   
    /**
     * Do actions before a test case execution 
     * 
     * @param result 
     */
    @Override
    public void onTestStart(ITestResult result) { AlertEvent.getInstance().register(this);}
    
    /**
     * This method is invoked after all the tests have run and all their Configuration methods have been called.
     */
    @Override
    public void onFinish(ITestContext context) { 
        
        try { 
            
            Properties props = utils.loadProperties(PROPERTIES_FILE);
            
            long duration = ((context.getEndDate().getTime() - context.getStartDate().getTime())) / 1000;
            
            props.put(TEST_DURATION, String.valueOf(duration));
            
            props.put(TEST_END_TIME, String.valueOf(context.getEndDate().getTime()));
            
            // if there are any skipped tests or failed tests mark as test status FAIL
            if((context.getSkippedTests().size() != 0) || context.getFailedTests().size() != 0) { props.put(TEST_RESULT, "FAIL"); props.put(TEST_RESULT_VALUE, TEST_STATUS_FAIL); }
            else { props.put(TEST_RESULT, "PASS"); props.put(TEST_RESULT_VALUE, TEST_STATUS_PASS); }
            
            if(System.getenv(TEST_ENVIRONMENT) != null) props.put(TEST_ENVIRONMENT, System.getenv(TEST_ENVIRONMENT));
            else props.put(TEST_ENVIRONMENT, "none");
           
            (new SendPrometheusPushGatewayMessageThread(props)).start();
            
            try { Thread.sleep(2000); }
            catch(Exception le) { }
        
        }
        catch(Exception e) { e.printStackTrace(); logger.debug(e); }   
    
    }
    
}

