package com.ibm.watson.scavenger.speechToText;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Base64;

import com.ibm.watson.scavenger.util.CommandsUtils;
import com.ibm.watson.scavenger.util.sound.JavaSoundRecorder;

public class SpeechToTextWhiskMain {

	public static void main(String[] args) throws FileNotFoundException {
		
		new SpeechToTextWhiskMain().recognizeSpeachWithWhisk();

	}
	
	public void recognizeSpeachWithWhisk() throws FileNotFoundException
	{
        final JavaSoundRecorder recorder = new JavaSoundRecorder();
        
        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });
        stopper.start();
        String recordedAudio = recorder.start();

        try{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(recordedAudio));

        int read;
        byte[] buff = new byte[1024];
        while ((read = in.read(buff)) > 0)
        {
            out.write(buff, 0, read);
        }
        out.flush();
        in.close();
        byte[] decoded_audioBytes = out.toByteArray();        
        String audio_payload = Base64.getEncoder().encodeToString(decoded_audioBytes);
        //String audio_payload = Base64.getEncoder().encodeToString(Base64.getDecoder().decode(decoded_audioBytes));
        System.out.println(audio_payload);
        String result = new CommandsUtils().executeCommand("/usr/local/bin/wsk","action","invoke","directWatsonSTTpkg/speechToText", "--param", "content_type", "'audio/wav'", "--param", "encoding", "'base64'", "--param", "username", "dd032ab8-6229-4a0c-8c3f-1dccf322145a", "--param", "password", "XocT4fgOKyKR", "--param", "interim_results", "true","--param","payload",audio_payload ,"--auth", "b8acfe02-1afe-4dfb-8f96-6241b5f7ccd8:KwUajNWQB8j1uLJEKit2RMZ69rFVwpLmOQzhC7p07dLoDneXdPoU9smbmi86QDis");
        System.out.println(result);
        }catch(Exception e){
        	e.printStackTrace();
        }
	}

}
