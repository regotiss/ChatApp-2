package source;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class WindowUtil {

	public static ImageIcon getScaledImage(Image srcImg, int w, int h) {
		  int s=0;
		 BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	      Graphics2D g2 = resizedImg.createGraphics();
	      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	      g2.setColor(Color.black);
	      g2.drawImage(srcImg, s, s, w-s, h-s, null);
	      for(int i=0;i<s;i++)
	    	  g2.drawRect(i, i, w-i, h-i);
	      g2.dispose();
	      return new ImageIcon(resizedImg);
	}
	public static ImageIcon getImage(byte b[]){
		
		ImageIcon img=null;
		File f=new File("C:/pic.jpg");
		try {
			Files.write(f.toPath(), b);
			img=new ImageIcon(f.getAbsolutePath());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return img;
	}
	public static ImageIcon getImage(byte b[],int w,int h){
		
		InputStream in = new ByteArrayInputStream(b);
		try {
			BufferedImage bImage = ImageIO.read(in);
			if(bImage!=null)
			{
				  int s=0;
					 BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				      Graphics2D g2 = resizedImg.createGraphics();
				      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				      g2.setColor(Color.black);
				      g2.drawImage(bImage, s, s, w-s, h-s, null);
				      for(int i=0;i<s;i++)
				    	  g2.drawRect(i, i, w-i, h-i);
				      g2.dispose();
				      return new ImageIcon(resizedImg);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	public static void setNativeLook(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//javax.swing.plaf.nimbus.NimbusLookAndFeel
		
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public static void setNimbusLook(){
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			//javax.swing.plaf.nimbus.NimbusLookAndFeel
		
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public static void setToCenter(Container c,int width,int height){
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int w = gd.getDisplayMode().getWidth();
		int h = gd.getDisplayMode().getHeight();
		
		c.setBounds(w/2-width/2,h/2-height/2,width,height);

	}
	public static int getScreenWidth(){
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int w = gd.getDisplayMode().getWidth();
		return w;
	}
	public static int getScreenHeight(){
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int h = gd.getDisplayMode().getHeight();
		return h;
	}

}
