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
 * Main class to launch the App to create custom classifier.
 */
package com.ibm.watson.scavenger;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.ibm.watson.scavenger.textToSpeech.TTSMain;
import com.ibm.watson.scavenger.util.ScavengerContants;
import com.ibm.watson.scavenger.util.camera.JavaImageCapture;

public class ImageTrainingApp {

	public static void main(String[] args) {
    ImageTrainingApp.getInstance().startTrainingApp();
	}
	
	static ImageTrainingApp obj = null;
	public static ImageTrainingApp getInstance(){
		if(obj == null){
			obj = new ImageTrainingApp();
		}
		return obj;
	}
	
    public TTSMain tts = null;

	public void startTrainingApp()
	{
		/*
		 * start all required below IBM Watson services:
		 * a. Text To Speech
		 */
		tts = new TTSMain(ScavengerContants.TTS_uname,ScavengerContants.TTS_pass);
		
		/*announce the welcome message*/
       	tts.playTextToSpeech("to train the model you need to give at least twenty or more images. the more clear images"
        			+ "you give. will increase the prediction accuracy of image. ");
       	
       	/*get the custom classifier name which we are going to create*/
        	String class_name = JOptionPane.showInputDialog(new JFrame("class name"),"Enter the class name you are going to create","enter Class name");
        	
        	JavaImageCapture startCap = new JavaImageCapture(ScavengerContants.vr_train_img_dir,class_name,ImageTrainingApp.getInstance());
        	try {
                /* start the camera capture window thread to capture the image*/
				SwingUtilities.invokeAndWait(startCap);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
