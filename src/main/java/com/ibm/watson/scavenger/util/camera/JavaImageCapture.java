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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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
import com.ibm.watson.scavenger.App;
import com.ibm.watson.scavenger.util.SharedResources;
import com.ibm.watson.scavenger.util.images.Photo;
import com.ibm.watson.scavenger.util.images.PhotoCaptureFrame;


/**
 * Proof of concept of how to handle webcam video stream from Java
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class JavaImageCapture extends JFrame implements Runnable, WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener, WebcamDiscoveryListener {

	private static final long serialVersionUID = 1L;

	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private WebcamPicker picker = null;
	JLabel statusLabel = null;
	JPanel statusPanel = null;

	public ArrayList<File> capturedImages = new ArrayList<File>();
	public EventListenerList images_lst = new EventListenerList();
	
	
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
		webcam.setViewSize(new Dimension(320,240));
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
					File capturedImage = File.createTempFile("cap1",".jpg");
					ImageIO.write(image, "JPG",capturedImage);
					SharedResources.getCapturedImageList().add(capturedImage);					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				statusLabel.setText("image captured click again to capture/add another image");
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
				App.getInstance().tts.playTextToSpeech("Hey ! you can click anywhere on the Image capture window to capture the image. Watson image recognition result will be displayed in saperate window.");
			}
		};
		announcThread.start();
		
	}
	
	public List<File> getCapturedImages()
	{
		return capturedImages;
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		JavaImageCapture startCap = new JavaImageCapture();
		SwingUtilities.invokeAndWait(startCap);
	}

	
	 
	public void webcamOpen(WebcamEvent we) {
		System.out.println("webcam open");
		PhotoCaptureFrame.getJFrame().setVisible(true);
		//add(new ImageCaptureButton().getButton(),BorderLayout.CENTER);

	}

	 
	public void webcamClosed(WebcamEvent we) {
		System.out.println("webcam closed");
		picker.getSelectedWebcam().close();
		
	}

	 
	public void webcamDisposed(WebcamEvent we) {
		System.out.println("webcam disposed");
		picker.getSelectedWebcam().shutdown();
		App.getInstance().tts.playTextToSpeech("Thanks for using this application. Hoping to see you soon on IBM Watson bluemix platform.");
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

