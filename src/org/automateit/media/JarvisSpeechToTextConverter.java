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

import org.apache.log4j.Logger;

import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;

/**
 * This interface is what is implementation classes need to use when converting
 * speech to text
 * 
 * @author mburnside
 */
public class JarvisSpeechToTextConverter extends SpeechToTextConverter { 
    
    /**
     * This is the google api key that is assigned when the user creates it
     */
    private String apiKey = null;
    
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(JarvisSpeechToTextConverter.class);
  
    /**
     * Copy Constructor.
     * 
     * @param apiKey The API key assigned when creating an account with Google Speech API
     */
    public JarvisSpeechToTextConverter(String apiKey) { this.apiKey = apiKey; }
    
    /**
     * Convert the speech audio to text.
     * 
     * @param speechAudioFile
     * 
     * @throws Exception 
     */
    public void execute(String speechAudioFile) throws Exception {
        
        try {
            
            logger.info("Executing the speech to text converter with this audio file: " + speechAudioFile + " using API Key: " + apiKey);
            
            if(apiKey == null) throw new Exception("Google API Key is not set, unable to translate text to speech");
            
            Recognizer recognizer = new Recognizer("en-us", apiKey);
            
            GoogleResponse response = recognizer.getRecognizedDataForWave(new File(speechAudioFile), 25);
            
            audioText = response.getResponse();
            
            logger.info("\n\nSpeech to text conversion result: " + audioText + "\n\n");
           
        }
        catch(Exception e) { throw e; }
        
    }

}

