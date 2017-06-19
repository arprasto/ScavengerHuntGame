package com.ibm.watson.scavenger.util.images;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ibm.watson.scavenger.App;
import com.ibm.watson.scavenger.util.ScavengerContants;
import com.ibm.watson.scavenger.util.SharedResources;

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
		                SharedResources.sharedCache.getCapturedImageList().add(file);
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
        
		Thread dirWatchThread = new Thread(){

		@Override
		public void run() {
			try{
			Path dir = Paths.get(ScavengerContants.tmp_image_dir.toURI());
	        new WatchDir(dir, false).processEvents();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		};
		dirWatchThread.start();

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
		
		String result = App.getInstance().imgsvc.analyzeImageJSon(capturedImgFile);
		System.out.println(result);
		
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