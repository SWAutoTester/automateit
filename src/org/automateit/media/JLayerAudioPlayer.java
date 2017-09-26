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

import java.io.InputStream;

import org.apache.log4j.Logger;

import javazoom.jl.player.Player;

/**
 * This class uses JLayer to play an audio file. It is the only implementation
 * in this framework that can play an MP3 file
 * 
 * @author mburnside
 */
public class JLayerAudioPlayer extends Audio implements AudioPlayer { 
    
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(JLayerAudioPlayer.class);
    
    /**
     * Default Constructor
     */
    public JLayerAudioPlayer() { }
    
    /**
     * Play the audio file.
     * 
     * @param audioFilename
     * 
     * @throws Exception 
     */
    public void play(String audioFilename) throws Exception { }
    
    /**
     * Play the audio file.
     * 
     * @param inputStream
     * 
     * @throws Exception 
     */
    public void play(InputStream inputStream) throws Exception { 
        
        try { 
            
            logger.info("Playing audio using audio file using JLayer");
          
            Player jlplayer = new javazoom.jl.player.Player(inputStream);
            
            jlplayer.play();
            
        }
        catch(Exception e) { throw e; }
    
    }
    
}

