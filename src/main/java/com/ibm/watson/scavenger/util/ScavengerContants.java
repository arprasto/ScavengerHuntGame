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
 * Placeholder class for holding bluemix configuration parameters for each Watson service.
 * this will be initialized from properties file. 
 */
package com.ibm.watson.scavenger.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.scavenger.LaunchApp;

public class ScavengerContants 
{
	public static String TTS_uname = LaunchApp.getLoadedProperties().getProperty("TTS_uname").trim(),
			TTS_pass = LaunchApp.getLoadedProperties().getProperty("TTS_pass").trim(),			
			TTS_gender = LaunchApp.getLoadedProperties().getProperty("TTS_gender").trim(),
			TTS_language = LaunchApp.getLoadedProperties().getProperty("TTS_language").trim(),
			TTS_name =  LaunchApp.getLoadedProperties().getProperty("TTS_name").trim(),
			
			/*
			 * Speech To Text cred
			 */
			STT_uname = LaunchApp.getLoadedProperties().getProperty("STT_uname").trim(),
			STT_pass = LaunchApp.getLoadedProperties().getProperty("STT_pass").trim(),
			
			/*
			 * Visual Recognition details
			 */
			vr_APIKey = LaunchApp.getLoadedProperties().getProperty("vr_APIKey").trim(),
			tmp_image_dir_path = LaunchApp.getLoadedProperties().getProperty("tmp_image_dir_path").trim(),
			vr_train_img_dir_path = tmp_image_dir_path+"/train",
			vr_process_img_dir_path = tmp_image_dir_path+"/process",
			vr_classifier_name = LaunchApp.getLoadedProperties().getProperty("vr_classifier_name").trim(),
			vr_version = LaunchApp.getLoadedProperties().getProperty("vr_version",VisualRecognition.VERSION_DATE_2016_05_20).trim(),
			vr_negative_example_zip = LaunchApp.getLoadedProperties().getProperty("vr_negative_example_zip","./src/extresources/images/australianterrier.zip").trim(),
			
			/*
			 * Cloudant NoSql details
			 */
			cloudant_uname = LaunchApp.getLoadedProperties().getProperty("cloudant_uname").trim(),
			cloudant_pass = LaunchApp.getLoadedProperties().getProperty("cloudant_pass").trim(),
			cloudant_url = LaunchApp.getLoadedProperties().getProperty("cloudant_url").trim(),
			cloudant_dbName = LaunchApp.getLoadedProperties().getProperty("cloudant_dbName").trim(),
			
			/*
			 * IOT device params
			 */
			iot_Organization_ID=LaunchApp.getLoadedProperties().getProperty("iot_Organization_ID").trim(),
			iot_device_type=LaunchApp.getLoadedProperties().getProperty("iot_device_type").trim(),
			iot_device_id=LaunchApp.getLoadedProperties().getProperty("iot_device_id").trim(),
			iot_Authentication_Token=LaunchApp.getLoadedProperties().getProperty("iot_Authentication_Token").trim(),
			iot_Authentication_Method=LaunchApp.getLoadedProperties().getProperty("iot_Authentication_Method").trim(),
			iot_event_for_img_base64=LaunchApp.getLoadedProperties().getProperty("iot_event_for_img_base64").trim(),
			
			/*
			 * allowable set of objects to capture images of
			 */
			allowable_obj_set[] = LaunchApp.getLoadedProperties().getProperty("allowable_obj_set").trim().split(","),

			/*
	    	 * get random objects for which imgs has to be captured for
	    	 */
	    	random_img_obj_str=getRandomImgObjString(allowable_obj_set,ScavengerContants.possible_number_of_obj);
			;
	
			/*
			 * camera resolution
			 * can be one of the following
			 * 
			QQVGA (176 x 144)
			QVGA (320 x 240)
			VGA (640 x 480)
			 */
			public static int camera_width = Integer.valueOf(LaunchApp.getLoadedProperties().getProperty("camera_width","320").trim()),
			camera_height = Integer.valueOf(LaunchApp.getLoadedProperties().getProperty("camera_height","240").trim()),
			
			/*
			 * actual number of allowable objects which will be selected randomly from allowable_obj_set for a particular app session
			 * value(possible_number_of_obj) <= size(allowable_obj_set)
			 */
			possible_number_of_obj = Integer.valueOf(LaunchApp.getLoadedProperties().getProperty("possible_number_of_obj","3").trim()),
			
			/*
			 * number of classifiers to be created in single run of training app
			 */
			classifier_count = Integer.valueOf(LaunchApp.getLoadedProperties().getProperty("possible_number_of_obj","1").trim()),
			
			/*
			 * number of positive images needed to create each custom classifier
			 */
			number_of_positive_imgs = Integer.valueOf(LaunchApp.getLoadedProperties().getProperty("number_of_positive_imgs","21").trim())
			;
			
	    	public static int unique_app_id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

			public static long time_frame=Long.valueOf(LaunchApp.getLoadedProperties().getProperty("time_frame","10000").trim());
	
	
	public static File tmp_image_dir = null,vr_train_img_dir=null,vr_process_img_dir=null;
	
	static {
		tmp_image_dir = new File(tmp_image_dir_path);
				if(!tmp_image_dir.exists()){
					tmp_image_dir.mkdir();
					tmp_image_dir.setWritable(true);
				}
		vr_train_img_dir = new File(vr_train_img_dir_path);
				if(!vr_train_img_dir.exists()){
					vr_train_img_dir.mkdir();
					vr_train_img_dir.setWritable(true);
				}
		vr_process_img_dir = new File(vr_process_img_dir_path);
				if(!vr_process_img_dir.exists()){
					vr_process_img_dir.mkdir();
					vr_process_img_dir.setWritable(true);
				}
	}
	
	private static String getRandomImgObjString(String[] allowable_obj_set, int possible_number_of_obj) {
		String obj_str = "";
		int nxt,i=0;
		Set<Integer> random_num=new HashSet<Integer>();
		if(possible_number_of_obj>allowable_obj_set.length){
			possible_number_of_obj = allowable_obj_set.length;
		}
		
		for(int j=0;;j++){
			nxt = new Random().nextInt(allowable_obj_set.length);
			if(!random_num.contains(new Integer(nxt)))
			{
			obj_str = allowable_obj_set[nxt]+". "+obj_str;
			random_num.add(nxt);
			if(random_num.size()==3) break;
			}
			
		}

		System.out.println("allowable set of objs is : "+obj_str);
		return obj_str;
	}

	
	/*public static void main(String[] arg)
	{
		System.out.println(new CommandsUtils().executeCommand("bash","-c","ls -l ./src/extresources/images/australianterrier.zip"));
	}*/
}
