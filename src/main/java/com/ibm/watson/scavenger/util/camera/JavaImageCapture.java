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
 * Java Camera window utility class.
 */
package com.ibm.watson.scavenger.util.camera;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import com.ibm.watson.scavenger.ImageTrainingApp;
import com.ibm.watson.scavenger.PredictionApp;
import com.ibm.watson.scavenger.util.CommandsUtils;
import com.ibm.watson.scavenger.util.ScavengerContants;
import com.ibm.watson.scavenger.visualrecognition.ImageTraining;


public class JavaImageCapture extends JFrame implements Runnable, WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener, WebcamDiscoveryListener {

	private static final long serialVersionUID = 1L;

	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private WebcamPicker picker = null;
	JLabel statusLabel = null;
	JPanel statusPanel = null;
	private File img_capture_tmp_dir = null;
	private String img_file_prefix = null;
	private Object invoking_ref = null;

	public ArrayList<File> capturedImages = new ArrayList<File>();
	public EventListenerList images_lst = new EventListenerList();
	
	public JavaImageCapture(File img_capture_tmp_dir, String img_file_prefix,Object ref)
	{
		this.img_capture_tmp_dir = img_capture_tmp_dir;
		this.img_file_prefix = img_file_prefix;
		this.invoking_ref = ref;
	}
	
	public void run() {

		Webcam.addDiscoveryListener(this);

		setTitle("Scavenger Image Capture");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		

		addWindowListener(this);

		picker = new WebcamPicker();
		picker.addItemListener(this);

		webcam = picker.getSelectedWebcam();

		if (webcam == null) {
			System.out.println("No webcams found...");
			System.exit(1);
		}

		webcam.setViewSize(WebcamResolution.VGA.getSize());
		/*
		 * 	QQVGA (176 x 144)
			QVGA (320 x 240)
			VGA (640 x 480)
		 */
		webcam.setViewSize(new Dimension(ScavengerContants.camera_width,ScavengerContants.camera_height));
		webcam.addWebcamListener(JavaImageCapture.this);

		panel = new WebcamPanel(webcam, false);
		panel.setFPSDisplayed(true);
		panel.setToolTipText("click anywhere on this window to capture image");
		panel.setImageSizeDisplayed(true);

		statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("click anywhere on above camera capture window to capture image");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		statusPanel.setVisible(true);
		
		add(picker, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		
		panel.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				statusLabel.setText("capturing image please wait");
				BufferedImage image = webcam.getImage();
				try {
					File capturedImage = File.createTempFile(img_file_prefix,".jpg",img_capture_tmp_dir);
					ImageIO.write(image, "JPG",capturedImage);
					System.out.println(capturedImage.getPath());
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				statusLabel.setText("image captured click again to capture/add another image");
				if(img_capture_tmp_dir.getPath().equals(ScavengerContants.vr_train_img_dir.getPath()))
				{
					String count = new CommandsUtils().executeCommand("bash","-c","ls "+ScavengerContants.vr_train_img_dir_path+"/"+img_file_prefix+"*.jpg | wc -l");
					System.out.println(count);
					if(!count.equals("") && count != null)
					if(Integer.valueOf(count.trim()) > 20)
					{
						CommandsUtils cmdUtils = new CommandsUtils();
						cmdUtils.executeCommand("bash","-c","cd "+ScavengerContants.vr_train_img_dir_path+"; zip "+img_file_prefix+"_positive_examples.zip "+img_file_prefix+"*.jpg");
						cmdUtils.executeCommand("bash","-c","rm "+ScavengerContants.vr_train_img_dir_path+"/"+img_file_prefix+"*.jpg");
						new ImageTraining().createClassifier(img_file_prefix,ScavengerContants.vr_train_img_dir_path+"/"+img_file_prefix+"_positive_examples.zip",ScavengerContants.vr_negative_example_zip);
						System.exit(0);
					}
				}
			}
		});

		pack();
		setVisible(true);

		Thread t = new Thread() {

			 
			public void run() {
				panel.start();
			}
		};
		t.setName("Scavenger Image Capture");
		t.setDaemon(true);
		t.setUncaughtExceptionHandler(this);
		t.start();

		Thread announcThread = new Thread() {			 
			public void run() {
				if(invoking_ref instanceof PredictionApp) {
					PredictionApp.getInstance().tts.playTextToSpeech("you can click anywhere on the camera capture window to capture the image. Watson image recognition result will be displayed in saperate window.");
				}
				if(invoking_ref instanceof ImageTrainingApp){
					ImageTrainingApp.getInstance().tts.playTextToSpeech("you can click anywhere on the Camera window to capture the image. once image count reaches to twenty or more, image classifier will be automatically created.");
				}
			}
		};
		announcThread.start();
		
	}
	
	public List<File> getCapturedImages()
	{
		return capturedImages;
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		/*JavaImageCapture startCap = new JavaImageCapture();
		SwingUtilities.invokeAndWait(startCap);*/
	}

	
	 
	public void webcamOpen(WebcamEvent we) {
		System.out.println("webcam open");
		if(invoking_ref instanceof PredictionApp) {
		//PhotoCaptureFrame.getJFrame().setVisible(true);
		}
	}

	 
	public void webcamClosed(WebcamEvent we) {
		System.out.println("webcam closed");
		picker.getSelectedWebcam().close();
	}

	 
	public void webcamDisposed(WebcamEvent we) {
		System.out.println("webcam disposed");
		picker.getSelectedWebcam().shutdown();
	}

	 
	public void webcamImageObtained(WebcamEvent we) {
		// do nothing
	}

	 
	public void windowActivated(WindowEvent e) {
	}

	 
	public void windowClosed(WindowEvent e) {
		webcam.close();
	}

	 
	public void windowClosing(WindowEvent e) {
	}

	 
	public void windowOpened(WindowEvent e) {
	}

	 
	public void windowDeactivated(WindowEvent e) {
	}

	 
	public void windowDeiconified(WindowEvent e) {
		System.out.println("webcam viewer resumed");
		panel.resume();
	}

	 
	public void windowIconified(WindowEvent e) {
		System.out.println("webcam viewer paused");
		panel.pause();
	}

	 
	public void uncaughtException(Thread t, Throwable e) {
		System.err.println(String.format("Exception in thread %s", t.getName()));
		e.printStackTrace();
	}

	 
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() != webcam) {
			if (webcam != null) {

				panel.stop();

				remove(panel);

				webcam.removeWebcamListener(this);
				webcam.close();

				webcam = (Webcam) e.getItem();
				webcam.setViewSize(WebcamResolution.VGA.getSize());
				webcam.addWebcamListener(this);

				System.out.println("selected " + webcam.getName());

				panel = new WebcamPanel(webcam, false);
				panel.setFPSDisplayed(true);

				add(panel, BorderLayout.CENTER);
				pack();

				Thread t = new Thread() {

					 
					public void run() {
						panel.start();
					}
				};
				t.setName("example-stoper");
				t.setDaemon(true);
				t.setUncaughtExceptionHandler(this);
				t.start();
			}
		}
	}

	public void webcamFound(WebcamDiscoveryEvent event) {
		if (picker != null) {
			picker.addItem(event.getWebcam());
		}
	}

	public void webcamGone(WebcamDiscoveryEvent event) {
		if (picker != null) {
			picker.removeItem(event.getWebcam());
		}
	}
}

