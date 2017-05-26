package com.ibm.watson.scavenger.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.scavenger.util.images.PhotoCaptureFrame;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class SharedResources {
  static  ObservableList<File> capturedImages = null;  
public static List<File> getCapturedImageList()
{
	if(capturedImages == null)
	{
		capturedImages = FXCollections.observableList(new ArrayList<File>());
		capturedImages.addListener(new ListChangeListener<File>() {

			public void onChanged(javafx.collections.ListChangeListener.Change<? extends File> c) {
				if(c.next()){
				for(File addedImage:c.getAddedSubList()){
					PhotoCaptureFrame.updateCaptureFrame(addedImage);
				}
			}
			}
		});
	}
	return capturedImages;
}
}
