package com.ibm.watson.scavengerHunt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ibm.watson.scavenger.util.images.Photo;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

public class ScrollImageTest extends JPanel {
    
    public static void main(String[] args) throws MalformedURLException, IOException {
    	String desc = "";
        Photo photo = new Photo("test", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description");
        Photo photo1 = new Photo("test11111111", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description11111111");
        Photo photo2 = new Photo("test11111111", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description11111111");
        Photo photo3 = new Photo("test11111111", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description11111111");
        /*Photo photo4 = new Photo("test11111111", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description11111111");
        Photo photo5 = new Photo("test11111111", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description11111111");
        Photo photo6 = new Photo("test11111111", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description11111111");
        Photo photo7 = new Photo("test11111111", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description11111111");
        Photo photo8 = new Photo("test11111111", new URL("http://interviewpenguin.com/wp-content/uploads/2011/06/java-programmers-brain.jpg"), "description11111111");
*/
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
        jp.add(photo, BorderLayout.BEFORE_FIRST_LINE);
        jp.add(photo1, BorderLayout.BEFORE_FIRST_LINE);
        jp.add(photo2, BorderLayout.BEFORE_FIRST_LINE);
        jp.add(photo3, BorderLayout.BEFORE_FIRST_LINE);
        /*jp.add(photo4, BorderLayout.BEFORE_FIRST_LINE);
        jp.add(photo5, BorderLayout.BEFORE_FIRST_LINE);
        jp.add(photo6, BorderLayout.BEFORE_FIRST_LINE);
        jp.add(photo7, BorderLayout.BEFORE_FIRST_LINE);
        */
        JScrollPane scrollPane = new JScrollPane(jp);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        scrollPane.setPreferredSize(new Dimension(dim.width/2-50,dim.height-100));
        JPanel contentPane = new JPanel();
        contentPane.add(scrollPane);
        JFrame f = new JFrame();
        f.setContentPane(contentPane);
        f.setSize(dim.width/2-30,dim.height-60);
        f.setResizable(false);
        f.setPreferredSize(new Dimension(dim.width/2-30,dim.height-60));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
         
    }
}

class ImageDef extends JPanel
{
	Image img=null;
	ImageDef(URL img_path){
		BufferedImage image=null;
		try {
			image = ImageIO.read(img_path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.img = image;
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, 200,200,null);
    }

}