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
 * Camera capture (Prediction window) class to display the VR results with image as icon.
 * also calls the VR service for each image.
 */

package com.ibm.watson.scavenger.util.images;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ibm.watson.scavenger.PredictionApp;
import com.ibm.watson.scavenger.util.CommandsUtils;
import com.ibm.watson.scavenger.util.ScavengerContants;

public class PhotoCaptureFrame extends JFrame {
	JPanel jp = null;
	JFrame f = null;
	Logger log = Logger.getLogger(PhotoCaptureFrame.class.getName());
	private static PhotoCaptureFrame obj = null;
	PhotoCaptureFrame(){
        jp = new JPanel();
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(jp);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        scrollPane.setPreferredSize(new Dimension(dim.width/2-40,dim.height-100));
        
    	JButton btn = new JButton("Upload Image");
    	
    	btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(System.getenv("user.home"));
		        fc.setFileFilter(new JPEGImageFileFilter());
		        int res = fc.showOpenDialog(null);
		        // We have an image!
		        try {
		            if (res == JFileChooser.APPROVE_OPTION) {
		                File file = fc.getSelectedFile();
		                //SharedResources.sharedCache.getCapturedImageList().add(file);
		                new CommandsUtils().executeCommand("bash","-c","cp "+file.getPath()+" "+ScavengerContants.vr_process_img_dir_path);
		            }
		        }catch (Exception iOException) {
		        }
				
			}
		});
        
        JPanel contentPane = new JPanel();
        contentPane.add(btn);
        contentPane.add(scrollPane);
        f = new JFrame("IBM Watson Visual Prediction Window");
        f.setContentPane(contentPane);
        f.setSize(dim.width/2-30,dim.height-40);
        f.setLocation(dim.width/2,0);
        f.setResizable(false);
        f.setPreferredSize(new Dimension(dim.width/2-30,dim.height-60));
        f.setVisible(true);
	}
	
	public static JPanel getJPanel()
	{
		if(obj == null){
			obj = new PhotoCaptureFrame();
		}
		return obj.jp;
	}

	public static JFrame getJFrame()
	{
		if(obj == null){
			obj = new PhotoCaptureFrame();
		}
		return obj.f;
	}
	
	public static void updateCaptureFrame(File capturedImgFile)
	{
		
		String result = PredictionApp.getInstance().imgsvc.analyzeImageJSon(capturedImgFile);
		//System.out.println(result);
		
		Photo photo=null;
		try {
			photo = new Photo("IBM Watson predictions for below image",capturedImgFile.toURI().toURL(),result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		PhotoCaptureFrame.getJPanel().add(photo);
		PhotoCaptureFrame.getJFrame().repaint();
		PhotoCaptureFrame.getJFrame().setVisible(true);
	}
}