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

import org.apache.log4j.Logger;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

import io.prometheus.client.exporter.PushGateway;

import org.automateit.util.CommonProperties;
        
/**
 * This will send a Prometheus mesage to PushGateway while not interfering with execution of main program
 * 
 * @author mburnside
 */
public class SendPrometheusPushGatewayMessageThread extends SendMessageThreadBase {
    
    /**
     * The label names for metrics collected.
     */
    public static final String[] LABELNAMES = { "test_name", "env" };
   
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(SendPrometheusPushGatewayMessageThread.class);
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param result The TestNG results object metadata
     */
    public SendPrometheusPushGatewayMessageThread(Properties props, ITestResult result) { super(props, result); }
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     * @param message A message to be relayed to the destination
     */
    public SendPrometheusPushGatewayMessageThread(Properties props, String message) { super(props, message); }
    
    /**
     * Copy Constructor
     * 
     * @param props The Properties object with email metadata
     */
    public SendPrometheusPushGatewayMessageThread(Properties props) { super(props); }
    
    //@Override
    public void run() {
        
        logger.info("Sending a Prometheus Push Gateway message with these properties: " + props);
          
        CollectorRegistry registry = new CollectorRegistry();
        
        pushTestTimestamp(registry, props);
        
        pushTestResult(registry, props);
        
        pushTestDuration(registry, props);
       
        try {
            
            logger.debug("Sending metrics to push gateway");
            
            PushGateway pg = new PushGateway(props.getProperty("host"));
            
            pg.pushAdd(registry, props.getProperty("batch_job_name"));
            
            logger.debug("Successfully sent metrics to push gateway");
        
        }
        catch(Exception e) { logger.error(e); }
                
        
    }
    
    /**
     * Push the test result to the pushgateway
     * 
     * @param registry
     * @param props 
     */
    private void pushTestResult(CollectorRegistry registry,Properties props) {
        
        logger.debug("Pushing test result");
        
        try { sendMetricToPushGateway(registry, props, "test_results", props.getProperty(PrometheusPushGatewayAlertHandler.TEST_RESULT), props.getProperty(PrometheusPushGatewayAlertHandler.TEST_CLASS) + '_' + props.getProperty(PrometheusPushGatewayAlertHandler.TEST_NAME), props.getProperty(PrometheusPushGatewayAlertHandler.TEST_ENVIRONMENT), getDoubleValue(props.getProperty(PrometheusPushGatewayAlertHandler.TEST_RESULT_VALUE))); }
        catch(Exception e) { e.printStackTrace(); }
        
    }
    
    /**
     * Push the test duration to the pushgateway
     * 
     * @param registry
     * @param props 
     */
    private void pushTestDuration(CollectorRegistry registry, Properties props) {
        
        logger.debug("Pushing test duration");
        
        try { sendMetricToPushGateway(registry, props, "test_duration", props.getProperty(PrometheusPushGatewayAlertHandler.TEST_DURATION), props.getProperty(PrometheusPushGatewayAlertHandler.TEST_CLASS) + '_' + props.getProperty(PrometheusPushGatewayAlertHandler.TEST_NAME), props.getProperty(PrometheusPushGatewayAlertHandler.TEST_ENVIRONMENT), getDoubleValue(props.getProperty(PrometheusPushGatewayAlertHandler.TEST_DURATION))); }
        catch(Exception e) { e.printStackTrace(); }
        
    }
    
    /**
     * Push the test timestamp to the pushgateway
     * 
     * @param registry
     * @param props 
     */
    private void pushTestTimestamp(CollectorRegistry registry, Properties props) {
        
        logger.debug("Pushing test timestamp");
        
        try { sendMetricToPushGateway(registry, props, "test_timestamp", props.getProperty(PrometheusPushGatewayAlertHandler.TEST_END_TIME), props.getProperty(PrometheusPushGatewayAlertHandler.TEST_CLASS) + '_' + props.getProperty(PrometheusPushGatewayAlertHandler.TEST_NAME), props.getProperty(PrometheusPushGatewayAlertHandler.TEST_ENVIRONMENT), getDoubleValue(props.getProperty(PrometheusPushGatewayAlertHandler.TEST_END_TIME))/1000); }
        catch(Exception e) { e.printStackTrace(); }
        
    }
    
    /**
     * Send metric to push gateway.
     * 
     * @param registry
     * @param props
     * @param name
     * @param help 
     */
    private synchronized void sendMetricToPushGateway(CollectorRegistry registry, Properties props, String name, String help, String namespace, String subsystem, double value) {
        
        try { 
              
            Gauge metric = Gauge.build().name(name).help(help).labelNames(LABELNAMES).create();
        
            metric
            .setChild(new Gauge.Child() {
                @Override
                public double get() {
                    return value;
                }
            }, getLabelValues(props, help));
           
            metric.register(registry);
            
            logger.debug("Preparing to send a Prometheus Push Gateway metric: " + name + "|" + value);
           
        }
        catch(Exception e) { logger.error(e); }
        
    }    
    
    /**
     * Get the label values
     * 
     * @param props
     * 
     * @return 
     */
    private String[] getLabelValues(Properties props, String help) {
        
        if(CommonProperties.getInstance().getFlowName() != null) {
            
            String[] values = { CommonProperties.getInstance().getFlowName(), props.getProperty(PrometheusPushGatewayAlertHandler.TEST_ENVIRONMENT) };
            
            return values;
            
        }
        else {
            
            String[] values = { props.getProperty(PrometheusPushGatewayAlertHandler.TEST_CLASS) + '_' + props.getProperty(PrometheusPushGatewayAlertHandler.TEST_NAME), props.getProperty(PrometheusPushGatewayAlertHandler.TEST_ENVIRONMENT) };
            
            return values;
        }
        
    }
    
    /**
     * Get the double value
     * 
     * @param value
     * 
     * @return 
     */
    private double getDoubleValue(String value) { return (new Double(value)).doubleValue(); }
    
}

