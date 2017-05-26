package com.ibm.watson.scavenger.textToSpeech;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import com.ibm.watson.scavenger.App;
import com.ibm.watson.scavenger.util.CommandsUtils;
import com.ibm.watson.scavenger.util.ScavengerContants;
import com.ibm.watson.scavenger.util.sound.JavaSoundPlayer;

public class TTSMain {

	public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		
		
		TTSMain obj = new TTSMain(ScavengerContants.TTS_uname,ScavengerContants.TTS_pass);
		obj.playTextToSpeech("Hello ! I am IBM watson artificially intelligent assisstence. "
				+ "To start the application you can say the keyword like game or scavenger hunt game or hunt game");
	}
	
	Logger LOG = Logger.getLogger(TTSMain.class.getName());
	
	TextToSpeech service = null;
	
	public TTSMain(String uname,String upass)
	{
		service = new TextToSpeech();
		service.setUsernameAndPassword(uname,upass);
	}
	
	public TTSMain(){
		
	}
	
	public void playTextToSpeechUsingWhisk(String txt){
    	String tmpStr = new CommandsUtils().executeCommand("/usr/local/bin/wsk","action", "invoke", "WatsonTTS", "--param", "message", txt);
    	String activationID = tmpStr.split(" ")[tmpStr.split(" ").length-1];
    	//System.out.println(activationID);
    	JsonObject res_payload = new JsonParser().parse(new CommandsUtils().executeCommand("/usr/local/bin/wsk","activation", "result",activationID)).getAsJsonObject();
    	System.out.println(res_payload.getAsJsonPrimitive("payload").getAsString().getBytes());
    	byte[] decoded = null;
			String encoded = res_payload.getAsJsonPrimitive("payload").getAsString();
			decoded = Base64.getDecoder().decode(encoded);
			try
		    {
		        File tmpWAV = File.createTempFile("tmp",".wav");
		        tmpWAV.deleteOnExit();
		        System.out.println(tmpWAV.getPath());
		        FileOutputStream os = new FileOutputStream(tmpWAV, true);
		        os.write(decoded);
		        os.close();
		        new JavaSoundPlayer().playWAVFile(tmpWAV);
		    }
		    catch (Exception e)
		    {
		        e.printStackTrace();
		    }

	}
	
	public void playTextToSpeech(String txt){
		
		
        InputStream stream = service.synthesize(txt, new Voice("en-US_LisaVoice","female","en-US"),
        		new com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat("audio/wav")).execute();
        InputStream in=null;
        OutputStream out=null;
		try {
			in = WaveUtils.reWriteWaveHeader(stream);
        
        File tmpWAV = File.createTempFile("tmpTTS",".wav");
        LOG.log(Level.INFO,"audio tmp file is "+tmpWAV.toURI().toURL());
        tmpWAV.deleteOnExit();
        out = new FileOutputStream(tmpWAV);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
          out.write(buffer, 0, length);
        }
        
        new JavaSoundPlayer().playWAVFile(tmpWAV);
        
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
	        try {
				out.close();
	        in.close();
	        stream.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
