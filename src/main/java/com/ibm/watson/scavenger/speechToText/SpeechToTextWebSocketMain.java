package com.ibm.watson.scavenger.speechToText;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognitionCallback;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.RecognizeCallback;
import com.ibm.watson.scavenger.App;
import com.ibm.watson.scavenger.util.ScavengerContants;
import com.ibm.watson.scavenger.util.camera.JavaImageCapture;

public class SpeechToTextWebSocketMain {
	private static Logger LOGGER = Logger.getLogger(SpeechToTextWebSocketMain.class.getName());
	SpeechToText sttsvc = null;
	RecognitionCallback callback = null;
	public SpeechToTextWebSocketMain(String uname,String upass)
	{
		sttsvc = new SpeechToText(uname,upass);
		//callback = sttsvc.registerCallback("http://www.google.com","arpit").execute();
	}
	
	public void startSTT()
	{
		try{
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, getAudioFormat());

            if (!AudioSystem.isLineSupported(info)) {
            	LOGGER.log(Level.SEVERE,"Line not supported");
            }

            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(getAudioFormat());
            line.start();

            AudioInputStream audio = new AudioInputStream(line);
            
		sttsvc.recognizeUsingWebSocket(audio,getRecognizeOptions(),new MicrophoneRecognizeDelegate());
		//LOGGER.log(Level.FINE,"callBack --------------- "+callback.toString());
		}catch (LineUnavailableException e) {
			LOGGER.log(Level.SEVERE,"Line not available");
			e.printStackTrace();
		}
	}
	
    private RecognizeOptions getRecognizeOptions() {
        return new RecognizeOptions.Builder()
                .continuous(true)
                .contentType(HttpMediaType.AUDIO_RAW+"; rate=16000; channels=1")
                .interimResults(true)
                .inactivityTimeout(-1)
                .build();
    }
    
    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }


    private class MicrophoneRecognizeDelegate implements RecognizeCallback {

		public void onConnected() {
			LOGGER.log(Level.INFO, "connected........please speak now");

		}

		public void onDisconnected() {
			LOGGER.log(Level.INFO, "disconnected........");	
		}

		public void onError(Exception arg0) {
			LOGGER.log(Level.SEVERE, "error........"+arg0.getMessage());
			JOptionPane.showMessageDialog(null,"some error in STT svc initialization "+arg0.getMessage(),"STT error",JOptionPane.ERROR_MESSAGE);
			arg0.printStackTrace();
			System.exit(0);
		}

		public void onTranscription(SpeechResults speechResults) {
			//LOGGER.log(Level.INFO, "in Transcription now "+speechResults);
            if(speechResults.getResults() != null && !speechResults.getResults().isEmpty()) {
                String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                LOGGER.log(Level.INFO,text);
                if(text.toLowerCase().contains("game") || text.toLowerCase().contains("hunt game") || text.toLowerCase().contains("scavenger"))
                {
                	JavaImageCapture startCap = new JavaImageCapture();
						SwingUtilities.invokeLater(startCap);
                }
                if(text.toLowerCase().contains("i am done") || text.toLowerCase().contains("exit") || text.toLowerCase().contains("i'm done"))
                {
                	System.exit(0);
                }
            }
		}

		public void onInactivityTimeout(RuntimeException arg0) {
			LOGGER.log(Level.SEVERE, "InactivityTimeout");
			arg0.printStackTrace();
		}

		public void onListening() {
			LOGGER.log(Level.INFO, "now listening");
		}
    }
    
}
