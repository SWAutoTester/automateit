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

package com.automateit.data;

import java.util.List;
import java.util.LinkedList;

import org.apache.log4j.Logger;
        
/**
 * Data archive base class. All data archive classes should extend this class.
 * 
 * @author mburnside
 */
public class DataArchiveBase {
    
    /**
     * Queue object for collecting commands
     */
    protected List<String[]> list = new LinkedList<String[]>();
    
    /**
     *  logging object
     */
    private static Logger log = Logger.getLogger(DataArchiveBase.class);
    
    /**
     * Add data to be archived.
     * 
     * @param data
     * 
     * @throws Exception 
     */
    public void addData(String[] data) throws Exception { list.add(data); }
    
    /**
     * Clear/remove all data collected.
     * 
     * @throws Exception 
     */
    public void clearData() throws Exception { list.clear(); }
    
}

