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

/**
 * Data archive interface. All data archiving implementation classes 
 * need to implement this interface.
 * 
 * @author mburnside
 */
public interface DataArchive {
    
    /**
     * Add data to be archived.
     * 
     * @param data
     * 
     * @throws Exception 
     */
    public void addData(String[] data) throws Exception;

    /**
     * Save the data to a file.
     * 
     * @param filename
     * 
     * @throws Exception 
     */
    public void saveData(String filename) throws Exception;
    
    /**
     * Clear/remove all data collected.
     * 
     * @throws Exception 
     */
    public void clearData() throws Exception;
    
}

