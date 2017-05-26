package com.ibm.watson.scavengerHunt;

import java.io.File;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.scavenger.util.ScavengerContants;
import com.ibm.watson.scavenger.visualrecognition.ImageAnalysis;

public class ImageAnalysisTest {

	public static void main(String[] args) {
		ImageAnalysis imgsvc = new ImageAnalysis(ScavengerContants.ImageRecogAPIKey); 
		/*ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
			    .images(new File("/Users/arpitrastogi/Pictures/test.jpg"))
			    .build();
		VisualClassification result = imgsvc.getVisualRecognitionInstance().classify(options).execute();
		System.out.println(result);*/
		
		DetectedFaces df = new DetectedFaces();
		
	}

}
