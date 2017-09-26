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

import javax.sound.sampled.AudioFileFormat;
import com.darkprograms.speech.microphone.Microphone;

/**
 * This interface is what is audio listeners need to implement to be used
 * by this framework.
 * 
 * @author mburnside
 */
public class JarvisAudioListener implements AudioListener { 
    
    /**
     * The implemented microphone object that records and saves the audio
     */
    private Microphone microphone = null;
    
    /**
     * This is the file that the the captured microphone input is saved to
     */
    public static final String AUDIO_FILENAME = "recording.wav";
    
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(JarvisAudioListener.class);
    
    /**
     * Default Constructor.
     * 
     * @throws Exception 
     */
    public JarvisAudioListener() throws Exception {
        
        logger = Logger.getLogger(JarvisAudioListener.class);
        
        logger.info("Initializing the listening device / microphone");
        
        try { this.microphone = new Microphone(AudioFileFormat.Type.WAVE); }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Start this video recorder.
     * 
     * @throws Exception 
     */
    public void start() throws Exception {
        
        try {
            
            logger.info("Starting to capture audio from listening device / microphone");
            
            if(this.microphone == null) throw new Exception("Unable to listen/record audio because the microphone is not active or initialized");
            
            this.microphone.captureAudioToFile(AUDIO_FILENAME);
            
        }
        catch(Exception e) { throw e; }
        
    }
    
    /**
     * Stops this video recorder.
     * 
     * @throws Exception 
     */
    public void stop() throws Exception {
        
        try {
            
            logger.info("Stopping the audio capture from listening device / microphone, saving recorded audio to file: " + AUDIO_FILENAME);
            
            if(this.microphone == null) throw new Exception("Unable to listen/record audio because the microphone is not active or initialized");
            
            this.microphone.close();
            
        }
        catch(Exception e) { throw e; }
        
    }

}

