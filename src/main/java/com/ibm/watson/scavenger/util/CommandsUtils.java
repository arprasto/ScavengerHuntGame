package com.ibm.watson.scavenger.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watson.scavenger.textToSpeech.TTSMain;

public class CommandsUtils {
	Logger LOG = Logger.getLogger(CommandsUtils.class.getName());
	
    public String executeCommand(String... command) {

		StringBuffer output = new StringBuffer();

		/*Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			//ProcessBuilder pcss = new ProcessBuilder(command);
			//p = pcss.start();
			p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/

    	Process process;
		try {
			process = new ProcessBuilder(command).start();
    	InputStream is = process.getInputStream();
    	InputStreamReader isr = new InputStreamReader(is);
    	BufferedReader br = new BufferedReader(isr);
    	String line;

    	LOG.log(Level.INFO, "retreiving result for "+command.toString()+" : ");

    	while ((line = br.readLine()) != null) {
    	  //System.out.println(line);
    	  output.append(line);
    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	LOG.log(Level.INFO, "Output of running "+output.toString());
		return output.toString();


	}

}
