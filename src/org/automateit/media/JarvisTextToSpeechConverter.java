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
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.darkprograms.speech.synthesiser.Synthesiser;

/**
 * This class uses the JARVIS framework to convert a text string into an
 * audio stream
 * 
 * @author mburnside
 */
public class JarvisTextToSpeechConverter implements TextToSpeechConverter { 
    
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(JarvisTextToSpeechConverter.class);
    
    /**
     * Default Constructor
     */
    public JarvisTextToSpeechConverter() { }
    
    /**
     * This method uses text-to-speech synthesizer to convert a string (text) to an audio file.
     * 
     * The default encoding is: "en-us"
     * 
     * @param message
     * 
     * @return The audio input stream
     * 
     * @throws Exception 
     */
    public InputStream execute(String message) throws Exception {
        
        try { 
            
            logger.info("Creating an audio input stream from the text string: " + message);
           
            String language = "en-us";
            
            Synthesiser synth = new Synthesiser(language);
            
            return synth.getMP3Data(message);
           
        }
        catch(Exception e) { throw e; }
        
    }

}

