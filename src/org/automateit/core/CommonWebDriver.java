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

package org.automateit.core;

import org.openqa.selenium.WebDriver;

/**
 * Globally-accessible single instance class that retrieves the WebDriver implementation
 * being used.
 */
public class CommonWebDriver {

    /**
     * The instance of this singleton class
     */
    private static CommonWebDriver instance = new CommonWebDriver();
    
    /**
     * Web Driver object
     */
    private WebDriver driver = null;
   
    /**
     * Default Constructor
     */
    protected CommonWebDriver() {}
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static CommonWebDriver getInstance() { return instance; }
    
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
     * Close the web driver (quit)
     */
    public void close() {
        
        if(driver == null) return;
        
        try { driver.quit(); }
        catch(Exception e) { }
        
    }
    
}