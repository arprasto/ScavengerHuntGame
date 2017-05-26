package com.ibm.watson.scavenger;

import java.util.logging.Logger;

import com.ibm.watson.scavenger.speechToText.SpeechToTextWebSocketMain;
import com.ibm.watson.scavenger.textToSpeech.TTSMain;
import com.ibm.watson.scavenger.util.ScavengerContants;
import com.ibm.watson.scavenger.visualrecognition.ImageAnalysis;

public class App 
{
	Logger LOG = Logger.getLogger(App.class.getName());

	
    public static void main( String[] args ) throws Exception
    {
    	    	App.getInstance().startGame();
    }    
    
    static App obj = null;
    public static App getInstance(){
    	if(obj == null){
    		obj=new App();
    	}
    	return obj;
    }
    
    public TTSMain tts = null;
    public SpeechToTextWebSocketMain stt = null;
    public ImageAnalysis imgsvc = null;
    void startGame()
    {
    	try{
    		//startup all the services
		tts = new TTSMain(ScavengerContants.TTS_uname,ScavengerContants.TTS_pass);
		stt = new SpeechToTextWebSocketMain(ScavengerContants.STT_uname,ScavengerContants.STT_pass);
		imgsvc = new ImageAnalysis(ScavengerContants.ImageRecogAPIKey); 

		tts.playTextToSpeech("Hello ! I am IBM watson artificially intelligent assisstence. "
				+ "To start the game you can say the keyword like. game. scavenger hunt game. hunt game. To end the game anytime you can say the keyword like. exit. i am done. or even please exit.");
		
		Thread hearingThread = new Thread() {
			
			public void run() {
				stt.startSTT();
			}
		};
		
		hearingThread.start();
    	}catch(RuntimeException e)
    	{
    		System.out.println("looks like internet connectivity issue");
    		e.printStackTrace();
    	}
		
    }
}
