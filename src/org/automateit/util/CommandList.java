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
 *  
 **/

package com.automateit.util;

import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * This class contains the list of commands that were run by selenium.
 * 
 * @author mburnside@Automate It!
 */
public class CommandList {
    
    /**
     * Queue object for collecting commands
     */
    private LinkedList list = new LinkedList();
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(CommandList.class);
    
    /**
     * CommandList instance
     */
    private static CommandList instance = new CommandList();
    
    /**
     * Default Constructor
     */
    public CommandList() { }
    
    /**
     * Return the instance of this object.
     * 
     * @return The instance of this object 
     */
    public static CommandList getInstance() { return instance; }
    
    /**
     * Add a command to the list.
     * 
     * @param s the command that was run 
     */
    public void addToList(String s) { list.add(s); }
    
    /**
     * Removes all commands in the list.
     */
    public void clear() { list.clear(); }
    
    /**
     * Get all of the commands in the list.
     * 
     * @return 
     */
    public String[] getAllInList() { 
        
        Object[] objectList = list.toArray();
        
        String[] stringList = new String[objectList.length];

        for (int i = 0; i < objectList.length; i++) stringList[i] = objectList[i].toString();

        return stringList; 
    
    }
    
    /**
     * Get the size of the list.
     * 
     * @return 
     */
    public int getSize() { return list.size(); }
    
    /**
     * Indicates if command list contains no commands
     * 
     * @return 
     */
    public boolean isEmpty() { return (getSize() == 0); }
            
}