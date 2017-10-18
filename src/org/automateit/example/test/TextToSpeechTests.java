package org.automateit.example.test;

import org.testng.annotations.Test;

import org.automateit.test.TestBase;

import org.automateit.media.JarvisTextToSpeechConverter;
import org.automateit.media.TextToSpeechConverter;
import org.automateit.media.JLayerAudioPlayer;
import org.automateit.media.AudioPlayer;

/**
 * This class shows an example of how to use the AutomateIt! framework
 * for testing a text-to-speech application.
 * 
 * @author mburnside
 */
public class TextToSpeechTests extends TestBase {
    
    /**
     * The text to speech converter used in this test
     */
    protected TextToSpeechConverter textToSpeechConverter = new JarvisTextToSpeechConverter();
    
    /**
     * The audio player used in this test
     */
    protected AudioPlayer audioPlayer = new JLayerAudioPlayer();
    
    /**
     * Read in a text that has a Welcome message and play the audio on the computer
     *
     * @throws Exception 
     */
    @Test(description = "Read in a text Hello There and play the audio on the computer", groups = { "example_texttospeech" })
    public void test_A_Convert_Text_To_Speech_Welcome_Message_English() throws Exception {
      
        try { audioPlayer.play(textToSpeechConverter.execute("Hello and welcome to the Automate It Test Automation Framework")); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Read in a text that has a Welcome message and play the audio on the computer
     *
     * @throws Exception 
     */
    @Test(description = "Read in a text Hello There and play the audio on the computer - Spanish accent", groups = { "example_texttospeech" })
    public void test_B_Convert_Text_To_Speech_Welcome_Message_Spanish_Accent() throws Exception {
      
        try { audioPlayer.play(textToSpeechConverter.execute("Hello and welcome to the Automate It Test Automation Framework", "es-MX")); }
        catch(Exception e) { throw e; }
    
    }
    
    /**
     * Read in a text that has a Welcome message and play the audio on the computer
     *
     * @throws Exception 
     */
    @Test(description = "Read in a text Hello There and play the audio on the computer - native spanish language", groups = { "example_texttospeech" })
    public void test_C_Convert_Text_To_Speech_Welcome_Message_Spanish() throws Exception {
      
        try { audioPlayer.play(textToSpeechConverter.execute("Hola, tu estas bueno tambien?", "es-MX")); }
        catch(Exception e) { throw e; }
    
    }
	
}