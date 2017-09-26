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
package com.automateit.media;

import org.apache.log4j.Logger;

/**
 * This interface is what is implementation classes need to use when converting
 * speech to text
 * 
 * @author mburnside
 */
public abstract class SpeechToTextConverter { 
    
    /**
     * This is the text representation of the audio in the speech audio file.
     * 
     * It needs get set and referenced to an actual String in the execute method.
     */
    protected String audioText = null;
    
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(SpeechToTextConverter.class);
    
    /**
     * Convert the speech audio to text.
     * 
     * @param speechAudioFile
     * 
     * @return
     * 
     * @throws Exception 
     */
    public abstract void execute(String speechAudioFile) throws Exception;
    
    /**
     * Verify that the expected text appears in the captured audio speech
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void validateTextAppearsInSpeech(String text) throws Exception {
        
        try {
            
            if(audioText == null) throw new Exception("Unable to verify that the expected text appears in the speech audio file because it is null");
            
            if(audioText.trim().length() == 0) throw new Exception("Unable to verify that the expected text appears in the speech audio file because it there was no text captured");
 
            logger.info("Validating that text appears in audio speech to text conversion: " + text + " from converted text result: " + audioText);
            
            if(!audioText.trim().contains(text)) throw new Exception("The expected text does not appear in the speech audio file: " + text + "|" + audioText);
            
        }
        catch(Exception e) { 
            
            logger.error("Contents of speech in audio file: " + audioText);
            
            throw e; 
        
        }
        
    }
    
    /**
     * Verify that the set of expected text appears in the captured audio speech
     * 
     * @param text
     * 
     * @throws Exception 
     */
    public void validateTextAppearsInSpeech(String[] text) throws Exception {
        
        try { for(int i = 0; i < text.length; i++) validateTextAppearsInSpeech(text[i]); }
        catch(Exception e) { throw e; }
        
    }

}

