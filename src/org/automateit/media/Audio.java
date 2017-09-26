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

package org.automateit.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * This abstract class is what is audio listeners need to extend to use
 * the basic audio features.
 * 
 * @author mburnside
 */
public abstract class Audio {
    
    /**
     * The audio in mpeg format
     */
    public static final String MPG = "mpg";
    
    /**
     * The audio in wav format
     */
    public static final String WAV = "wav";
    
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(Audio.class);
    
    /**
     * This method uses text-to-speech synthesizer to convert a string (text) to an audio file.
     * 
     * The default encoding is: "en-us"
     * 
     * @param message
     * @param saveToFilename
     * 
     * @throws Exception 
     */
    public void save(String message, String saveToFilename) throws Exception {
        
        try { 
            
            logger.info("Saving voice synthesizer audio message: " + message + " to file: " + saveToFilename);
           
            InputStream blurb = (new JarvisTextToSpeechConverter()).execute(message);
            
            File targetFile = new File(saveToFilename);
    
            OutputStream outStream = new FileOutputStream(targetFile);
    
            byte[] buffer = new byte[8 * 1024];
    
            int bytesRead;
    
            while((bytesRead = blurb.read(buffer)) != -1) { outStream.write(buffer, 0, bytesRead); }
            
            outStream.close();
            
        }
        catch(Exception e) { throw e; }
        
    }
    
}

