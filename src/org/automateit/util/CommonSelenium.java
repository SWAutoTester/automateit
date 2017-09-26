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

package org.automateit.util;

import org.apache.log4j.Logger;

import org.openqa.selenium.WebDriver;

/**
 * Common class that retrieves the Selenium implementation class configured
 * through dynamic properties.
 *
 * @author mburnside@Automate It!
 */

public class CommonSelenium {

    /**
     * The instance of this singleton class
     */
    private static CommonSelenium instance = new CommonSelenium();
    
    /**
     * Web Driver object
     */
    private WebDriver driver = null;
    
    /**
     *  logging object
     */
    protected static Logger log = Logger.getLogger(CommonSelenium.class);
    
    /**
     * Default Constructor
     */
    protected CommonSelenium() {}
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static CommonSelenium getInstance() { return instance; }
    
    /**
     * Set the web driver object to be shared
     * 
     * @param driver 
     */
    public void setWebDriver(WebDriver driver) { this.driver = driver; }
    
    /**
     * Get the web driver object being used in this session
     * 
     * @return 
     */
    public WebDriver getWebDriver() { return this.driver; }
    
    /**
     * Close the web browser without needing page class.
     */
    public void closeWebBrowser() {
        
        try { if(this.driver != null) this.driver.quit(); }
        catch(Exception e) { }
        
    }
    
    /**
     * Close the mobile app.
     * 
     * @throws Exception 
     */
    public void closeApp() throws Exception {
        
        try { closeWebBrowser(); }
        catch(Exception e) { }
        
    }
    
}