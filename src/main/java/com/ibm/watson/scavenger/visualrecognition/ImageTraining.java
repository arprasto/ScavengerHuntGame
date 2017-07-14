/**
 *****************************************************************************
 * Copyright (c) 2017 IBM Corporation and other Contributors.

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Arpit Rastogi - Initial Contribution
 *****************************************************************************
 */

/*
 * Utility class to create/train custom classifier by passing negative and positive images zip file.<br>
 * negative file has to be specified in properties file(vr_negative_example_zip)<br>
 * if not specified default is australianterrier.zip will be taken.
 */

package com.ibm.watson.scavenger.visualrecognition;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.ibm.watson.scavenger.ImageTrainingApp;
import com.ibm.watson.scavenger.util.CommandsUtils;
import com.ibm.watson.scavenger.util.ScavengerContants;

public class ImageTraining {
	Logger LOGGER = Logger.getLogger(ImageTraining.class.getName());
	public void createClassifier(String class_name,String positiveZipPath,String negativeZipPath)
	{
		String cmd = "curl -X POST -F \""+class_name+"_positive_examples=@"+positiveZipPath+
				"\" -F \"negative_examples=@"+negativeZipPath+
				"\" -F \"name="+ScavengerContants.vr_classifier_name+"\" \""+
				ScavengerContants.vr_classifier_uri+"?version="+ScavengerContants.vr_version+"&api_key="+ScavengerContants.vr_APIKey+"\"";
		LOGGER.info("executing cmd \n"+cmd);
		String response = new CommandsUtils().executeCommand("bash","-c",cmd);
		LOGGER.info("classifier creation response = "+response);
		if(response.toLowerCase().contains("error")){
			JOptionPane.showMessageDialog(null,"error in creating classifier\n"+response);
			ImageTrainingApp.getInstance().tts.playTextToSpeech("i am sorry ! there was some error while creating image classifier. Please try after some time.");
		}
		else{
			ImageTrainingApp.getInstance().tts.playTextToSpeech("please wait classifier is being trained.");
			while(true){
				try {
					Thread.sleep(6000);
					String res = new CommandsUtils().executeCommand("bash","-c","curl -X GET \""+ScavengerContants.vr_classifier_uri+"/v3/classifiers?api_key="+ScavengerContants.vr_APIKey+"&version="+ScavengerContants.vr_version+"\"");
					if(res.toLowerCase().contains("\"status\": \"training\"")){
						ImageTrainingApp.getInstance().tts.playTextToSpeech("please wait classifier is being trained.");
					}
					else{
						ImageTrainingApp.getInstance().tts.playTextToSpeech("classifier has been trained now. To create another classifier you need to rerun this application.");
						System.exit(0);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
