/**
 * Copyright 2017 IBM Corp. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * VR utility class to perform Image Recognition related tasks.
 * calls the Face detection, Image Keyword and Scene text for each passed image.
 */
package com.ibm.watson.scavenger.visualrecognition;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Face;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageFace;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageText;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.RecognizedText;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier.VisualClass;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualRecognitionOptions;
import com.ibm.watson.scavenger.util.CommandsUtils;
import com.ibm.watson.scavenger.util.PatchedCredentialUtils;
import com.ibm.watson.scavenger.util.ScavengerContants;

public class ImageAnalysis {

  private static Logger LOGGER = Logger.getLogger(ImageAnalysis.class.getName());

  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private final VisualRecognition vision;
  private List<String> classifiers_lst = new ArrayList<String>(); 

  public ImageAnalysis(String apiKey) {
    // API key is automatically retrieved from VCAP_SERVICES by the Watson SDK
    vision = new VisualRecognition(ScavengerContants.vr_version);

    // get the key from the VCAP_SERVICES as workaround for
    // https://github.com/watson-developer-cloud/java-sdk/issues/371
    vision.setApiKey(PatchedCredentialUtils.getVRAPIKey(null));

    // Allow a developer running locally to override the API key with
    // an environment variable. When working with Liberty, it can be defined
    // in server.env.
    classifiers_lst.add("default");
    if (apiKey != null) {
      vision.setApiKey(apiKey);
    }
  }

  public VisualRecognition getVisualRecognitionInstance(){
	  return vision;
  }
  
  public Response analyzeImage(File file) {
    try {
      // write it to disk
      InputStream fileInput = new FileInputStream(file);
      File tmpFile = File.createTempFile("vision-", ".jpg");
      tmpFile.deleteOnExit();

      LOGGER.severe("Analyzing a binary file " + tmpFile);
      Files.copy(fileInput, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

      VisualRecognitionOptions options = new VisualRecognitionOptions.Builder().images(tmpFile).build();
      ClassifyImagesOptions classifyOptions = new ClassifyImagesOptions.Builder().classifierIds(classifiers_lst).images(tmpFile).build();

      Result result = analyze(options, classifyOptions);
      System.out.println("analysis result "+gson.toJson(result));
      return Response.ok(gson.toJson(result), MediaType.APPLICATION_JSON_TYPE).build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

  public String analyzeImageJSon(File file) {
	    try {
	      // write it to disk
	      InputStream fileInput = new FileInputStream(file);
	      File tmpFile = File.createTempFile("vision-", ".jpg");
	      tmpFile.deleteOnExit();

	      LOGGER.severe("Analyzing a binary file " + tmpFile);
	      Files.copy(fileInput, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	      
	      loadClassifiers();

	      VisualRecognitionOptions options = new VisualRecognitionOptions.Builder().images(tmpFile).build();
	      ClassifyImagesOptions classifyOptions = new ClassifyImagesOptions.Builder().classifierIds(classifiers_lst).images(tmpFile).build();

	      Result result = analyze(options, classifyOptions);
	      LOGGER.info("analysis result "+gson.toJson(result));
	      
	      StringBuffer result_sb = new StringBuffer();
	      result_sb.append("<html><body>");
	      if(result.faces.size()>0){
	      for(Face face: result.faces){
	    	  if(face.getAge() != null){
	    	  result_sb.append("Max age:"+face.getAge().getMax()+" ,Min age:"+face.getAge().getMin()+", score:"+new Double(face.getAge().getScore()*100).intValue());
	    	  }
	    	  if(face.getGender() != null){
	    		  result_sb.append("<br/>gender:"+face.getGender().getGender()+", score:"+new Double(face.getGender().getScore()*100).intValue());
	    	  }
	    	  if(face.getIdentity() !=null){
	    		  result_sb.append("<br/>identity:"+face.getIdentity().getName()+", score:"+new Double(face.getIdentity().getScore()*100).intValue());
	    	  }    	  
	      }}
	      
	      if(result.keywords.size()>0){
	    	  for(VisualClassifier class_key : result.keywords){
	    		  if(class_key.getClasses().size()>0){
	    			  int i=1;
	    		  for(VisualClass clazz : class_key.getClasses()){
	    		  result_sb.append("<br/>"+clazz.getName()+":"+new Double(clazz.getScore()*100).intValue());
	    		  i++;
	    		  if(i==5) break;
	    		  }
	    		  }
	    	  }
	      }
	      result_sb.append("</body></html>");
	      
	      return result_sb.toString();
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	  }

  private Result analyze(VisualRecognitionOptions options, ClassifyImagesOptions classifyOptions) {
    Result result = new Result();

    LOGGER.info("Calling Face Detection...");
    try {
      DetectedFaces execute = vision.detectFaces(options).execute();
      List<ImageFace> imageFaces = execute.getImages();
      if (!imageFaces.isEmpty()) {
        result.faces = imageFaces.get(0).getFaces();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    LOGGER.info("Calling Image Keyword...");
    try {
      VisualClassification execute = vision.classify(classifyOptions).execute();
      System.out.println(execute);
      List<ImageClassification> imageClassifiers = execute.getImages();
      if (!imageClassifiers.isEmpty()) {
        result.keywords = imageClassifiers.get(0).getClassifiers();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    LOGGER.info("Calling Scene Text...");
    try {
      RecognizedText execute = vision.recognizeText(options).execute();
      List<ImageText> imageTexts = execute.getImages();
      if (!imageTexts.isEmpty()) {
        result.sceneText = imageTexts.get(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }

  static class Result {
    String url;
    List<Face> faces;
    List<VisualClassifier> keywords;
    ImageText sceneText;
  }
  
  private void loadClassifiers()
  {
		String classifiers = new CommandsUtils().executeCommand("bash","-c","curl -X GET \""+ScavengerContants.vr_classifier_uri+"?version="+ScavengerContants.vr_version+"&api_key="+ScavengerContants.vr_APIKey+"\"");
		JsonArray classifier_arr = new JsonParser().parse(classifiers.toLowerCase()).getAsJsonObject().get("classifiers").getAsJsonArray();
		for(int i=0;i<classifier_arr.size();i++){
			if(classifier_arr.get(i).getAsJsonObject().get("status").getAsString().equals("ready")){
				classifiers_lst.add(classifier_arr.get(i).getAsJsonObject().get("classifier_id").getAsString());
				LOGGER.info("classifier added : "+classifier_arr.get(i).getAsJsonObject().get("classifier_id").getAsString());
			}
		}
  }

}
