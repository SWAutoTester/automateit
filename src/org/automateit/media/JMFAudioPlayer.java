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

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.Player;

/**
 * This class uses Java Media Framework (JMF) to play an audio file
 * 
 * @author mburnside
 */
public class JMFAudioPlayer extends Audio implements AudioPlayer, ControllerListener { 
    
    /**
     * The reference to the actual player object
     */
    private Player player = null;
    
    /**
     *  logging object
     */
    private static Logger logger = Logger.getLogger(JMFAudioPlayer.class);
    
    /**
     * Default Constructor
     */
    public JMFAudioPlayer() { }
    
    /**
     * Play the audio file.
     * 
     * @param audioFilename
     * 
     * @throws Exception 
     */
    public void play(String audioFilename) throws Exception { 
        
        try { 
            
            logger.info("Playing audio using audio file using Java Media Framework: " + audioFilename);
            
            player = Manager.createPlayer(new File(audioFilename).toURI().toURL());
				
            player.addControllerListener(this);
	
            player.start();
            
        }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Play the audio file.
     * 
     * @param inputStream
     * 
     * @throws Exception 
     */
    public void play(InputStream inputStream) throws Exception { }
    
    @Override
    public void controllerUpdate(ControllerEvent c) { if(player == null) return; }

}

