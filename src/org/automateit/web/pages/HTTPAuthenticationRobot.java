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

package org.automateit.web.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

/**
 * This class is a special helper class used to type in the authorization
 * prompts that may occur.
 * 
 * @author mburnside@Automate It!
 */
public  class HTTPAuthenticationRobot extends Robot {

    /**
     * Default Constructor.
     * 
     * @throws AWTException 
     */
    public HTTPAuthenticationRobot() throws Exception { super(); }

    /**
     * Paste to the clipboard.
     * 
     * @throws Exception 
     */
    public void pasteClipboard() throws Exception {
	
        try {
            
            keyPress(KeyEvent.VK_CONTROL);
            keyPress(KeyEvent.VK_V);
            delay(50);
            keyRelease(KeyEvent.VK_V);
            keyRelease(KeyEvent.VK_CONTROL);
            
        }
        catch(Exception e) { throw e; }
	
    }

    /**
     * Type <code>text</code> from the clipboard.
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void type(String text) throws Exception { 
	
        try {
            
            writeToClipboard(text);
            
            pasteClipboard();
        
        }
	catch(Exception e) { throw e; }
   
    }

    /**
     * Write the <code>text</code> to the clipboard.
     * 
     * @param text 
     */
    private void writeToClipboard(String text) {
        
        try {
            
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
            Transferable transferable = new StringSelection(text);
	
            clipboard.setContents(transferable, null);
            
        }
        catch(Exception e) { throw e; }

    }
    
}