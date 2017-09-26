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

package org.automateit.shell;
 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.automateit.util.CommandList;
import org.automateit.util.CommonProperties;
import org.automateit.util.Utils;

/**
 * This class is an API to obfuscate complexity and implementation of 
 * SSH clients, so that connections using SSH can be made. Other features
 * include SFTP, SCP.
 * 
 * @author mburnside
 */
public class BaseShell {
    
    /**
     * Logging class
     */
    private Logger logger = Logger.getLogger(BaseShell.class);
    /**
     * Properties that can be used for any class extending this class
     */
    protected CommonProperties properties = CommonProperties.getInstance();
    
    /**
     * If the web driver has been initialized
     */
    protected boolean hasBeenInitialized = false;
    
    /**
     * If the logging mechanism has been initialized
     */
    protected boolean loggingSetup = false;
   
    /**
     * Utilities for convenience methods
     */
    protected Utils utils = new Utils();
    
    /**
     * The implementation of the SSH Client.
     */
    private JSch jsch = new JSch();
    
    /**
     * The session object used with JSch
     */
    private Session session = null;
    
    /**
     * The jsch channel
     */
    private Channel channel = null;
    
    /**
     * The channel exec used with jsch
     */
    private ChannelExec channelExec = null;
    
    /**
     * The connection properties used (if not the CommonProperties)
     */
    protected Properties connectionProperties = null;
    
    /**
     * Command List
     */
    protected CommandList commandList = CommandList.getInstance();
    
    /**
     * Default Constructor. The first screen that appears on the app must use this constructor
     * 
     * @throws Exception 
     */
    public BaseShell() throws Exception { }
    
    /**
     * Copy Constructor.
     * 
     * @param connectionPropertiesFile
     * 
     * @throws Exception 
     */
    public BaseShell(String connectionPropertiesFile) throws Exception {
    
        try { 
            
            logger.info("Setting up a new base shell with this connection properies file: " + connectionPropertiesFile);
            
            this.connectionProperties = utils.loadProperties(connectionPropertiesFile); 
        
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Copy Constructor.
     * 
     * @param connectionProperties
     * 
     * @throws Exception 
     */
    public BaseShell(Properties connectionProperties) throws Exception {
    
        try { this.connectionProperties = connectionProperties; }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Connect to a remote host using SSH.
     * 
     * @param username
     * @param password
     * @param hostname
     * @param port
     * @param connectionProperties
     * 
     * @throws Exception 
     */
    public void connect(String username, String password, String hostname, String port, Properties connectionProperties) throws Exception {
        
        try { 
            
            logger.info("Attempting a new ssh connection: " + username + "|" + password + "|" + port + "|" + connectionProperties);
            
            commandList.addToList("connect|" + username + "|" + password + "|" + port + "|" + connectionProperties);
            
            if(!this.connectionProperties.containsKey("private_key_file")) throw new Exception("The properties/configuration file must contain key: private_key_file");
            if(!this.connectionProperties.containsKey("private_key_password")) throw new Exception("The properties/configuration file must contain key: private_key_password");
        
            this.session = jsch.getSession(username, hostname, (new Integer(port)).intValue());
            
            if((password != null) && (!password.trim().equals(""))) this.session.setPassword(password);
            
            this.session.setConfig(connectionProperties);
            
            jsch.addIdentity(this.connectionProperties.getProperty("private_key_file"), this.connectionProperties.getProperty("private_key_password"));
            
            this.session.connect();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Connect to a remote host using SSH.
     * 
     * @param username
     * @param password
     * @param hostname
     * @param port
     * 
     * @throws Exception 
     */
    public void connect(String username, String password, String hostname, String port) throws Exception {
        
        try { 
            
            logger.info("Attempting a new ssh connection: " + username + "|" + password + "|" + port);
            
            commandList.addToList("connect|" + username + "|" + password + "|" + port);
            
            if(!this.connectionProperties.containsKey("private_key_file")) throw new Exception("The properties/configuration file must contain key: private_key_file");
            if(!this.connectionProperties.containsKey("private_key_password")) throw new Exception("The properties/configuration file must contain key: private_key_password");
            
            this.session = jsch.getSession(username, hostname, (new Integer(port)).intValue());
            
            if((password != null) && (!password.trim().equals(""))) this.session.setPassword(password);
            
            logger.info("setting up the ssh client to use these properties: " + this.connectionProperties);
            
            this.session.setConfig(this.connectionProperties);
            
            jsch.addIdentity(this.connectionProperties.getProperty("private_key_file"), this.connectionProperties.getProperty("private_key_password"));
            
            this.session.connect();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Connect to the server using ssh.
     * 
     * @throws Exception 
     */
    public void connect() throws Exception {
       
        try {
            
            logger.info("Starting process of setting up / resetting a router with properties from this : " + connectionProperties);
            
            logger.info("Hostname: " + (String)connectionProperties.getProperty("hostname"));
            logger.info("Username: " + (String)connectionProperties.getProperty("username"));
            logger.info("Password: " + (String)connectionProperties.getProperty("password"));
            logger.info("Port: " + (String)connectionProperties.getProperty("port"));
            
            connect((String)connectionProperties.getProperty("username"), (String)connectionProperties.getProperty("password"), (String)connectionProperties.getProperty("hostname"), (String)connectionProperties.getProperty("port"));
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Thread.sleep in milliseconds. Makes less code to type.
     * 
     * @param milliseconds 
     */
    public void delay(long milliseconds) {
        
        try { 
            
            commandList.addToList("delay|" + milliseconds);
            
            Thread.sleep(milliseconds); 
        
        } 
        catch(Exception e) { }
        
    }
    
    /**
     * Enter something on the command line.
     * 
     * @param command
     * 
     * @return The response (STDOUT after command entered)
     * 
     * @throws Exception 
     */
    public String enter(String command) throws Exception {
        
        try { 
            
            logger.info("Entering this command: " + command);
            
            commandList.addToList("enter|" + command);
            
            this.channel = this.session.openChannel("exec");
    
            this.channelExec = (ChannelExec) channel;

            this.channelExec.setCommand(command);
    
            this.channelExec.setErrStream(System.err);

            this.channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(this.channelExec.getInputStream()));
    
            String line;
            
            StringBuffer returnedLine = new StringBuffer("");
    
            while((line = reader.readLine()) != null) returnedLine.append(line);
            
            this.channelExec.disconnect();
            
            return returnedLine.toString();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Perform an SCP To (scp a file to a remote server)
     * 
     * @param localFilename
     * @param remoteFilename
     * 
     * @return
     * 
     * @throws Exception 
     */
    public String scpTo(String localFilename, String remoteFilename) throws Exception {
        
        try { 
            /*
            this.channel = this.session.openChannel("exec");
    
            this.channelExec = (ChannelExec) this.channel;

            this.channelExec.setCommand("scp " + localFilename + " " + remoteFilename);
    
            this.channelExec.setErrStream(System.err);

            this.channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(this.channelExec.getInputStream()));
    
            String line;
            
            StringBuffer returnedLine = new StringBuffer("");
    
            while((line = reader.readLine()) != null) returnedLine.append(line);
            
            return returnedLine.toString();
            */
            
            return null;
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Set configuration parameters.
     * 
     * @param key
     * 
     * @param value 
     */
    public void setConfig(String key, String value) {
        
        try { 
            
            commandList.addToList("setConfig:" + key + "|" + value);
            
            if(this.jsch != null) this.jsch.setConfig(key, value); 
        
        }
        catch(Exception e) { }
        
    }
    
    /**
     * Close the terminal session and the remote connection
     */
    public void close() {
        
        try { 
            
            commandList.addToList("close");
            
            if(this.session != null) this.session.disconnect(); 
        
        }
        catch(Exception e) { }
        
    }
    
    /**
     * Verify expected text in response/output/stdout after issuing a command.
     * 
     * @param keyPrefix
     * @param response
     * 
     * @throws Exception 
     */
    public void verifyExpectedTextInCommandResponse(String keyPrefix, String response) throws Exception {
        
        try {
            
            for(int i = 0; i < 25; i++) {
                
                String key = keyPrefix + String.valueOf(i);
                
                if(connectionProperties.containsKey(key)) {
                    
                    if(!response.trim().contains((String)connectionProperties.getProperty(key).trim())) throw new Exception("Unable to find expected text in command response data: " + (String)connectionProperties.getProperty(key) + "|" + response);
                     
                }
                
            }
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}

